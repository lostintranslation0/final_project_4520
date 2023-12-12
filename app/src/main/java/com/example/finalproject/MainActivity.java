package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.notificationservice.MyBroadcastReceiver;
import com.example.finalproject.notificationservice.MyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private Button registerButton;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private TextView errorTextView;
    private BroadcastReceiver receiver;
    FirebaseAuthentication auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Login");

        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);
        usernameEntry = findViewById(R.id.usernameEditText);
        passwordEntry = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.signInErrorTextView);

        auth = new FirebaseAuthentication();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorTextView.setText(""); // Clear the error box
                String username = usernameEntry.getText().toString();
                String password = passwordEntry.getText().toString();

                auth.loginUser(username, password, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful

                            FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot d : task.getResult())
                                        {
                                            String uname = (String)d.getData().get("username");

                                            // Request notification permission for Android 13 and above

                                            configureReceiver(uname);
                                            startService(uname);
                                            Toast.makeText(MainActivity.this, "Login successful",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            intent.putExtra("USERNAME", uname);
                                            startActivity(intent);
                                        }
                                    }
                                }

                            });

                        } else {
                            // Login failed
                            Toast.makeText(MainActivity.this, "Login failed. " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if (!hasNotificationPermission()) {
            requestNotificationPermission();
        }
    }



    private void configureReceiver(String uname) {
        receiver = new MyBroadcastReceiver(uname);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.finalproject.BLOG_NOTIFICATION"); // Updated action string
        registerReceiver(receiver, filter);
    }

    public void startService(String uname) {

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("USERNAME", uname);
        startService(intent);
    }

    private boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Below Android 13, this permission is not required
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 2);
        }
    }
}