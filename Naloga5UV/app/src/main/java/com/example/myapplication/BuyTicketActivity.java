package com.example.myapplication;

import android.content.DialogInterface;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuyTicketActivity extends AppCompatActivity {

    private Spinner spinnerTravelers, spinnerDestination, spinnerClass;
    private DatePicker datePickerDeparture, datePickerReturn;
    private CheckBox checkRoundabout;
    private TextView tvTotalPrice;
    private Button btnBuyTicket;
    private List<String> passengerNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        spinnerTravelers = findViewById(R.id.spinner_travelers);
        spinnerDestination = findViewById(R.id.spinner_destination);
        spinnerClass = findViewById(R.id.spinner_class);
        datePickerDeparture = findViewById(R.id.date_picker_departure);
        datePickerReturn = findViewById(R.id.date_picker_return);
        checkRoundabout = findViewById(R.id.check_roundabout);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnBuyTicket = findViewById(R.id.btn_buy_ticket);
        // Find the spinner by its ID
        final Spinner spinnerTravelers = findViewById(R.id.spinner_travelers);

        setupSpinners();
        // Get references to DatePicker
        datePickerDeparture = findViewById(R.id.date_picker_departure);
        datePickerReturn = findViewById(R.id.date_picker_return);

// Get current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

// Set minimum date for datePickerDeparture to today's date (future dates only)
        datePickerDeparture.setMinDate(calendar.getTimeInMillis());

// Set minimum date for datePickerReturn initially to today's date (dates after departure only)
        datePickerReturn.setMinDate(calendar.getTimeInMillis());

// Set OnDateSetListener for datePickerDeparture to update minimum date for datePickerReturn
        datePickerDeparture.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update minimum date for datePickerReturn
                Calendar minDate = Calendar.getInstance();
                minDate.set(year, monthOfYear, dayOfMonth);
                datePickerReturn.setMinDate(minDate.getTimeInMillis());
            }
        });

// Set OnDateSetListener for datePickerReturn to ensure selected date is after departure
        datePickerReturn.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Ensure selected date is after departure
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                // Check if selected date is before departure date
                if (selectedDate.before(calendar)) {
                    // Reset to departure date
                    datePickerReturn.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
                }
            }
        });


        spinnerTravelers.setOnItemSelectedListener(selectionListener);
        spinnerDestination.setOnItemSelectedListener(selectionListener);
        spinnerClass.setOnItemSelectedListener(selectionListener);
        checkRoundabout.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());



// Create an AlertDialog for selecting the number of passengers
        AlertDialog.Builder numberOfPassengersDialogBuilder = new AlertDialog.Builder(BuyTicketActivity.this);
        numberOfPassengersDialogBuilder.setTitle("Select Number of Passengers");
        numberOfPassengersDialogBuilder.setItems(new CharSequence[]{"1", "2", "3", "4", "5"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int numberOfPassengers = which + 1; // Add 1 because which is zero-based index

                // Update the spinner to reflect the selected number of passengers
                spinnerTravelers.setSelection(which + 1);

                // Loop through the number of passengers to create dialogs
                for (int i = 0; i < numberOfPassengers; i++) {
                    // Create an AlertDialog for entering passenger data
                    AlertDialog.Builder passengerDataDialogBuilder = new AlertDialog.Builder(BuyTicketActivity.this);
                    passengerDataDialogBuilder.setTitle("Enter Passenger Data for Passenger " + (i + 1));

                    // Inflate the layout for the EditText fields
                    View passengerView = getLayoutInflater().inflate(R.layout.dialog_passenger_data_entry, null);

                    // Find EditText fields for passenger data
                    EditText etFirstName = passengerView.findViewById(R.id.et_first_name);
                    EditText etLastName = passengerView.findViewById(R.id.et_last_name);
                    DatePicker datePicker = passengerView.findViewById(R.id.date_picker);

                    // Set the layout containing EditText fields in the AlertDialog
                    passengerDataDialogBuilder.setView(passengerView);

                    // Add buttons to the dialog


// Inside the positive button's onClick method
                    passengerDataDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Retrieve passenger data from EditText fields
                            EditText etFirstName = passengerView.findViewById(R.id.et_first_name);
                            EditText etLastName = passengerView.findViewById(R.id.et_last_name);

                            // Get passenger's first name and last name
                            String firstName = etFirstName.getText().toString();
                            String lastName = etLastName.getText().toString();

                            // Combine first name and last name
                            String passengerName = firstName + " " + lastName;

                            // Add passenger name to the ArrayList
                            passengerNames.add(passengerName);

                            // Clear EditText fields for next entry (optional)
                            etFirstName.setText("");
                            etLastName.setText("");
                        }
                    });

                    passengerDataDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel button clicked, do nothing
                        }
                    });

                    // Show the created dialog
                    passengerDataDialogBuilder.show();
                }
            }
        });

// Show the AlertDialog for selecting the number of passengers
        numberOfPassengersDialogBuilder.show();





        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the credit card dialog layout
                LayoutInflater inflater = LayoutInflater.from(BuyTicketActivity.this);
                View creditCardDialogView = inflater.inflate(R.layout.dialog_credit_card, null);

                // Create the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyTicketActivity.this);
                builder.setView(creditCardDialogView);
                AlertDialog creditCardDialog = builder.create();

                // Find views in the dialog
                EditText etCardNumber = creditCardDialogView.findViewById(R.id.et_card_number);
                EditText etCardHolderName = creditCardDialogView.findViewById(R.id.et_card_holder_name);
                EditText etExpirationDate = creditCardDialogView.findViewById(R.id.et_expiration_date);
                EditText etCVV = creditCardDialogView.findViewById(R.id.et_cvv);
                Button btnPay = creditCardDialogView.findViewById(R.id.btn_pay);

                // Set up the Pay button click listener
                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Construct ticketInfo string
                        // Save ticket information to SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("tickets", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        int ticketCount = sharedPreferences.getInt("ticketCount", 0);
                        StringBuilder ticketInfoBuilder = new StringBuilder();
                        ticketInfoBuilder.append("Ticket #").append(ticketCount + 1).append("\n");
                        ticketInfoBuilder.append("Number of Travelers: ").append(spinnerTravelers.getSelectedItem().toString()).append("\n");
                        ticketInfoBuilder.append("Destination: ").append(spinnerDestination.getSelectedItem().toString()).append("\n");
                        ticketInfoBuilder.append("Departure Date: ").append(datePickerDeparture.getYear()).append("-").append(datePickerDeparture.getMonth() + 1).append("-").append(datePickerDeparture.getDayOfMonth()).append("\n");
                        ticketInfoBuilder.append("Return Date: ").append(datePickerReturn.getYear()).append("-").append(datePickerReturn.getMonth() + 1).append("-").append(datePickerReturn.getDayOfMonth()).append("\n");
                        ticketInfoBuilder.append("Class: ").append(spinnerClass.getSelectedItem().toString()).append("\n");
                        ticketInfoBuilder.append("Roundabout: ").append(checkRoundabout.isChecked() ? "Yes" : "No").append("\n");

                        // Add passenger information to ticketInfo
                        for (int i = 0; i < passengerNames.size(); i++) {
                            ticketInfoBuilder.append("Passenger ").append(i + 1).append(": ").append(passengerNames.get(i)).append("\n");
                        }
                        editor = sharedPreferences.edit();
                        editor.putString("ticket_" + ticketCount, ticketInfoBuilder.toString());
                        editor.putInt("ticketCount", ticketCount + 1);
                        editor.apply();

                        // Dismiss the credit card dialog and show payment successful message
                        creditCardDialog.dismiss();
                        Toast.makeText(BuyTicketActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
                    }
                });

                creditCardDialog.show();
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> travelersAdapter = ArrayAdapter.createFromResource(this,
                R.array.travelers_array, android.R.layout.simple_spinner_item);
        travelersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTravelers.setAdapter(travelersAdapter);

        ArrayAdapter<CharSequence> destinationAdapter = ArrayAdapter.createFromResource(this,
                R.array.destination_array, android.R.layout.simple_spinner_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestination.setAdapter(destinationAdapter);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this,
                R.array.class_array, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
    }

    private AdapterView.OnItemSelectedListener selectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateTotalPrice();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    };

    private void updateTotalPrice() {
        int numberOfTravelers = Integer.parseInt(spinnerTravelers.getSelectedItem().toString());
        String destination = spinnerDestination.getSelectedItem().toString();
        String travelClass = spinnerClass.getSelectedItem().toString();
        boolean isRoundabout = checkRoundabout.isChecked();

        double basePrice;
        switch (destination) {
            case "Frankfurt":
                basePrice = 200;
                break;
            case "Paris":
                basePrice = 300;
                break;
            case "London":
                basePrice = 500;
                break;
            case "Madrid":
                basePrice = 400;
                break;
            case "Barcelona":
                basePrice = 400;
                break;
            case "Zurich":
                basePrice = 300;
                break;
            case "Vienna":
                basePrice = 100;
                break;
            case "Amsterdam":
                basePrice = 200;
                break;
            case "Skopje":
                basePrice = 200;
                break;
            case "Moscow":
                basePrice = 1000;
                break;
            default:
                basePrice = 100;
                break;
        }

        // Update price based on travel class
        double classMultiplier;
        switch (travelClass) {
            case "Economy":
                classMultiplier = 1.0;
                break;
            case "Business":
                classMultiplier = 1.5;
                break;
            case "First":
                classMultiplier = 2.0;
                break;
            default:
                classMultiplier = 1.0;
                break;
        }

        // Calculate price for roundabout trip
        double roundaboutMultiplier = isRoundabout ? 2.0 : 1.0;

        double totalPrice = basePrice * numberOfTravelers * classMultiplier * roundaboutMultiplier;
        tvTotalPrice.setText("Total Price: $" + totalPrice);
    }
}