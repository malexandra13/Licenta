package com.example.licenta.client.appoiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.owner.activities.ViewServicesActivity;
import com.example.licenta.owner.others.Service;
import com.example.licenta.owner.others.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseServiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    DatabaseReference databaseReference;
    TextView textNoServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);

        textNoServices = findViewById(R.id.noServices);
        recyclerView = findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        serviceAdapter = new ServiceAdapter();
        recyclerView.setAdapter(serviceAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("services");

        String salonId = getIntent().getStringExtra("salonId");

        databaseReference.orderByChild("salonId").equalTo(salonId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceAdapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    serviceAdapter.addService(service);
                }

                if (serviceAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    textNoServices.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    textNoServices.setVisibility(View.GONE);
                    serviceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChooseServiceActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}