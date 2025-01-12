package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class UpdateTicketActivity extends AppCompatActivity {

    private AppViewModel viewModel;
    private PlaneTicket ticket;
    private EditText fromTextView, toTextView, departureDateView, returnDateView, classTypeView;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ticket);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Retrieve ticket ID from intent
        int ticketId = getIntent().getIntExtra("TICKET_ID", -1);
        if (ticketId == -1) {
            Toast.makeText(this, "Invalid ticket", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch ticket details
        //viewModel.getTicketById(ticketId).observe(this, this::initializeFields);

        // Initialize components
        fromTextView = findViewById(R.id.update_from);
        toTextView = findViewById(R.id.update_to);
        departureDateView = findViewById(R.id.update_departure_date);
        returnDateView = findViewById(R.id.update_return_date);
        classTypeView = findViewById(R.id.update_class_type);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> saveUpdates());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void initializeFields(PlaneTicket fetchedTicket) {
        if (fetchedTicket == null) {
            Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ticket = fetchedTicket;

        // Populate fields with ticket data
        fromTextView.setText(ticket.getFromDestination());
        toTextView.setText(ticket.getToDestination());
        departureDateView.setText(ticket.getDepartureDate());
        returnDateView.setText(ticket.getReturnDate());
        classTypeView.setText(ticket.getClassType());
    }

    private void saveUpdates() {
        // Validate input fields
        if (isInputValid()) {
            // Update ticket details
            ticket.setFromDestination(fromTextView.getText().toString());
            ticket.setToDestination(toTextView.getText().toString());
            ticket.setDepartureDate(departureDateView.getText().toString());
            ticket.setReturnDate(returnDateView.getText().toString());
            ticket.setClassType(classTypeView.getText().toString());

            // Save updated ticket to the database
            //viewModel.updateTicket(ticket);
            Toast.makeText(this, "Ticket updated successfully", Toast.LENGTH_SHORT).show();

            // Return to the previous activity
            finish();
        }
    }

    private boolean isInputValid() {
        // Check for empty fields
        if (TextUtils.isEmpty(fromTextView.getText().toString()) ||
                TextUtils.isEmpty(toTextView.getText().toString()) ||
                TextUtils.isEmpty(departureDateView.getText().toString()) ||
                TextUtils.isEmpty(classTypeView.getText().toString())) {

            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate date format (YYYY-MM-DD)
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        if (!datePattern.matcher(departureDateView.getText().toString()).matches() ||
                (!TextUtils.isEmpty(returnDateView.getText().toString()) &&
                        !datePattern.matcher(returnDateView.getText().toString()).matches())) {

            Toast.makeText(this, "Invalid date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
