package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // To make the logo look like it's breathing
        ImageView rectangleBackground = findViewById(R.id.rectangleBackground);

        // For the login button to switch to LoginActivity
        Button navigationButton = findViewById(R.id.navigationButton);

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences preferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                    String storedPassword = preferences.getString(email, null);

                    if (storedPassword != null && storedPassword.equals(password)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}