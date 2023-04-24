package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Appointment_Booking2_Now_Warning extends AppCompatActivity {

    FrameLayout go_back_btn;
    TextView dateWarningPage;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking2_now_warning);

        dateWarningPage = findViewById(R.id.date_warning_page);
        backBtn = findViewById(R.id.imgBackAppointment);
        go_back_btn = findViewById(R.id.go_back_btn);

        Intent intent = getIntent();

        if (intent != null) {
            String warningTime = intent.getStringExtra("warningTime");
            if (warningTime != null) {
                Log.i("warningTime", warningTime);
                dateWarningPage.setText(warningTime);
            } else {
                Log.i("warningTime", "warningTime extra data not found in Intent");
            }
        } else {
            Log.i("warningTime", "Intent is null");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Appointment_Booking2.class));
            }
        });

        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Appointment_Booking2.class));
            }
        });
    }
}