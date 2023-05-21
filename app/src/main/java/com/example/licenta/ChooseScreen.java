package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.licenta.client.login_register.LoginClient;
import com.example.licenta.employee.LoginEmployee;
import com.example.licenta.owner.login_register.LoginOwner;

public class ChooseScreen extends AppCompatActivity {

    Button btnClient;
    Button btnOwner;
    Button btnEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_screen);

        btnClient = findViewById(R.id.buttonClient);
        btnOwner = findViewById(R.id.buttonOwner);
        btnEmployee = findViewById(R.id.buttonEmployee);

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseScreen.this, LoginClient.class);
                startActivity(intent);
            }
        });

        btnOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseScreen.this, LoginOwner.class);
                startActivity(intent);

            }
        });

        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseScreen.this, LoginEmployee.class);
                startActivity(intent);
            }
        });

    }
}