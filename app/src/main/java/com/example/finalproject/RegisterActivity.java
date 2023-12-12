package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class RegisterActivity extends AppCompatActivity {

    FirebaseAuthentication auth;
    EditText usernameEditText;
    EditText passwordEditText;
    Button registerButton;
    private Uri imageUri;

    EditText ageEditText;

    EditText displayNameEditText;

    private static final int OPEN_REQUEST_CODE = 102;
    private static final int PHOTO_REQUEST_CODE = 101;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Register");
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.imageViewRegister);

        auth = new FirebaseAuthentication();

        usernameEditText = findViewById(R.id.regUsernameEditText);
        passwordEditText = findViewById(R.id.regPasswordEditText);
        registerButton = findViewById(R.id.registerUserButton);
        ageEditText = findViewById(R.id.regAgeEditText);
        displayNameEditText = findViewById(R.id.regDisplayNameEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayName = displayNameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String ageText = ageEditText.getText().toString();
                final int[] age = {0};

                if (!ageText.isEmpty()) {
                    try {
                        age[0] = Integer.parseInt(ageText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(RegisterActivity.this, "Invalid age entered", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                auth.registerUser(username, password, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setAge(age[0]);
                            user.setUsername(displayName);


                            if (imageUri != null) {
                                uploadImageToFirebase(imageUri, user);
                            } else {
                                saveUserToFirestore(user);
                            }

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void saveUserWithoutImage(User user) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> Log.d("DatabaseUpdate", "User object stored successfully in database for user: " + userId))
                .addOnFailureListener(e -> Log.e("DatabaseUpdate", "Failed to store user object in database: " + e.getMessage(), e));
    }


    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    public void TakePicture(View view) {
        if (hasCamera()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHOTO_REQUEST_CODE);
        } else {
            view.setEnabled(false);
        }
    }

    public void UploadPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                imageUri = getImageUri(getApplicationContext(), imageBitmap);
            } else if (requestCode == OPEN_REQUEST_CODE) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


    private void uploadImageToFirebase(Uri imageUri, User user) {
        if (imageUri == null) {
            Log.d("UploadImage", "Image URI is null");
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("user_images/" + UUID.randomUUID().toString());
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    user.setImageUrl(imageUrl);

                    saveUserToFirestore(user);
                }))
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image Title", null);
        return Uri.parse(path);
    }

    private void saveUserToFirestore(User user) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> Log.d("DatabaseUpdate", "User object stored successfully in database for user: " + userId))
                .addOnFailureListener(e -> Log.e("DatabaseUpdate", "Failed to store user object in database: " + e.getMessage(), e));
    }




}


