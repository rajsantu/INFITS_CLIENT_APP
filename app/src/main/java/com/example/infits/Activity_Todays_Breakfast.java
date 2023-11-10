package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Todays_Breakfast extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    JSONObject mainJsonobj;
    JSONArray mainJsonArray;

    ArrayList<String> mealInfotransfer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_breakfast);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {

//            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//            String json = preferences.getString("mealInfotransfer", null);
//            if (json != null) {
//                try {
//                    JSONArray jsonArray = new JSONArray(json);
//                    mealInfotransfer = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        mealInfotransfer.add(jsonArray.getString(i));
//                        Toast.makeText(this, jsonArray.getString(i), Toast.LENGTH_SHORT).show();
//                    }
//                    // do something with mealInfotransfer
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }


//            Intent intent = getIntent();
//            JSONArray jsonArray = new JSONArray();
//            mainJsonArray=new JSONArray();
//            mainJsonobj=new JSONObject();
//            JSONObject jsonObject = new JSONObject(intent.getStringExtra("mealInfoForPhoto"));
//            sharedPreferences=getSharedPreferences("TodaysBreakFast", Context.MODE_PRIVATE);
//            if(sharedPreferences.contains("TodaysBreakFast")) {
//                JSONObject jsonObject1=new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
//                JSONArray jsonArray1=jsonObject1.getJSONArray("TodaysBreakFast");
//                for (int i=0;i<jsonArray1.length();i++){
//                    mainJsonArray.put(jsonArray1.getJSONObject(i));
//                }
//            }
//            mainJsonArray.put(jsonObject);
//            mainJsonobj.put("TodaysBreakFast",mainJsonArray);
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putString("TodaysBreakFast",mainJsonobj.toString());
//            editor.commit();
//            Log.d("mealInfoForPhoto", sharedPreferences.getString("TodaysBreakFast","").toString());
//
//            Bundle bundle=new Bundle();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////            MealtrackerTodays_Breakfast mealtrackerTodays_breakfast = new MealtrackerTodays_Breakfast();
////            fragmentTransaction.replace(R.id.frameLayout,mealtrackerTodays_breakfast).commit();
//            MealtrackerTodays_Breakfast fragmentTodays_breakFast = new MealtrackerTodays_Breakfast();
//            fragmentTransaction.replace(R.id.frameLayout,fragmentTodays_breakFast).commit();

//

//            FragmentTodays_BreakFast fragmentTodays_breakFast = new FragmentTodays_BreakFast();
//            fragmentTransaction.replace(R.id.frameLayout,fragmentTodays_breakFast).commit();


//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, new MyFragment());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

//            Log.d("TAG", "TodayBreakfast: Called");
//
//            Toast.makeText(this, "TodayBreakfast: Called", Toast.LENGTH_SHORT).show();

            String message = getIntent().getStringExtra("fragment");

            if(message.equals("mealinfowithphoto")) {
                Bundle bundle = getIntent().getBundleExtra("bundle");
//                if (bundle != null) {
//                    Toast.makeText(this, "Bundle not null", Toast.LENGTH_SHORT).show();
//                    ArrayList<String> mealInfotransfer = bundle.getStringArrayList("mealInfotransfer");
//                    Toast.makeText(this, "Array not null", Toast.LENGTH_SHORT).show();
//                    // Do something with the array list
//                }
//                else{Toast.makeText(this, "Bundle null", Toast.LENGTH_SHORT).show();}
                Fragment mealInfoWithPhotoFragment = new mealInfoWithPhoto();
                mealInfoWithPhotoFragment.setArguments(bundle);
//                Toast.makeText(this, "Started ", Toast.LENGTH_SHORT).show();
                transaction.replace(R.id.frameLayout, mealInfoWithPhotoFragment).commit();
//                Toast.makeText(this, "Called ", Toast.LENGTH_SHORT).show();
            }
            else {
//                String mealInfoJson = getIntent().getStringExtra("mealInfoForPhoto");
////                Toast.makeText(this, "Final", Toast.LENGTH_SHORT).show();
////                try {
////
////
////                    JSONObject mealInfoObject = new JSONObject(mealInfoJson);
////                    String mealName = mealInfoObject.getString("mealName");
////                    String mealType = mealInfoObject.getString("Meal_Type");
////                    String calorieValue = mealInfoObject.getString("calorieValue");
////                    String carbsValue = mealInfoObject.getString("carbsValue");
////                    String fatValue = mealInfoObject.getString("fatValue");
////                    String proteinValue = mealInfoObject.getString("proteinValue");
////                    String quantity = mealInfoObject.getString("Quantity");
////                    String size = mealInfoObject.getString("Size");
////
////                    Toast.makeText(this, mealName, Toast.LENGTH_SHORT).show();
////
////                    // Do something with the values...
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                Bundle bundle = new Bundle();
//                bundle.putString("mealInfoObject", mealInfoJson);
//                Fragment mealtrackerTodaysBreakfast = new MealtrackerTodays_Breakfast();
//                mealtrackerTodaysBreakfast.setArguments(bundle);
//                transaction.replace(R.id.frameLayout, mealtrackerTodaysBreakfast);



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
//            MealtrackerTodays_Breakfast mealtrackerTodays_breakfast = new MealtrackerTodays_Breakfast();
//            fragmentTransaction.replace(R.id.frameLayout,mealtrackerTodays_breakfast).commit();
                MealtrackerTodays_Breakfast fragmentTodays_breakFast = new MealtrackerTodays_Breakfast();
                fragmentTodays_breakFast.setArguments(bundle);
                transaction.replace(R.id.frameLayout,fragmentTodays_breakFast).commit();


                Log.d("TAG", "TodayBreakfast: Called");

//            Toast.makeText(this, "TodayBreakfast: Called", Toast.LENGTH_SHORT).show();


            }
//            transaction.addToBackStack(null);
            transaction.commit();
//            finish();
//            Toast.makeText(this, "Finished activity", Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            Log.d("ActivityException", e.toString());
        }
    }
}