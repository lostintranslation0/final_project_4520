package com.example.finalproject.notificationservice;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.FirebaseAuthentication;
import com.example.finalproject.ui.blogs.Blog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyService extends Service {
    private FirebaseFirestore db;
    private List<Blog> previousLookup;

    final class MyThread implements Runnable {
        int serviceId;

        public MyThread(int serviceId) {
            this.serviceId = serviceId;
            db = FirebaseFirestore.getInstance();

        }

        @Override
        public void run() {
            synchronized (this) {
                while (true) {
                    try {
                        wait(30000); // Wait for 15 minutes
                        Log.v("MyService", "Checking for new posts");
                        checkAndSendBroadcast();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void checkAndSendBroadcast() {
            // get list of all users they are following
            db = FirebaseFirestore.getInstance();
            db.collection("users").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        List<Blog> allBlogs = new ArrayList<>();
                        List<String> following = (List<String>) task.getResult().get("following");
                        if (following == null)
                        {
                            return;
                        }
                        for (String s: following)
                        {
                            db.collection("blogs").whereEqualTo("userWhoCreated", s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.v("in explore querying database for data" , document.getId() + " => " + document.getData());
                                            String title = (String)document.get("title");
                                            String description = (String)document.get("description");
                                            String content = (String)document.get("content");
                                            String userWhoCreated = (String)document.get("userWhoCreated");
                                            Timestamp date = (Timestamp)document.get("date");
                                            String jsonComments = (String)document.get("comments");

                                            Blog b = new Blog(userWhoCreated, title, description, content, date.toDate(), jsonComments);
                                            allBlogs.add(b);
                                        }
                                    }
                                }
                            });
                        }

                        // check if last blog list was different to this blog list
                        if (allBlogs.size() != previousLookup.size())
                        {
                            sendBlogBroadcast();
                            previousLookup = allBlogs;
                        }
                    }
                }
            });
        }



        private void sendBlogBroadcast() {
            Intent intent = new Intent();
            intent.setAction("com.example.finalproject.BLOG_NOTIFICATION");
            sendBroadcast(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started " + startId, Toast.LENGTH_SHORT).show();

        Thread thread = new Thread(new MyThread(startId));
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service ended!", Toast.LENGTH_SHORT).show();
        Log.i("MyService", "Service ended!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // no communication with the service yet
    }
}