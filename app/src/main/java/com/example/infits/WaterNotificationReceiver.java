package com.example.infits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.infits.databinding.FragmentWaterReminderBinding;

import java.util.Calendar;

public class WaterNotificationReceiver extends BroadcastReceiver {

    Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HII","WaterTracker");
        Intent resultIntent = new Intent(context, FragmentWaterReminderBinding.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("notification", "water");
        String notificationData="It's time to drink water!";
        resultIntent.putExtra("notification_id",notificationData);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

//        String contentText = "Only " + getRemainingTime() + " hours remaining. You still have not reached your goal.";
//
//        Notification notification = new NotificationCompat.Builder(context, "WaterChannelId")
//                .setContentTitle("Water Tracker")
//                .setContentText(contentText)
//                .setSmallIcon(R.mipmap.logo)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(resultPendingIntent)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(contentText))
//                .build();
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//        managerCompat.notify(1, notification);

    }

    public long getRemainingTime() {
        calendar = Calendar.getInstance();
        long currHour = calendar.get(Calendar.HOUR_OF_DAY);

        return 24 - currHour;
    }
}
