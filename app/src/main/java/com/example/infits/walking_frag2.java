package com.example.infits;

import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class walking_frag2 extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepSensor;
    int pre_step = 0, current = 0, flag_steps = 0, current_steps;
    float distance, calories;
    Button btnPause, btnStart;
    TextView runningTxt, contRunningTxt, stepsDisp, calorieDisp, timeDisp;

    ImageView imageViewClockwise, imageViewAntiClockwise;
    private ObjectAnimator clockwiseRotationAnimator;
    private ObjectAnimator anticlockwiseRotationAnimator;

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
        runningTxt = view.findViewById(R.id.textView63);
        contRunningTxt = view.findViewById(R.id.textView89);
        stepsDisp = view.findViewById(R.id.textView70);
        calorieDisp = view.findViewById(R.id.textView72);
        timeDisp = view.findViewById(R.id.textView73);
        imageViewClockwise = view.findViewById(R.id.imageView76);
        imageViewAntiClockwise = view.findViewById(R.id.imageView79);

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

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (flag_steps == 0) {
            pre_step = (int) event.values[0] - 1;
            flag_steps = 1;
        }

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            current = (int) event.values[0];
            current_steps = current - pre_step;
            distance = (float) 0.002 * current_steps;
            calories = (float) 0.06 * current_steps;

            stepsDisp.setText(String.format("%.2f", current_steps));
            calorieDisp.setText(String.format("%.2f", calories));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void register() {
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
            sensorManager.unregisterListener(this, stepSensor);
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 52a7283f68c8d9f4458eb4ef2f9536bebbb861b5
