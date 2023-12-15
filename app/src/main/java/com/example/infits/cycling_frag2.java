package com.example.infits;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infits.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class cycling_frag2 extends Fragment implements LocationListener {
    Long time;
    long elapsed_time;
    String formattedTime;
    float totalDistance, weight, calorie_burn, temp_calorie = 0;
    float distance, calories;
    int pre_step = 0, current = 0, flag_steps = 0, current_steps;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Location lastLocation;
    private RotateAnimation rotateAnimation;
    SensorManager sensorManager;
    Sensor stepSensor;
    private boolean isRotationStarted = false;
    TextView distance_show, Calorie_meter, time_meter,todaygoal;
    ImageView imageView80;
    ImageView imageView79;
    ImageView imageView76;
    ImageView btn_stop;
    Button btn_pause, btn_start;
    TextView running_txt, cont_running_txt, dunit;
    String  goal;
    public cycling_frag2() {
        // Required empty public constructor
    }

    public static cycling_frag2 newInstance(String param1, String param2) {
        cycling_frag2 fragment = new cycling_frag2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cycling_frag2, container, false);

        distance_show = view.findViewById(R.id.textView70);
        btn_pause = view.findViewById(R.id.imageView86);
        btn_start = view.findViewById(R.id.imageView105);
        running_txt = view.findViewById(R.id.textView63);
        cont_running_txt = view.findViewById(R.id.textView89);
        dunit = view.findViewById(R.id.textView83);
        Calorie_meter = view.findViewById(R.id.textView72);
        imageView80 = view.findViewById(R.id.imageView80);
        imageView76 = view.findViewById(R.id.imageView76);
        imageView79 = view.findViewById(R.id.imageView79);
        btn_stop = view.findViewById(R.id.imageView89);
        time_meter = view.findViewById(R.id.textView73);
        todaygoal = view.findViewById(R.id.textView87);
        Calorie_meter.setText("0");
        time_meter.setText("0");

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        startLocationUpdates();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String todayGoal = bundle.getString("todaysgoal");
            todaygoal.setText(todayGoal);
        }
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(requireContext());
        weight = data.getFloat("weight", 60);

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.GONE);
                running_txt.setVisibility(View.GONE);
                cont_running_txt.setVisibility(View.VISIBLE);
                stopLocationUpdates();
                stopRotationAnimation();
                time = 0L;
                temp_calorie += calorie_burn;
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.GONE);
                btn_pause.setVisibility(View.VISIBLE);
                running_txt.setVisibility(View.VISIBLE);
                cont_running_txt.setVisibility(View.GONE);
                startLocationUpdates();
                startRotationAnimation();
                time = System.currentTimeMillis();
                flag_steps = 0;
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
                Navigation.findNavController(v).navigate(R.id.cycling_frag2_to_activitythirdfragment);
            }
        });

        return view;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float accuracy = location.getAccuracy();
        if (accuracy < 10) {
            if (lastLocation != null) {
                float distance = location.distanceTo(lastLocation);
                totalDistance += distance;
            }
            lastLocation = location;
            Log.d("Distance", String.valueOf(totalDistance));
            distance_show.setText(String.format("%.2f", totalDistance));
            if (totalDistance / 1000 >= 1) dunit.setText("KM");
            Calorieburn(location.getSpeed(), weight);
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(this);
        lastLocation = null;
    }

    public void Calorieburn(float speed, float weight) {
        speed = (float) (speed * 3.6);
        float MET = 0;

        elapsed_time = (System.currentTimeMillis() - time) / 1000;
        if (speed == 0) {
        } else {
            MET = (float) ((0.175 * speed) + 3.5);
            calorie_burn = MET * weight * (elapsed_time) / 3600;
        }
        formattedTime = formatTime(elapsed_time);
        Log.d("time", formattedTime);
        Log.d("calories",String.format(Locale.getDefault(), "%.1f", (calorie_burn + temp_calorie)));
        time_meter.setText(formattedTime);
        time = Long.valueOf(formattedTime);
        Calorie_meter.setText(String.format(Locale.getDefault(), "%.1f", (calorie_burn + temp_calorie)));
    }

    private void startRotationAnimation() {
        if (!isRotationStarted) {
            RotateAnimation rotateAnimation79 = new RotateAnimation(
                    0, -360, // Starting and ending angles of rotation (0 to -360 degrees for anti-clockwise)
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point for the X coordinate (center)
                    Animation.RELATIVE_TO_SELF, 0.5f // Pivot point for the Y coordinate (center)
            );
            rotateAnimation79.setInterpolator(new LinearInterpolator());
            rotateAnimation79.setDuration(4000);
            rotateAnimation79.setRepeatCount(Animation.INFINITE);
            rotateAnimation79.setFillAfter(true);
            imageView79.startAnimation(rotateAnimation79);

            rotateAnimation = new RotateAnimation(
                    0, 360, // Starting and ending angles of rotation (0 to 360 degrees for clockwise)
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point for the X coordinate (center)
                    Animation.RELATIVE_TO_SELF, 0.5f // Pivot point for the Y coordinate (center)
            );
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setDuration(4000);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setFillAfter(true);
            imageView80.startAnimation(rotateAnimation);
            imageView76.startAnimation(rotateAnimation);
            isRotationStarted = true;
        }
    }
    private String formatTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;

        minutes = minutes % 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }
    private void stopRotationAnimation() {
        if (isRotationStarted) {
            imageView79.clearAnimation();
            imageView80.clearAnimation();
            imageView76.clearAnimation();
            isRotationStarted = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationUpdates();
        stopRotationAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        stopRotationAnimation();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopLocationUpdates();
            stopRotationAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btn_pause.getVisibility() == View.VISIBLE) {
            startRotationAnimation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
    private void sendDataToServer() {
        Log.e("Value of string value of time", String.valueOf(time));
        Log.e(" value of time", String.valueOf(time));
        String url = "http:// 192.168.29.52/infits/runningTracker.php";
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
                data.put("runtime",formattedTime);
                data.put("goal", String.valueOf(todaygoal));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                data.put("date", dtf.format(now));
                data.put("dateandtime", DTF.format(now));
                data.put("operationtodo","updatedata");
                return data;
            }
        };

        int socketTimeout = 30000;
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        Toast.makeText(getActivity(), "Updating data...", Toast.LENGTH_SHORT).show();

    }
}