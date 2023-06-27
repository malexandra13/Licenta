package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ThirdScreen extends AppCompatActivity {

    TextView textViewNext;
    TextView textViewBack;
    TextView textViewSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screen);

        textViewNext = findViewById(R.id.twNext);
        textViewBack = findViewById(R.id.twBack);
        textViewSkip= findViewById(R.id.twSkip);

        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdScreen.this, ChooseScreen.class);
                startActivity(intent);
            }
        });

        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdScreen.this, ChooseScreen.class);
                startActivity(intent);
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdScreen.this, SecondScreen.class);
                startActivity(intent);
            }
        });
    }
}