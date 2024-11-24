package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewTicketsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        displaySavedTickets();
    }

    private void displaySavedTickets() {
        SharedPreferences sharedPreferences = getSharedPreferences("tickets", MODE_PRIVATE);
        int ticketCount = sharedPreferences.getInt("ticketCount", 0);
        LinearLayout ticketContainer = findViewById(R.id.ticket_container);

        for (int i = 0; i < ticketCount; i++) {
            String ticketInfo = sharedPreferences.getString("ticket_" + i, "");

            TextView ticketTextView = new TextView(this);
            ticketTextView.setText(ticketInfo);
            ticketTextView.setTextSize(16);
            ticketTextView.setPadding(0, 16, 0, 16);

            ticketContainer.addView(ticketTextView);
        }
    }
}