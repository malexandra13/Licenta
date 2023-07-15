package com.example.licenta.owner.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.R;
import com.example.licenta.client.others.Appointment;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("appointments");

        fetchAppointmentsDataForSalon(getIntent().getStringExtra("salonId"));
    }

    private void fetchAppointmentsDataForSalon(String salonId) {
        databaseReference.orderByChild("salonId").equalTo(salonId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Integer> clientsPerMonth = new HashMap<>();
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        String month = getMonthFromAppointmentDate(appointment.getDate());
                        clientsPerMonth.put(month, clientsPerMonth.getOrDefault(month, 0) + 1);
                    }
                }

                // Display the bar chart
                displayBarChart(clientsPerMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
    }


    private String getMonthFromAppointmentDate(String date) {
        Log.d("DEBUG", "getMonthFromAppointmentDate - Date: " + date);
        return date.substring(0, 7);
    }


    private void displayBarChart(Map<String, Integer> clientsPerMonth) {
        BarChart barChart = findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : clientsPerMonth.entrySet()) {
            String month = entry.getKey();
            int numClients = entry.getValue();

            entries.add(new BarEntry(index++, numClients));
            labels.add(month);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Number of Clients");
        dataSet.setColor(getResources().getColor(R.color.lavender_bright));

        BarData barData = new BarData(dataSet);

        // Set custom X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

// Rotate X-axis labels if necessary (optional)
        xAxis.setLabelRotationAngle(45);

// Set the position of X-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Add this line to force the interval to be 1 between labels

        barChart.setData(barData);
        barChart.invalidate();

    }


}
