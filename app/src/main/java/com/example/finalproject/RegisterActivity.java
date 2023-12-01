package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuthentication auth;


    EditText usernameEditText;
    EditText passwordEditText;
    Button registerButton;

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                auth.registerUser(username, password, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            Toast.makeText(RegisterActivity.this, "Registration successful",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Registration failed
                            Toast.makeText(RegisterActivity.this, "Registration failed. " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

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
                // Handle camera image
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            } else if (requestCode == OPEN_REQUEST_CODE) {
                // Handle gallery image
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }


}


