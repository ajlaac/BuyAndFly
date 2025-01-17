package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class UpdateTicketActivity extends AppCompatActivity {

    private TextView tv_title;
    private Spinner spinner_class;
    private Button btn_save_class, btn_cancel;
    private PlaneTicket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ticket);

        // Initialize components
        tv_title = findViewById(R.id.tv_title);
        spinner_class = findViewById(R.id.spinner_class);
        btn_save_class = findViewById(R.id.btn_save_class);
        btn_cancel = findViewById(R.id.btn_cancel);

        // Get the current ticket ID from TicketSession
        int ticketId = TicketSession.getInstance().getCurrentTicketId();
        if (ticketId == 0) {
            Toast.makeText(this, "No ticket selected!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the ticket asynchronously
        fetchTicket(ticketId);

        // Handle Cancel button
        btn_cancel.setOnClickListener(v -> finish());
    }

    private void fetchTicket(int ticketId) {
        new Thread(() -> {
            PlaneTicket ticket = AppDatabase.getInstance(this).planeTicketDao().getTicketById(ticketId);
            runOnUiThread(() -> {
                if (ticket == null) {
                    Toast.makeText(this, "No ticket found for ID: " + ticketId, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    this.ticket = ticket;
                    setupSpinner();
                }
            });
        }).start();
    }

    private void setupSpinner() {
        // Example data for class types
        List<String> classes = Arrays.asList("Economy", "Business", "First Class");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(adapter);

        // Set the current class selection for the ticket
        int position = adapter.getPosition(ticket.getClassType());
        if (position >= 0) {
            spinner_class.setSelection(position);
        }

        // Handle Save button
        btn_save_class.setOnClickListener(v -> saveTicketClass());
    }

    private void saveTicketClass() {
        String selectedClass = spinner_class.getSelectedItem().toString();
        ticket.setClassType(selectedClass);

        // Update the ticket asynchronously
        new Thread(() -> {
            AppDatabase.getInstance(this).planeTicketDao().update(ticket);
            runOnUiThread(() -> {
                Toast.makeText(this, "Ticket class updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
