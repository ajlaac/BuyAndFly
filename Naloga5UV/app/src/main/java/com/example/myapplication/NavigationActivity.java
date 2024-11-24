package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Button viewTicketButton = findViewById(R.id.viewTicketButton);

        Button buyTicketButton = findViewById(R.id.buyTicketButton);

        buyTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, BuyTicketActivity.class);
                startActivity(intent);
            }
        });

        viewTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this, ViewTicketsActivity.class);
                startActivity(intent);
            }
        });
    }
}