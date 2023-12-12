package com.example.finalproject.ui.blogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.ui.profile.ViewPublicProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    Blog blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        Intent intent = getIntent();
        currUser = intent.getStringExtra("USERNAME");

        blog = (Blog) intent.getSerializableExtra("BLOG_DATA");
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
        adapter = new CommentAdapter(blog.getComments() == null ? new ArrayList<>() : Comment.flattenComments(blog.getComments()), this::onCommentClicked);
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
                                Comment newComment = new Comment(currUser, input, new Date(), 0);

                                blog.addComment(newComment);
                                List<Comment> blogComments = blog.getComments();
                                Gson gson = new Gson();
                                String json = gson.toJson(blogComments);
                                // commit to database
                                FirebaseFirestore.getInstance().collection("blogs").document(blog.getTitle()).update("comments", json);
                                adapter.setDataList(Comment.flattenComments(blogComments));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
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

    private void refreshComments()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("blogs").document(blog.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {

                }
            }
        });
    }


    private void onCommentClicked(Comment comment) {
        Log.v("comment clicked", "on comment clicked");

//        int indOfThisComment = indexOf(comment);
//        if (indOfThisComment == -1)
//        {
//            throw new Error("Internal error, indexing comments");
//        }

        Comment commentToChange = comment;

        final EditText editText = new EditText(BlogDetailActivity.this);
        editText.setHint("Enter reply");
        AlertDialog.Builder builder = new AlertDialog.Builder(BlogDetailActivity.this)
                .setTitle("Add reply")
                .setView(editText)
                .setPositiveButton("Post Reply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = editText.getText().toString();
                        Comment newComment = new Comment(currUser, input, new Date(), commentToChange.getLevel() + 1);
                        commentToChange.addReply(newComment);

                        Gson gson = new Gson();
                        String newJson = gson.toJson(blog.getComments());
                        FirebaseFirestore.getInstance().collection("blogs").document(blog.getTitle()).update("comments", newJson);
                        adapter.setDataList(Comment.flattenComments(blog.getComments()));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
