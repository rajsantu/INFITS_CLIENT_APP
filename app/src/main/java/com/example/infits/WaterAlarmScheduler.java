package com.example.infits;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.LocalDateTime;

public class WaterAlarmScheduler {
    PendingIntent waterReceiverPendingIntent;
    AlarmManager alarmManager;
    long timeDiff_s_to_e,timeDiff_c_to_s,alarmTime,targetTime;
    long ondDayInMillis = 24*3600*1000;
    long todayTimeInMilli = LocalDateTime.now().getMillisOfDay();
    public void setAlarm(Context context,long startTime,long endTime,long intervalTimeInMilli,long times){
        //  Toast.makeText(context, "val"+times, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "setAlarm: startTime:"+startTime+" endTime:"+endTime+" interval:"+intervalTimeInMilli+" times:"+times);

        if (startTime> endTime){
            endTime = ondDayInMillis-(startTime - endTime);
            Log.i(TAG, "setAlarm: if (startTime> endTime) { endtime:"+endTime+" }");
            timeDiff_s_to_e = endTime-startTime;
        }else {
            timeDiff_s_to_e = endTime-startTime;
        }

        if(todayTimeInMilli>startTime){
            timeDiff_c_to_s =ondDayInMillis - (todayTimeInMilli -startTime);
            Log.i(TAG, "setAlarm: if(todayTimeInMilli>startTime) { timeDiff:"+timeDiff_c_to_s+" }");
        }else {
            timeDiff_c_to_s = startTime-todayTimeInMilli;
            Log.i(TAG, "setAlarm: if(todayTimeInMilli<startTime) { timeDiff:"+timeDiff_c_to_s+" }");
        }

        if(times==0){//if times not selected
            times = ((endTime - startTime) /(intervalTimeInMilli))+1;
            Log.i(TAG, "setAlarm: if remindsTime not selected then times:"+times);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            targetTime = System.currentTimeMillis() + timeDiff_c_to_s + timeDiff_s_to_e;
            for(int i=0;i<times;i++){

                alarmTime = System.currentTimeMillis() + timeDiff_c_to_s + (i * intervalTimeInMilli);
                Log.i(TAG, "setAlarm: values: alarmTime:"+alarmTime+" endTime:"+targetTime);

                if (alarmTime <= targetTime) {
                    Intent waterReceiverIntent = new Intent(context,NotificationReceiver.class );
                    waterReceiverIntent.putExtra("tracker", "water");

                    PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(
                            context, i, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

                    // Setting the repeating alarm
                   // alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, waterReceiverPendingIntent);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime, waterReceiverPendingIntent);
                    Log.e(TAG, "alarm set! : at "+(alarmTime-System.currentTimeMillis())/60000+" minutes");
                }else{
                    Log.e(TAG, "setAlarm: error in endTime");
                }
            }

        }else {//if selected
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            targetTime = System.currentTimeMillis() + timeDiff_c_to_s + timeDiff_s_to_e;
            for (int i = 0; i < times; i++) {
                alarmTime = System.currentTimeMillis() + timeDiff_c_to_s + (i * intervalTimeInMilli);

                Log.i(TAG, "setAlarm: values: alarmTime:" + alarmTime + " endTime:" + targetTime);
                if (alarmTime <= targetTime) {
                    Intent waterReceiverIntent = new Intent(context, NotificationReceiver.class);
                    waterReceiverIntent.putExtra("tracker", "water");

                    PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(
                            context, i, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

                    // Setting the repeating alarm
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, waterReceiverPendingIntent);
                    Log.e(TAG, "alarm set! : at " + (alarmTime-System.currentTimeMillis()) / 60000 + " minutes   " +(alarmTime-System.currentTimeMillis()) /3600000 + " hours" );
                } else {
                    Log.e(TAG, "setAlarm: error in endTime");
                }
            }
        }
    }


    public void cancelAlarm(Context context){
        if(alarmManager==null){
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        Intent waterReceiverIntent = new Intent(context,NotificationReceiver.class );
        waterReceiverIntent.putExtra("tracker", "water");

        for(int i=0;i<100;i++){
            waterReceiverPendingIntent = PendingIntent.getBroadcast(
                    context, i, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(waterReceiverPendingIntent);
            Log.i(TAG, "cancelAlarm: "+i);
        }

    }



}
