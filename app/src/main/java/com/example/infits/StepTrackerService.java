package com.example.infits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class StepTrackerService extends Service {

    Handler handler = new Handler();
    Runnable runnable;
    int counter = 0;
    int STEP_REQUEST_CODE = 1;

    public StepTrackerService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "StepChannelId");

        runnable = () ->
        {
            if(counter == StepTrackerFragment.goalVal)
            {
                stopSelf();

                Notification goalReachedNotification = builder.setOngoing(false)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Step Tracker")
                        .setContentText("Congratulations! You have reached your goal.")
                        .setChannelId("StepChannelId")
                        .setAutoCancel(true)
                        .build();

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(2, goalReachedNotification);
            }

            NotificationChannel channel = new NotificationChannel("StepChannelId", "Steps Tracker", NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Intent notificationIntent = new Intent(getApplicationContext(), SplashScreen.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), STEP_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle("Step Tracker is running...")
                    .setContentIntent(pendingIntent)
                    .setChannelId("StepChannelId")
                    .build();

            startForeground(1, notification);
        };
        handler.post(runnable);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.post(runnable);
    }
}
