package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {

    ImageButton imgback;
    SwitchCompat stepSwitch, waterSwitch, sleepSwitch, calorieSwitch;

    SharedPreferences preferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Notification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notification.
     */
    // TODO: Rename and change types and number of parameters
    public static Notification newInstance(String param1, String param2) {
        Notification fragment = new Notification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        imgback = view.findViewById(R.id.imgback);
        stepSwitch = view.findViewById(R.id.step_switch);
        waterSwitch = view.findViewById(R.id.water_switch);
        sleepSwitch = view.findViewById(R.id.sleep_switch);
        calorieSwitch = view.findViewById(R.id.calorie_switch);

        imgback.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_notification_to_settingsFragment));

        preferences = requireActivity().getSharedPreferences("notificationDetails",MODE_PRIVATE);

//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove("stepSwitch");
//        editor.apply();
        boolean isStepChecked = preferences.getBoolean("stepSwitch", false);
        boolean isWaterChecked = preferences.getBoolean("waterSwitch", false);
        boolean isSleepChecked = preferences.getBoolean("sleepSwitch", false);
        boolean isCalorieChecked = preferences.getBoolean("calorieSwitch", false);

        if (isStepChecked) stepSwitch.performClick();
        stepSwitch.setOnCheckedChangeListener(listenerStep);

        if (isWaterChecked) waterSwitch.performClick();
        waterSwitch.setOnCheckedChangeListener(listenerWater);

        if (isSleepChecked) sleepSwitch.performClick();
        sleepSwitch.setOnCheckedChangeListener(listenerSleep);

        if (isCalorieChecked) calorieSwitch.performClick();
        calorieSwitch.setOnCheckedChangeListener(listenerCalorie);

        return view;
    }

    CompoundButton.OnCheckedChangeListener listenerStep = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            SharedPreferences.Editor editor = preferences.edit();
            if (b) {
                // start service
                editor.putBoolean("stepSwitch", true);
            } else {
                // end service
                editor.putBoolean("stepSwitch", false);
            }
            editor.apply();
        }
    };

    CompoundButton.OnCheckedChangeListener listenerWater = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                // start service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("waterSwitch", true);
                editor.apply();
            } else {
                // end service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("waterSwitch", false);
                editor.apply();

                cancelWaterNotification();
            }
        }
    };

    private void cancelWaterNotification() {
        Intent waterReceiverIntent = new Intent(requireContext(), WaterNotificationReceiver.class);
        PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(waterReceiverPendingIntent);
    }

    CompoundButton.OnCheckedChangeListener listenerSleep = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                // start service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sleepSwitch", true);
                editor.apply();
            } else {
                // end service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sleepSwitch", false);
                editor.apply();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener listenerCalorie = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                // start service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("calorieSwitch", true);
                editor.apply();
            } else {
                // end service
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("calorieSwitch", false);
                editor.apply();
            }

        }
    };
}