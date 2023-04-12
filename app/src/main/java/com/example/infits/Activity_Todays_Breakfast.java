package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activity_Todays_Breakfast extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    JSONObject mainJsonobj;
    JSONArray mainJsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_breakfast);
        try {


            Intent intent = getIntent();
            JSONArray jsonArray = new JSONArray();
            mainJsonArray=new JSONArray();
            mainJsonobj=new JSONObject();
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("mealInfoForPhoto"));
            sharedPreferences=getSharedPreferences("TodaysBreakFast", Context.MODE_PRIVATE);
            if(sharedPreferences.contains("TodaysBreakFast")) {
                JSONObject jsonObject1=new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
                JSONArray jsonArray1=jsonObject1.getJSONArray("TodaysBreakFast");
                for (int i=0;i<jsonArray1.length();i++){
                    mainJsonArray.put(jsonArray1.getJSONObject(i));
                }
            }
            mainJsonArray.put(jsonObject);
            mainJsonobj.put("TodaysBreakFast",mainJsonArray);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("TodaysBreakFast",mainJsonobj.toString());
            editor.commit();
            Log.d("mealInfoForPhoto", sharedPreferences.getString("TodaysBreakFast","").toString());

            Bundle bundle=new Bundle();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentTodays_BreakFast fragmentTodays_breakFast = new FragmentTodays_BreakFast();
            fragmentTransaction.replace(R.id.frameLayout,fragmentTodays_breakFast).commit();
        }catch (Exception e){
            Log.d("Exception", e.toString());
        }
    }
}