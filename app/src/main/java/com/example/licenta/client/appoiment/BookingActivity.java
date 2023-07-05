package com.example.licenta.client.appoiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.licenta.R;
import com.example.licenta.owner.others.SalonAdapter;
import com.example.licenta.owner.others.SalonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SalonModel> recycleList;
    FirebaseDatabase database;
    Spinner spinnerCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.recyclerView);
        recycleList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        SalonAdapter recycleAdapter = new SalonAdapter(recycleList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recycleAdapter);

        spinnerCountries = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_items_county));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(spinnerAdapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = parent.getItemAtPosition(position).toString();
                List<SalonModel> filteredSalons = filterSalonsByCountry(selectedCountry);
                SalonAdapter adapter = new SalonAdapter(filteredSalons, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        database.getReference().child("salon").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            SalonModel salonModel = dataSnapshot.getValue(SalonModel.class);
                            recycleList.add(salonModel);
                        }
                        recycleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private List<SalonModel> filterSalonsByCountry(String state) {
        List<SalonModel> filteredSalons = new ArrayList<>();
        for (SalonModel salon : recycleList) {
            if (salon.getSalonState().equals(state)) {
                filteredSalons.add(salon);
            }
        }
        return filteredSalons;
    }
}