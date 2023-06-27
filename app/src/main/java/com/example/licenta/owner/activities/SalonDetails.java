package com.example.licenta.owner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.licenta.R;
import com.example.licenta.owner.others.SalonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SalonDetails extends AppCompatActivity {

    private TextView salonNameTextView;
    private TextView salonStateTextView, salonCityTextView, salonStreetTextView, salonPostalCodeTextView;
    private TextView salonPhoneTextView, salonEmailTextView;
    private TextView salonDescriptionTextView;
    private ImageView salonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_details);

        salonNameTextView = findViewById(R.id.twNameSalon);
        salonStateTextView = findViewById(R.id.twSalonState);
        salonCityTextView = findViewById(R.id.twSalonCity);
        salonStreetTextView = findViewById(R.id.twSalonStreet);
        salonPostalCodeTextView = findViewById(R.id.twPostalCode);
        salonPhoneTextView = findViewById(R.id.twPhoneNumber);
        salonEmailTextView = findViewById(R.id.twEmailAddress);
        salonDescriptionTextView = findViewById(R.id.twDescription);
        salonImageView = findViewById(R.id.imageViewSalon);

        String salonId = getIntent().getStringExtra("salonId");

        DatabaseReference salonRef = FirebaseDatabase.getInstance().getReference().child("salon").child(salonId);
        salonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseData", "Snapshot: " + snapshot.toString());
                if (snapshot.exists()) {
                    SalonModel salonModel = snapshot.getValue(SalonModel.class);
                    Log.d("FirebaseData", "Salon Model: " + salonModel.toString());
                    if (salonModel != null) {

                        salonNameTextView.setText(salonModel.getSalonName());
                        salonStateTextView.setText(salonModel.getSalonState());
                        salonCityTextView.setText(salonModel.getSalonCity());
                        salonStreetTextView.setText(salonModel.getSalonStreet());
                        salonPostalCodeTextView.setText(salonModel.getSalonPostalCode());
                        salonPhoneTextView.setText(salonModel.getSalonPhone());
                        salonEmailTextView.setText(salonModel.getSalonEmail());
                        salonDescriptionTextView.setText(salonModel.getSalonDescription());

                        // Load the salon image using Glide
                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.drawable.loading)
                                .error(R.drawable.error);

                        Glide.with(SalonDetails.this)
                                .load(salonModel.getSalonImage())
                                .apply(requestOptions)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(salonImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
