package com.example.infits;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WaterReminderFragment extends Fragment {

    ImageView imgBack;
    TextView fromTime, fromAmPm, toTime, toAmPm, remindEveryTime, remindEveryHM, remindTimes, remindTimesTV, remindOnceTime, remindOnceAmPm;
    CheckBox remindEveryRB, remindTimesRB, remindOnceRB;
    Button set, dismiss;

    long pickedFromTime = 0L, pickedToTime = 0L, intervalTime = 0L, timesTime = 0L, remindOnceTimeMillis = 0L;
    long millisInHour = 60 * 60 * 1000;
    long millisInMinute = 60 * 1000;
    long defaultInterval = 60 * 60 * 1000;
    String[] hours, minutes, type, times;

    AlarmManager alarmManager;
    SharedPreferences sharedPreferences;

    PendingIntent waterReceiverPendingIntent;

    public WaterReminderFragment() {
        // Required empty public constructor
    }
    private boolean isAppActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_reminder, container, false);
        hooks(view);
        setFields();

        millisInHour = 60 * 60 * 1000;
        millisInMinute = 60 * 1000;

        hours = new String[] {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24"
        };
        minutes = new String[] {"15", "30", "45"};
        type = new String[] {"hours", "minutes"};
        times = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        sharedPreferences = requireActivity().getSharedPreferences("waterReminderPrefs", Context.MODE_PRIVATE);

        imgBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_waterReminderFragment_to_waterTrackerFragment));

        fromTime.setOnClickListener(v -> timePickerFrom());
        fromAmPm.setOnClickListener(v -> timePickerFrom());

        toTime.setOnClickListener(v -> timePickerTo());
        toAmPm.setOnClickListener(v -> timePickerTo());

        remindEveryRB.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                remindTimesRB.setChecked(false);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindEveryRB", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindEveryRB", false);
                editor.apply();
            }
        });

        remindTimesRB.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                remindEveryRB.setChecked(false);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindTimesRB", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindTimesRB", false);
                editor.apply();
            }
        });

        remindOnceRB.setOnCheckedChangeListener(((compoundButton, b) -> {
            if(b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindOnceRB", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("remindOnceRB", false);
                editor.apply();
            }
        }));

        remindEveryTime.setOnClickListener(v -> showIntervalPicker());
        remindEveryHM.setOnClickListener(v -> showIntervalPicker());

        remindTimes.setOnClickListener(v -> showTimesPicker());
        remindTimesTV.setOnClickListener(v -> showTimesPicker());

        remindOnceTime.setOnClickListener(v -> showTimePickerOnce());
        remindOnceAmPm.setOnClickListener(v -> showTimePickerOnce());

        set.setOnClickListener(v -> {
            if(remindOnceRB.isChecked()) {setOnceAlarm();

            //showInAppNotification();
                }
           else if(pickedFromTime == 0L || pickedToTime == 0L) {

                Toast.makeText(requireActivity(), "Please select a time slot.", Toast.LENGTH_LONG).show();
            } else {

                setAlarm();
            }

        });

        dismiss.setOnClickListener(v -> cancelAlarm());

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        isAppActive = true;
    }
    @Override
    public void onPause() {
        super.onPause();
        isAppActive = false;
    }



    private void showInAppNotification() {
        if (isAppActive) {
            // Inflate your custom in-app notification layout
            View notificationView = LayoutInflater.from(requireContext()).inflate(R.layout.water_in_app_notification, null);

            // Customize the UI elements of the notification view
            // For example:
            TextView name=notificationView.findViewById(R.id.name);
            name.setText("Hey "+DataFromDatabase.name+"!");
            TextView notificationText = notificationView.findViewById(R.id.in_app_item_text);
            notificationText.setText("It's time to drink water!");


            // Add the notification view to the root view of your fragment
            ViewGroup rootView = requireActivity().findViewById(android.R.id.content);

            // Create LayoutParams for positioning the notification at the center of the screen
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            );
            layoutParams.gravity = Gravity.CENTER;

            // Set LayoutParams and add the notification view to the root view
            notificationView.setLayoutParams(layoutParams);
            rootView.addView(notificationView);

            // Optional: Set a timer to remove the notification view after a certain delay
            new Handler().postDelayed(() -> rootView.removeView(notificationView), 10000); // Remove after  5 seconds
        }
    }

    private void setOnceAlarm() {
        createNotificationChannel();

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent waterReceiverIntent = new Intent(requireActivity(), WaterNotificationReceiver.class);
        waterReceiverIntent.putExtra("tracker", "water");
        PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(
                requireActivity(), 410, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Calculate the difference in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        long midnightMillis = calendar.getTimeInMillis();
        long millisecondsPassed = currentTimeMillis - midnightMillis;
        long delayMillis = remindOnceTimeMillis - millisecondsPassed;

        // If the delay is positive, schedule the notification after the delay

        if (delayMillis > 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, remindOnceTimeMillis, waterReceiverPendingIntent);
            new Handler().postDelayed(() -> {
                    // App is not active, show push notification
                    scheduleNotification(remindOnceTimeMillis);

            }, delayMillis);
        }
    }

    private void setFields() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("waterReminderPrefs", Context.MODE_PRIVATE);

        fromTime.setText(sharedPreferences.getString("fromTime", "-:--"));
        fromAmPm.setText(sharedPreferences.getString("fromAmPm", ""));
        toTime.setText(sharedPreferences.getString("toTime", "-:--"));
        toAmPm.setText(sharedPreferences.getString("toAmPm", ""));
        remindEveryTime.setText(sharedPreferences.getString("remindEveryTime", "30"));
        remindEveryHM.setText(sharedPreferences.getString("remindEveryHM", "minutes"));
        remindTimes.setText(sharedPreferences.getString("remindTimes", "6"));
        remindOnceTime.setText(sharedPreferences.getString("remindOnceTime", "10:30"));
        remindOnceAmPm.setText(sharedPreferences.getString("remindOnceAmPm", "AM"));
        remindEveryRB.setChecked(sharedPreferences.getBoolean("remindEveryRB", false));
        remindTimesRB.setChecked(sharedPreferences.getBoolean("remindTimesRB", false));
        remindOnceRB.setChecked(sharedPreferences.getBoolean("remindOnceRB", false));
    }

    private void setAlarm() {
        long alarmInterval = defaultInterval;

        if(remindEveryRB.isChecked()){ alarmInterval = intervalTime;
        }

        else if(remindTimesRB.isChecked()) {alarmInterval = timesTime;
        }

        // set alarm
        createNotificationChannel();

        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent waterReceiverIntent = new Intent(requireActivity(), WaterNotificationReceiver.class);
        waterReceiverIntent.putExtra("tracker", "water");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Calculate the difference in milliseconds
        long currentTimeMillis = System.currentTimeMillis();
        long midnightMillis = calendar.getTimeInMillis();
        long millisecondsPassed = currentTimeMillis - midnightMillis;

        if (remindTimesRB.isChecked())
        {
            for (long i = 0; i < Long.parseLong(remindTimes.getText().toString()); i++)
            {

                long alarmTimeMillis = millisecondsPassed + (i * alarmInterval);
                int requestCode = getNextNotificationId(requireContext());
                waterReceiverPendingIntent = PendingIntent.getBroadcast(
                        requireActivity(), requestCode, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE
                );

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, waterReceiverPendingIntent);
                long delayMillis = (i+1) * alarmInterval; // Calculate the delay in milliseconds

                new Handler().postDelayed(() -> scheduleNotification(alarmTimeMillis), delayMillis);

               // Log.d("setAlarm", "Alarm set for: " + new Date(alarmTimeMillis));
            }
        } else {
            // For remindEveryRB
            long fromTimeMillis = pickedFromTime;
            long toTimeMillis = pickedToTime;

            long nextAlarmTimeMillis = fromTimeMillis;
            nextAlarmTimeMillis += alarmInterval;

            int notificationId = 0;
            int i=0;
            while (nextAlarmTimeMillis <= toTimeMillis ) {
                waterReceiverPendingIntent = PendingIntent.getBroadcast(
                        requireActivity(), notificationId, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE
                );
                long delayMillis=(i+1)*alarmInterval;
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarmTimeMillis, waterReceiverPendingIntent);
                long finalNextAlarmTimeMillis = nextAlarmTimeMillis;
                new Handler().postDelayed(() -> scheduleNotification(finalNextAlarmTimeMillis), delayMillis);
                i++;


               // Log.d("setAlarm", "Alarm sets for: " + new Date(nextAlarmTimeMillis));


                nextAlarmTimeMillis += alarmInterval;

                if (nextAlarmTimeMillis > toTimeMillis) {
                    break; // Terminate the loop when the nextAlarmTimeMillis exceeds toTimeMillis
                }
                notificationId++;
                // Increment the requestCode for unique PendingIntents
            }
        }


        setCancelAlarm();
    }
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String NOTIFICATION_COUNTER_KEY = "notification_counter";

    private int getNextNotificationId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int notificationIdCounter = prefs.getInt(NOTIFICATION_COUNTER_KEY, 0);
        int nextNotificationId = notificationIdCounter++;

        // Increment the counter and save it back to SharedPreferences
        prefs.edit().putInt(NOTIFICATION_COUNTER_KEY, notificationIdCounter).apply();

        return nextNotificationId;
    }

    private void scheduleNotification(long notificationTime) {

            createNotificationChannel();

            Intent waterReceiverIntent = new Intent(requireActivity(), WaterNotificationReceiver.class);
            waterReceiverIntent.putExtra("tracker", "water");

            PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(
                    requireActivity(), 0, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, waterReceiverPendingIntent);

            Log.d("setAlarm", "alarm set for: " + new Date(notificationTime));

            // Create and schedule notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "WaterChannelId")
                    .setContentTitle("Hey " + DataFromDatabase.name)
                    .setContentText("It's time to drink water!")
                    .setSmallIcon(R.mipmap.logo)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(waterReceiverPendingIntent)
                    .setAutoCancel(true);

            int notificationId = getNextNotificationId(requireContext());
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(requireActivity());
            managerCompat.notify(notificationId, builder.build());

        String inAppUrl = String.format("%sinAppNotifications.php", DataFromDatabase.ipConfig);

        String type = "water";
        String text = "It's time to drink water!";

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.format(date);

        StringRequest inAppRequest = new StringRequest(

                Request.Method.POST,
                inAppUrl,
                response -> {

                    if (response.equals("inserted")) Log.d("WaterFragment", "success");
                    else Log.d("WaterFragment", "failure");
                },
                error -> Log.e("WaterFragment",error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("type", type);
                data.put("text", text);
                data.put("date", String.valueOf(date));
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(inAppRequest);
        inAppRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void setCancelAlarm() {
        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, pickedToTime, waterReceiverPendingIntent);
        Toast.makeText(requireActivity(), "Reminder Dismissed", Toast.LENGTH_LONG).show();
    }


    private void cancelAlarm() {
        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(waterReceiverPendingIntent);
        Toast.makeText(requireContext(), "Alarm Dismissed", Toast.LENGTH_LONG).show();
    }

    private void showTimePickerOnce() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerWater)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            remindOnceTimeMillis = pickedHour * millisInHour + pickedMinute * millisInMinute;

            setTextFieldsOnce(pickedHour, pickedMinute);
        });
    }

    private void setTextFieldsOnce(int pickedHour, int pickedMinute) {
        String timeText = "", amPm = "AM";

        if(pickedHour > 12) {
            pickedHour -= 12;
            amPm = "PM";
        }
        if(pickedHour == 12) amPm = "PM";

        timeText = pickedHour + ":";
        if(pickedMinute < 10) {
            timeText += "0" + pickedMinute;
        } else {
            timeText += pickedMinute;
        }

        remindOnceTime.setText(timeText);
        remindOnceAmPm.setText(amPm);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("remindOnceTime", timeText);
        editor.putString("remindOnceAmPm", amPm);
        editor.apply();
    }

    private void showTimesPicker() {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.times_picker);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        NumberPicker timesPicker = dialog.findViewById(R.id.times);
        Button ok = dialog.findViewById(R.id.ok);


        timesPicker.setMinValue(0);
        timesPicker.setMaxValue(times.length - 1);
        timesPicker.setDisplayedValues(times);

        ok.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            remindTimes.setText(times[timesPicker.getValue()]);
            editor.putString("remindTimes", times[timesPicker.getValue()]);

            timesTime = (pickedToTime - pickedFromTime) / Integer.parseInt(times[timesPicker.getValue()]);

            dialog.dismiss();
            editor.apply();
        });

        dialog.show();
    }

    private void showIntervalPicker() {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.interval_picker);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        NumberPicker intervalPicker = dialog.findViewById(R.id.interval);
        NumberPicker typePicker = dialog.findViewById(R.id.type);
        Button ok = dialog.findViewById(R.id.ok);

        intervalPicker.setMinValue(0);
        intervalPicker.setMaxValue(hours.length - 1);
        intervalPicker.setDisplayedValues(hours);

        typePicker.setMinValue(0);
        typePicker.setMaxValue(type.length - 1);
        typePicker.setDisplayedValues(type);

        typePicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            if(typePicker.getValue() == 0) {
                intervalPicker.setDisplayedValues(null);
                intervalPicker.setMinValue(0);
                intervalPicker.setMaxValue(hours.length - 1);
                intervalPicker.setDisplayedValues(hours);
            } else {
                intervalPicker.setDisplayedValues(null);
                intervalPicker.setMinValue(0);
                intervalPicker.setMaxValue(minutes.length - 1);
                intervalPicker.setDisplayedValues(minutes);
            }
        });

        ok.setOnClickListener(it -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            remindEveryHM.setText(type[typePicker.getValue()]);
            editor.putString("remindEveryHM", type[typePicker.getValue()]);

            if(typePicker.getValue() == 0) {
                remindEveryTime.setText(hours[intervalPicker.getValue()]);
                editor.putString("remindEveryTime", hours[intervalPicker.getValue()]);
                intervalTime = Integer.parseInt(hours[intervalPicker.getValue()]) * millisInHour;

            }
            else {
                remindEveryTime.setText(minutes[intervalPicker.getValue()]);
                editor.putString("remindEveryTime", minutes[intervalPicker.getValue()]);
                intervalTime = Integer.parseInt(minutes[intervalPicker.getValue()]) * millisInMinute;

            }
            System.out.println("interval: " + intervalTime);
            dialog.dismiss();
            editor.apply();
        });

        dialog.show();
    }

    private void timePickerTo() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerWater)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            pickedToTime = pickedHour * millisInHour + pickedMinute * millisInMinute;

            setTextFieldsTo(pickedHour, pickedMinute);
        });
    }

    private void setTextFieldsTo(int pickedHour, int pickedMinute) {
        String timeText = "", amPm = "AM";

        if(pickedHour > 12) {
            pickedHour -= 12;
            amPm = "PM";
        }
        if(pickedHour == 12) amPm = "PM";

        timeText = pickedHour + ":";
        if(pickedMinute < 10) {
            timeText += "0" + pickedMinute;
        } else {
            timeText += pickedMinute;
        }

        toTime.setText(timeText);
        toAmPm.setText(amPm);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("toTime", timeText);
        editor.putString("toAmPm", amPm);
        editor.apply();
    }

    private void timePickerFrom() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("SELECT REMINDER TIMING")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTheme(R.style.TimePickerWater)
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "Reminder");

        timePicker.addOnPositiveButtonClickListener(view -> {
            int pickedHour = timePicker.getHour();
            int pickedMinute = timePicker.getMinute();

            long millisInHour = 60 * 60 * 1000;
            long millisInMinute = 60 * 1000;
            pickedFromTime = pickedHour * millisInHour + pickedMinute * millisInMinute;

            setTextFieldsFrom(pickedHour, pickedMinute);
        });
    }

    private void setTextFieldsFrom(int pickedHour, int pickedMinute) {
        String timeText = "", amPm = "AM";

        if(pickedHour > 12) {
            pickedHour -= 12;
            amPm = "PM";
        }
        if(pickedHour == 12) amPm = "PM";

        timeText = pickedHour + ":";
        if(pickedMinute < 10) {
            timeText += "0" + pickedMinute;
        } else {
            timeText += pickedMinute;
        }

        fromTime.setText(timeText);
        fromAmPm.setText(amPm);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fromTime", timeText);
        editor.putString("fromAmPm", amPm);
        editor.apply();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WaterChannelId", "Water Reminder", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = requireActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void hooks(View view) {
        imgBack = view.findViewById(R.id.img_back);
        fromTime = view.findViewById(R.id.from);
        fromAmPm = view.findViewById(R.id.fromAmPm);
        toTime = view.findViewById(R.id.to);
        toAmPm = view.findViewById(R.id.toAmPm);
        remindEveryTime = view.findViewById(R.id.remind_every_time);
        remindEveryHM = view.findViewById(R.id.remind_every_timeHM);
        remindTimes = view.findViewById(R.id.remindTimes);
        remindTimesTV = view.findViewById(R.id.remindTimesTV);
        remindOnceTime = view.findViewById(R.id.remind_once_time);
        remindOnceAmPm = view.findViewById(R.id.remind_once_time_am_pm);
        remindOnceRB = view.findViewById(R.id.remind_once_check);
        remindEveryRB = view.findViewById(R.id.remind_every_check);
        remindTimesRB = view.findViewById(R.id.remind_times_check);
        dismiss = view.findViewById(R.id.dismiss);
        set = view.findViewById(R.id.set);
    }
}