package com.example.finalproject.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewFollowersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyFollowerAdapter adapter;

    TextView nameTextView, numFollowersTextView;

    String subjectUsername;
    String viewerUsername;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_followers);

        Intent intent = getIntent();
        subjectUsername = intent.getStringExtra("USERNAME_SUBJECT");
        viewerUsername = intent.getStringExtra("USERNAME_VIEWER");
        mode = intent.getStringExtra("MODE");


        nameTextView = findViewById(R.id.followersUserTitle);
        numFollowersTextView = findViewById(R.id.numFollowersTextView);
        recyclerView = findViewById(R.id.followersRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyFollowerAdapter(new ArrayList<>(), this::onFollowerClicked);
        recyclerView.setAdapter(adapter);

        if (mode.equals("FOLLOWERS"))
        {
            String toShow = subjectUsername + "'s Followers";
            nameTextView.setText(toShow);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(subjectUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // gets the user
                    User user = task.getResult().toObject(User.class);

                    int numFollowers = -1;
                    if (user.getFollowers() == null)
                    {
                        numFollowers = 0;
                    }
                    else
                    {
                        numFollowers = user.getFollowers().size();
                    }

                    nameTextView.setText(user.getUsername());
                    numFollowersTextView.setText("Number of Followers: " + numFollowers);
                    if (numFollowers > 0)
                    {
                        adapter.setDataList(user.getFollowers());
                    }
                }
            });
        }

        if (mode.equals("FOLLOWING"))
        {
            String toShow = subjectUsername + "'s Following";
            nameTextView.setText(toShow);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(subjectUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    // gets the user
                    User user = task.getResult().toObject(User.class);
                    int numFollowing = -1;
                    if (user.getFollowing() == null)
                    {
                        numFollowing = 0;
                    }
                    else
                    {
                        numFollowing = user.getFollowing().size();
                    }

                    nameTextView.setText(user.getUsername());
                    numFollowersTextView.setText("Number of Users Following: " + numFollowing);
                    if (numFollowing > 0)
                    {
                        adapter.setDataList(user.getFollowing());
                    }

                }
            });
        }


    }

    private void onFollowerClicked(String follower)
    {
        Intent intent = new Intent(ViewFollowersActivity.this, ViewPublicProfileActivity.class);
        intent.putExtra("USERNAME_SUBJECT", follower);
        intent.putExtra("USERNAME_VIEWER", viewerUsername);
        startActivity(intent);
    }
}