package com.example.licenta.client.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.owner.others.Service;
import com.example.licenta.owner.others.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemClickListener {

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
        serviceAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(serviceAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("services");

        String salonId = getIntent().getStringExtra("salonId");
        String department = getIntent().getStringExtra("selectedDepartment");
        Log.d("ChooseServiceActivity", "onCreate: " + department);

        databaseReference.orderByChild("salonId").equalTo(salonId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceAdapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service.getServiceDepartment().equals(department)) {
                        serviceAdapter.addService(service);
                    }
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

    @Override
    public void onItemClick(int position) {
        Service service = serviceAdapter.getService(position);
        Intent intent = new Intent(ChooseServiceActivity.this, ChooseEmployeeActivity.class);
        intent.putExtra("serviceId", service.getServiceId());
        intent.putExtra("salonId", getIntent().getStringExtra("salonId"));
        intent.putExtra("salonName", getIntent().getStringExtra("salonName"));
        intent.putExtra("selectedDepartment", getIntent().getStringExtra("selectedDepartment"));
        intent.putExtra("price", service.getServicePrice());
        intent.putExtra("serviceName", service.getServiceName());
        intent.putExtra("startHour", getIntent().getStringExtra("startHour"));
        intent.putExtra("endHour", getIntent().getStringExtra("endHour"));
        startActivity(intent);
    }
}