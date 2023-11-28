package com.example.infits;

import android.annotation.SuppressLint;
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


public class SleepReminderFragment extends Fragment {


    ImageView imgBack;
    TextView time, timeAmPm, remindOnceTime, remindOnceAmPm;
    CheckBox checkBox;
    Button dismiss,set;

    SharedPreferences sharedPreferences;
    AlarmManager alarmManager;
    //  long remindOnceTimeInMillis = 2000L;
    long timeDiffOnce =0;


    PendingIntent sleepReceiverPendingIntent;

    public SleepReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_reminder, container, false);

        hooks(view);
        setFields();

        imgBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_sleepReminderFragment_to_sleepTrackerFragment));

        time.setOnClickListener(v -> showTimePicker());

        timeAmPm.setOnClickListener(v -> showTimePicker());

        remindOnceTime.setOnClickListener(v -> showTimePickerOnce());

        remindOnceAmPm.setOnClickListener(v -> showTimePickerOnce());

        sharedPreferences = getActivity().getSharedPreferences("SleepReminderPrefs", Context.MODE_PRIVATE);

        set.setOnClickListener(v -> {
            if(checkBox.isChecked()){
                // setAlarm(remindOnceTimeInMillis);
                if(timeDiffOnce ==0){
                    Toast.makeText(getActivity(), "please set alarm first", Toast.LENGTH_SHORT).show();
                }else{

                    setAlarmOnce(timeDiffOnce);

                }

            }else {
                cancelOnceAlarm();
                Navigation.findNavController(v).navigate(R.id.action_sleepReminderFragment_to_sleepTrackerFragment);
            }
        });

        checkBox.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked",true);
                editor.apply();
                //   setAlarm(remindOnceTimeInMillis);

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked",false);
                editor.apply();
                cancelOnceAlarm();
            }
        }));

        dismiss.setOnClickListener(v -> {
            dismissAlarm();
        });

        return view;
    }

    private void setFields() {
        sharedPreferences = requireActivity().getSharedPreferences("SleepReminderPrefs", Context.MODE_PRIVATE);

        time.setText(sharedPreferences.getString("get_sleepTime","8:20"));
        timeAmPm.setText(sharedPreferences.getString("get_SleepTime_am_pm","PM"));
        remindOnceTime.setText(sharedPreferences.getString("get_sleepTimeOnce","5:20"));
        remindOnceAmPm.setText(sharedPreferences.getString("get_SleepTimeOnce_am_pm","PM"));
        checkBox.setChecked(sharedPreferences.getBoolean("isChecked",false));
        Toast.makeText(getActivity(), "data retrieved!", Toast.LENGTH_SHORT).show();
    }

    private void dismissAlarm() {
        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(sleepReceiverPendingIntent);
        Toast.makeText(requireContext(), "Alarm Dismissed", Toast.LENGTH_LONG).show();
    }

    private void cancelOnceAlarm() {
        Intent sleepReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent sleepReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 1, sleepReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(sleepReceiverPendingIntent);
    }

    private void showTimePickerOnce() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerSleep)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();
            if(checkBox.isChecked()) {
                AlarmHelper.createNotificationChannel(getContext(),"SleepChannelId","Sleep Reminder");
                setOnceAlarm(pickedHour, pickedMinute);
            }


            else{
                Toast.makeText(getActivity(), "Please check the box to set the alarm", Toast.LENGTH_SHORT).show();
            }
            setTextFieldsOnce(pickedHour, pickedMinute);

        });
    }

    private void setOnceAlarm(int pickedHour, int pickedMinute) {
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent sleepReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        sleepReceiverIntent.putExtra("tracker", "sleep");

        sleepReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 0, sleepReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, pickedHour);
        calendar.set(Calendar.MINUTE, pickedMinute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , sleepReceiverPendingIntent);
    }

    private void showTimePicker() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerSleep)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
//            obtaining user specified hour and minute
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            AlarmHelper.createNotificationChannel(getContext(),"SleepChannelId","Sleep Reminder");
            AlarmHelper.setTrackerAlarm(getContext(),"sleep",pickedHour,pickedMinute);

            setTextFields(pickedHour, pickedMinute);
            Log.d("repeat", pickedHour + "" + pickedMinute);


            
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

        sharedPreferences = requireActivity().getSharedPreferences("SleepReminderPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("get_sleepTimeOnce",timeText);
        editor.putString("get_SleepTimeOnce_am_pm",amPm);
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



        sharedPreferences = requireActivity().getSharedPreferences("SleepReminderPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("get_sleepTime",timeText);
        editor.putString("get_SleepTime_am_pm",amPm);
        editor.apply();
        Toast.makeText(getActivity(), "data stored", Toast.LENGTH_SHORT).show();
    }


        sharedPreferences = requireActivity().getSharedPreferences("SleepReminderPrefs", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("get_sleepTime",timeText);
        editor.putString("get_SleepTime_am_pm",amPm);
        editor.apply();
        Toast.makeText(getActivity(), "data stored", Toast.LENGTH_SHORT).show();
}

    private void setAlarmOnce(long time) {
        createNotificationChannel();

        long timeInMillis =time + System.currentTimeMillis();

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent sleepReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        sleepReceiverIntent.putExtra("tracker", "sleep");

        sleepReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 1, sleepReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sleepReceiverPendingIntent);
        Log.d("setAlarm", "alarm set");

    }


    private void cancelAlarm() {
        Intent sleepReceiverIntent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent sleepReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, sleepReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(sleepReceiverPendingIntent);
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