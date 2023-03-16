package com.example.infits;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimerTask;


public class ScheduleTask extends TimerTask
{
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ScheduleTask( AlarmManager alarmManager, PendingIntent pendingIntent){
        this.alarmManager=alarmManager;
        this.pendingIntent=pendingIntent;
    }
    public void run()
    {
        try {
            alarmManager.cancel(pendingIntent);
            System.out.println("EndTime: " + Calendar.getInstance().getTime().toString());



        }catch (Exception e){
            System.out.println("error123::   "+e.getMessage());
        }

    }
}
