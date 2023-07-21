package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class StepTrackerFragment extends Fragment implements UpdateStepCard {
    Float goalPercent2;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
    Handler handler = new Handler();
    Thread mythread;
    Button setgoal;
    ImageButton imgback;
    TextView steps_label, goal_step_count, distance, calories, speed, Distance_unit;
    ImageView reminder;
    private static final int PERMISSION_REQUEST_BODY_SENSORS = 1;
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1000;
    private static final int PERMISSION_REQUEST_ALL_SENSORS = 100;

    SharedPreferences stepPrefs;

    GaugeSeekBar progressBar;

    static float goalVal;

    float goalPercent = 0;

    UpdateStepCard updateStepCard;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StepTrackerFragment() {

    }

    public static StepTrackerFragment newInstance(String param1, String param2) {
        StepTrackerFragment fragment = new StepTrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        updateStepCard = (UpdateStepCard) context;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getArguments() != null && getArguments().getBoolean("notification") /* coming from notification */) {
                    startActivity(new Intent(getActivity(), DashBoardMain.class));
                    requireActivity().finish();
                } else {
                    Navigation.findNavController(requireActivity(), R.id.imgback).navigate(R.id.action_stepTrackerFragment_to_dashBoardFragment);
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    manager.popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_tracker, container, false);
        steps_label = view.findViewById(R.id.steps_label);
        setgoal = view.findViewById(R.id.setgoal);
        imgback = view.findViewById(R.id.imgback);
        goal_step_count = view.findViewById(R.id.goal_step_count);
        RecyclerView pastActivity = view.findViewById(R.id.past_activity);
        progressBar = view.findViewById(R.id.progressBar);
        speed = view.findViewById(R.id.speed);
        distance = view.findViewById(R.id.distance);
        calories = view.findViewById(R.id.calories);
        reminder = view.findViewById(R.id.reminder);
        Distance_unit = view.findViewById(R.id.distance_unit);

        getPermission_Body();

        //progressBar.setProgress(0.2F);

        stepPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        float goal = stepPrefs.getFloat("goal", 1f);
        var steps = Math.min(stepPrefs.getFloat("steps", 0f), goal);

        float goalPercent = stepPrefs.getFloat("goalPercent", 0f);

        //progressBar.setProgress(goalPercent);
        goal_step_count.setText(String.valueOf((int) goal));

        steps_label.setText(String.valueOf(steps));


        mythread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (goalVal >= FetchTrackerInfos.currentSteps && goalVal != 1 && goalVal != 0) {
                    Log.d("completed", "1");

                    //Toast.makeText(getActivity().getApplicationContext(), "Steps Completed", Toast.LENGTH_SHORT).show();
                    mythread.interrupt();

                }

                steps_label.setText(String.valueOf(FetchTrackerInfos.currentSteps));
                handler.postDelayed(this, 0);

                goalPercent2 = (float) (FetchTrackerInfos.currentSteps) / (int) goalVal;
                progressBar.setProgress(goalPercent2);

                speed.setText(FetchTrackerInfos.Avg_speed.substring(0, 1));


                if (FetchTrackerInfos.Distance > 1) {
                    distance.setText(String.format("%.3f", (FetchTrackerInfos.Distance)));
                    Distance_unit.setText("Km");
                } else {
                    distance.setText(String.format("%.2f", FetchTrackerInfos.Distance * 1000));
                    Distance_unit.setText("Meters");
                }


                calories.setText(String.format("%.2f", (FetchTrackerInfos.Calories)));

            }

        });

        mythread.start();


        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> datas = new ArrayList<>();

//        if (DataFromDatabase.stepsGoal.equals(null)){
//            goal_step_count.setText("5000");
//        }
//        else{
//            goal_step_count.setText(DataFromDatabase.stepsGoal+" ml");
//            try {
//                 = Integer.parseInt(DataFromDatabase.waterGoal);
//            }catch (NumberFormatException ex){
//                goal = 1800;
//                waterGoal.setText(1800+" ml");
//                System.out.println(ex);
//            }
//        }
//
//        if (DataFromDatabase.stepsStr.equals(null)|| DataFromDatabase.stepsStr.equals("null")){
//            steps_label.setText("0");
//        }
//        else{
//            steps_label.setText(DataFromDatabase.waterStr+" ml");
//            try {
//                 = Integer.parseInt(DataFromDatabase.waterStr);
//                waterGoalPercent.setText(String.valueOf(calculateGoal()));
//            }catch (NumberFormatException ex){
//                consumedInDay = 0;
//                waterGoalPercent.setText(String.valueOf(calculateGoal()));
//            }
//        }

        //String url = String.format("%spastActivity.php", DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/pastActivity.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("dattaaaa:", response.toString());
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("steps");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String data = object.getString("steps");
                    String date = object.getString("date");
                    dates.add(date);
                    datas.add(data);
                    System.out.println(datas.get(i));
                    System.out.println(dates.get(i));
                }
                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(), dates, datas, Color.parseColor("#FF9872"));
                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
                pastActivity.setAdapter(ad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Error", error.toString());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientID", DataFromDatabase.clientuserID);
                return data;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        PowerManager powerManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            powerManager = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        }
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FetchTrackerInfos.flag_steps=0;
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.setgoaldialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                EditText goal = dialog.findViewById(R.id.goal);
                Button save = dialog.findViewById(R.id.save_btn_steps);
                FetchTrackerInfos.currentSteps = 0;
                FetchTrackerInfos.flag_steps = 0;


                if (true) {
                    Intent serviceIntent = new Intent(requireContext(), MyService.class);
                    requireActivity().stopService(serviceIntent);
                    mythread.interrupt();
                }


//                steps_label.setText(String.valueOf(0));
                save.setOnClickListener(v -> {
//                    FetchTrackerInfos.previousStep = FetchTrackerInfos.totalSteps;
                    goal_step_count.setText(goal.getText().toString());
                    progressBar.setProgress(0);
                    steps_label.setText(String.valueOf(0));

                    if (goal.getText().toString().equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "fill goal", Toast.LENGTH_SHORT).show();
                    } else
                        goalVal = Integer.parseInt(goal.getText().toString());
                    FetchTrackerInfos.stop_steps = (int) goalVal;

                    SharedPreferences stepPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    SharedPreferences.Editor editor = stepPrefs.edit();
                    editor.putFloat("goal", goalVal);
                    editor.putFloat("steps", 0f);
                    editor.putFloat("goalPercent", 0f);
                    editor.apply();

                    SharedPreferences preferences = requireActivity().getSharedPreferences("notificationDetails", MODE_PRIVATE);
                    boolean stepNotificationPermission = preferences.getBoolean("stepSwitch", true);


                    if (stepNotificationPermission) {
                        // we have permission to run step service
                        if (!foregroundServiceRunning()) {
                            Intent serviceIntent = new Intent(requireContext(), MyService.class);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                serviceIntent.putExtra("goal", goalVal);
                                serviceIntent.putExtra("notificationPermission", stepNotificationPermission);
                                requireContext().startForegroundService(serviceIntent);

                            } else {
                                requireContext().startService(serviceIntent);
                            }


                        }
                    }
                    //String url = String.format("%supdatestepgoal.php", DataFromDatabase.ipConfig);
                    String url = "https://infits.in/androidApi/updatestepgoal.php";
                    final StringRequest requestGoal = new StringRequest(Request.Method.POST, url, response -> {
                        try {
                            Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show();
                            Log.e("goal","success");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", error.toString());
                        Log.e("goal","error");
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("clientuserID",DataFromDatabase.clientuserID );
                            data.put("goal",String.valueOf(goalVal));
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                            LocalDateTime now = LocalDateTime.now();
                            data.put("dateandtime",dtf.format(now));
                            return data;
                        }
                    };
                    Volley.newRequestQueue(requireContext()).add(requestGoal);
                    dialog.dismiss();
                });
                dialog.show();
            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null && getArguments().getBoolean("notification") /* coming from notification */) {
                    startActivity(new Intent(getActivity(), DashBoardMain.class));
                    requireActivity().finish();
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_stepTrackerFragment_to_dashBoardFragment);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.popBackStack();
                }
            }
        });

        reminder.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_stepTrackerFragment_to_stepReminderFragment));

//        final Handler handler = new Handler();
//        final int delay = 1000; // 1000 milliseconds == 1 second
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                System.out.println(FetchTrackerInfos.stepsWalked);
//                if (!FetchTrackerInfos.stepsWalked.isEmpty()){
//                        steps_label.setText(FetchTrackerInfos.currentSteps);
//                }
//                handler.postDelayed(this, delay);
//            }
//        }, delay);
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
            updateStepCard.updateStepCardData(intent);
        }
    };

    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void updateGUI(Intent intent) {
        Log.d("gui", "entered");
        if (intent.getExtras() != null) {
            float steps = intent.getIntExtra("steps", 0);
            Log.i("StepTracker", "Countdown seconds remaining:" + steps);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goalPercent = ((steps / goalVal) * 100) / 100;
                    System.out.println("steps: " + steps);
                    System.out.println("goalVal: " + goalVal);
                    System.out.println("goalPercent: " + goalPercent);
                    progressBar.setProgress(goalPercent);
                    int stepText = (int) Math.min(steps, goalVal);

                    steps_label.setText(String.valueOf((int) stepText));
                    distance.setText(String.format("%.2f", (steps / 1312.33595801f)));
                    calories.setText(String.format("%.2f", (0.04f * steps)));
                    Date date = new Date();



                    SimpleDateFormat hour = new SimpleDateFormat("HH");
                    SimpleDateFormat mins = new SimpleDateFormat("mm");

                    int h = Integer.parseInt(hour.format(date));
                    int m = Integer.parseInt(mins.format(date));

                    int time = h + (m / 60);
                    Toast.makeText(requireContext(), String.valueOf(steps), Toast.LENGTH_SHORT).show();
                    speed.setText(String.format("%.2f", (steps / 1312.33595801f) / time));
                    System.out.println("steps: " + 0.04f * steps);
                    System.out.println("steps/time: " + (steps / 1312.33595801f) / time);

                    //String url = String.format("%supdateStepFragmentDetails.php", DataFromDatabase.ipConfig);
                    String url = "https://infits.in/androidApi/updateStepFragmentDetails.php";
                    StringRequest stringRequest =  new StringRequest(Request.Method.POST,url,response -> {
                        Log.e("calorieUpdate","success");
                    },
                            error -> {
                                Log.e("calorieUpdate","fail");
                                Log.e("calorieUpdate",error.toString());
                            }){
                        @SuppressLint("DefaultLocale")
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("clientuserID",DataFromDatabase.clientuserID);
                            data.put("steps",String.valueOf(steps));
                            data.put("distance",String.valueOf(steps / 1312.33595801f));
                            data.put("calories",String.valueOf(0.04f * steps));
                            data.put("avgspeed",String.format("%.2f", (steps / 1312.33595801f) / time));
                            data.put("goal",goal_step_count.getText().toString());
                            LocalDateTime now = LocalDateTime.now();
                            data.put("dateandtime",dtf.format(now));
                            return data;
                        }
                    };
                    Volley.newRequestQueue(requireContext()).add(stringRequest);
                }
            }, 5000);

//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
//            sharedPreferences.edit().putInt("steps",steps).apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.example.infits"));
        Log.i("Steps", "Registered broadcast receiver");
    }









    public  void getPermission_Body()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed with your activity logic
            // ...
        } else {
            // Permission not yet granted, request it from the user
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BODY_SENSORS, Manifest.permission.ACTIVITY_RECOGNITION},PERMISSION_REQUEST_ALL_SENSORS);
        }


    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, start listening to activity updates

            } else {
                // Permission is denied, show an error message
                Log.d("error in physical activity permission", "");
            }
        }
    }

    @Override
    public void updateStepCardData(Intent intent) {
        updateGUI(intent);
    }
}
