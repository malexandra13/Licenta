package com.example.licenta.welcome_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.licenta.R;

public class SplashScreen extends AppCompatActivity {

    public static int Splash_Screen_Time = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, ChooseScreen.class);
                startActivity(intent);
                finish();
            }
        }, Splash_Screen_Time);
    }
}