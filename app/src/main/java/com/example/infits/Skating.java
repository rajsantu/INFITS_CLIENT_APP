package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infits.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Skating extends Fragment implements SensorEventListener {

    ImageView imgback;

    private RotateAnimation rotateAnimation;
    private boolean isRotationStarted = false;
    SensorManager sensorManager;
    Sensor stepSensor;
    int pre_step=0,current=0,flag_steps=0,current_steps;
    float distance, calories;
    Button btn_pause, btn_start;

    ImageView btn_stop;
    ImageView imageView80;
    ImageView imageView79;
    ImageView imageView76;
    TextView running_txt, cont_running_txt, distance_disp, calorie_disp, time_disp,distance_show,distance_show2,todaygoal;
    private long startTime = 0;
    private long elapsedTime = 0;

    public static final String preference = "running_values";
    SharedPreferences sharedpreferences;
    Context c;

    String calories_save = "", distance_save = " ", time_disp_save = " ", goal="5", goal_save = "", time;

    public Skating() {

    }

    public static Skating newInstance(String param1, String param2) {
        Skating fragment = new Skating();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.skating, container, false);
        distance_show=view.findViewById(R.id.textView71);
        distance_show2=view.findViewById(R.id.textView88);

        btn_pause = view.findViewById(R.id.imageView86);
        btn_start = view.findViewById(R.id.imageView105);
        btn_stop = view.findViewById(R.id.imageView89);
        imageView80 = view.findViewById(R.id.imageView80);
        imageView76 = view.findViewById(R.id.imageView76);
        imageView79 = view.findViewById(R.id.imageView79);
        running_txt = view.findViewById(R.id.textView63);
        cont_running_txt = view.findViewById(R.id.textView89);
        distance_disp = view.findViewById(R.id.textView70);
        calorie_disp = view.findViewById(R.id.textView72);
        time_disp = view.findViewById(R.id.textView73);
        todaygoal = view.findViewById(R.id.textView87);
        imgback = view.findViewById(R.id.skate_imgback);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        register();

        Bundle bundle = getArguments();
        register();
        if (bundle != null) {
            String todayGoal = bundle.getString("todaysgoal");
            todaygoal.setText(todayGoal);
        }
        // Start the rotation animation when the fragment is created
        startRotationAnimation();


        //back button
        imgback.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(
                    R.id.action_Skating_to_activitySkating,
                    null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.activitySkating, true)
                            .build()
            );

           // Navigation.findNavController(v).navigate(R.id.action_Skating_to_activitySkating);

        });

        //Activity Paused
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                running_txt.setVisibility(View.GONE);
                cont_running_txt.setVisibility(View.VISIBLE);
                imageView76.clearAnimation();
                imageView79.clearAnimation();
                imageView80.clearAnimation();
                isRotationStarted = false;
                flag_steps = 0;
                register();
            }
        });

        // stop activity
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to the server when "Stop" button is pressed
                sendDataToServer();

                // Navigate to the next fragment after sending the data
                Navigation.findNavController(v).navigate(R.id.action_Skating_to_activitySkating);
            }
        });

        //Activity Start/Resume
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.GONE);
                btn_pause.setVisibility(View.VISIBLE);
                running_txt.setVisibility(View.VISIBLE);
                cont_running_txt.setVisibility(View.GONE);

                if (!isRotationStarted) {
                    // Start the rotation animations
                    startRotationAnimation();
                    isRotationStarted = true;
                }
                flag_steps = 0;
                stop();
            }
        });
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (flag_steps == 0) {
            pre_step = (int) event.values[0] - 1;
            flag_steps = 1;
            startTime = SystemClock.elapsedRealtime();
            Log.d("MyApp", "startTime initialized: " + startTime);
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            current = (int) event.values[0];
            current_steps = current - pre_step;
            distance = (float) 0.004 * current_steps;
            calories = (float) 0.06 * current_steps;
            long currentTime = SystemClock.elapsedRealtime();
            elapsedTime = currentTime - startTime;

            int seconds = (int) (elapsedTime / 1000); // Convert milliseconds to seconds
            int minutes = seconds / 60; // Convert seconds to minutes
            int hours = minutes / 60; // Convert minutes to hours
            Log.d("seconds", String.valueOf(seconds));
            Log.d("minutes", String.valueOf(minutes));
            seconds = seconds % 60; // Remaining seconds
            minutes = minutes % 60; // Remaining minutes
            String formattedTime = String.format("%02d.%02d", hours, minutes);
            Log.d("elapsed time", formattedTime);
            distance_disp.setText(String.format("%.2f", distance));
            distance_show.setText(String.format("%.2f", distance));
            distance_show2.setText(String.format("%.2f", distance));
            calorie_disp.setText(String.format("%.2f", calories));
            time_disp.setText(formattedTime);
            time = formattedTime;
            Log.e("Value of distance :", String.valueOf(distance));
            Log.e("Value of time :", time);
            if (distance == Float.parseFloat(todaygoal.getText().toString())) {
                Toast.makeText(getContext(),"Today's Goal Achieved !",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void register() {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this, stepSensor);
    }

    public void startRotationAnimation() {
        // Create a RotateAnimation for imageView79 (anti-clockwise)
        RotateAnimation rotateAnimation79 = new RotateAnimation(
                0, -360, // Starting and ending angles of rotation (0 to -360 degrees for anti-clockwise)
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point for the X coordinate (center)
                Animation.RELATIVE_TO_SELF, 0.5f // Pivot point for the Y coordinate (center)
        );

        // Set the animation properties
        rotateAnimation79.setInterpolator(new LinearInterpolator()); // LinearInterpolator for smooth rotation
        rotateAnimation79.setDuration(4000); // Duration of the rotation animation in milliseconds
        rotateAnimation79.setRepeatCount(Animation.INFINITE); // Infinite repeat count for continuous rotation
        rotateAnimation79.setFillAfter(true); // Set to true to keep the final rotation state after the animation ends

        // Create a RotateAnimation for imageView80 and imageView76 (clockwise)
        rotateAnimation = new RotateAnimation(
                0, 360, // Starting and ending angles of rotation (0 to 360 degrees for clockwise)
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point for the X coordinate (center)
                Animation.RELATIVE_TO_SELF, 0.5f // Pivot point for the Y coordinate (center)
        );

        // Set the animation properties
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(4000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);

        // Apply the animations to the corresponding ImageViews
        imageView80.startAnimation(rotateAnimation);
        imageView76.startAnimation(rotateAnimation);
        imageView79.startAnimation(rotateAnimation79);
    }

    private void sendDataToServer() {

        if (time != null) {
        Log.e("Value of string value of time", String.valueOf(time));
        Log.e(" value of time", time);
        String url = "http://192.168.29.52/infits/trekkingTracker.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            String cleanResponse = response.trim();
            if (cleanResponse.equalsIgnoreCase("updated")) {
                Toast.makeText(requireActivity().getApplicationContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
                Bundle result = new Bundle();
                result.putString("updateKey", "dataUpdated");
                getParentFragmentManager().setFragmentResult("updateDataKey", result);
            } else {
                Toast.makeText(getActivity(), "Failed to update data", Toast.LENGTH_SHORT).show();
                Log.e("MyApp", "Failed to update data: " + cleanResponse);
            }
        }, error -> {
            Toast.makeText(requireContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Data to be sent in the request body
                Map<String, String> data = new HashMap<>();
                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("distance",  String.valueOf(distance));
                data.put("calories", String.format("%.2f", calories));
                data.put("runtime",time);
                data.put("goal", goal);
                //data.put("duration", "0");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                data.put("date", dtf.format(now));
                data.put("dateandtime", DTF.format(now));
                data.put("operationtodo","updatedata");
                data.put("table","skatingtracker");
                data.put("category","Skating");
                data.put("steps",  String.valueOf(current_steps));
                return data;
            }
        };
        // Set a retry policy in case of timeout or connection errors
        int socketTimeout = 30000; // 30 seconds
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        Toast.makeText(getActivity(), "Updating data...", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("MyApp", "Time variable is null");
            // Handle the case where time is null (show a message, log, or perform other actions)
        }
    }
}
