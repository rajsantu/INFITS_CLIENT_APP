package com.example.infits.customDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.infits.ConsultationFragment;
import com.example.infits.DataSectionOne;

public class SectionPref {

    public static void saveform(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
           // Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC1PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress", progressVal);
            editor1.apply();
        }
    }

    public static void saveformsection2(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
//            Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC2PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress2", progressVal);
            editor1.apply();
        }

    }
    public static void saveformsection3(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
//            Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC3PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress3", progressVal);
            editor1.apply();
        }

    }
    public static void saveformsection4(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
//            Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC4PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress4", progressVal);
            editor1.apply();
        }

    }
    public static void saveformsection5(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
//            Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC5PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress5", progressVal);
            editor1.apply();
        }

    }
    public static void saveformsection6(String key,String value,int SectionValue,int previousVal,int progressVal,String sharedPrefName,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        String sharedemail = sharedPreferences.getString(key, "");
        // Example in an Activity
        if (!(sharedemail.isEmpty()) && previousVal==SectionValue){
//            Toast.makeText(context, String.valueOf(previousVal), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("SEC6PROG", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putInt("progress6", progressVal);
            editor1.apply();
        }

    }
}
