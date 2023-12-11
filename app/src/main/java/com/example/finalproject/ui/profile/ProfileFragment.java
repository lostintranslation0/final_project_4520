package com.example.finalproject.ui.profile;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.example.finalproject.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ImageView profileImageView;
    private Uri imageUri;
    private Button takeNewProfilePicButton, uploadNewProfilePicButton, saveProfileChangesButton;

    private static final int OPEN_REQUEST_CODE = 102;
    private static final int PHOTO_REQUEST_CODE = 101;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileImageView = root.findViewById(R.id.profileFragmentImage);
        takeNewProfilePicButton = root.findViewById(R.id.takeNewProfilePicButton);
        uploadNewProfilePicButton = root.findViewById(R.id.uploadNewProfilePicButton);
        saveProfileChangesButton = root.findViewById(R.id.saveProfileChanges);

        takeNewProfilePicButton.setOnClickListener(view -> takePicture());
        uploadNewProfilePicButton.setOnClickListener(view -> uploadPicture());
        saveProfileChangesButton.setOnClickListener(view -> {
            if (imageUri != null) {
                uploadImageToFirebase(imageUri);
            }
        });

        loadProfileImage();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    private void uploadPicture() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileImageView.setImageBitmap(imageBitmap);
                imageUri = getImageUri(getContext(), imageBitmap);
            } else if (requestCode == OPEN_REQUEST_CODE) {
                imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
            }
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("user_images/" + UUID.randomUUID().toString());
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    User user = new User();
                    user.setImageUrl(imageUrl);

                    FirebaseFirestore.getInstance().collection("users").document(userId).set(user)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }



    private void loadProfileImage() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve the User object from Firestore
                            User user = document.toObject(User.class);

                            // Check if the user has an image URL
                            if (user != null && user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                // Load the profile image into the ImageView using Glide
                                Glide.with(requireContext())
                                        .load(user.getImageUrl())
                                        .placeholder(R.drawable.default_profile_image)
                                        .error(R.drawable.default_profile_image)
                                        .into(profileImageView);
                            } else {
                                profileImageView.setImageResource(R.drawable.default_profile_image);
                            }
                        }
                    }
                });
    }
}
