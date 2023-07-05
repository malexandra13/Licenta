package com.example.licenta.owner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.licenta.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 5f)); // Ianuarie
        barEntries.add(new BarEntry(1f, 3f)); // Februarie
        barEntries.add(new BarEntry(2f, 7f)); // Martie
        barEntries.add(new BarEntry(3f, 4f)); // Aprilie
        barEntries.add(new BarEntry(4f, 9f)); // Mai
        barEntries.add(new BarEntry(5f, 6f)); // Iunie
        barEntries.add(new BarEntry(6f, 6f)); // Iulie
        barEntries.add(new BarEntry(7f, 0f)); // August
        barEntries.add(new BarEntry(8f, 0f)); // Septembrie
        barEntries.add(new BarEntry(9f, 0f)); // Octombrie
        barEntries.add(new BarEntry(10f, 0f)); // Noiembrie
        barEntries.add(new BarEntry(11f, 0f)); // Decembrie

        BarDataSet barDataSet = new BarDataSet(barEntries, "Clients");

        barDataSet.setColor(Color.parseColor("#8692f7"));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);

        String[] luni = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(luni));

        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);


        barChart.invalidate();
    }
}