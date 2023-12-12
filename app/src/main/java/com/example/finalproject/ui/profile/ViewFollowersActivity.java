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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_followers);

        Intent intent = getIntent();
        subjectUsername = intent.getStringExtra("USERNAME_SUBJECT");
        viewerUsername = intent.getStringExtra("USERNAME_VIEWER");


        nameTextView = findViewById(R.id.followersUserTitle);
        numFollowersTextView = findViewById(R.id.numFollowersTextView);
        recyclerView = findViewById(R.id.followersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyFollowerAdapter(new ArrayList<>(), this::onFollowerClicked);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(subjectUsername).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // gets the user
                User user = task.getResult().toObject(User.class);
                nameTextView.setText(user.getUsername());
                numFollowersTextView.setText("Number of Followers: " + user.getFollowers().size());
                adapter.setDataList(user.getFollowers());
            }
        });
    }

    private void onFollowerClicked(String follower)
    {
        Intent intent = new Intent(ViewFollowersActivity.this, ViewPublicProfileActivity.class);
        intent.putExtra("USERNAME_SUBJECT", subjectUsername);
        intent.putExtra("USERNAME_VIEWER", viewerUsername);
        startActivity(intent);
    }
}