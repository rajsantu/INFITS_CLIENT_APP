package com.example.infits;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataFromDatabase {




//    public static String ipConfig = "http://192.168.29.187/infits/";
    public static String ipConfig = "https://infits.in/androidApi/";

//    public static String ipConfig = "https://192.168.1.9/infits/";
 

    public static boolean flag= false;
    public static String profilePhoto="default.jpg";
//    public static String dietitianuserID,clientuserID,password,name,email,mobile,location,age,gender,weight,plan,height,dietitian_id,client_id,dateandtime,verification;
    //Mustafa Chanes
    public static String fatMeal,caloriesMeal,carbsMeal,proteinMeal,fiber,nameMeal,typeMeal;
    //Mustafa Changes

    public static String dietitianuserID,clientuserID,password,name,email,mobile,location,age,gender,weight,plan,height,dietitian_id,client_id,dateandtime,verification,verification_code;

    public static Bitmap profile;
    public static Bitmap dtPhoto;
    public static String profilePhotoBase;
    public static String macAddress;



    public static boolean proUser ;

    public static String stepsStr = "0",stepsGoal= "0",waterStr= "0",waterGoal= "0",sleephrsStr= "0",
            sleepminsStr= "0",sleepGoal= "0",weightStr= "0",weightGoal= "0",bmi = "0",bpm = "0",bpmUp = "0",bpmDown = "0";
}