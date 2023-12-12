package com.example.finalproject.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.example.finalproject.ui.blogs.Blog;
import com.example.finalproject.ui.blogs.BlogDetailActivity;
import com.example.finalproject.ui.blogs.MyBlogsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPublicProfileActivity extends AppCompatActivity {

    String subjectUsername;
    String viewerUsername;

    RecyclerView recyclerView;
    MyBlogsAdapter adapter;

    TextView usernameTextView;

    Button followButton;
    Button viewFollowersButton;
    private ImageView publicProfilePictureImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_public_profile);
        getSupportActionBar().setTitle("Blogs");

        Intent intent = getIntent();
        subjectUsername = intent.getStringExtra("USERNAME_SUBJECT");
        viewerUsername = intent.getStringExtra("USERNAME_VIEWER");

        recyclerView = findViewById(R.id.publicUserBlogsRecyclerView);
        followButton = findViewById(R.id.followButton);
        viewFollowersButton = findViewById(R.id.seeFollowersButton);

        adapter = new MyBlogsAdapter(new ArrayList<>(), this::onBlogClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        usernameTextView = findViewById(R.id.publicProfileNameTextView);
        usernameTextView.setText(subjectUsername);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Blog> userBlogs = new ArrayList<>();
        db.collection("blogs").whereEqualTo("userWhoCreated", subjectUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.v("in pp querying database for blogs" , document.getId() + " => " + document.getData());
                        String title = (String)document.get("title");
                        String description = (String)document.get("description");
                        String content = (String)document.get("content");
                        String userWhoCreated = (String)document.get("userWhoCreated");
                        Timestamp date = (Timestamp)document.get("date");
                        String jsonComments = (String)document.get("comments");

                        Blog b = new Blog(userWhoCreated, title, description, content, date.toDate(), jsonComments);
                        userBlogs.add(b);
                    }
                    adapter.setDataList(userBlogs);

                }
            }
        });



        // Populate the profile data with the users stuff
        // Start with name, profile picture, blogs

        // determine follow button state
        db.collection("users").document(subjectUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful())
                {
                    List<String> followers = (List<String>)task.getResult().get("followers");

                    if (followers == null || !followers.contains(viewerUsername))
                    {
                        followButton.setText("Follow");
                    }
                    else {
                        followButton.setText("Unfollow");
                    }
                }
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(subjectUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful())
                        {
                            List<String> followers = (List<String>)task.getResult().get("followers");
                            if (followers == null)
                            {
                                followers = new ArrayList<>();
                            }
                            if (followButton.getText().toString().equals("Follow"))
                            {
                                followButton.setText("Unfollow");
                                followers.add(viewerUsername);
                                db.collection("users").document(subjectUsername).update("followers", followers);
                            }
                            else if (followButton.getText().toString().equals("Unfollow"))
                            {
                                // unfollow the user
                                followButton.setText("Follow");
                                followers.remove(viewerUsername);
                                db.collection("users").document(subjectUsername).update("followers", followers);
                            }
                        }
                    }
                });
            }
        });

        viewFollowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPublicProfileActivity.this, ViewFollowersActivity.class);
                intent.putExtra("USERNAME_SUBJECT", subjectUsername);
                intent.putExtra("USERNAME_VIEWER", viewerUsername);
                startActivity(intent);
            }
        });

        publicProfilePictureImageView = findViewById(R.id.publicProfilePictureImageView);

        // Load the profile image
        loadProfileImage(subjectUsername);
    }

    private void onBlogClicked(Blog blog) {
        Intent intent = new Intent(ViewPublicProfileActivity.this, BlogDetailActivity.class);
        intent.putExtra("USERNAME", viewerUsername);
        intent.putExtra("BLOG_DATA", blog); // Make sure Article is Serializable or Parcelable
        startActivity(intent);
    }
    private void loadProfileImage(String username) {
        FirebaseFirestore.getInstance().collection("users")
                .document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            if (user != null && user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                Glide.with(this)
                                        .load(user.getImageUrl())
                                        .placeholder(R.drawable.default_profile_image)
                                        .error(R.drawable.default_profile_image)
                                        .into(publicProfilePictureImageView);
                            } else {
                                publicProfilePictureImageView.setImageResource(R.drawable.default_profile_image);
                            }
                        }
                    } else {
                        Log.e("ViewPublicProfile", "Error getting user data: ", task.getException());
                    }
                });
    }
}