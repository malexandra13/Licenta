package com.example.licenta.welcome_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.licenta.R;

public class FirstScreen extends AppCompatActivity {

    TextView textViewSkip;
    TextView textViewNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        textViewSkip = findViewById(R.id.twSkip);
        textViewNext = findViewById(R.id.twNext);

        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, ChooseScreen.class);
                startActivity(intent);
            }
        });

        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this, SecondScreen.class);
                startActivity(intent);
            }
        });
    }
}