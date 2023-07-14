package com.example.licenta.owner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.licenta.R;
import com.example.licenta.owner.others.Salon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SalonDetails extends AppCompatActivity {

    private TextView salonNameTextView;
    private TextView salonCountyTextView, salonCityTextView, salonStreetTextView, salonPostalCodeTextView;
    private TextView salonPhoneTextView, salonEmailTextView;
    private TextView salonDescriptionTextView;
    private ImageView salonImageView;
    private Button buttonAddServices;
    private Button buttonAddEmployees;
    private Button buttonViewServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_details);

        buttonAddServices = findViewById(R.id.buttonAddService);
        buttonAddEmployees = findViewById(R.id.buttonAddEmployee);
        buttonViewServices = findViewById(R.id.buttonViewServices);

        salonNameTextView = findViewById(R.id.twNameSalon);
        salonCountyTextView = findViewById(R.id.twSalonCounty);
        salonCityTextView = findViewById(R.id.twSalonCity);
        salonStreetTextView = findViewById(R.id.twSalonStreet);
        salonPostalCodeTextView = findViewById(R.id.twPostalCode);
        salonPhoneTextView = findViewById(R.id.twPhoneNumber);
        salonEmailTextView = findViewById(R.id.twEmailAddress);
        salonDescriptionTextView = findViewById(R.id.twDescription);
        salonImageView = findViewById(R.id.imageViewSalon);

        String salonId = getIntent().getStringExtra("salonId");

        buttonViewServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalonDetails.this, ViewServicesActivity.class);
                intent.putExtra("salonId", salonId);
                startActivity(intent);
            }
        });

        buttonAddEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalonDetails.this, AddEmployee.class);
                intent.putExtra("salonId", salonId);
                startActivity(intent);
            }
        });

        buttonAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalonDetails.this, AddServiceActivity.class);
                intent.putExtra("salonId", salonId);
                startActivity(intent);
            }
        });

        DatabaseReference salonRef = FirebaseDatabase.getInstance().getReference().child("salon");
        salonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseData", "Snapshot: " + snapshot.toString());
                if (snapshot.exists()) {
                    for (DataSnapshot salonSnapshot : snapshot.getChildren()) {
                        Salon salon = salonSnapshot.getValue(Salon.class);
                        if (salon != null && salon.getSalonId().equals(salonId)) {
                            salonNameTextView.setText(salon.getSalonName());
                            salonCountyTextView.setText(salon.getSalonCounty());
                            salonCityTextView.setText(salon.getSalonCity());
                            salonStreetTextView.setText(salon.getSalonStreet());
                            salonPostalCodeTextView.setText(salon.getSalonPostalCode());
                            salonPhoneTextView.setText(salon.getSalonPhone());
                            salonEmailTextView.setText(salon.getSalonEmail());
                            salonDescriptionTextView.setText(salon.getSalonDescription());

                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.error);

                            Glide.with(SalonDetails.this)
                                    .load(salon.getSalonImage())
                                    .apply(requestOptions)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(salonImageView);

                            break;
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
