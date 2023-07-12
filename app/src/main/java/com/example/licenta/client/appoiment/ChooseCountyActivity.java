package com.example.licenta.client.appoiment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.owner.others.Salon;
import com.example.licenta.owner.others.SalonAdapter;
import com.example.licenta.owner.others.Service;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseCountyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SalonAdapter.OnItemClickListener {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private SalonAdapter adapter;

    private Spinner countySpinner;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_county);
        context = this;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        countySpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_items_county,
                android.R.layout.simple_spinner_item
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinner.setAdapter(spinnerAdapter);
        countySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDepartment = getIntent().getStringExtra("selectedDepartment");
        String selectedCounty = parent.getItemAtPosition(position).toString();
        retrieveSalons(selectedDepartment, selectedCounty);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle the case where no department is selected
    }

    private void retrieveSalons(String selectedDepartment, final String selectedCounty) {
        Query query = databaseReference.child("services").orderByChild("serviceDepartment").equalTo(selectedDepartment);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> salonIds = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        salonIds.add(service.getSalonId());
                    }
                }

                querySalons(selectedCounty, salonIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the database query
            }
        });
    }

    private void querySalons(final String selectedCounty, final List<String> salonIds) {
        Query query = databaseReference.child("salon").orderByChild("salonState").equalTo(selectedCounty);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Salon> salonList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Salon salon = snapshot.getValue(Salon.class);
                    if (salon != null && salonIds.contains(salon.getSalonId())) {
                        salonList.add(salon);
                    }
                }

                Log.d("SalonList", "Salon List Size: " + salonList.size());

                adapter = new SalonAdapter(salonList, context);
                adapter.setOnItemClickListener(ChooseCountyActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the database query
            }
        });
    }

    @Override
    public void onItemClick(String salonId) {
        Intent intent = new Intent(this, ClientSalonDetailsActivity.class);
        intent.putExtra("salonId", salonId);
        startActivity(intent);
    }
}




