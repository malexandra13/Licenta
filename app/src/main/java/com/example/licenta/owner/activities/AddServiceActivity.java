package com.example.licenta.owner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

public class AddServiceActivity extends AppCompatActivity {

    TextInputEditText textServiceName, textServicePrice, textServiceDescription;
    Spinner spinnerDepartment;
    Button addServiceButton;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference servicesReference = firebaseDatabase.getReference().child("services");
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        textServiceName = findViewById(R.id.serviceName);
        textServicePrice = findViewById(R.id.price);
        textServiceDescription = findViewById(R.id.serviceDescription);
        addServiceButton = findViewById(R.id.addService);
        spinnerDepartment = findViewById(R.id.spinner);

        String salonId = getIntent().getStringExtra("salonId");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_items_department));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Uploading service");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                // Get the values from the input fields
                String serviceName = textServiceName.getText().toString();
                String servicePrice = textServicePrice.getText().toString();
                String serviceDepartment = spinnerDepartment.getSelectedItem().toString();
                String serviceDescription = textServiceDescription.getText().toString();

                // Validate the input fields
                if (TextUtils.isEmpty(serviceName)) {
                    textServiceName.setError("Service name is required");
                    progressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(servicePrice)) {
                    textServicePrice.setError("Price is required");
                    progressDialog.dismiss();
                    return;
                }

                DatabaseReference newServiceRef = servicesReference.push();
                String newServiceId = newServiceRef.getKey();

                newServiceRef.child("serviceName").setValue(serviceName);
                newServiceRef.child("servicePrice").setValue(servicePrice);
                newServiceRef.child("serviceDepartment").setValue(serviceDepartment);
                newServiceRef.child("serviceDescription").setValue(serviceDescription);
                newServiceRef.child("salonId").setValue(salonId)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddServiceActivity.this, "Service added successfully!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddServiceActivity.this, "Failed to add service.", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
            }
        });

    }
}