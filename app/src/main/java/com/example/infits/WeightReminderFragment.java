package com.example.infits;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.LocalDateTime;

import Utility.AlarmHelper;

public class WeightReminderFragment extends Fragment {

    ImageView imgBack;
    TextView time, timeAmPm, remindOnceTime, remindOnceAmPm;
    CheckBox checkBox;
    Button dismiss,set;

    AlarmManager alarmManager;

    PendingIntent weightReceiverPendingIntent;

    long remindOnceTimeInMillis = 0L,timeInMillis = 0L;
    private SharedPreferences sharedPreferences;

    public WeightReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_reminder, container, false);

        hooks(view);
        setFields();
        sharedPreferences = requireActivity().getSharedPreferences("WeightReminderPrefs", Context.MODE_PRIVATE);
        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        time.setOnClickListener(v -> showTimePicker());

        timeAmPm.setOnClickListener(v -> showTimePicker());

        remindOnceTime.setOnClickListener(v -> showTimePickerOnce());

        remindOnceAmPm.setOnClickListener(v -> showTimePickerOnce());

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeInMillis ==0L){
                    Toast.makeText(getContext(), "Please select time first", Toast.LENGTH_SHORT).show();
                }else{
                    long todayInMillis = LocalDateTime.now().getMillisOfDay();
                    long timeDiff;
                    if(timeInMillis>todayInMillis){
                        timeDiff = timeInMillis - todayInMillis;
                    }else{
                        timeDiff = todayInMillis - timeInMillis;
                        timeDiff = 24*3600*1000 - timeDiff;
                    }
                    Toast.makeText(getContext(), "set for :"+timeDiff/1000+" sec", Toast.LENGTH_SHORT).show();
                    setAlarm(timeDiff);
                    Navigation.findNavController(v).navigate(R.id.action_weightReminderFragment_to_weightTrackerFragment);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                if (remindOnceTimeInMillis ==0L){
                    Toast.makeText(getContext(), "Please select reminder time first", Toast.LENGTH_SHORT).show();
                }else{
                    long todayInMillis = LocalDateTime.now().getMillisOfDay();
                    long timeDiff;
                    if(remindOnceTimeInMillis>todayInMillis){
                        timeDiff = remindOnceTimeInMillis - todayInMillis;
                    }else{
                        timeDiff = todayInMillis - remindOnceTimeInMillis;
                        timeDiff = 24*3600*1000 - timeDiff;
                    }
                    Toast.makeText(getContext(), "set for :"+timeDiff/1000+" sec", Toast.LENGTH_SHORT).show();
                    setOnceAlarm(timeDiff);
                    Toast.makeText(getContext(), "Alarm set!", Toast.LENGTH_SHORT).show();
                }
            } else {
                cancelOnceAlarm();
            }
        }));

        dismiss.setOnClickListener(v -> {
            dismissAlarm();
            cancelOnceAlarm();
            Navigation.findNavController(v).navigate(R.id.action_weightReminderFragment_to_weightTrackerFragment);
        });

        return view;
    }

    private void setFields() {
        sharedPreferences = requireActivity().getSharedPreferences("WeightReminderPrefs", Context.MODE_PRIVATE);

        time.setText(sharedPreferences.getString("get_weightTime","--:--"));
        timeAmPm.setText(sharedPreferences.getString("get_weightTime_am_pm",""));
        remindOnceTime.setText(sharedPreferences.getString("get_weightTimeOnce","09:30"));
        remindOnceAmPm.setText(sharedPreferences.getString("get_weightTimeOnce_am_pm","AM"));
        checkBox.setChecked(sharedPreferences.getBoolean("isChecked",false));
        Toast.makeText(getActivity(), "data retrieved!", Toast.LENGTH_SHORT).show();

        timeInMillis = (long) sharedPreferences.getFloat("timeInMillis",0L);
        remindOnceTimeInMillis = (long) sharedPreferences.getFloat("remindOnceTimeInMillis",0L);
    }


    private void dismissAlarm() {
        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }
        Intent weightReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent weightReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 3001, weightReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(weightReceiverPendingIntent);
        Toast.makeText(requireContext(), "Alarm Dismissed", Toast.LENGTH_LONG).show();
    }

    private void cancelOnceAlarm() {
        Intent weightReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent weightReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 3000, weightReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(weightReceiverPendingIntent);
    }

    private void showTimePickerOnce() {
        checkBox.setChecked(false);
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerWeight)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            remindOnceTimeInMillis = pickedHour * millisInHour + pickedMinute * millisInMinute;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("remindOnceTimeInMillis",remindOnceTimeInMillis);
            editor.apply();
            setTextFieldsOnce(pickedHour, pickedMinute);
        });
    }

    private void showTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerWeight)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();
            AlarmHelper.createNotificationChannel(getContext(),"WeightChannelId","Weight Reminder");
            AlarmHelper.setTrackerAlarm(getContext(),"weight",pickedHour,pickedMinute);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("timeInMillis",timeInMillis);
            editor.apply();

            setTextFields(pickedHour, pickedMinute);
        });
    }

    private void setTextFieldsOnce(int pickedHour, int pickedMinute) {
        String timeText = "", amPm = "AM";

        if(pickedHour > 12) {
            pickedHour -= 12;
            amPm = "PM";
        }

        timeText = pickedHour + ":";
        if(pickedMinute < 10) {
            timeText += "0" + pickedMinute;
        } else {
            timeText += pickedMinute;
        }

        remindOnceTime.setText(timeText);
        remindOnceAmPm.setText(amPm);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("get_weightTimeOnce",timeText);
        editor.putString("get_weightTimeOnce_am_pm",amPm);
        editor.apply();
    }

    private void setTextFields(int pickedHour, int pickedMinute) {
        String timeText = "", amPm = "AM";

        if(pickedHour > 12) {
            pickedHour -= 12;
            amPm = "PM";
        }

        timeText = pickedHour + ":";
        if(pickedMinute < 10) {
            timeText += "0" + pickedMinute;
        } else {
            timeText += pickedMinute;
        }

        time.setText(timeText);
        timeAmPm.setText(amPm);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("get_weightTime",timeText);
        editor.putString("get_weightTime_am_pm",amPm);
        editor.apply();
    }

    private void setOnceAlarm(long time) {
AlarmHelper.createNotificationChannel(getContext(),"WeightChannelId","Weight Reminder");
        long timeInMillis = System.currentTimeMillis() + time;

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent weightReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        weightReceiverIntent.putExtra("tracker", "weight");

        weightReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 3001, weightReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, weightReceiverPendingIntent);
        Log.d("setOnceAlarm", "alarm set for reminder");
    }


    private void setAlarm(long time) {
AlarmHelper.createNotificationChannel(getContext(),"WeightChannelId","Weight Reminder");
        long timeInMillis = System.currentTimeMillis() + time;

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent weightReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        weightReceiverIntent.putExtra("tracker", "weight");

        weightReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 3000, weightReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, weightReceiverPendingIntent);
        Log.d("setAlarm", "alarm set");
    }

    private void cancelAlarm() {
        Intent weightReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent weightReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, weightReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(weightReceiverPendingIntent);
    }


    private void hooks(View view) {
        set = view.findViewById(R.id.set);
        imgBack = view.findViewById(R.id.img_back);
        time = view.findViewById(R.id.sleep_time);
        timeAmPm = view.findViewById(R.id.sleep_time_am_pm);
        remindOnceTime = view.findViewById(R.id.remind_once_time);
        remindOnceAmPm = view.findViewById(R.id.remind_once_time_am_pm);
        checkBox = view.findViewById(R.id.checkbox);
        dismiss = view.findViewById(R.id.dismiss);
    }
}