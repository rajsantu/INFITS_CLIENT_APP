package com.example.infits;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class walking_frag2 extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    private RotateAnimation rotateAnimation;
    Sensor stepSensor;
    int pre_step = 0, current = 0, flag_steps = 0, current_steps;
    float distance, calories;
    Button btnPause, btnStart;
    TextView runningTxt, contRunningTxt, stepsDisp, calorieDisp, timeDisp, current_steps1,current_steps2,distance_travl,todaygoal;
    private long startTime = 0;
    private long elapsedTime = 0;
    String time,goal ="5";
    ImageView imageViewClockwise, imageViewAntiClockwise,btn_stop,imageView80,imageView76,imageView79;
    private ObjectAnimator clockwiseRotationAnimator;
    private ObjectAnimator anticlockwiseRotationAnimator;
    private static final int REQUEST_CODE = 123;
    public walking_frag2() {
        // Required empty public constructor
    }

    public static walking_frag2 newInstance(String param1, String param2) {
        walking_frag2 fragment = new walking_frag2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walking_frag2, container, false);


        btnPause = view.findViewById(R.id.imageView86);
        btnStart = view.findViewById(R.id.imageView105);
        btn_stop = view.findViewById(R.id.imageView89);
        runningTxt = view.findViewById(R.id.textView63);
        current_steps1 = view.findViewById(R.id.textView88);
        current_steps2 = view.findViewById(R.id.textView70);
        contRunningTxt = view.findViewById(R.id.textView89);
        distance_travl = view.findViewById(R.id.textView71);
        calorieDisp = view.findViewById(R.id.textView72);
        timeDisp = view.findViewById(R.id.textView73);
        todaygoal = view.findViewById(R.id.textView87);
        imageView76 = view.findViewById(R.id.imageView76);
        imageView79 = view.findViewById(R.id.imageView79);
        imageView80 = view.findViewById(R.id.imageView80);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Bundle bundle = getArguments();
        register();
        if (bundle != null) {
            String todayGoal = bundle.getString("todaysgoal");
            todaygoal.setText(todayGoal);
        }

        startRotationAnimation();

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                runningTxt.setVisibility(View.GONE);
                contRunningTxt.setVisibility(View.VISIBLE);
                stop();
                stopClockwiseRotation();
                stopAntiClockwiseRotation();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                runningTxt.setVisibility(View.VISIBLE);
                contRunningTxt.setVisibility(View.GONE);
                register();
                startClockwiseRotation();
                startAntiClockwiseRotation();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to the server when "Stop" button is pressed
                sendDataToServer();
                // Navigate to the next fragment after sending the data
                Navigation.findNavController(v).navigate(R.id.action_walking_frag2_to_activityfourthFragment);
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
            distance = (float) 0.001 * current_steps;
            calories = (float) 0.04 * current_steps;
            long currentTime = SystemClock.elapsedRealtime();
            elapsedTime = currentTime - startTime;
            int seconds = (int) (elapsedTime / 1000); // Convert milliseconds to seconds
            int minutes = seconds / 60; // Convert seconds to minutes
            int hours = minutes / 60; // Convert minutes to hours
            Log.d("seconds", String.valueOf(seconds));
            Log.d("minutes", String.valueOf(minutes));
            //seconds = seconds % 60; // Remaining seconds
            minutes = minutes % 60; // Remaining minutes
            String formattedTime = String.format("%02d.%02d", hours, minutes);
            Log.d("elapsed time", formattedTime);
            current_steps1.setText(String.valueOf(current_steps));
            current_steps2.setText(String.valueOf(current_steps));
            distance_travl.setText(String.format("%.2f", distance));
            calorieDisp.setText(String.format("%.2f", calories));
            timeDisp.setText(formattedTime);
            time = formattedTime;
            Log.e("Value of distance :", String.valueOf(distance));
            Log.e("Value of time :", time);
        }
    }
    private void sendDataToServer() {
        Log.e("Value of string value of time", String.valueOf(time));
        //String url = "https://infits.in/androidApi/runningTracker.php";
        //String url = "http://10.12.2.128/infits/runningTracker.php";
        String url = "http://192.168.29.52/infits/trekkingTracker.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            String cleanResponse = response.trim();
            if (cleanResponse.equalsIgnoreCase("updated")) {
                Toast.makeText(requireActivity().getApplicationContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
                Bundle result = new Bundle();
                result.putString("updateKey", "dataUpdated");
                getParentFragmentManager().setFragmentResult("updateDataKey", result);
            } else {
                Toast.makeText(requireActivity().getApplicationContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                Log.e("MyApp", "Failed to update data: " + cleanResponse);
            }
        }, error -> {
            Toast.makeText(requireContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @SuppressLint("DefaultLocale")
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Data to be sent in the request body
                Map<String, String> data = new HashMap<>();
                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("steps",  String.valueOf(current_steps));
                data.put("distance",  String.format("%.2f", distance));
                data.put("calories", String.format("%.2f", calories));
                data.put("runtime",time);
                //data.put("goal", goal);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                data.put("date", dtf.format(now));
                data.put("operationtodo","updatedata");
                data.put("table","walkingtracker");
                data.put("category","Walking");
                return data;
            }
        };
        // Set a retry policy in case of timeout or connection errors
        int socketTimeout = 30000; // 30 seconds
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        Toast.makeText(getActivity(), "Updating data...", Toast.LENGTH_SHORT).show();
        boolean sendDataSuccess = true;
    }

    private void setResult(int resultOk, Intent resultIntent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void register() {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
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
    private void startClockwiseRotation() {
        if (clockwiseRotationAnimator != null && clockwiseRotationAnimator.isRunning()) {
            return;
        }
        clockwiseRotationAnimator = ObjectAnimator.ofFloat(imageViewClockwise, "rotation", 0f, 360f);
        clockwiseRotationAnimator.setDuration(3000); // Adjust the duration as needed
        clockwiseRotationAnimator.setInterpolator(new LinearInterpolator());
        clockwiseRotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        clockwiseRotationAnimator.start();
    }

    private void startAntiClockwiseRotation() {
        if (anticlockwiseRotationAnimator != null && anticlockwiseRotationAnimator.isRunning()) {
            return;
        }
        anticlockwiseRotationAnimator = ObjectAnimator.ofFloat(imageViewAntiClockwise, "rotation", 0f, -360f);
        anticlockwiseRotationAnimator.setDuration(3000); // Adjust the duration as needed
        anticlockwiseRotationAnimator.setInterpolator(new LinearInterpolator());
        anticlockwiseRotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        anticlockwiseRotationAnimator.start();
    }

    private void stopClockwiseRotation() {
        if (clockwiseRotationAnimator != null) {
            clockwiseRotationAnimator.cancel();
        }
    }

    private void stopAntiClockwiseRotation() {
        if (anticlockwiseRotationAnimator != null) {
            anticlockwiseRotationAnimator.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
        stopClockwiseRotation();
        stopAntiClockwiseRotation();
    }
}