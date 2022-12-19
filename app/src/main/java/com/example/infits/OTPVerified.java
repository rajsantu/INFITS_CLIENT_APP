package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class OTPVerified extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverified);

        String email = getIntent().getStringExtra("email");

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Intent intent = new Intent(this, ChangePassword.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}