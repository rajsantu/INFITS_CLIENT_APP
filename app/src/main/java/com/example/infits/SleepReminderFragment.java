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
        // Inflate the layout for this fragment
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
                    Navigation.findNavController(v).navigate(R.id.action_sleepReminderFragment_to_sleepTrackerFragment);
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

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            long OnceTimeInMillis = pickedHour * millisInHour + pickedMinute * millisInMinute;

            long currentMillisOfDayOnce = LocalDateTime.now().getMillisOfDay();

            if(OnceTimeInMillis<currentMillisOfDayOnce){
                long currentMillisOfDayOnceExtra =currentMillisOfDayOnce + 24*60*60*1000;
                long diff = currentMillisOfDayOnce - OnceTimeInMillis;
                timeDiffOnce = (currentMillisOfDayOnceExtra - diff)/2;
                Toast.makeText(getActivity(), "time : "+timeDiffOnce/1000, Toast.LENGTH_SHORT).show();
                //setAlarm(timeDiffOnce);
            }else {
                timeDiffOnce = (OnceTimeInMillis-currentMillisOfDayOnce);
                Toast.makeText(getActivity(),String.valueOf(timeDiffOnce/1000), Toast.LENGTH_SHORT).show();
                //  setAlarm(timeDiffOnce);
            }
            setTextFieldsOnce(pickedHour, pickedMinute);

        });
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
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            long timeInMillis =  pickedHour * millisInHour + pickedMinute * millisInMinute;

            long currentMillisOfDay = LocalDateTime.now().getMillisOfDay();

            long timeDiff;

            if(timeInMillis<currentMillisOfDay){
                long currentMillisOfDayOnceExtra =currentMillisOfDay + 24*60*60*1000;
                long diff = currentMillisOfDay - timeInMillis;
                timeDiff = (currentMillisOfDayOnceExtra - diff)/2;
                Toast.makeText(getActivity(), "time : "+timeDiff/1000, Toast.LENGTH_SHORT).show();
                setAlarm(timeDiff);
            }else {
                timeDiff = (timeInMillis-currentMillisOfDay);
                Toast.makeText(getActivity(),String.valueOf(timeDiff/1000), Toast.LENGTH_SHORT).show();
                setAlarm(timeDiff);
            }
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

    private void setAlarm(long time) {
        createNotificationChannel();

        long timeInMillis =time + System.currentTimeMillis();

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent sleepReceiverIntent = new Intent(requireActivity(), NotificationReceiver.class);
        sleepReceiverIntent.putExtra("tracker", "sleep");

        sleepReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 0, sleepReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sleepReceiverPendingIntent);
        Log.d("setAlarm", "alarm set");
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

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("SleepChannelId", "Sleep Reminder", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = requireActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
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