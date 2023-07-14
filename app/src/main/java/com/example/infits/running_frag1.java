package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
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
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class running_frag1 extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepSensor;
    int pre_step=0,current=0,flag_steps=0,current_steps;
    float distance,calories;

    Button btn_pause,btn_start;
    ImageView  btn_stop;
    TextView running_txt, cont_running_txt,distance_disp,calorie_disp,time_disp;

    public static final String preference = "running_values";
    SharedPreferences sharedpreferences;
    Context c = getActivity();

    String calories_save = "", distance_save =" ", time_disp_save=" ", goal="8", goal_save="", time="2";

    public running_frag1() {

    }

    public static running_frag1 newInstance(String param1, String param2) {
        running_frag1 fragment = new running_frag1();
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
        View view=inflater.inflate(R.layout.fragment_running_frag1, container, false);
        //distance_show=view.findViewById(R.id.textView70);
        btn_pause=view.findViewById ( R.id.imageView86 );
        btn_start=view.findViewById ( R.id.imageView105 );
        btn_stop = view.findViewById(R.id.imageView89);
        running_txt=view.findViewById ( R.id.textView63 );
        cont_running_txt=view.findViewById ( R.id.textView89 );
        distance_disp=view.findViewById(R.id.textView70);
        calorie_disp=view.findViewById(R.id.textView72);
        time_disp=view.findViewById(R.id.textView73);

//        sharedpreferences = c.getSharedPreferences(preference,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        register();




        // distance_show.setText("0");

        //Activity Paused
        btn_pause.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.VISIBLE );
                btn_pause.setVisibility ( View.GONE );
                running_txt.setVisibility ( View.GONE );
                cont_running_txt.setVisibility ( View.VISIBLE );
                flag_steps=0;
                register();
            }
        } );


        // stop activity
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String url=String.format("%srunningTracker.php",DataFromDatabase.ipConfig);
                String url = "https://infits.in/androidApi/runningTracker.php";

                StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
                    if (response.equals("updated")){
//                        editor.putString(calories_save, String.valueOf(calories));
//                        editor.putString(distance_save, String.valueOf(distance));
//                        editor.putString(time_disp_save, String.valueOf(time));
//                        editor.putString(goal_save, String.valueOf(goal));
//                        editor.commit();
                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(response);
                        Toast.makeText(getActivity(), "Not working" + String.valueOf(response), Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(requireContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> data = new HashMap<>();
                        data.put("client_id",DataFromDatabase.client_id);
                        data.put("clientuserID",DataFromDatabase.clientuserID);
                        data.put("distance",String.valueOf(distance));
                        data.put("calories", String.valueOf(calories));
                        data.put("runtime", String.valueOf(time));
                        data.put("goal", goal);
                        data.put("duration","0");
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd H:MM:SS");
                        LocalDateTime now = LocalDateTime.now();
                        data.put("date",dtf.format(now));
                        data.put("dateandtime",DTF.format(now));
                        return data;
                    }
                };

                Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
                Toast.makeText(getActivity(),"check 1",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_running_frag1_to_activitySecondFragment);
            }
        });


        //Activity Start/Resume
        btn_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.GONE );
                btn_pause.setVisibility ( View.VISIBLE );
                running_txt.setVisibility ( View.VISIBLE);
                cont_running_txt.setVisibility ( View.GONE);
                flag_steps=0;
                stop();
            }
        } );

        return view;
    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        if(flag_steps == 0) {
            pre_step = (int) event.values[0] - 1;
            flag_steps= 1;
        }

        if(event.sensor.getType() ==Sensor.TYPE_STEP_COUNTER)
        {
            current= (int) event.values[0];
            current_steps=current-pre_step;
            distance = (float)0.002*current_steps;
            calories = (float)0.06*current_steps;

            distance_disp.setText(String.format("%.2f",distance));
            calorie_disp.setText(String.format("%.2f",calories));

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    public  void register()
    {
        sensorManager.registerListener(this, stepSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    public  void stop()
    {
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this,stepSensor);
    }


}