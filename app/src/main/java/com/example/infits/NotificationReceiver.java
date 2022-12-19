package com.example.infits;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive", "received");

        String tracker = intent.getStringExtra("tracker");

        switch (tracker) {
            case "sleep" : sleep(context);
            case "step" : step(context);
            case "water" : water(context);
            case "calorie" : calorie(context);
            case "weight" : weight(context);
        }
    }

    private void weight(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "weight");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String contentText = "It's time to eat and track your calories.";

        Notification notification = new NotificationCompat.Builder(context, "WeightChannelId")
                .setContentTitle("Weight Reminder")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        Log.d("weight()", "set");
    }

    private void calorie(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "calorie");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String contentText = "It's time to eat some food and reach your goal.";

        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                .setContentTitle("Calorie Reminder")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        Log.d("calorie()", "set");
    }

    private void water(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "water");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String contentText = "It's time to drink a glass of water and reach your goal.";

        Notification notification = new NotificationCompat.Builder(context, "WaterChannelId")
                .setContentTitle("Water Reminder")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        Log.d("water()", "set");
    }

    private void step(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "step");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String contentText = "It's time to go for a walk and finish your daily goal.";

        Notification notification = new NotificationCompat.Builder(context, "SleepChannelId")
                .setContentTitle("Step Reminder")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        Log.d("step()", "set");
    }

    private void sleep(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "sleep");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String contentText = "It's time to go to sleep.";

        Notification notification = new NotificationCompat.Builder(context, "SleepChannelId")
                .setContentTitle("Sleep Reminder")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
        Log.d("sleep()", "set");
    }
}
