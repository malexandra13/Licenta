package com.example.licenta.owner.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
                displayBarChart(clientsPerMonth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private String getMonthFromAppointmentDate(String date) {
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

        if (clientsPerMonth.isEmpty()) {
            TextView noAppointmentsTextView = findViewById(R.id.noAppointmentsTextView);
            noAppointmentsTextView.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
        } else {
            TextView noAppointmentsTextView = findViewById(R.id.noAppointmentsTextView);
            noAppointmentsTextView.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);

            BarDataSet dataSet = new BarDataSet(entries, "Numărul de clienți");
            dataSet.setColor(getResources().getColor(R.color.lavender_bright));

            BarData barData = new BarData(dataSet);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setLabelRotationAngle(45);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);

            barChart.setData(barData);
            barChart.invalidate();
        }
    }



}
