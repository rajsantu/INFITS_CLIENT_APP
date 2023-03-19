package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment_Booking2_Now extends AppCompatActivity {

    FrameLayout confirmBtn;
    Dialog confirmDialog, customDialog;
    ImageView imgBack;
    private SwitchCompat phoneRadioButton, videoRadioButton, inpersonRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking2_now);

        imgBack = findViewById(R.id.imgBackAppointment);
        confirmBtn = findViewById(R.id.confirm_btn2);

        confirmDialog = new Dialog(this);

        // get the current date
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM, yyyy");
        String formattedDate = formatter.format(today);
        TextView todayDateTextView = findViewById(R.id.today_date_for_appointment);
        todayDateTextView.setText(formattedDate);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Appointment_booking.class));
            }
        });

        phoneRadioButton = findViewById(R.id.phone_radio_button);
        videoRadioButton = findViewById(R.id.video_radio_button);
        inpersonRadioButton = findViewById(R.id.inperson_radio_button);

        phoneRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Turn off other switches
                    videoRadioButton.setChecked(false);
                    inpersonRadioButton.setChecked(false);
                }
            }
        });

        videoRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Turn off other switches
                    phoneRadioButton.setChecked(false);
                    inpersonRadioButton.setChecked(false);
                }
            }
        });

        inpersonRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Turn off other switches
                    phoneRadioButton.setChecked(false);
                    videoRadioButton.setChecked(false);
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

    }

    private void showConfirmDialog() {
        confirmDialog.setContentView(R.layout.confirm_appointment);
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView confirmCard = confirmDialog.findViewById(R.id.confirm_appointment_card);
        confirmDialog.show();

        FrameLayout confirmButton = confirmDialog.findViewById(R.id.confirm_appointment_dialog_confirm_btn);
        FrameLayout cancelButton = confirmDialog.findViewById(R.id.confirm_appointment_dialog_cancel_btn);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View confirmationView = LayoutInflater.from(Appointment_Booking2_Now.this).inflate(R.layout.appointment_book, null);
                Dialog dialog = new Dialog(Appointment_Booking2_Now.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(confirmationView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CardView confirmCard = confirmDialog.findViewById(R.id.appointment_booked);
                dialog.setCancelable(true);
                dialog.show();
                confirmDialog.dismiss();

//                Toast.makeText(Appointment_Booking2.this, "Appointment confirmed", Toast.LENGTH_SHORT).show();
//                confirmDialog.dismiss();
//                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

    }
}