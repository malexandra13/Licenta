package com.example.licenta.owner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.owner.MainOwnerActivity;
import com.example.licenta.owner.others.SalonModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity {

    TextInputEditText textServiceName, textServicePrice, textServiceDescription;
    Button addServiceButton;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference servicesReference = firebaseDatabase.getReference().child("services");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        textServiceName = findViewById(R.id.serviceName);
        textServicePrice = findViewById(R.id.price);
        textServiceDescription = findViewById(R.id.serviceDescription);
        addServiceButton = findViewById(R.id.addService);

        String salonId = getIntent().getStringExtra("salonId");

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
                String serviceName = textServiceName.getText().toString();
                String servicePrice = textServicePrice.getText().toString();
                String serviceDescription = textServiceDescription.getText().toString();

                DatabaseReference newServiceRef = servicesReference.push();
                String newServiceId = newServiceRef.getKey();

                newServiceRef.child("serviceName").setValue(serviceName);
                newServiceRef.child("servicePrice").setValue(servicePrice);
                newServiceRef.child("serviceDescription").setValue(serviceDescription);
                newServiceRef.child("salonId").setValue(salonId)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddServiceActivity.this, "Service added successfully", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddServiceActivity.this, "Failed to add service", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
            }
        });
    }
}