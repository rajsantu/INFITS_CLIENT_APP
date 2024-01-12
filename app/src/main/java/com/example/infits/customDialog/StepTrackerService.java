package com.example.infits.customDialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infits.DataFromDatabase;
import com.example.infits.FetchTrackerInfos;
import com.example.infits.MainActivity;
import com.example.infits.R;
import com.example.infits.WorkerForUpdateStepDetailsInDb;
import com.google.android.gms.fitness.data.Goal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StepTrackerService extends Service implements SensorEventListener {
    private GoalReachedReceiver goalReachedReceiver;
    private SharedPreferences previous;
    private NotificationCompat.Builder notificationBuilder1;
    private static final int SENSOR_PERMISSION_REQUEST = 1;
    private static final int PERMISSION_REQUEST_BODY_SENSORS = 1;
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1000;
    private static final int PERMISSION_REQUEST_ALL_SENSORS = 100;
    //
    private SensorManager sensorManager;
    private PeriodicWorkRequest workRequest;
    private Sensor stepCounterSensor,accelerometer;;
    private TextView stepCountTextView;
    private int GoalSteps;
    private int stepPercent1;
    private int currentsteps;

    //
    private int steps = 0;
    private   long lastUpdateTime = System.currentTimeMillis();
    private float totalDistance = 0f;
    private float totalSpeed = 0f;
    private int readingsCount = 0;
    private static final float ALPHA = 0.8f;
    private static final float NS2S = 1.0f / 1_000_000_000.0f;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private float[] velocity = new float[3];
    private long lastTimestamp = 0;
    private float[] accelerationValues = new float[3];
    private long lastTime = 0;
    private int sampleCount = 0;
    private  float speed,distance,s=1;
    private int previousStepCount = 0;
   private float[] Last_accelerometer_values;
   private long lastUpdatetime;
    private SharedPreferences notificationsharedPreferences;
    static float goalVal;
    // private int currentsteps;
    SharedPreferences currentPreferences;
    SharedPreferences stepPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationsharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //  ActivityCompat#requestPermissions
            return;
        }
        loadData();

    }

    public final String NOTIFICATION_CHANNEL_ID = "MyNotificationChannel";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // saveData();

        previousStepCount=steps;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //   viewModel = new ViewModelProvider((ViewModelStoreOwner) getApplication()).get(StepCounterViewModel.class);
        SharedPreferences sharedPreferences1 = getApplication().getApplicationContext().getSharedPreferences("GOALVALUE", MODE_PRIVATE);
        GoalSteps = sharedPreferences1.getInt("goalValue", 0);
        steps =0;
        sensorManager.registerListener(this, stepCounterSensor, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,accelerometer,sensorManager.SENSOR_DELAY_NORMAL);
        goalReachedReceiver = new GoalReachedReceiver();
        IntentFilter filter = new IntentFilter("GOAL_REACHED");
        getApplication().registerReceiver(goalReachedReceiver, filter);
        final String Channel_id = "Steps Counting Service";
        NotificationChannel channel1 = new NotificationChannel(
                Channel_id,
                Channel_id,
                NotificationManager.IMPORTANCE_LOW
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel1);
        notificationBuilder1 = new NotificationCompat.Builder(getApplicationContext(), Channel_id)
                .setContentText("Service is running" + currentsteps + " steps")
                .setContentTitle("Step Count")
                .setSmallIcon(R.drawable.ic_launcher_background);
        startForeground(102, notificationBuilder1.build());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("Range")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps = (int) sensorEvent.values[0];

            previous = this.getSharedPreferences("PREVIOUSDATA", MODE_PRIVATE);
            SharedPreferences.Editor editor = previous.edit();
            editor.putInt("previousdata", steps);
            editor.apply();
            savePre();
            currentsteps = steps - previousStepCount;
            broadcastStepCount(currentsteps);
            FetchTrackerInfos.currentSteps=currentsteps;
//            currentsteps = steps - previousStepCount;
//            broadcastStepCount(steps);
            // FetchTrackerInfos.currentSteps = steps;
            currentPreferences = getApplication().getSharedPreferences("CURRENTSTEP", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = currentPreferences.edit();
            editor1.putInt("key2", currentsteps);
            editor1.apply();
            FetchTrackerInfos.Calories=(float)0.05*FetchTrackerInfos.currentSteps;
            float stepPercent = GoalSteps == 0 ? 0 : (int) ((currentsteps * 100) / GoalSteps);
            double stepPercent1 = Math.min(100, Math.max(0, stepPercent));
            //  if(stepPercent<=100 &&stepPercent>=0){
//            if (stepPercent1 == 0) {
//                stepPercent1 = 0.1; // Set a minimum value to prevent 0% display
//            }
            if (stepPercent >= 100) {
//                Intent goalReachedIntent = new Intent("GOAL_REACHED");
//                requireActivity().sendBroadcast(goalReachedIntent);
                setAlarm();
            }
            // calculate speed using pedometer


            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            notificationBuilder1.setContentText("Service is running" + currentsteps + " steps");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return;
            }
            managerCompat.notify(102, notificationBuilder1.build());
        }
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long current_time=System.currentTimeMillis();
            long elapsed_time=current_time-lastUpdatetime;

            float[] values=sensorEvent.values;
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
            String formatedSpeed = String.format("%2f",(speed/s));

            FetchTrackerInfos.avgs= FetchTrackerInfos.Avg_speed.charAt(0);

            //float average= (speed/s);
            //Log.d("avggggg=",String.valueOf(average));

            //  Log.d("Sensor_data_speed",String.valueOf(speed));
            Log.d("Sensor_data_Avg_speed",String.valueOf(FetchTrackerInfos.Avg_speed.charAt(0)));
            Log.d("distance",String.valueOf(FetchTrackerInfos.Distance));
            broadcastSpeedAndDistance(totalDistance, String.valueOf(formatedSpeed.charAt(0)));
        }

        //}
    }
    private void calculateSpeed(float[] values) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastTime;
        lastTime = currentTime;

        float accelerationMagnitude = calculateAccelerationMagnitude(values);
        float speed = accelerationMagnitude * timeDifference / 1000; // in m/s

        totalSpeed += speed;
        sampleCount++;

        float averageSpeed = (sampleCount == 0) ? 0 : totalSpeed / sampleCount;
        updateUI(averageSpeed);
       // broadcastSpeedAndDistance(0,averageSpeed);
    }
    private float calculateAccelerationMagnitude(float[] values) {
        return (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
    }

    private void updateUI(float averageSpeed) {
        String speedText = String.format("Average Speed: %.2f m/s", averageSpeed);
      //  tvAverageSpeed.setText(speedText);
    }

//    private void updateUI(float averageSpeed) {
//        String speedText = String.format("Average Speed: %.2f m/s", averageSpeed);
//        tvAverageSpeed.setText(speedText);
//    }
//    private void updateUI(float speed) {
//        averageSpeedTextView.setText("Average Speed: " + String.format("%.2f", speed) + " m/s");
//        distanceTextView.setText("Distance: " + String.format("%.2f", totalDistance) + " meters");
//    }
    private float calculateDistance(float ax, float ay, float az, float dt) {
        // Simple integration to calculate distance
        float deltaV = (float) Math.sqrt(ax * ax + ay * ay + az * az) * dt;
        return deltaV;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void savePre() {
        SharedPreferences sharedPreferences4 = this.getSharedPreferences("PREFDATA",MODE_PRIVATE);
        boolean saved = sharedPreferences4.getBoolean("PREF",false);
        if(!saved) {
            previous = this.getSharedPreferences("PREVIOUSDATA",MODE_PRIVATE);
            previousStepCount = previous.getInt("previousdata", 0);
            //  previous = this.getSharedPreferences("PREVIOUSDATA",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences4.edit();
            editor.putBoolean("PREF",true);
            editor.apply();
        }
    }
    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, YourNotificationReceiver.class); // Replace with your BroadcastReceiver
        Intent goalReachedIntent = new Intent("GOAL_REACHED");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, goalReachedIntent, PendingIntent.FLAG_IMMUTABLE);

        getApplication().sendBroadcast(goalReachedIntent);
        // Set the alarm to trigger immediately
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }

    private class GoalReachedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("GOAL_REACHED".equals(intent.getAction())) {
//                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean notificationSent = notificationsharedPreferences.getBoolean("notification_sent", false);
                // Goal reached, show a notification
                if (!notificationSent) {
                    // Create and send the notification
                    // ...
                    showNotification("Goal Reached", "Congratulations, you've reached your step goal!");
                    // Mark that the notification has been sent to avoid showing it again
                    SharedPreferences.Editor editor = notificationsharedPreferences.edit();
                    editor.putBoolean("notification_sent", true);
                    editor.apply();
                    updateDetails();
                   // goalupdate();
                }

            }
        }
    }

    private void goalupdate() {
        String url = "https://infits.in/androidApi/updatestepgoal.php";
        final StringRequest requestGoal = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                Log.e("goal",response.toString());
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("stepsGoalInt",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putInt("stepTrackerGoal",(int)GoalSteps);
                editor1.apply();

                    //Constraints
                    Constraints constraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();
                    //for periodicWork
                    workRequest = new PeriodicWorkRequest.Builder(
                            WorkerForUpdateStepDetailsInDb.class,
                            15,   //minimum 15 minute
                            TimeUnit.MINUTES
                    ).addTag("WORKUPDATESTEP").setConstraints(constraints).build();
                  //  saveTotalInPref(getApplicationContext(),workRequest.getStringId(),"Steps_db");
                    WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
                    Toast.makeText(getApplicationContext(), "Worker Set", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
               // e.printStackTrace();
                Log.d("GOALERROR",e.getMessage().toString());
            }
        }, error -> {

            try {
                Toast.makeText(getApplicationContext(), "Updated Goal error", Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
                Log.e("goal","error");
                Toast.makeText(this,error.toString() , Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //throw new RuntimeException(e);


            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientuserID",DataFromDatabase.clientuserID );
                data.put("goal",String.valueOf(GoalSteps));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                LocalDateTime now = LocalDateTime.now();
                data.put("dateandtime",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(requestGoal);

    }

    private void updateDetails(){
        if (FetchTrackerInfos.currentSteps > 1) {
           // String url = "https://infits.in/androidApi/updateStepFragmentDetails.php";
            String url = "http://192.168.27.94/phpProjects/updateStepFragmentDetails.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Log.e("calorieUpdate", "success");
                Toast.makeText(getApplicationContext(), "Details Updated in DB", Toast.LENGTH_SHORT).show();
                if(response.equals("success")){
                    Toast.makeText(getApplicationContext(), "Details Updated in DB", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            },
                    error -> {
                        Log.e("calorieUpdate", "fail");
                        Log.e("calorieUpdate", error.toString());
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @SuppressLint("DefaultLocale")
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("clientuserID", DataFromDatabase.clientuserID);
                    data.put("steps", currentsteps + "");
                    data.put("distance", String.format("%.3f", (0.0005*currentsteps)));
                    data.put("calories", String.format("%.2f", (FetchTrackerInfos.Calories)));
                    data.put("avgspeed", FetchTrackerInfos.Avg_speed.substring(0, 1));
                    data.put("goal", String.valueOf(GoalSteps));
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                    LocalDateTime now = LocalDateTime.now();
                    data.put("dateandtime", dtf.format(now));
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        }
    }


    private void showNotification(String title, String content) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Description for my notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplication().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_png)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        // Create an intent to open the app when the notification is tapped
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }


    @Override
    public void onDestroy() {
        // saveData();
        stopSelf();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this,stepCounterSensor);
        stopForeground(true);
        stopSelf();
        FetchTrackerInfos.flag_steps=0;
        super.onDestroy();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SAVEDATA", MODE_PRIVATE);
        int savedStepCount = sharedPreferences.getInt("key1", 0);

        // Check if the saved step count is non-zero or if it's not a valid representation of steps since last boot
        if (savedStepCount != 0 && savedStepCount > steps) {
            // Reset the previous step count to zero
            previousStepCount = 0;
        } else {
            // Use the saved step count as the previous step count
            previousStepCount = savedStepCount;
        }
    }

    private void broadcastStepCount(int stepCount) {
        Intent intent = new Intent("step-count-update");
        intent.putExtra("stepCount", stepCount);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastSpeedAndDistance(float totalDistance,String averageSpeed){
        Intent intent = new Intent("averageSpeed-and-distance-update");
        intent.putExtra("averageSpeed", averageSpeed);
        intent.putExtra("distance", totalDistance);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

//    private double calculateCaloriesBurned() {
//        // Calculate the time duration in hours
//       return =
//    }
    

}

