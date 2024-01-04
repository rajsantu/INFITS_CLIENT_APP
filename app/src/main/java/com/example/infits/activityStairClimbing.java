package com.example.infits;

import static com.facebook.FacebookSdk.getApplicationContext;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class activityStairClimbing extends Fragment {
    // Button btn_setgoal, btn_start_trd;
    Button run_goal_btn,btn_start;
    TextView textView71, textView72, textView73, textView74,goal_type,textView61,textView75,textView76,goal_unit;
    ImageView back_button;
    ImageView set_goal;
    EditText goal_value_txt;
    String goal_value;
    private GaugeSeekBar progressBarWalking;
    private static final int REQUEST_CODE = 123;


    public activityStairClimbing() {
        // Required empty public constructor
    }
    public static activityStairClimbing newInstance(String param1, String param2) {
        activityStairClimbing fragment = new activityStairClimbing();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.todaytotalstairclimbing, container, false);
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.activity_trck_popup);
        run_goal_btn = view.findViewById(R.id.imageView74_btn);
        btn_start = view.findViewById(R.id.imageView86_trd);
        goal_value_txt = dialog.findViewById(R.id.textView88);
        goal_type = dialog.findViewById(R.id.textView83);
        goal_unit = dialog.findViewById(R.id.textView89);
        textView71 = view.findViewById(R.id.textView71);
        textView72 = view.findViewById(R.id.textView72);
        textView73 = view.findViewById(R.id.textView73);
        textView74 = view.findViewById(R.id.textView74);
        textView75 = view.findViewById(R.id.textView75);
        textView61 = view.findViewById(R.id.textView61);
        set_goal = dialog.findViewById(R.id.imageView89);
        back_button=view.findViewById(R.id.imageView75);
        progressBarWalking =  view.findViewById(R.id.progressBarCycling);
        LoadTodayData();
        getParentFragmentManager().setFragmentResultListener("updateDataKey", this, (requestKey, result) -> {
            // Check if the updateKey is present in the result
            if (result.containsKey("updateKey")) {
                String updateKey = result.getString("updateKey", "");
                if ("dataUpdated".equals(updateKey)) {
                    // Data is updated, perform your actions here
                    LoadTodayData(); // Call your method to update the TextView
                }
            }
        });

        run_goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView closeImageView = dialog.findViewById(R.id.imageView87);
                dialog.show();
                goal_unit.setText("ST");
                goal_type.setText("Set Goal");
                closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                set_goal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goal_value = goal_value_txt.getText().toString();
                        //Toast.makeText(getApplicationContext(), goal_value, Toast.LENGTH_SHORT).show();
                        String goal = goal_value;
                        //String url = "http://10.12.2.128/infits/activitygoal.php";
                        String url = "http://192.168.29.52/infits/TrekkingTracker.php";

                        Log.d("Request", "Sending a request to: " + url);

                        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                            Log.d("Response", "Received response: " + response);
                            if (response.trim().equals("updated")) {
                                Toast.makeText(getApplicationContext(),"Goal set : "+ goal_value, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("Response", "Server response: " + response);
                                Toast.makeText(getApplicationContext(), "Not working: " + response, Toast.LENGTH_SHORT).show();
                            }
                        }, error -> {
                            Log.e("RequestError", "Error: " + error.toString());
                            Toast.makeText(getApplicationContext(), "Error: " + error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                data.put("client_id", DataFromDatabase.client_id);
                                data.put("clientuserID", DataFromDatabase.clientuserID);
                                data.put("goal", goal);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                java.time.LocalDateTime now = LocalDateTime.now();
                                data.put("date", dtf.format(now));
                                data.put("operationtodo","setgoal");
                                data.put("table","stairclimbtracker");
                                data.put("category","Stair Climbing");
                                return data;
                            }
                        };
                        request.setRetryPolicy(new DefaultRetryPolicy(
                                10000,  // 10 seconds
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                        Volley.newRequestQueue(getApplicationContext().getApplicationContext()).add(request);
                        //  Toast.makeText(getApplicationContext(),"check 1",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        textView71.setText(goal);

                    }
                });
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_activityStairClimbing_to_activityTracker2);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TodayGoal = textView71.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("todaysgoal", TodayGoal);
                int actionId = R.id.action_activityStairClimbing_to_StairClimbing;
                Navigation.findNavController(v).navigate(actionId, bundle);
            }
        });
        textView71.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update the progress of the GaugeSeekBar when the goal text changes
                updateProgress();
            }
        });
        textView75.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update the progress of the GaugeSeekBar when the goal text changes
                updateProgress();
            }
        });
        return view;
    }



    private void updateProgress() {
        String goalValue = textView71.getText().toString();
        String todayTotalValue =textView75.getText().toString();

        // Convert the string values to floats (you might want to add error handling here)
        float goal = Float.parseFloat(goalValue);
        float todayTotal = Float.parseFloat(todayTotalValue);

        // Set the progress of the GaugeSeekBar based on the goal and today's total
        float progress = Math.min(1.0f, todayTotal / goal);
        // Set the progress of the GaugeSeekBar
        progressBarWalking.setProgress(progress);
        //progressBarWalking.setProgress(progress);

    }

    private void LoadTodayData() {
        String url = "http://192.168.29.52/infits/trekkingTracker.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("dancing Tracker Data", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");
                        if (!dataObject.isNull("steps")) {
                            // Data found in walkingtracker table
                            String totalsteps = dataObject.getString("steps");
                            String totaldistance = dataObject.getString("distance");
                            String totalcalories = dataObject.getString("calories");
                            String totalruntime = dataObject.getString("runtime");
                            String todaysgoal = dataObject.getString("goal");

                            textView71.setText(todaysgoal);
                            textView72.setText(totalsteps + " Steps");
                            textView73.setText(totalcalories + " KCAL");
                            textView74.setText(totalruntime + " HOURS");
                            textView75.setText(totalsteps);
                            textView61.setText(totalsteps + " Steps");
                        } else {
                            // No data found in walkingtracker table, set values to 0
                            String todaysgoal = dataObject.getString("goal");
                            textView71.setText(todaysgoal);
                            textView72.setText("0 Steps");
                            textView73.setText("0 KCAL");
                            textView74.setText("0 HOURS");
                            textView75.setText("0");
                            textView61.setText("0 Steps");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("Stair Climbing Tracker data", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                data.put("date", dtf.format(now));
                data.put("operationtodo","get");
                data.put("table","stairclimbtracker");
                data.put("category","Stair Climbing");
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}