package Utility;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.infits.NotificationReceiver;

import java.util.Calendar;

public class AlarmHelper {

    public static void createNotificationChannel(Context context,String id,String name)

    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id,name, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }



    @SuppressLint("ScheduleExactAlarm")
    public static void setTrackerAlarm(Context context, String tracker, int hour, int minute){
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent trackerReceiverIntent = new Intent(context, NotificationReceiver.class);
         trackerReceiverIntent.putExtra("tracker",tracker);
        PendingIntent trackerReceiverPendingIntent=PendingIntent.getBroadcast(context,0,trackerReceiverIntent,PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
       alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),trackerReceiverPendingIntent);

    }




}
