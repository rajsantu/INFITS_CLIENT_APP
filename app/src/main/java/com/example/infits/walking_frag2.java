package com.example.infits;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class walking_frag2 extends Fragment implements SensorEventListener {


    SensorManager sensorManager;
    Sensor stepSensor;
    int pre_step=0,current=0,flag_steps=0,current_steps;
    float distance,calories;
    Button btn_pause,btn_start;
    TextView running_txt, cont_running_txt,steps_disp,calorie_disp,time_disp;

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
        View view =inflater.inflate(R.layout.fragment_walking_frag2, container, false);
        // Inflate the layout for this fragment

        btn_pause=view.findViewById ( R.id.imageView86 );
        btn_start=view.findViewById ( R.id.imageView105 );
        running_txt=view.findViewById ( R.id.textView63 );
        cont_running_txt=view.findViewById ( R.id.textView89 );
        steps_disp=view.findViewById(R.id.textView70);
        calorie_disp=view.findViewById(R.id.textView72);
        time_disp=view.findViewById(R.id.textView73);



        btn_pause.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.VISIBLE );
                btn_pause.setVisibility ( View.GONE );
                running_txt.setVisibility ( View.GONE );
                cont_running_txt.setVisibility ( View.VISIBLE );
                stop();
            }
        } );

        btn_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.GONE );
                btn_pause.setVisibility ( View.VISIBLE );
                running_txt.setVisibility ( View.VISIBLE);
                cont_running_txt.setVisibility ( View.GONE);
                register();
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

            steps_disp.setText(String.format("%.2f",current_steps));
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