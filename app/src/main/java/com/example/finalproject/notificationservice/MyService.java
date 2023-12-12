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
import java.util.concurrent.atomic.AtomicInteger;

public class MyService extends Service {
    private FirebaseFirestore db;
    String username;
    private int prevAmountOfBlogs;

    final class MyThread implements Runnable {
        int serviceId;
        String username;

        public MyThread(int serviceId, String username) {
            this.serviceId = serviceId;
            this.username = username;
        }

        @Override
        public void run() {
            synchronized (this) {
                while (true) {
                    try {
                        wait(300000); // Wait for 15 minutes
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
            db.collection("users").document(this.username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> following = (List<String>) task.getResult().get("following");
                        if (following == null) {
                            Log.v("In service", "following null, returning from service");
                            return;
                        }
                        AtomicInteger numBlogs = new AtomicInteger(0);
                        AtomicInteger completedRequests = new AtomicInteger(0);

                        for (String s : following) {
                            db.collection("blogs").whereEqualTo("userWhoCreated", s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        numBlogs.addAndGet(task.getResult().size());
                                    }
                                    // Check if all requests are completed
                                    if (completedRequests.incrementAndGet() == following.size()) {
                                        // All Firestore queries completed
                                        Log.v("Comparison broadcast", "num blogs found: " + numBlogs.get() + " prev amount of blogs = " + prevAmountOfBlogs);
                                        if (numBlogs.get() != prevAmountOfBlogs) {
                                            Log.v("Sending broadcast", "sending broadcast");
                                            sendBlogBroadcast();
                                            prevAmountOfBlogs = numBlogs.get();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }



    private void sendBlogBroadcast() {
        Intent intent = new Intent();
        intent.setAction("com.example.finalproject.BLOG_NOTIFICATION");
        sendBroadcast(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started " + startId, Toast.LENGTH_SHORT).show();
        this.username = intent.getStringExtra("USERNAME");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(this.username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> following = (List<String>) task.getResult().get("following");
                    if (following == null) {
                        Log.v("In service", "following null, returning from service");
                        return;
                    }
                    AtomicInteger numBlogs = new AtomicInteger(0);
                    AtomicInteger completedRequests = new AtomicInteger(0);

                    for (String s : following) {
                        db.collection("blogs").whereEqualTo("userWhoCreated", s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    numBlogs.addAndGet(task.getResult().size());
                                }
                                // Check if all requests are completed
                                if (completedRequests.incrementAndGet() == following.size()) {
                                    Log.v("setting initial blogs", "initil blogs num:" + numBlogs.get())    ;
                                    prevAmountOfBlogs = numBlogs.get();
                                }
                            }
                        });
                    }

                }
            }
        });

        Thread thread = new Thread(new MyThread(startId, this.username));
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