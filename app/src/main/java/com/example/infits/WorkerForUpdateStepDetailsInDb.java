package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Scheduler;

public class WorkerForUpdateStepDetailsInDb extends Worker {
    RequestQueue requestQueue;
    final String tag = "WORKUPDATESTEP";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
    SharedPreferences loginDetails;
    SharedPreferences goal;

    public WorkerForUpdateStepDetailsInDb(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        loginDetails = getApplicationContext().getSharedPreferences("loginDetails",MODE_PRIVATE);
        goal = getApplicationContext().getSharedPreferences("stepsGoalInt",MODE_PRIVATE);
        if (FetchTrackerInfos.currentSteps > 1 && FetchTrackerInfos.currentSteps <= goal.getInt("stepTrackerGoal",0) ){
            updateDetails("100");
        }else Log.e(tag,"else call updateDetailsCall");
        return Result.success();
    }


    private void updateDetails(String goal) {
        Log.e(tag,"updateDetailsCall");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://infits.in/androidApi/updateStepFragmentDetails.php", response -> {
                Log.e(tag,response);
            },
                    error -> {
                        Log.e(tag,error.toString());
                    }) {
                @SuppressLint("DefaultLocale")
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("clientuserID",loginDetails.getString("clientuserID",""));
                    data.put("steps",FetchTrackerInfos.currentSteps + "");
                    data.put("distance",String.format("%.3f", (FetchTrackerInfos.Distance)));
                    data.put("calories",String.format("%.2f", (FetchTrackerInfos.Calories)));
                    data.put("avgspeed",FetchTrackerInfos.Avg_speed.substring(0, 1));
                    data.put("goal",goal);
                    LocalDateTime now = LocalDateTime.now();
                    data.put("dateandtime",dtf.format(now));
                    return data;
                }
            };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        }

    }

