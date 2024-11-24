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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView rectangleBackground = findViewById(R.id.rectangleBackground);

        // For the signup button to switch to LoginActivity
        Button navigationButton = findViewById(R.id.navigationButton);

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
                String confirmPassword = ((EditText) findViewById(R.id.confirmPasswordEditText)).getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences preferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(email, password);
                    editor.apply();
                    Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }
            }
        });
    }
}