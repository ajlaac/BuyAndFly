package com.example.myapplication;

import android.content.DialogInterface;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuyTicketActivity extends AppCompatActivity {

    private Spinner spinner_from;
    private Spinner spinner_destination;
    private DatePicker date_picker_departure;
    private DatePicker date_picker_return;
    private Spinner spinner_class;
    private CheckBox check_roundabout;
    private Button btn_buy_ticket;
    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        // Initialize components
        spinner_from = findViewById(R.id.spinner_from);
        spinner_destination = findViewById(R.id.spinner_destination);
        date_picker_departure = findViewById(R.id.date_picker_departure);
        date_picker_return = findViewById(R.id.date_picker_return);
        spinner_class = findViewById(R.id.spinner_class);
        check_roundabout = findViewById(R.id.check_roundabout);
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Set onClickListener for the button
        btn_buy_ticket.setOnClickListener(v -> handleBookTicket());

        // Populate spinners with sample data
        setupSpinners();
    }

    private void setupSpinners() {
        // Example data for "From" and "Destination" spinners
        List<String> locations = Arrays.asList("New York", "Los Angeles", "Chicago", "Houston", "Miami");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_from.setAdapter(adapter);
        spinner_destination.setAdapter(adapter);

        // Example data for "Class" spinner
        List<String> classes = Arrays.asList("Economy", "Business", "First Class");
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_class.setAdapter(classAdapter);
    }

    private void handleBookTicket() {
        // Get selected values
        String from = spinner_from.getSelectedItem().toString();
        String to = spinner_destination.getSelectedItem().toString();
        String classType = spinner_class.getSelectedItem().toString();
        boolean isRoundTrip = check_roundabout.isChecked();

        // Get selected dates
        String departureDate = getDateFromDatePicker(date_picker_departure);
        String returnDate = isRoundTrip ? getDateFromDatePicker(date_picker_return) : "";

        // Validate inputs
        if (from.equals(to)) {
            Toast.makeText(this, "Source and destination cannot be the same!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (departureDate.isEmpty() || (isRoundTrip && returnDate.isEmpty())) {
            Toast.makeText(this, "Please select valid dates!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and save ticket
        PlaneTicket ticket = new PlaneTicket();
        ticket.setFromDestination(from);
        ticket.setToDestination(to);
        ticket.setDepartureDate(departureDate);
        ticket.setReturnDate(returnDate);
        ticket.setClassType(classType);
        ticket.setRoundTrip(isRoundTrip);

        viewModel.insertTicket(ticket);
        Toast.makeText(this, "Ticket booked successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        return String.format("%d-%02d-%02d", year, month + 1, day); // Format: YYYY-MM-DD
    }
}
