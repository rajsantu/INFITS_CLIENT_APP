package com.example.infits;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinav.progress_view.ProgressData;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activitySecondFragment extends Fragment {

    //GaugeSeekBar progressBar;
    Button run_goal_btn,btn_start;
    TextView textView71, textView72, textView73, textView74, textView76,textView61,textView75;
    ImageView back_button;
    ImageView set_goal;
    EditText goal_value_txt;
    String goal_value;

    private int distance,calories,runtime,goal;
    private RequestQueue requestQueue;
    public activitySecondFragment() {
        // Required empty public constructor
    }
    public static activitySecondFragment newInstance(String param1, String param2) {
        activitySecondFragment fragment = new activitySecondFragment();
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
        View view =inflater.inflate(R.layout.todaystotalrunning, container, false);
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.activity_trck_popup);
        run_goal_btn = view.findViewById(R.id.imageView74_btn);
        btn_start = view.findViewById(R.id.imageView86_trd);
        goal_value_txt = dialog.findViewById(R.id.textView88);
        textView71 = view.findViewById(R.id.textView71);
        textView72 = view.findViewById(R.id.textView72);
        textView73 = view.findViewById(R.id.textView73);
        textView74 = view.findViewById(R.id.textView74);
        textView76 = view.findViewById(R.id.textView76);
        textView61 = view.findViewById(R.id.textView61);
        set_goal = dialog.findViewById(R.id.imageView89);
        textView75 = view.findViewById(R.id.textView75);

        back_button=view.findViewById(R.id.imageView75);

        // String url = "https://infits.in/androidApi/runningTracker.php";
        //  String url = "http://10.12.2.128/infits/runningTracker.php";
        String url = "http://192.168.29.52/infits/runningTracker.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Running Tracker Data", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");

                        // Parse your data and update your UI elements here
                        // For example:
                        String totaldistance = dataObject.getString("distance");
                        String totalcalories = dataObject.getString("calories");
                        String totalruntime = dataObject.getString("runtime");
                        String todaysgoal = dataObject.getString("goal");

                        // Update your TextViews after converting the values to strings
                        textView71.setText(todaysgoal+" KM");
                        textView72.setText(totaldistance+" KM");
                        textView73.setText(totalcalories+ " KCAL");
                        textView74.setText(totalruntime + " HOURS");
                        textView75.setText(totaldistance);
                        textView61.setText(totaldistance+"KM");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("Running Tracker data", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                data.put("date", dtf.format(now));
                data.put("distance", "0");
                data.put("calories", "0");
                data.put("runtime", "0");
                data.put("goal","0");
                data.put("operationtodo","get");
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        run_goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView closeImageView = dialog.findViewById(R.id.imageView87);
                dialog.show();
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
                        String url = "http://192.168.29.52/infits/runningTracker.php";

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
//                                data.put("distance", "0");
//                                data.put("calories", "0");
//                                data.put("runtime", "0");
                                data.put("goal", goal);
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                java.time.LocalDateTime now = LocalDateTime.now();
                                data.put("date", dtf.format(now));
                                data.put("operationtodo","setgoal");
                                //data.put("categories", "Running");
//                                data.put("heartrate", "0");
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
                        textView71.setText(goal+" KM");

                    }
                });
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_activityTracker2);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String goal = "5";
//                String url = "https://infits.in/androidApi/runningTracker.php";
//
//                StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String calories = jsonObject.getString("calories");
//                        String distance = jsonObject.getString("distance");
//                        String runtime = jsonObject.getString("runtime");
//
//                        textView71.setText(calories);
//                        textView72.setText(distance);
//                        textView73.setText(runtime);
//
//                        Toast.makeText(getContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
//                    }
//                }, error -> {
//                    Toast.makeText(getContext(), "Error: for " + error.toString(), Toast.LENGTH_SHORT).show();
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> data = new HashMap<>();
//                        Date now = new Date();
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
//                        data.put("client_id", DataFromDatabase.client_id);
//                        data.put("clientuserID", DataFromDatabase.clientuserID);
//                        data.put("distance", "0");
//                        data.put("calories", "0");
//                        data.put("runtime", "0");
//                        data.put("duration", "0");
//                        data.put("dateandtime", dtf.format((TemporalAccessor) now));
//                        data.put("goal", goal);
//                        data.put("operationtodo","get");
//                        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                        data.put("date", dtf2.format((TemporalAccessor) now));
//                        return data;
//                    }
//                };
//
//                Volley.newRequestQueue(getContext()).add(request);
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_running_frag1);
            }
        });

        return view;
    }

}