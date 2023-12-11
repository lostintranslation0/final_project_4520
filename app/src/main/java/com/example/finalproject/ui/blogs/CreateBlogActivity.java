package com.example.finalproject.ui.blogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class CreateBlogActivity extends AppCompatActivity {
    Button publishBlogButton;

    EditText titleEditText;
    EditText descriptionEditText;

    EditText contentEditText;
    String currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        Intent intent = getIntent();
        currUser = intent.getStringExtra("USERNAME");

        publishBlogButton = findViewById(R.id.publishBlogButton);
        titleEditText = findViewById(R.id.createBlogTitleEditText);
        descriptionEditText = findViewById(R.id.createDescriptionEditText);
        contentEditText = findViewById(R.id.createBlogContentEditText);

        publishBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String desc = descriptionEditText.getText().toString();
                String content = contentEditText.getText().toString();
                if (title == "" || desc == "" || content == "") {
                    titleEditText.setHint("Error, invalid input");
                    descriptionEditText.setHint("Error, invalid input");
                    contentEditText.setHint("Error, invalid input");
                }
                // check if title is unique, then we can add document
                Blog b = new Blog(currUser, title, desc, content, new Date());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("blogs").document(title).set(b);

                Intent intent = new Intent(CreateBlogActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", currUser);
                startActivity(intent);
            }
        });
    }
}