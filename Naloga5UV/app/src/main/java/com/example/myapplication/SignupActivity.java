package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.navigationButton);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user already exists
        viewModel.getUserByEmail(email).observe(this, existingUser -> {
            if (existingUser != null) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            } else {
                // Insert new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword(password);
                viewModel.insertUser(newUser);

                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
