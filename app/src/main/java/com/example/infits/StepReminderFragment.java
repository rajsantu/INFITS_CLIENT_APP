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

import java.util.Calendar;

import Utility.AlarmHelper;

public class StepReminderFragment extends Fragment {

    ImageView imgBack;
    TextView time, timeAmPm, remindOnceTime, remindOnceAmPm;
    CheckBox checkBox;
    Button dismiss,set;

    AlarmManager alarmManager;

    PendingIntent stepReceiverPendingIntent;

    long remindOnceTimeInMillis = 0L,timeDiff=0L,timeDiffOnce=0L;

    long OneDayInMillis = 24*3600*1000;
    SharedPreferences sharedPreferences;

    public StepReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_reminder, container, false);

        hooks(view);
        setFields();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        imgBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_stepReminderFragment_to_stepTrackerFragment));

        time.setOnClickListener(v -> showTimePicker());

        timeAmPm.setOnClickListener(v -> showTimePicker());

        remindOnceTime.setOnClickListener(v -> showTimePickerOnce());

        remindOnceAmPm.setOnClickListener(v -> showTimePickerOnce());

        sharedPreferences = getActivity().getSharedPreferences("StepReminderPrefs", Context.MODE_PRIVATE);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeDiff == 0L){
                    Toast.makeText(getContext(), "Please set Alarm first", Toast.LENGTH_SHORT).show();
                }else{
                    setAlarm(timeDiff);
                    Toast.makeText(getContext(), "set: "+timeDiff/1000, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.action_stepReminderFragment_to_stepTrackerFragment);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                editor.putBoolean("isChecked",true);
                editor.apply();

                if(timeDiffOnce == 0L){
                    Toast.makeText(getContext(), "Please select reminder time", Toast.LENGTH_SHORT).show();
                }else {
                    setOnceAlarm(timeDiffOnce);
                }

            } else {
                editor.putBoolean("isChecked",false);
                editor.apply();
                cancelOnceAlarm();
            }
        }));

        dismiss.setOnClickListener(v -> {
            dismissAlarm();
            Navigation.findNavController(v).navigate(R.id.action_stepReminderFragment_to_stepTrackerFragment);
        });

        return view;
    }

    private void setFields() {
        sharedPreferences = requireActivity().getSharedPreferences("StepReminderPrefs", Context.MODE_PRIVATE);

        time.setText(sharedPreferences.getString("get_stepTime","--:--"));
        timeAmPm.setText(sharedPreferences.getString("get_StepTime_am_pm","--"));
        remindOnceTime.setText(sharedPreferences.getString("get_stepTimeOnce","--:--"));
        remindOnceAmPm.setText(sharedPreferences.getString("get_StepTimeOnce_am_pm","--"));
        checkBox.setChecked(sharedPreferences.getBoolean("isChecked",false));
        Toast.makeText(getActivity(), "data retrieved!", Toast.LENGTH_SHORT).show();

    }

    private void dismissAlarm() {
        Intent stepReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent stepReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, stepReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(stepReceiverPendingIntent);
        Toast.makeText(requireContext(), "Alarm Dismissed", Toast.LENGTH_LONG).show();
    }

    private void cancelOnceAlarm() {
        Intent stepReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent stepReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 1, stepReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(stepReceiverPendingIntent);
    }

    private void showTimePickerOnce() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerStep)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            remindOnceTimeInMillis = pickedHour * millisInHour + pickedMinute * millisInMinute;

            long todayTimeInMillis = LocalDateTime.now().getMillisOfDay();

            if(remindOnceTimeInMillis>todayTimeInMillis){
                timeDiffOnce  =remindOnceTimeInMillis - todayTimeInMillis;
            }else {
                long timeD  =remindOnceTimeInMillis - todayTimeInMillis;
                timeDiffOnce = OneDayInMillis - timeD;
            }

            if(checkBox.isChecked()){
                setOnceAlarm(timeDiffOnce);
                Toast.makeText(getContext(), "set: "+timeDiffOnce/1000, Toast.LENGTH_SHORT).show();
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("remindOnceTime",remindOnceTimeInMillis);
            editor.putFloat("getTimeDiffOnce",timeDiffOnce);
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
                .setTheme(R.style.TimePickerStep)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();
            Log.d("alerrt dialog ok button clicked","alert");
            AlarmHelper.createNotificationChannel(getContext(),"StepChannelId","Step Reminder");
            AlarmHelper.setTrackerAlarm(getContext(),"step",pickedHour,pickedMinute);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("getTimeDiff",timeDiffOnce);
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
        editor.putString("get_stepTimeOnce",timeText);
        editor.putString("get_StepTimeOnce_am_pm",amPm);
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
        editor.putString("get_stepTime",timeText);
        editor.putString("get_StepTime_am_pm",amPm);
        editor.apply();
    }

    private void setAlarm(long time) {

        time = System.currentTimeMillis() + time;

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent stepReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        stepReceiverIntent.putExtra("tracker", "step");

        stepReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 0, stepReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, stepReceiverPendingIntent);
        Log.d("setAlarm", "alarm set");
    }

    private void setOnceAlarm(long time) {
//        createNotificationChannel();

        long timeInMilli = System.currentTimeMillis() + time;

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent stepReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        stepReceiverIntent.putExtra("tracker", "step");

        stepReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 1001, stepReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilli, stepReceiverPendingIntent);
        Log.d("setAlarm", "alarm set");
    }


    private void hooks(View view) {
        set = view.findViewById(R.id.set);
        imgBack = view.findViewById(R.id.img_back);
        time = view.findViewById(R.id.time);
        timeAmPm = view.findViewById(R.id.timeAmPm);
        remindOnceTime = view.findViewById(R.id.remind_once_time);
        remindOnceAmPm = view.findViewById(R.id.remind_once_time_am_pm);
        checkBox = view.findViewById(R.id.checkbox);
        dismiss = view.findViewById(R.id.dismiss);
    }
}