package com.example.infits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class MyService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepSensor;
    PendingIntent pendingIntent = null;
    boolean updatePrev = true, notificationPermission, inAppNotificationUpdated = false;
    float goal;
   private static final int NOTIFICATION_ID = 123;
    NotificationManager manager;

    Intent intentAction = new Intent("com.example.infits");

    @Override
    public void onCreate() {
        super.onCreate();




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID,createNotification());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        goal = intent.getFloatExtra("goal", 0f);
        var Goal = Double.toString(goal);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Log.d("goal--",Goal);
        if (stepSensor == null) {
            Log.e("service", "No sensor");
            no_sensor();

        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);


            goal = intent.getFloatExtra("goal", 0f);


            notificationPermission = intent.getBooleanExtra("notificationPermission", false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel chan = new NotificationChannel(
                        "MyChannelId",
                        "My Foreground Service",
                        NotificationManager.IMPORTANCE_HIGH);
                chan.setLightColor(Color.BLUE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);
            }

//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);


        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("service", "sensorChng");
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(updatePrev) {
                FetchTrackerInfos.previousStep = (int) event.values[0];
                updatePrev = false;
            }

            SharedPreferences sh = getSharedPreferences("DateForSteps", Context.MODE_PRIVATE);

            Date dateObj = new Date();

            String date = sh.getString("date","");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

            System.out.println("sensor date: " + date);

            System.out.println("sensor dateObj: " + simpleDateFormat.format(dateObj));

            if (!date.equals(simpleDateFormat.format(dateObj))) {
                FetchTrackerInfos.previousStep = FetchTrackerInfos.totalSteps;
                System.out.println("Reset");
                SharedPreferences sharedPreferences = getSharedPreferences("DateForSteps", Context.MODE_PRIVATE);
                Date dateForSteps = new Date();

                System.out.println("sensor dateForSteps: " + simpleDateFormat.format(dateForSteps));

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString("date", simpleDateFormat.format(dateForSteps));
                myEdit.putBoolean("verified",false);
                myEdit.commit();
            }

            Calendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);

            FetchTrackerInfos.totalSteps = (int) event.values[0];
            FetchTrackerInfos.currentSteps = ((int) FetchTrackerInfos.totalSteps - (int) FetchTrackerInfos.previousStep);
            Log.d("service", "totalSteps: " + FetchTrackerInfos.totalSteps);
            Log.d("service", "previousSteps: " + FetchTrackerInfos.previousStep);
            Log.d("service", "currentSteps: " + FetchTrackerInfos.currentSteps);

            updateSteps();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat("steps", (float) FetchTrackerInfos.currentSteps);
            float goalPercent = ((FetchTrackerInfos.currentSteps/goal)*100)/100;
            editor.putFloat("goalPercent", goalPercent);
            editor.apply();

            if(FetchTrackerInfos.currentSteps >= goal) {
                stopSelf();
                if(!inAppNotificationUpdated) {
                    inAppNotificationUpdated = true;
                    updateInAppNotifications((int) goal);
                }

                if(notificationPermission) {
                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    intent.putExtra("notification", "step");

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    Notification notification = new NotificationCompat.Builder(this, "MyChannelId")
                            .setOngoing(false)
                            .setSmallIcon(R.mipmap.logo)
                            .setContentTitle("Step Tracker")
                            .setContentText("Congratulations! You have reached your goal.")
                            .setChannelId("MyChannelId")
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();

                    manager.notify(2, notification);
                }
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                intent.putExtra("notification", "step");

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                        this, "MyChannelId");
                Notification notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Step Tracker")
                        .setContentText(String.valueOf(FetchTrackerInfos.currentSteps))
                        .setPriority(NotificationManager.IMPORTANCE_LOW)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setChannelId("MyChannelId")
                        .setContentIntent(pendingIntent)
                        .build();
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,TAG,
//                            NotificationManager.IMPORTANCE_HIGH);
//                    notificationManager.createNotificationChannel(channel);
//
//                    Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
//                    startForeground(1, notification);
//                }
//                else {
//
//                     startForeground(1, notification);
//                }

//                Intent notificationIntent= new Intent(this, DashBoardMain.class);
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//                    pendingIntent = PendingIntent.getActivity
//                            (this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
//                }
//                else
//                {
//                    pendingIntent = PendingIntent.getActivity
//                            (this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
//                }
                startForeground(1, notification);
                intentAction.putExtra("steps", FetchTrackerInfos.currentSteps);
                sendBroadcast(intentAction);
                String time = "Current Time : " + hour + ":" + minute + " " ;
                System.out.println(time);
            }
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
                data.put("userID",DataFromDatabase.clientuserID);
                data.put("dateandtime", String.valueOf(date));
                data.put("distance", distance);
                data.put("avgspeed", speed);
                data.put("calories",calories);
                data.put("steps", steps);
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

    private Notification createNotification() {
        // Create a notification for the foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannelId")
                .setContentTitle("Foreground Service")
                .setContentText("Running...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId("MyChannelId");

        return builder.build();
    }
    }


