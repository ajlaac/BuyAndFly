package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewTicketsActivity extends AppCompatActivity {

    private LinearLayout ticketContainer;
    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        // Initialize components
        ticketContainer = findViewById(R.id.ticket_container);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Fetch and display tickets
        viewModel.getAllTickets().observe(this, this::populateTickets);

        //viewModel.getTicketsByDepartureDate("").observe(this, this::populateTickets);
    }

    private void populateTickets(List<PlaneTicket> tickets) {
        // Clear existing views
        ticketContainer.removeAllViews();

        // Check if there are no tickets
        if (tickets == null || tickets.isEmpty()) {
            Toast.makeText(this, "No tickets found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inflate and add a view for each ticket
        for (PlaneTicket ticket : tickets) {
            View ticketView = LayoutInflater.from(this).inflate(R.layout.item_ticket, ticketContainer, false);

            // Populate ticket data
            TextView fromTextView = ticketView.findViewById(R.id.fromTextView);
            TextView toTextView = ticketView.findViewById(R.id.toTextView);
            TextView dateTextView = ticketView.findViewById(R.id.dateTextView);
            TextView classTextView = ticketView.findViewById(R.id.classTextView);

            fromTextView.setText("From: " + ticket.getFromDestination());
            toTextView.setText("To: " + ticket.getToDestination());
            dateTextView.setText("Date: " + ticket.getDepartureDate());
            classTextView.setText("Class: " + ticket.getClassType());

            // Add the ticket view to the container
            ticketContainer.addView(ticketView);
        }
    }
}
