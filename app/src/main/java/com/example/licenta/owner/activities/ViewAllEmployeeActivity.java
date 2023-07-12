package com.example.licenta.owner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.owner.others.Employee;
import com.example.licenta.owner.others.EmployeeAdapter;
import com.example.licenta.owner.others.Salon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllEmployeeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TextView textNoEmployees;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employee);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = currentUser.getUid();

        textNoEmployees = findViewById(R.id.noEmployee);
        recyclerView = findViewById(R.id.recyclerViewEmployee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        db = FirebaseFirestore.getInstance();

        db.collection("employee")
                .whereEqualTo("ownerId", currentUserId)
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
                        employee.setSalonId(documentSnapshot.getString("salonId"));
                        employee.setUserType(documentSnapshot.getString("userType"));

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
                    // Handle failure to fetch employee data
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

                // Associate salon data with employees
                for (Employee employee : employeeList) {
                    String salonId = employee.getSalonId();
                    for (Salon salon : salonList) {
                        if (salon.getSalonId().equals(salonId)) {
                            employee.setSalon(salon);
                            break;
                        }
                    }
                }

                // Set up the adapter and RecyclerView
                EmployeeAdapter adapter = new EmployeeAdapter(employeeList, salonList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


}

