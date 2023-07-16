package com.example.licenta.owner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.licenta.owner.others.SalonAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalonStatisticsActivity extends AppCompatActivity implements SalonAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private SalonAdapter adapter;
    private List<Salon> salonList;
    private DatabaseReference salonRef;
    TextView emptyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_statistics);

        emptyMessageTextView = findViewById(R.id.emptyMessageTextView);

        recyclerView = findViewById(R.id.recyclerView);
        salonList = new ArrayList<>();
        adapter = new SalonAdapter(salonList, this);
        adapter.setOnItemClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        salonRef = FirebaseDatabase.getInstance().getReference().child("salon");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String accountId = currentUser.getUid();

            salonRef.orderByChild("accountId").equalTo(accountId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Salon salon = dataSnapshot.getValue(Salon.class);
                        salonList.add(salon);
                    }
                    adapter.notifyDataSetChanged();

                    if (salonList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyMessageTextView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyMessageTextView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onItemClick(String salonId) {
        DatabaseReference userSalonRef = FirebaseDatabase.getInstance().getReference().child("user-salons");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userSalonRef.child(userId).child(salonId).setValue(true);

        Intent intent = new Intent(SalonStatisticsActivity.this, StatisticsActivity.class);
        intent.putExtra("salonId", salonId);
        startActivity(intent);
    }
}