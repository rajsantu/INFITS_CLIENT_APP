package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appointment_Booking2 extends AppCompatActivity {
    FrameLayout confirmBtn;
    Dialog confirmDialog, customDialog;
    ImageView imgBack, imgCustom;

    String url = String.format("%sappointment1.php", DataFromDatabase.ipConfig);

    String eventName, addDietition, appointmentTimeString, description, attachment, fileType, fileName, selectSchedule, timingSlotsString, appointmentType;
    String timingSlotText, appointmentTypeText;

    Intent intent;

    CalendarAdapter adapter;

    private SwitchCompat phoneRadioButton, videoRadioButton, inpersonRadioButton;

    private List<String> timingList = new ArrayList<>();

    private TextView selectedDateValue, customSlot, customSlotEve;

    Date endOfMonth;

    String selectedHours;
    String selectedMinutes;
    String selectedAmPm;

    FrameLayout customBtnMorningSlot, customBtnEveningSlot, customBtnAfterMorning, customBtnAfterEvening;

    private RecyclerView recyclerView;
    private List<Date> dateList;

    String selectedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking2);

        timingList.add("PM");
        timingList.add("AM");

        imgBack = findViewById(R.id.imgBackAppointment);
        // confirmBtn = findViewById(R.id.confirm_btn1);
        confirmBtn = findViewById(R.id.confirm_btn);

        intent = getIntent();

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
                    appointmentTypeText = "Video call";
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
                    appointmentTypeText = "In person";
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

        // All slots
        // Declare a list to hold all the morning and evening slot FrameLayouts
        List<FrameLayout> allSlots = new ArrayList<>();

        // Find all the morning and evening slot FrameLayouts and add them to the list
        allSlots.add(findViewById(R.id.morningSlot1));
        allSlots.add(findViewById(R.id.morningSlot2));
        allSlots.add(findViewById(R.id.morningSlot3));
        allSlots.add(findViewById(R.id.morningSlot4));
        allSlots.add(findViewById(R.id.morningSlot5));
        allSlots.add(findViewById(R.id.eveningSlot1));
        allSlots.add(findViewById(R.id.eveningSlot2));
        allSlots.add(findViewById(R.id.eveningSlot3));
        allSlots.add(findViewById(R.id.eveningSlot4));
        allSlots.add(findViewById(R.id.eveningSlot5));

        // TextView
        TextView[] slotTexts = new TextView[10];
        slotTexts[0] = findViewById(R.id.slotText1);
        slotTexts[1] = findViewById(R.id.slotText2);
        slotTexts[2] = findViewById(R.id.slotText3);
        slotTexts[3] = findViewById(R.id.slotText4);
        slotTexts[4] = findViewById(R.id.slotText5);
        slotTexts[5] = findViewById(R.id.slotText7);
        slotTexts[6] = findViewById(R.id.slotText8);
        slotTexts[7] = findViewById(R.id.slotText9);
        slotTexts[8] = findViewById(R.id.slotText10);
        slotTexts[9] = findViewById(R.id.slotText11);

        // Set the onClickListener for all the morning and evening slot FrameLayouts
        for (FrameLayout slot : allSlots) {
            slot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform the same operations on all the morning and evening slot FrameLayouts here
                    for (int i = 0; i < allSlots.size(); i++) {
                        FrameLayout currentSlot = allSlots.get(i);
                        TextView slotText = slotTexts[i];

                        currentSlot.setSelected(false);
                        currentSlot.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_grey));
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

                    selectedSlotText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    timingSlotText = selectedSlotText.getText().toString();
                }
            });
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

        // Select Schedule Dates RecyclerView
        Date today = new Date();
        dateList = new ArrayList<>();

        // Add some test data to the dateList
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        recyclerView = findViewById(R.id.date_recyclerView);

        adapter = new CalendarAdapter(dateList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Custom Time section
        customBtnMorningSlot = findViewById(R.id.customBtn_morningSlot);
        customBtnEveningSlot = findViewById(R.id.customBtn_EveningSlot);

        customBtnAfterMorning = findViewById(R.id.customBtn_afterSelected);
        customBtnAfterMorning.setVisibility(View.GONE);

        customBtnAfterEvening = findViewById(R.id.customBtnEve_afterSelected);
        customBtnAfterEvening.setVisibility(View.GONE);

//        customBtnAfterMorning.setVisibility(View.GONE);
        customSlot = findViewById(R.id.slotTextCustom);
        imgCustom = findViewById(R.id.custom_timeSelect);
        customSlotEve = findViewById(R.id.slotTextEveCustom);

        customBtnMorningSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTimeDialog(customSlot);
                customBtnMorningSlot.setVisibility(View.GONE);
                customBtnAfterMorning.setVisibility(View.VISIBLE);
            }
        });

        customBtnAfterMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customBtnMorningSlot.setVisibility(View.GONE);
                customBtnAfterMorning.setVisibility(View.VISIBLE);
                customTimeDialog(customSlot);
            }
        });

        customBtnEveningSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTimeDialog(customSlotEve);
                customBtnEveningSlot.setVisibility(View.GONE);
                customBtnAfterEvening.setVisibility(View.VISIBLE);
            }
        });

        customBtnAfterEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customBtnEveningSlot.setVisibility(View.GONE);
                customBtnAfterEvening.setVisibility(View.VISIBLE);
                customTimeDialog(customSlotEve);
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
    }

    private boolean checkIfFieldsAreFilled(
            String eventName, String addDietition, String appointmentTimeString, String description, String attachment, String selectSchedule, String timingSlotsString, String appointmentType
    ) {
        return !eventName.equals("") && !addDietition.equals("") && !appointmentTimeString.equals("") && !description.equals("") &&
                !selectSchedule.equals("") && !timingSlotsString.equals("") && !appointmentType.equals("");
    }

    private void customTimeDialog(TextView slot) {
//        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

        customDialog.setContentView(R.layout.custom_time_for_booking_appointment);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView confirmCard = customDialog.findViewById(R.id.custom_time);
        customDialog.show();

        // Set selected date in TextView
        TextView selectedDateValue = customDialog.findViewById(R.id.date_year_custom_appointment);
        selectedDateValue.setText(adapter.getSelectedDate());
//        Toast.makeText(this, adapter.getSelectedDate(), Toast.LENGTH_SHORT).show();

        // Hours
        RecyclerView hoursRV = customDialog.findViewById(R.id.hours_recycler_view);
        List<String> hoursData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            String formattedNumber = String.format("%02d", i);
            hoursData.add(formattedNumber);
        }
        NumberedAdapter hoursAdapter = new NumberedAdapter(hoursData);
        hoursRV.setLayoutManager(new CenteredLinearLayoutManager(this));
        hoursRV.setAdapter(hoursAdapter);
//        hoursRV.scrollToPosition(getPosition(hoursRV));
//        hoursRV.setRecycledViewPool(recycledViewPool);

        // Minutes
        RecyclerView minutesRV = customDialog.findViewById(R.id.min_recycler_view);
        List<String> minutesData = new ArrayList<>();
        for (int i = 0; i < 60; i += 5) {
            String formattedNumber = String.format("%02d", i);
            minutesData.add(formattedNumber);
        }
        NumberedAdapter minutesAdapter = new NumberedAdapter(minutesData); // Set the interval to 5
        minutesRV.setLayoutManager(new CenteredLinearLayoutManager(this));
        minutesRV.setAdapter(minutesAdapter);
//        minutesRV.scrollToPosition(minutesAdapter.getPosition(selectedMinutes));
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

                // Get selected hours and minutes
//                String selectedHours = hoursAdapter.get().toString();
                String selectedHours = getSelectedValue(hoursRV);
                String selectedMinutes = getSelectedValue(minutesRV);
//
//                // Get selected AM or PM
                int selectedTimingPosition = ((LinearLayoutManager) timingRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                String selectedTiming = timingList.get(selectedTimingPosition);
//
//                // Combine the selected values into a time string
                selectedTime = selectedHours + ":" + selectedMinutes + " " + selectedTiming;
//                Toast.makeText(Appointment_Booking2.this, selectedTime, Toast.LENGTH_SHORT).show();
//
//                // Do something with the selected time, e.g. show in a TextView or pass to another function
                setTime(slot, selectedTime);
//                Log.d("Selected Time", selectedTime);

//                // Dismiss the dialog
                customDialog.dismiss();
            }
        });
    }

    private String getSelectedValue(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        assert layoutManager != null;
        int selectedPosition = layoutManager.findLastVisibleItemPosition();
        NumberedAdapter adapter = (NumberedAdapter) recyclerView.getAdapter();
        assert adapter != null;
        return adapter.getNumber(selectedPosition);
//        return ""+selectedPosition;
    }

    private void setTime(TextView tv, String time) {
        tv.setText(time);
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

                eventName = intent.getStringExtra("event_name");
                addDietition = intent.getStringExtra("add_dietitian");
                appointmentTimeString = intent.getStringExtra("appointmentTime");
                description = intent.getStringExtra("description");
                attachment = intent.getStringExtra("attachment");
                fileType = intent.getStringExtra("file_type");
                fileName = intent.getStringExtra("file_name");
                selectSchedule = adapter.getSelectedDate();
                timingSlotsString = timingSlotText;
                appointmentType = appointmentTypeText;

                if (!checkIfFieldsAreFilled(eventName, addDietition, appointmentTimeString, description, attachment, selectSchedule, timingSlotsString, appointmentType)) {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.getString("status").equals("success")) {
                                View confirmationView = LayoutInflater.from(Appointment_Booking2.this).inflate(R.layout.appointment_book, null);
                                Dialog dialog = new Dialog(Appointment_Booking2.this, android.R.style.Theme_Translucent_NoTitleBar);
                                dialog.setContentView(confirmationView);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                CardView confirmCard = confirmDialog.findViewById(R.id.appointment_booked);
                                dialog.setCancelable(true);
                                dialog.show();
                                confirmDialog.dismiss();
                                Toast.makeText(Appointment_Booking2.this, "Appointment Booked!", Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println("Response error " + response);
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle the JSONException here
                        }
                    }, error -> {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("event_name", eventName);
                            data.put("add_dietitian", addDietition);
                            data.put("appointment_time", appointmentTimeString);
                            data.put("description", description);
                            data.put("attachment", attachment);
                            data.put("select_schedule", selectSchedule);
                            data.put("timing_slots", timingSlotsString);
                            data.put("appointment_type", appointmentType);
                            data.put("file_type", fileType);
                            data.put("file_name", fileName);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
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