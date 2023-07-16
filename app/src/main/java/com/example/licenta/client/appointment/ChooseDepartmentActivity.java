package com.example.licenta.client.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.licenta.R;

public class ChooseDepartmentActivity extends AppCompatActivity {

    LinearLayout llNails, llHairstyle, llMakeup, llEyebrow, llHairRemoval, llfacialTreatments;
    private String selectedLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_department);

        llNails = findViewById(R.id.nails);
        llHairstyle = findViewById(R.id.hairstyle);
        llMakeup = findViewById(R.id.makeup);
        llEyebrow = findViewById(R.id.eyebrow);
        llHairRemoval = findViewById(R.id.hairRemoval);
        llfacialTreatments = findViewById(R.id.facialTreatments);

        llNails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Manichiură și Pedichiură";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);
            }
        });

        llEyebrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Pensat";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);

            }
        });

        llfacialTreatments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Tratamente faciale";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);
            }
        });

        llMakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Make Up";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);
            }
        });

        llHairRemoval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Epilare";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);
            }
        });

        llHairstyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLinearLayout = "Coafat";
                Intent intent = new Intent(ChooseDepartmentActivity.this, ChooseCountyActivity.class);
                intent.putExtra("selectedDepartment", selectedLinearLayout);
                startActivity(intent);
            }
        });

    }
}