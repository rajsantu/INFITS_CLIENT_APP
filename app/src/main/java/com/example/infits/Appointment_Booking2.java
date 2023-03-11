package com.example.infits;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Appointment_Booking2 extends AppCompatActivity{

    FrameLayout confirmBtn;
    Dialog confirmDialog, customDialog;
    ImageView imgBack;

    Intent intent;
    String eventName, addDietitian, appointmentTimeText, description, attachment;
    String selectScheduleText, morningSlotsText, eveningSlotsText, appointmentTypeText;

    private Date selectedDate;

    CalendarAdapter adapter;

    private SwitchCompat phoneRadioButton, videoRadioButton, inpersonRadioButton;

    private List<String> timingList = new ArrayList<>();

    private TextView selectedDateValue;

    Date endOfMonth;

    private String selectedHours = "";
    private String selectedMinutes = "";
    private String selectedAmPm = "";

    FrameLayout customBtnMorningSlot, customBtnEveningSlot;

    private RecyclerView recyclerView;
    private List<Date> dateList;

    CardView confirmCard;

    String url = String.format("%sappointment.php",DataFromDatabase.ipConfig);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking2);

        intent = getIntent();

        eventName = intent.getStringExtra("event_name");
        addDietitian = intent.getStringExtra("add_dietitian");
        appointmentTimeText = intent.getStringExtra("appointmentTime");
        description = intent.getStringExtra("description");
        attachment = intent.getStringExtra("attachment");

        dateList = new ArrayList<>();
        adapter = new CalendarAdapter(dateList);

        timingList.add("PM");
        timingList.add("AM");

        imgBack = findViewById(R.id.imgBackAppointment);
        confirmBtn = findViewById(R.id.confirm_btn);

        confirmDialog = new Dialog(this);
        customDialog = new Dialog(this);

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
                    appointmentTypeText = "Phone";
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
                    appointmentTypeText = "Video Call";
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
                    appointmentTypeText = "In Person";
                }
            }
        });

        View.OnClickListener dayClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout dayLayout = (FrameLayout) v;
                boolean isSelected = dayLayout.isSelected();
                dayLayout.setSelected(!isSelected);
                dayLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), isSelected ? R.drawable.rounded_background_grey : R.drawable.rounded_background));
                TextView dayText = (TextView) dayLayout.getChildAt(0);
                dayText.setTextColor(ContextCompat.getColor(getApplicationContext(), isSelected ? R.color.black : R.color.white));
                TextView dateText = (TextView) dayLayout.getChildAt(1);
                dateText.setTextColor(ContextCompat.getColor(getApplicationContext(), isSelected ? R.color.black : R.color.white));
            }
        };

        View.OnClickListener appointmentTime = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout dayLayout = (FrameLayout) v;

                // Check if this is the now or anytime button
                boolean isNowButton = dayLayout.getId() == R.id.nowBtn;
                boolean isAnytimeButton = dayLayout.getId() == R.id.anyTimeBtn;

                FrameLayout nowButton, anyTimeButton;
                TextView nowText, anytimeText;

                nowButton = findViewById(R.id.nowBtn);
                anyTimeButton = findViewById(R.id.anyTimeBtn);

                anytimeText = anyTimeButton.findViewById(R.id.anyTimeText);
                nowText = nowButton.findViewById(R.id.nowText);

                // If the now button is selected, deselect the anytime button (and vice versa)
                if (isNowButton) {
                    anyTimeButton.setSelected(false);
                    anyTimeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_white));
                    anytimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));

                    nowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));
                    nowText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                } else if (isAnytimeButton) {
                    nowButton.setSelected(false);
                    nowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_white));
                    nowText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));

                    anyTimeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));
                    anytimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }

                // Toggle the selection state of the clicked button
                dayLayout.setSelected(!dayLayout.isSelected());
                dayLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), dayLayout.isSelected() ? R.drawable.rounded_background : R.drawable.rounded_background_white));

                // Check if the child views exist before accessing them
                TextView dayText = null;
                TextView dateText = null;
                if (dayLayout.getChildCount() > 0) {
                    View child0 = dayLayout.getChildAt(0);
                    if (child0 instanceof TextView) {
                        dayText = (TextView) child0;
                    }
                }
                if (dayLayout.getChildCount() > 1) {
                    View child1 = dayLayout.getChildAt(1);
                    if (child1 instanceof TextView) {
                        dateText = (TextView) child1;
                    }
                }

                if (dayText != null) {
                    dayText.setTextColor(ContextCompat.getColor(getApplicationContext(), dayLayout.isSelected() ? R.color.white : R.color.skyBlue));
                }
                if (dateText != null) {
                    dateText.setTextColor(ContextCompat.getColor(getApplicationContext(), dayLayout.isSelected() ? R.color.white : R.color.skyBlue));
                }
            }
        };

        // Morning slot ...
        // Declare a list to hold all the morning slot FrameLayouts
        List<FrameLayout> morningSlots = new ArrayList<>();

        // Find all the morning slot FrameLayouts and add them to the list
        morningSlots.add(findViewById(R.id.morningSlot1));
        morningSlots.add(findViewById(R.id.morningSlot2));
        morningSlots.add(findViewById(R.id.morningSlot3));
        morningSlots.add(findViewById(R.id.morningSlot4));
        morningSlots.add(findViewById(R.id.morningSlot5));

        //TextView
        TextView[] slotTexts = new TextView[6];
        slotTexts[0] = findViewById(R.id.slotText1);
        slotTexts[1] = findViewById(R.id.slotText2);
        slotTexts[2] = findViewById(R.id.slotText3);
        slotTexts[3] = findViewById(R.id.slotText4);
        slotTexts[4] = findViewById(R.id.slotText5);

        // Set the onClickListener for all the morning slot FrameLayouts
        for (FrameLayout morningSlot : morningSlots) {
            morningSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform the same operations on all the morning slot FrameLayouts here
                    for (int i = 0; i < morningSlots.size(); i++) {
                        FrameLayout slot = morningSlots.get(i);

                        slot.setSelected(false);
                        slot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_grey));

                        TextView slotText = slotTexts[i];
                        slotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    }

                    FrameLayout selectedSlot = (FrameLayout) v;
                    selectedSlot.setSelected(true);
                    selectedSlot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));

                    TextView selectedSlotText;
                    switch (selectedSlot.getId()) {
                        case R.id.morningSlot1:
                            selectedSlotText = findViewById(R.id.slotText1);
                            break;
                        case R.id.morningSlot2:
                            selectedSlotText = findViewById(R.id.slotText2);
                            break;
                        case R.id.morningSlot3:
                            selectedSlotText = findViewById(R.id.slotText3);
                            break;
                        case R.id.morningSlot4:
                            selectedSlotText = findViewById(R.id.slotText4);
                            break;
                        case R.id.morningSlot5:
                            selectedSlotText = findViewById(R.id.slotText5);
                            break;
                        default:
                            selectedSlotText = null;
                            break;
                    }

                    if (selectedSlotText != null) {
                        selectedSlotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    }
                    morningSlotsText = selectedSlotText.getText().toString();
                    selectedSlotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
            });
        }

        // Evening slots ...
        List<FrameLayout> eveningSlots = new ArrayList<>();

        eveningSlots.add(findViewById(R.id.eveningSlot1));
        eveningSlots.add(findViewById(R.id.eveningSlot2));
        eveningSlots.add(findViewById(R.id.eveningSlot3));
        eveningSlots.add(findViewById(R.id.eveningSlot4));
        eveningSlots.add(findViewById(R.id.eveningSlot5));

        TextView[] slotText1 = new TextView[6];
        slotText1[0] = findViewById(R.id.slotText7);
        slotText1[1] = findViewById(R.id.slotText8);
        slotText1[2] = findViewById(R.id.slotText9);
        slotText1[3] = findViewById(R.id.slotText10);
        slotText1[4] = findViewById(R.id.slotText11);

        // Set the onClickListener for all the evening slot FrameLayouts
        for (FrameLayout eveningSlot : eveningSlots) {
            eveningSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform the same operations on all the morning slot FrameLayouts here
                    for (int i = 0; i < eveningSlots.size(); i++) {
                        FrameLayout slot = eveningSlots.get(i);

                        slot.setSelected(false);
                        slot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_grey));

                        TextView slotText = slotText1[i];
                        slotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    }

                    FrameLayout selectedSlot = (FrameLayout) v;
                    selectedSlot.setSelected(true);
                    selectedSlot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));

                    TextView selectedSlotText;
                    switch (selectedSlot.getId()) {
                        case R.id.eveningSlot1:
                            selectedSlotText = findViewById(R.id.slotText7);
                            break;
                        case R.id.eveningSlot2:
                            selectedSlotText = findViewById(R.id.slotText8);
                            break;
                        case R.id.eveningSlot3:
                            selectedSlotText = findViewById(R.id.slotText9);
                            break;
                        case R.id.eveningSlot4:
                            selectedSlotText = findViewById(R.id.slotText10);
                            break;
                        case R.id.eveningSlot5:
                            selectedSlotText = findViewById(R.id.slotText11);
                            break;
                        default:
                            selectedSlotText = null;
                            break;
                    }
                    if (selectedSlotText != null) {
                        selectedSlotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    }
                    eveningSlotsText = selectedSlotText.getText().toString();
                    selectedSlotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
            });
        }

        /*
        View.OnClickListener allSlot = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout dayLayout = (FrameLayout) v;
                boolean isSelected = dayLayout.isSelected();
                dayLayout.setSelected(!isSelected);
                dayLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), isSelected ? R.drawable.rounded_background_grey : R.drawable.rounded_background));

                // Check if the child views exist before accessing them
                TextView dayText = null;
                TextView dateText = null;
                if (dayLayout.getChildCount() > 0) {
                    View child0 = dayLayout.getChildAt(0);
                    if (child0 instanceof TextView) {
                        dayText = (TextView) child0;
                    }
                }
                if (dayLayout.getChildCount() > 1) {
                    View child1 = dayLayout.getChildAt(1);
                    if (child1 instanceof TextView) {
                        dateText = (TextView) child1;
                    }
                }

                if (dayText != null) {
                    dayText.setTextColor(ContextCompat.getColor(getApplicationContext(), isSelected ? R.color.black : R.color.white));
                }
                if (dateText != null) {
                    dateText.setTextColor(ContextCompat.getColor(getApplicationContext(), isSelected ? R.color.black : R.color.white));
                }
            }
        };
        */

        // Select Schedule
//        findViewById(R.id.monday).setOnClickListener(dayClickListener);
//        findViewById(R.id.tuesday).setOnClickListener(dayClickListener);
//        findViewById(R.id.wednesday).setOnClickListener(dayClickListener);
//        findViewById(R.id.thursday).setOnClickListener(dayClickListener);
//        findViewById(R.id.friday).setOnClickListener(dayClickListener);
//        findViewById(R.id.saturday).setOnClickListener(dayClickListener);
//        findViewById(R.id.sunday).setOnClickListener(dayClickListener);

//        // Morning slots
//        findViewById(R.id.morningSlot1).setOnClickListener(allSlot);
//        findViewById(R.id.morningSlot2).setOnClickListener(allSlot);
//        findViewById(R.id.morningSlot3).setOnClickListener(allSlot);
//        findViewById(R.id.morningSlot4).setOnClickListener(allSlot);
//        findViewById(R.id.morningSlot5).setOnClickListener(allSlot);
//        findViewById(R.id.morningSlot6).setOnClickListener(allSlot);
//
//        // Evening slots
//        findViewById(R.id.eveningSlot1).setOnClickListener(allSlot);
//        findViewById(R.id.eveningSlot2).setOnClickListener(allSlot);
//        findViewById(R.id.eveningSlot3).setOnClickListener(allSlot);
//        findViewById(R.id.eveningSlot4).setOnClickListener(allSlot);
//        findViewById(R.id.eveningSlot5).setOnClickListener(allSlot);
//        findViewById(R.id.eveningSlot6).setOnClickListener(allSlot);
//

        // Dates rcyclerView

        Date today = new Date();

        // Add some test data to the dateList
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        recyclerView = findViewById(R.id.date_recyclerView);
        recyclerView.setAdapter(adapter);

        selectScheduleText = adapter.getSelectedDate();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Custom Time section
        customBtnMorningSlot = findViewById(R.id.customBtn_morningSlot);
        customBtnEveningSlot = findViewById(R.id.customBtn_EveningSlot);

        customBtnMorningSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTimeDialog();
            }
        });

        customBtnEveningSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTimeDialog();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.getId() == checkedId) {
                        // Enable the clicked RadioButton and set opacity to 100%
                        button.setEnabled(true);
                        button.setAlpha(1f);
                    } else {
                        // Disable the other RadioButtons and set opacity to 50%
                        button.setEnabled(false);
                        button.setAlpha(0.5f);
                    }
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName1 = eventName;
                String dietitianName1 = addDietitian;
                String appintmentTime1 = appointmentTimeText;
                String description1 = "description";
                String attachment1 = "attachment";
                String selectSchedule = selectScheduleText;
                String morningSlots = morningSlotsText;
                String eveningSlots = eveningSlotsText;
                String appointmentType = appointmentTypeText;

                if(!checkIfFieldsAreFilled(eventName1, dietitianName1, appintmentTime1, description1, attachment1, selectSchedule, morningSlots, eveningSlots, appointmentType)) {
                    Toast.makeText(Appointment_Booking2.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
                        System.out.println(response);
                        if (response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Appointment completed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            System.out.println("Response error "+response);
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    },error -> {
                        Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();}){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> data = new HashMap<>();
                            data.put("event_name",eventName1);
                            data.put("add_dietitian",dietitianName1);
                            data.put("appointment_time", appintmentTime1);
                            data.put("description",description1);
                            data.put("attachment", attachment1);
                            data.put("select_schedule", selectSchedule);
                            data.put("morning_slots",morningSlots);
                            data.put("evening_slots",eveningSlots);
                            data.put("appointment_type", appointmentType);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }

                showConfirmDialog();
            }
        });
    }

    private boolean checkIfFieldsAreFilled(
            String eventName1, String dietitianName1, String appointmentTime, String description1, String attachment, String selectSchedule, String morningSlots, String eveningSlots, String appointmentType
    ) {
        return !eventName1.equals("") && !dietitianName1.equals("") && !appointmentTime.equals("") && !description1.equals("") && !attachment.equals("") && !selectSchedule.equals("") && !morningSlots.equals("") &&
                !eveningSlots.equals("") && !appointmentType.equals("");
    }

    private void customTimeDialog(){
//        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        customDialog.setContentView(R.layout.custom_time_for_booking_appointment);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmCard = customDialog.findViewById(R.id.custom_time);
        customDialog.show();

        // Set selected date in TextView
        selectedDateValue = customDialog.findViewById(R.id.date_year_custom_appointment);
        selectedDateValue.setText(adapter.getSelectedDate());

        // Hours
        RecyclerView hoursRV = customDialog.findViewById(R.id.hours_recycler_view);
        List<String> hoursData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String formattedNumber = String.format("%02d", i);
            hoursData.add(formattedNumber);
        }
        hoursRV.setLayoutManager(new CenteredLinearLayoutManager(this));
        NumberedAdapter hoursAdapter = new NumberedAdapter(hoursData, 1);
        hoursRV.setAdapter(hoursAdapter);
//        hoursRV.setRecycledViewPool(recycledViewPool);

        // Minutes
        RecyclerView minutesRV = customDialog.findViewById(R.id.min_recycler_view);
        List<String> minutesData = new ArrayList<>();
        for (int i = 0; i < 60; i+=5) {
            String formattedNumber = String.format("%02d", i);
            minutesData.add(formattedNumber);
        }
        NumberedAdapter minutesAdapter = new NumberedAdapter(minutesData, 5); // Set the interval to 5
        minutesRV.setLayoutManager(new CenteredLinearLayoutManager(this));
        minutesRV.setAdapter(minutesAdapter);
//        minutesRV.setRecycledViewPool(recycledViewPool);

        // AM or PM
        RecyclerView timingRecyclerView = customDialog.findViewById(R.id.timing_recycler_view);
        timingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timingRecyclerView.setAdapter(new TimingAdapter(timingList));

        // Confirm and Cancel Button
        FrameLayout closeBtn = customDialog.findViewById(R.id.custom_time_cancel_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        FrameLayout confirmBtn = customDialog.findViewById(R.id.custom_time_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Get selected hours and minutes
//                String selectedHours = hoursAdapter.getSelectedNumber();
//                String selectedMinutes = minutesAdapter.getSelectedNumber();
//
//                // Get selected AM or PM
//                int selectedTimingPosition = ((LinearLayoutManager) timingRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                String selectedTiming = timingList.get(selectedTimingPosition);
//
//                // Combine the selected values into a time string
//                String selectedTime = selectedHours + ":" + selectedMinutes + " " + selectedTiming;
//
//                // Do something with the selected time, e.g. show in a TextView or pass to another function
//                Log.d("Selected Time", selectedTime);
//
//                // Dismiss the dialog
//                customDialog.dismiss();
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
                View confirmationView = LayoutInflater.from(Appointment_Booking2.this).inflate(R.layout.appointment_book, null);
                Dialog dialog = new Dialog(Appointment_Booking2.this, android.R.style.Theme_Translucent_NoTitleBar);
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