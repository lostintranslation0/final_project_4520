package com.example.finalproject.ui.blogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.Comment;
import com.example.finalproject.CommentAdapter;
import com.example.finalproject.R;
import com.example.finalproject.ui.profile.ViewPublicProfileActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogDetailActivity extends AppCompatActivity {

    private TextView titleTV, descTV, authorTV, dateTV, contentTV;

    private String currUser;

    Button viewAuthorButton;
    Button addCommentButton;

    RecyclerView recyclerView;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        Intent intent = getIntent();
        currUser = intent.getStringExtra("USERNAME");

        Blog blog = (Blog) intent.getSerializableExtra("BLOG_DATA");
        if (blog == null) {
            throw new Error("Internal Error: blog null");
        }



        titleTV= findViewById(R.id.blogTitleTextView);
        descTV = findViewById(R.id.blogDescTextView);
        authorTV = findViewById(R.id.blogAuthorTextView);
        dateTV = findViewById(R.id.blogDateTextView);
        contentTV = findViewById(R.id.blogContentTextView);
        addCommentButton = findViewById(R.id.addCommentButton);
        viewAuthorButton = findViewById(R.id.viewAuthorButton);

        titleTV.setText(blog.getTitle());
        descTV.setText(blog.getDescription());
        authorTV.setText(blog.getUserWhoCreated());
        dateTV.setText(blog.getDate().toString());
        contentTV.setText(blog.getContent());

        recyclerView = findViewById(R.id.blogCommentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(blog.getComments() == null ? new ArrayList<>() : blog.getComments());
        recyclerView.setAdapter(adapter);

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(BlogDetailActivity.this);
                editText.setHint("Enter comment");
                AlertDialog.Builder builder = new AlertDialog.Builder(BlogDetailActivity.this)
                        .setTitle("Add Comment")
                        .setView(editText)
                        .setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String input = editText.getText().toString();
                                Comment newComment = new Comment(currUser, input, new Date());
                                List<Comment> blogComments = blog.getComments();
                                if (blogComments == null)
                                {
                                    blogComments = new ArrayList<>();
                                }
                                blogComments.add(newComment);
                                Gson gson = new Gson();
                                String json = gson.toJson(blogComments);
                                // commit to database
                                FirebaseFirestore.getInstance().collection("blogs").document(blog.getTitle()).update("comments", json);
                                adapter.setDataList(blogComments);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();

                // update value in db (also make sure that when we add a new thing that one doesn't
                // exist for that name already

                //TODO
            }
        });

        viewAuthorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogDetailActivity.this, ViewPublicProfileActivity.class);
                intent.putExtra("USERNAME_SUBJECT", blog.getUserWhoCreated());
                intent.putExtra("USERNAME_VIEWER", currUser);
                startActivity(intent);
            }
        });

    }
}