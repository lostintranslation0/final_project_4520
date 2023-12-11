package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent intent = getIntent();
        String subjectUsername = intent.getStringExtra("USERNAME_SUBJECT");
        String viewerUsername = intent.getStringExtra("USERNAME_VIEWER");

        // Show users profile picture, number of followers, recycler view of their blogs (clickable)
        // follow button (must be stateful)
    }
}