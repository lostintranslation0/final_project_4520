package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private Button registerButton;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private TextView errorTextView;

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
                            Toast.makeText(MainActivity.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
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
    }
}