package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondScreen extends AppCompatActivity {

    TextView textViewSkip;
    TextView textViewNext;
    TextView textViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        textViewSkip = findViewById(R.id.twSkip);
        textViewNext = findViewById(R.id.twNext);
        textViewBack = findViewById(R.id.twBack);

        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondScreen.this, ChooseScreen.class);
                startActivity(intent);
            }
        });

        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondScreen.this, ThirdScreen.class);
                startActivity(intent);
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondScreen.this, FirstScreen.class);
                startActivity(intent);
            }
        });
    }


}