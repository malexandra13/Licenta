package com.example.licenta.client.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.owner.others.Employee;
import com.example.licenta.owner.others.EmployeeAdapter;
import com.example.licenta.owner.others.Salon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseEmployeeActivity extends AppCompatActivity implements EmployeeAdapter.OnItemClickListener {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TextView textNoEmployees;
    EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_employee);

        textNoEmployees = findViewById(R.id.noEmployee);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        db = FirebaseFirestore.getInstance();
        String selectedDepartment = getIntent().getStringExtra("selectedDepartment");

        db.collection("employee")
                .whereEqualTo("department", selectedDepartment)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Employee> employeeList = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Employee employee = new Employee();
                        employee.setDepartment(documentSnapshot.getString("department"));
                        employee.setEmail(documentSnapshot.getString("email"));
                        employee.setFirstName(documentSnapshot.getString("firstName"));
                        employee.setLastName(documentSnapshot.getString("lastName"));
                        employee.setOwnerId(documentSnapshot.getString("ownerId"));
                        employee.setPhoneNumber(documentSnapshot.getString("phoneNumber"));
                        employee.setEmployeeId(documentSnapshot.getString("employeeId"));
                        employee.setSalonId(documentSnapshot.getString("salonId"));
                        employee.setUserType(documentSnapshot.getString("userType"));
                        employee.setNivelPregatire(documentSnapshot.getString("nivelPregatire"));


                        employeeList.add(employee);
                    }

                    if (employeeList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        textNoEmployees.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        textNoEmployees.setVisibility(View.GONE);

                        List<String> salonIds = new ArrayList<>();
                        for (Employee employee : employeeList) {
                            salonIds.add(employee.getSalonId());
                        }

                        fetchSalonData(employeeList, salonIds);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    private void fetchSalonData(List<Employee> employeeList, List<String> salonIds) {
        DatabaseReference salonRef = FirebaseDatabase.getInstance().getReference().child("salon");

        salonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Salon> salonList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Salon salon = snapshot.getValue(Salon.class);
                    salonList.add(salon);
                }

                for (Employee employee : employeeList) {
                    String salonId = employee.getSalonId();
                    for (Salon salon : salonList) {
                        if (salon.getSalonId().equals(salonId)) {
                            employee.setSalon(salon);
                            break;
                        }
                    }
                }

                employeeAdapter = new EmployeeAdapter(employeeList, salonList);
                employeeAdapter.setOnItemClickListener(ChooseEmployeeActivity.this);
                recyclerView.setAdapter(employeeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Employee selectedEmployee = employeeAdapter.getItem(position);

        Intent intent = new Intent(ChooseEmployeeActivity.this, ChooseDateActivity.class);
        intent.putExtra("serviceId", getIntent().getStringExtra("serviceId"));
        intent.putExtra("salonId", getIntent().getStringExtra("salonId"));
        intent.putExtra("salonName", getIntent().getStringExtra("salonName"));
        intent.putExtra("employeeId", selectedEmployee.getEmployeeId());
        intent.putExtra("employeeName", selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
        intent.putExtra("selectedDepartment", getIntent().getStringExtra("selectedDepartment"));
        intent.putExtra("price", getIntent().getStringExtra("price"));
        intent.putExtra("serviceName", getIntent().getStringExtra("serviceName"));
        intent.putExtra("startHour", getIntent().getStringExtra("startHour"));
        intent.putExtra("endHour", getIntent().getStringExtra("endHour"));
        startActivity(intent);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("employeeId", selectedEmployee.getEmployeeId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
