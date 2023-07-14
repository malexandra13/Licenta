package com.example.licenta.client.appoiment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.client.MainClientActivity;

public class SuccessfulPaymentActivity extends AppCompatActivity {

    TextView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        btnClose = findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessfulPaymentActivity.this, MainClientActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}