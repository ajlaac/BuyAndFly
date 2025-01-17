package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewTicketsActivity extends AppCompatActivity {

    private LinearLayout ticketContainer;
    private AppViewModel viewModel;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accelerationCurrentValue;
    private float accelerationLastValue;
    private float shakeThreshold = 12.0f; // Adjust sensitivity as needed

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                accelerationLastValue = accelerationCurrentValue;
                accelerationCurrentValue = (float) Math.sqrt((x * x) + (y * y) + (z * z));

                float delta = accelerationCurrentValue - accelerationLastValue;

                if (delta > shakeThreshold) {
                    Toast.makeText(ViewTicketsActivity.this, "Shake detected! Refreshing tickets...", Toast.LENGTH_SHORT).show();
                    loadUserFutureTickets();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // No action needed for accuracy changes
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);
        // Initialize components
        ticketContainer = findViewById(R.id.ticket_container);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        // Initialize SensorManager and accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // Fetch and display user's future tickets
        loadUserFutureTickets();
    }

    private void loadUserFutureTickets() {
        String userEmail = UserSession.getInstance().getLoggedInEmail();

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        viewModel.getUserFutureTickets(userEmail, currentDate).observe(this, this::populateTickets);
    }

    private void fetchAndSetWeather(String cityName, TextView weatherTextView) {
        WeatherService weatherService = WeatherService.create();
        String apiKey = "8b66dff3e97799a5a7a60987c8cae804";

        weatherService.getWeather(cityName, apiKey).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    double tempCelsius = weather.main.temp - 273.15; // Convert from Kelvin to Celsius
                    String cityDisplayName = cityName.split(",")[0]; // Extract city name from "City,US"
                    weatherTextView.setText(String.format("Weather in %s: %.1f°C, %s",
                            cityDisplayName, tempCelsius, weather.weather[0].description));
                } else {
                    String cityDisplayName = cityName.split(",")[0];
                    weatherTextView.setText(String.format("Weather in %s: Not available", cityDisplayName));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                String cityDisplayName = cityName.split(",")[0];
                weatherTextView.setText(String.format("Weather in %s: Not available", cityDisplayName));
            }
        });
    }


    private void populateTickets(List<PlaneTicket> tickets) {
        ticketContainer.removeAllViews();

        if (tickets == null || tickets.isEmpty()) {
            Toast.makeText(this, "No tickets found", Toast.LENGTH_SHORT).show();
            return;
        }

        for (PlaneTicket ticket : tickets) {
            View ticketView = LayoutInflater.from(this).inflate(R.layout.item_ticket, ticketContainer, false);

            TextView fromTextView = ticketView.findViewById(R.id.fromTextView);
            TextView toTextView = ticketView.findViewById(R.id.toTextView);
            TextView classTextView = ticketView.findViewById(R.id.classTextView);
            TextView dateTextView = ticketView.findViewById(R.id.dateTextView);
            TextView weatherTextView = ticketView.findViewById(R.id.weatherTextView);
            TextView dateReturnTextView = ticketView.findViewById(R.id.dateReturnTextView);
            TextView weatherToTextView = ticketView.findViewById(R.id.weatherToTextView);

            Button updateButton = ticketView.findViewById(R.id.updateButton);
            Button deleteButton = ticketView.findViewById(R.id.deleteButton);

            fromTextView.setText("From: " + ticket.getFromDestination());
            toTextView.setText("To: " + ticket.getToDestination());
            classTextView.setText("Class: " + ticket.getClassType());
            dateTextView.setText("Departure Date: " + ticket.getDepartureDate());
            if (ticket.isRoundTrip()) {
                dateReturnTextView.setText("Return Date: " + ticket.getReturnDate());
                dateReturnTextView.setVisibility(View.VISIBLE);
                fetchAndSetWeather(ticket.getToDestination() + ",US", weatherToTextView);
                weatherToTextView.setVisibility(View.VISIBLE);
            }

            fetchAndSetWeather(ticket.getFromDestination() + ",US", weatherTextView);
            // Set up Edit button action
            updateButton.setOnClickListener(v -> navigateToUpdateTicket(ticket));
            // Set up Delete button action
            deleteButton.setOnClickListener(v -> confirmAndDeleteTicket(ticket));
            ticketContainer.addView(ticketView);
        }
    }


    private void confirmAndDeleteTicket(PlaneTicket ticket) {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Ticket")
                .setMessage("Are you sure you want to delete this ticket?")
                .setPositiveButton("Yes", (dialog, which) -> deleteTicket(ticket))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteTicket(PlaneTicket ticket) {
        viewModel.deleteTicket(ticket);
        Toast.makeText(this, "Ticket deleted", Toast.LENGTH_SHORT).show();
        //loadUserFutureTickets();
    }

    private void navigateToUpdateTicket(PlaneTicket ticket) {
        TicketSession.getInstance().setCurrentTicketId(ticket.getId());
        Intent intent = new Intent(this, UpdateTicketActivity.class);
        Toast.makeText(this, "Sent to update", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
