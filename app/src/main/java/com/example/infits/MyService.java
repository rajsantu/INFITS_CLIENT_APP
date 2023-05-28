package com.example.infits;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class MyService extends Service implements SensorEventListener {


    private static final int PERMISSION_REQUEST_BODY_SENSORS = 1;

    float[] Last_accelerometer_values;
    long lastUpdatetime;
    float speed,distance,s=1;
    int pre_step=0,current=0, flag = 0;
    SensorManager sensorManager;
    Sensor stepSensor, accelerometer;
    PendingIntent pendingIntent = null;
    boolean updatePrev = true, notificationPermission, inAppNotificationUpdated = false;
    float goal;
    private static final int NOTIFICATION_ID = 123;

    NotificationCompat.Builder notificationBuilder;
    Intent intentAction = new Intent("com.example.infits");
    private boolean isNotificationPermissionGranted, sensoravailable;

    int stepCount, stepGoal;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FetchTrackerInfos.Avg_speed="0000";
        FetchTrackerInfos.Distance=0;
        Last_accelerometer_values=new float[3];





        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);






        if(accelerometer !=null)
        {
            sensorManager.registerListener(this,accelerometer,sensorManager.SENSOR_DELAY_NORMAL);
            Log.d("accelerometer Alive:",accelerometer.toString());
        }
        else{
            Log.d("Accelerometer not Alive:","");
        }

        if (stepSensor != null) {
            Log.d("Sensor ALive::", stepSensor.toString());
            sensoravailable = true;
        } else {
            Toast.makeText(this, "sensor Not Available", Toast.LENGTH_SHORT).show();
            sensoravailable = false;
        }
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor,sensorManager.SENSOR_DELAY_NORMAL);
        }

        final String Channel_id = "Steps Counting Service";
        NotificationChannel channel = new NotificationChannel(
                Channel_id,
                Channel_id,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        notificationBuilder = new NotificationCompat.Builder(this, Channel_id)
                .setContentText("Service is running" + FetchTrackerInfos.currentSteps + " steps")
                .setContentTitle("Step Count")
                .setSmallIcon(R.drawable.ic_launcher_background);
        startForeground(101, notificationBuilder.build());


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(FetchTrackerInfos.currentSteps>=FetchTrackerInfos.stop_steps-1 && FetchTrackerInfos.currentSteps!=1)
                onDestroy();
        Log.d("service", "sensorChng");
        if (FetchTrackerInfos.flag_steps == 0) {
            pre_step = (int) event.values[0] - 1;
            FetchTrackerInfos.flag_steps= 1;
        }

        Log.d("Sensor changing", "1");

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            current= (int) event.values[0];
            FetchTrackerInfos.currentSteps  =  current - pre_step;
            FetchTrackerInfos.Distance= (float)0.0005*FetchTrackerInfos.currentSteps;
            Log.d("steps",String.valueOf(FetchTrackerInfos.currentSteps));
            FetchTrackerInfos.Calories=(float)0.05*FetchTrackerInfos.currentSteps;
            notificationBuilder.setContentText("Service is running" + FetchTrackerInfos.currentSteps + " steps");
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            managerCompat.notify(101, notificationBuilder.build());

        }

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                long current_time=System.currentTimeMillis();
                long elapsed_time=current_time-lastUpdatetime;

                    float[] values=event.values;
                    float x,y,z;
                    x=values[0];
                    y=values[1];
                    z=values[2];




                    float acceleration=(float)(Math.sqrt(x*x + y*y + z*z));

                    float dV=acceleration *elapsed_time;
                    speed+=dV;
                    FetchTrackerInfos.speed=speed;
                    s++;
                    Last_accelerometer_values=values;

                    float dT=elapsed_time/1000f;


                   FetchTrackerInfos.Avg_speed= String.format("%2f",(speed/s));

            FetchTrackerInfos.avgs= FetchTrackerInfos.Avg_speed.charAt(0);

            //float average= (speed/s);
                   //Log.d("avggggg=",String.valueOf(average));

                  //  Log.d("Sensor_data_speed",String.valueOf(speed));
            Log.d("Sensor_data_Avg_speed",String.valueOf(FetchTrackerInfos.Avg_speed.charAt(0)));
            Log.d("distance",String.valueOf(FetchTrackerInfos.Distance));
        }




    }




    private void updateInAppNotifications(int goal) {
        SharedPreferences inAppPrefs = getApplicationContext().getSharedPreferences("inAppNotification", MODE_PRIVATE);
        SharedPreferences.Editor inAppEditor = inAppPrefs.edit();
        inAppEditor.putBoolean("newNotification", true);
        inAppEditor.apply();

        String inAppUrl = String.format("%sinAppNotifications.php", DataFromDatabase.ipConfig);

        String type = "step";
        String text = "You have reached your goal of " + goal + " steps.";

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.format(date);

        StringRequest inAppRequest = new StringRequest(
                Request.Method.POST,
                inAppUrl,
                response -> {
                    if (response.equals("inserted")) Log.d("MyService", "success");
                    else Log.d("MyService", "failure");
                },
                error -> Log.e("MyService",error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("type", type);
                data.put("text", text);
                data.put("date", String.valueOf(date));

                return data;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(inAppRequest);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this,stepSensor);
        stopForeground(true);
        stopSelf();
        FetchTrackerInfos.flag_steps=0;
    }





    void updateSteps() {
        String url= String.format("%ssteptracker.php",DataFromDatabase.ipConfig);
        StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
            if (response.equals("updated")){
//                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                Log.d("Response",response);
            }
            else{
                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
                Log.d("Response",response);
            }
        },error -> {
            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String distance = String.format("%.2f",(FetchTrackerInfos.currentSteps/1312.33595801f));
                String calories = (String.format("%.2f",(0.04f*FetchTrackerInfos.currentSteps)));
                Date dateSpeed = new Date();

                SimpleDateFormat hour = new SimpleDateFormat("HH");
                SimpleDateFormat mins = new SimpleDateFormat("mm");

                int h = Integer.parseInt(hour.format(dateSpeed));
                int m = Integer.parseInt(mins.format(dateSpeed));

                int time = h+(m/60);

                String speed = (String.format("%.2f",(FetchTrackerInfos.currentSteps/1312.33595801f)/time));

                System.out.println("update: " + calories + " "+distance+" "+speed);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                sdf.format(date);
                Map<String,String> data = new HashMap<>();
                data.put("userID",DataFromDatabase.clientuserID);
                data.put("dateandtime", String.valueOf(date));
                data.put("distance", distance);
                data.put("avgspeed", speed);
                data.put("calories",calories);
                data.put("steps", String.valueOf(FetchTrackerInfos.currentSteps));
                var Goal = Double.toString(goal);
                data.put("goal", Goal);
                return data;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }




    public void no_sensor()
    {
        String url= String.format("%ssteptracker.php",DataFromDatabase.ipConfig);
        StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
            if (response.equals("updated")){
//                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                Log.d("Respons3232e",response);
            }
            else{
                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
                Log.d("Response",response);
            }
        },error -> {
            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String steps="0";
                String distance = "0";
                String calories = "0";
                Date dateSpeed = new Date();

                SimpleDateFormat hour = new SimpleDateFormat("HH");
                SimpleDateFormat mins = new SimpleDateFormat("mm");

                int h = Integer.parseInt(hour.format(dateSpeed));
                int m = Integer.parseInt(mins.format(dateSpeed));

                int time = h+(m/60);

                String speed = "0";
                System.out.println("update: " + calories + " "+distance+" "+speed);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                sdf.format(date);
                Map<String,String> data = new HashMap<>();
                data.put("clientuserID",DataFromDatabase.clientuserID);
                data.put("dateandtime", String.valueOf(date));
                data.put("distance", distance);
                data.put("avgspeed", speed);
                data.put("calories",calories);
                data.put("steps", steps);
                data.put("clientID",DataFromDatabase.client_id);
                data.put("dietitian_id",DataFromDatabase.dietitian_id);
                var Goal=Double.toString(goal);
                data.put("goal", Goal);
                return data;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

   /* private Notification createNotification() {
        // Create a notification for the foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannelId")
                .setContentTitle("Foreground Service")
                .setContentText("Running...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId("MyChannelId");

        return builder.build();
    }*/


}





