package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class WaterTrackerFragment extends Fragment {

    ImageView imgback, addliq, reminder;

    LottieAnimationView lottieAnimationViewWater;
    TextView waterGoalPercent, wgoal3, consumed;
    TextView waterGoal;
    String liqType = "water", liqAmt;
    Button setgoal;
    RecyclerView rc ;
    float goalWater;
    static int goal = 1800;

    ProgressBar progressBar;
    int consumedInDay;

    AlarmManager alarmManager;
    PendingIntent waterReceiverPendingIntent;
    Calendar calendar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private int progr = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.getDefault());
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    Date date = new Date();

    String curr_date=dateFormat.format(date);
    public WaterTrackerFragment() {

    }

    public static WaterTrackerFragment newInstance(String param1, String param2) {
        WaterTrackerFragment fragment = new WaterTrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        lottieAnimationViewWater.playAnimation();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(getArguments() != null && getArguments().getBoolean("notification") /* coming from notification */) {
                    startActivity(new Intent(getActivity(),DashBoardMain.class));
                    requireActivity().finish();
                } else {
                    Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_waterTrackerFragment_to_dashBoardFragment);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_tracker, container, false);
//        progressBar = (ProgressBar) view.findViewById(R.id.progress_barr);
//        CircularProgressIndicator circularProgress = view.findViewById(R.id.progress_barr);
        addliq = view.findViewById(R.id.addliq);
        imgback = view.findViewById(R.id.imgback);
        lottieAnimationViewWater = view.findViewById(R.id.waterAnimation);
        waterGoalPercent = view.findViewById(R.id.water_goal_percent);
        wgoal3 = view.findViewById(R.id.wgoal3);
        setgoal = view.findViewById(R.id.setgoal_watertracker);
        consumed = view.findViewById(R.id.water_consumed);
        waterGoal = view.findViewById(R.id.water_goal);
        reminder = view.findViewById(R.id.reminder);
        rc = view.findViewById(R.id.past_activity);
        getLatestWaterData();
        //updateProgressBar();
        if (DataFromDatabase.waterGoal.equals(null) || DataFromDatabase.waterGoal.equalsIgnoreCase("null")) {
            waterGoal.setText("0 ml");
        } else {
            waterGoal.setText(DataFromDatabase.waterGoal + " ml");
            try {
                goal = Integer.parseInt(DataFromDatabase.waterGoal);
<<<<<<< HEAD
               // Log.d("Goal",String.valueOf(goal));
            } catch (NumberFormatException ex) {
                goal = 1800;
                waterGoal.setText(1800 + " ml");
               // Log.d("Goal",String.valueOf(goal));
=======
                // Log.d("Goal",String.valueOf(goal));
            } catch (NumberFormatException ex) {
                goal = 1800;
                waterGoal.setText(1800 + " ml");
                // Log.d("Goal",String.valueOf(goal));
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                System.out.println(ex);
            }
        }

        if (DataFromDatabase.waterStr.equals(null) || DataFromDatabase.waterStr.equalsIgnoreCase("null")) {
            consumed.setText("0 ml");
        } else {
            consumed.setText(DataFromDatabase.waterStr + " ml");  // waterStr = waterConsumed
            try {
                consumedInDay = Integer.parseInt(DataFromDatabase.waterStr);
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
            } catch (NumberFormatException ex) {
                consumedInDay = 0;
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
            }
        }

        getLatestWaterData();

        calendar = Calendar.getInstance();
        Log.d("water", "currHour: " + calendar.get(Calendar.HOUR_OF_DAY));

        createNotificationChannel();
        waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));

        ///////////////////////////////////////
        pastActivity();

        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.watergoaldialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                final EditText goaltxt = dialog.findViewById(R.id.goal_water);
                Button setGoalBtn = dialog.findViewById(R.id.set_water_goal);  // Save Button in SetGoal Fragment
                setGoalBtn.setOnClickListener(v -> {
                    if (!goaltxt.getText().toString().equals("")) {
                        goal = Integer.parseInt(goaltxt.getText().toString());
<<<<<<< HEAD
                       // Log.d("Goal",String.valueOf(goal));
=======
                        // Log.d("Goal",String.valueOf(goal));
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                        waterGoal.setText(goaltxt.getText().toString() + " ml");
                        waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
                        consumedInDay = 0;
                        //String url = String.format("%supdatewatergoal.php",DataFromDatabase.ipConfig);
<<<<<<< HEAD
                        String url =  DataFromDatabase.ipConfig +"updatewatergoal.php";
=======
                        //String url =  DataFromDatabase.ipConfig +"updatewatergoal.php";
                        String url = "https://infits.in/androidApi/updatewatergoal.php";
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

<<<<<<< HEAD
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");

                                    int newGoal = jsonObject.getInt("goal");


                                    // Update the UI with the new goal value if the operation was successful
                                    if (goal!=newGoal) {
                                        goaltxt.setText(String.valueOf(newGoal));
                                        waterGoalPercent.setText(String.valueOf(calculateGoal(newGoal)));
                                        getLatestWaterData();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("response;;", "JSON parsing error.");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("response1;;","error");
                            }
                        }
=======
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String message = jsonObject.getString("message");

                                            int newGoal = jsonObject.getInt("goal");


                                            // Update the UI with the new goal value if the operation was successful
                                            if (goal!=newGoal) {
                                                goaltxt.setText(String.valueOf(newGoal));
                                                waterGoalPercent.setText(String.valueOf(calculateGoal(newGoal)));
                                                getLatestWaterData();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("response;;", "JSON parsing error.");
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("response1;;","error");
                                    }
                                }
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                        ){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                                data.put("clientuserID", DataFromDatabase.clientuserID);
                                data.put("client_id", DataFromDatabase.client_id);
                                data.put("dateandtime",dtf.format(now));
                                data.put("drinkConsumed", String.valueOf(consumedInDay));
                                data.put("goal",String.valueOf(goal));
                                data.put("type", "water");
                                data.put("dietitian_id",DataFromDatabase.dietitian_id);
                                data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
                                data.put("amount", String.valueOf(consumedInDay));
                                return data;
                            }
                        };
                        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        SharedPreferences notificationPrefs = requireActivity().getSharedPreferences("notificationDetails",MODE_PRIVATE);
                        boolean waterNotificationPermission = notificationPrefs.getBoolean("waterSwitch", false);

                        if(false /*waterNotificationPermission*/) {
                            setNotificationAlarm();
                            cancelNotificationAlarmAtEndOfDay();
                        }

                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Enter Goal", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
                getLatestWaterData();
            }

        });

        addliq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.fragment_add_liquid);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button addDrank = dialog.findViewById(R.id.addbtn);

                Slider slider = dialog.findViewById(R.id.slider);

                TextView choosed = dialog.findViewById(R.id.liqamt); // choosed = choosedLiquedAmount

                final int[] value = new int[1];


                RadioGroup typeOfLiquid = dialog.findViewById(R.id.radioGroup);
                typeOfLiquid.setOnCheckedChangeListener((group, checkedId) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkedId == R.id.soda) {
                            dialog.findViewById(checkedId).setForeground(getActivity().getDrawable(R.drawable.outline_liq));
                            dialog.findViewById(R.id.coffee).setForeground(null);
                            dialog.findViewById(R.id.water).setForeground(null);
                            dialog.findViewById(R.id.juice).setForeground(null);
                            liqType = "soda";
                        }
                        if (checkedId == R.id.water) {
                            dialog.findViewById(checkedId).setForeground(getActivity().getDrawable(R.drawable.outline_liq));
                            dialog.findViewById(R.id.coffee).setForeground(null);
                            dialog.findViewById(R.id.soda).setForeground(null);
                            dialog.findViewById(R.id.juice).setForeground(null);
                            liqType = "water";
                        }
                        if (checkedId == R.id.juice) {
                            dialog.findViewById(checkedId).setForeground(getActivity().getDrawable(R.drawable.outline_liq));
                            dialog.findViewById(R.id.coffee).setForeground(null);
                            dialog.findViewById(R.id.water).setForeground(null);
                            dialog.findViewById(R.id.soda).setForeground(null);
                            liqType = "juice";
                        }
                        if(checkedId == R.id.coffee) {
                            dialog.findViewById(checkedId).setForeground(getActivity().getDrawable(R.drawable.outline_liq));
                            dialog.findViewById(R.id.soda).setForeground(null);
                            dialog.findViewById(R.id.water).setForeground(null);
                            dialog.findViewById(R.id.juice).setForeground(null);
                            liqType = "coffee";
                        }
                    }
                });

                slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                    @Override
                    public void onStartTrackingTouch(@NonNull Slider slider) {

                    }

                    @Override
                    public void onStopTrackingTouch(@NonNull Slider slider) {

                        value[0] = (int) slider.getValue();
                        choosed.setText(String.valueOf((float) value[0]));
                        Log.d("Water", String.valueOf(value[0]));

                    }
                });

                choosed.setText(String.valueOf(slider.getValue()));

                addDrank.setOnClickListener(v1 -> {
                    consumedInDay = (int) Float.parseFloat(choosed.getText().toString());
                    //consumed.setText(consumedInDay);


//                    int progress = (goal*value[0])/100; // setting progress
//                    circularProgress.setProgress(progress, goal); // Added max to goal
                    if(consumedInDay >= goal) {
                        cancelNotificationAlarm();
                        updateInAppNotifications(goal);
                    }

                    dialog.dismiss();
//                    String url = String.format("%swatertracker.php", DataFromDatabase.ipConfig);
//                    StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
//                        if (response.equals("updated")) {
//                            consumed.setText(consumedInDay + " ml");
//                            waterGoalPercent.setText(String.valueOf(calculateGoal()));
//                        } else {
//                            Toast.makeText(getActivity(), "Not working", Toast.LENGTH_SHORT).show();
//                        }
//                    }, error -> {
//                        Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
//                    }) {
//                        @Nullable
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Date date = new Date();
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            sdf.format(date);
//                            Map<String, String> data = new HashMap<>();
//                            SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
//                            data.put("userID", DataFromDatabase.clientuserID);
//                            data.put("date", String.valueOf(date));
//                            data.put("consumed", String.valueOf(consumedInDay));
//                            data.put("goal", String.valueOf(goal));
//                            data.put("time", stf.format(date));
//                            data.put("type", liqType);
//                            data.put("amount", String.valueOf(value[0]));
//                            return data;
//                        }
//                    };
//                    Date date = new Date();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Log.d("date", sdf.format(date));
//                    Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

//                    updateLastRecord();

                    //String url = String.format("%supdatewatertracker.php", DataFromDatabase.ipConfig);
<<<<<<< HEAD
                    String url = DataFromDatabase.ipConfig+"updateWatertracker.php";
=======
                    //String url = DataFromDatabase.ipConfig+"updateWatertracker.php";
                    String url = "https://infits.in/androidApi/updatewatergoal.php";
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed

                    StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String amount=jsonObject.getString("total");
                            consumedInDay= Integer.parseInt(amount);
                            //String total=jsonObject.getString("total");
                            consumed.setText(consumedInDay + " ml");
                            waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
//                        circularProgress.setProgress(calculateGoalReturnInt(),100);
                            lottieAnimationViewWater.setAnimation(R.raw.water_loading_animation_bottle);
                            int durationOfAnimationFromLottie = 6000;
                            int durationOfWaterAnimation = (durationOfAnimationFromLottie*calculateGoalReturnInt()/100)-500;
                            lottieAnimationViewWater.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              lottieAnimationViewWater.pauseAnimation();
                                                          }
                                                      },durationOfWaterAnimation
                            );
<<<<<<< HEAD
                           // consumed.setText(String.valueOf(consumedInDay));
=======
                            // consumed.setText(String.valueOf(consumedInDay));
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                            pastActivity();
                        } catch (JSONException e) {
                            Log.d("response","error");
                            e.printStackTrace();
                        }

                    }, error -> {
                        Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            int amt = (int) Float.parseFloat(choosed.getText().toString());
                            Map<String, String> data = new HashMap<>();
                            data.put("client_id", DataFromDatabase.client_id);
                            data.put("clientuserID", DataFromDatabase.clientuserID);
                            data.put("dateandtime", sdf.format(date));
                            data.put("drinkConsumed", String.valueOf(consumedInDay));
                            data.put("goal", String.valueOf(goal));
                            data.put("type", liqType);
                            data.put("amount", String.valueOf(consumedInDay));
                            data.put("dietitian_id",DataFromDatabase.dietitian_id);
                            data.put("dietitianuserID",DataFromDatabase.dietitianuserID);

<<<<<<< HEAD
                          //  Log.d("update", "consumed: " + consumedInDay);
                           // Log.d("update", "amount: " + amt);
=======
                            //  Log.d("update", "consumed: " + consumedInDay);
                            // Log.d("update", "amount: " + amt);
>>>>>>> bfd15b2b9398e67a134027655549449eb96948ed
                            return data;
                        }
                    };
                    Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
                    request.setRetryPolicy(new DefaultRetryPolicy(50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // waterGoalPercent
                    if(goal != 0)
                        waterGoalPercent.setText((consumedInDay * 100) / goal + " %");
                    else{
                        waterGoalPercent.setText("0 %");
                    }
                });

                dialog.show();

            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments() != null && getArguments().getBoolean("notification") /* coming from notification */) {
                    startActivity(new Intent(getActivity(),DashBoardMain.class));
                    requireActivity().finish();
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_dashBoardFragment);
                }
            }
        });

        reminder.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_waterReminderFragment));

        getParentFragmentManager().setFragmentResultListener("liquidData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                liqType = result.getString("liquidType");
                liqAmt = result.getString("liquidAmt");
            }
        });

        /*
        public void onClick(DialogInterface dialog,
                                int id) {
                            Dialog f = (Dialog) dialog;
                            //This is the input I can't get text from
                            EditText inputTemp = (EditText) f.findViewById(R.id.search_input_text);
                            query = inputTemp.getText().toString();
                           ...
                        }
         */

//        waterGoal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.watergoaldialog);
//
//                final EditText goal = dialog.findViewById(R.id.goal);
//                Button button = dialog.findViewById(R.id.button);
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String wGoal = goal.getText().toString();
//
//                        //textViewsleep.setText("Goal: "+wGoal);
//
//
//                        float liqAmount = Float.parseFloat(liqAmt);
//                        //textViewsleep.setText("Goal: "+wGoal);
//
//
//                        float res = (liqAmount/Float.parseFloat(wGoal)) *100;
//
//                        wgoalperc.setText((int)res+ " %");
//
//                        goalWater = Float.parseFloat(wGoal);
//
//                        dialog.dismiss();
//
//                    }
//                });
//
//                dialog.show();
//
//                //Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_addLiquidFragment);
//            }
//        });

        //textViewsleep.setText("Goal: "+goalWater);


        return view;
    }

    private void updateProgressBar() {

        progressBar.setProgress(progr);
    }
    private void pastActivity(){


        ArrayList<String> dates = new ArrayList<>(); // ArrayList for past Activity
        ArrayList<String> datas = new ArrayList<>(); // ArrayList for past Activity
        ArrayList<String> fetchedDateswater=new ArrayList<>();
        fetchedDateswater.clear();
        //String url = String.format("%spastActivitywater.php", DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/pastActivitywater.php";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {

                System.out.println("water data="+response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("water");
                Log.d("arraylength",String.valueOf(jsonArray.length()));
                Log.d("watervalue:",jsonArray.toString());
                for (int i=0;i<jsonArray.length();i++){
                    fetchedDateswater.add(jsonArray.getJSONObject(i).getString("date"));
                    datas.add(jsonArray.getJSONObject(i).getString("water"));
                    Log.d("watt",datas.toString());
                }
//                for (int i=0;i<5;i++){ //noOfDays
//                    Calendar cal = Calendar.getInstance();
////                    cal.add(Calendar.DATE, -i);
//                    Log.d("featched",fetchedDateswater.toString());
//                    //Log.d("currentInstance",dateFormat.format(cal.getTime()).toString());
//                    if(fetchedDateswater.contains(dateFormat.format(cal.getTime()).toString())==true){
//                        int index=fetchedDateswater.indexOf(dateFormat.format(cal.getTime()));
//                        Log.d("index",String.valueOf(index));
//                        JSONObject object=jsonArray.getJSONObject(index);
//                        dates.add(dateFormat.format(cal.getTime()));
//                        //String data=object.getString("water");
//                        //datas.add(data);
//                    }
//                    else {
//                        dates.add(dateFormat.format(cal.getTime()));
//                        //datas.add("0");
//                    }
//                }


//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String data = object.getString("water");
//                        String date = object.getString("date");
//                        dates.add(date);
//                        datas.add(data);
//                        System.out.println(datas.get(i));
//                        System.out.println(dates.get(i));
//                }

                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(), fetchedDateswater, datas, Color.parseColor("#76A5FF"));
                rc.setLayoutManager(new LinearLayoutManager(getContext()));
                rc.setAdapter(ad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Error", error.toString());
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
//                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                Log.d("clientuserID",DataFromDatabase.clientuserID);
                return data;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void updateInAppNotifications(int goal) {
        SharedPreferences inAppPrefs = requireActivity().getSharedPreferences("inAppNotification", MODE_PRIVATE);
        SharedPreferences.Editor inAppEditor = inAppPrefs.edit();
        inAppEditor.putBoolean("newNotification", true);
        inAppEditor.apply();

        String inAppUrl = String.format("%sinAppNotifications.php", DataFromDatabase.ipConfig);

        String type = "water";
        String text = "You have reached your goal of " + goal + " ml.";

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.format(date);

        StringRequest inAppRequest = new StringRequest(
                Request.Method.POST,
                inAppUrl,
                response -> {
                    if (response.equals("inserted")) Log.d("WaterTrackerFragment", "success");
                    else Log.d("WaterTrackerFragment", "failure");
                },
                error -> Log.e("WaterTrackerFragment",error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("type", type);
                data.put("text", text);
                data.put("date", String.valueOf(date));

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(inAppRequest);
        inAppRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getLatestWaterData() {
        //String url = String.format("%sgetLatestWaterdt.php", DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/getLatestWaterdt.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("dashboardFrag", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("water");


                        if(array.length() != 0) {
                            String waterGoalStr = array.getString("goal");
                            String waterConsumedStr = array.getString("drinkConsumed");
                            goal = Integer.parseInt(waterGoalStr);
                            consumedInDay = Integer.parseInt(waterConsumedStr);

                            waterGoal.setText(goal + " ml");
                            waterGoalPercent.setText((consumedInDay * 100) / goal + " %");
                            consumed.setText(consumedInDay + " ml");
                            lottieAnimationViewWater.setAnimation(R.raw.water_loading_animation_bottle);
                            int durationOfAnimationFromLottie = 6000;
                            int durationOfWaterAnimation = (durationOfAnimationFromLottie*calculateGoalReturnInt()/100)-500;
                            lottieAnimationViewWater.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              lottieAnimationViewWater.pauseAnimation();
                                                          }
                                                      },durationOfWaterAnimation
                            );
                            consumed.setText(String.valueOf(consumedInDay));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("dashboardFrag", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date= new Date();
                data.put("client_id", DataFromDatabase.client_id);
                data.put("clientuserID",DataFromDatabase.clientuserID);
                data.put("dateandtime",dateFormat.format(date));
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void updateLastRecord() {
        //String url = String.format("%sgetLatestWaterdt.php", DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/getLatestWaterdt.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("water", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("water");

                        if(array.length() != 0) {
                            int waterConsumedStr = Integer.parseInt(array.getJSONObject(0).getString("drinkConsumed"));
                        } else {
                            createNewEntry();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("dashboardFrag", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientuserID",DataFromDatabase.clientuserID);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void createNewEntry() {
        //String url = String.format("%swatertracker.php",DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/watertracker.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
            consumed.setText(consumedInDay +" ml");
            waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
        },error -> {
            Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                sdf.format(date);
                Map<String, String> data = new HashMap<>();
                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("client_id",DataFromDatabase.client_id);
                data.put("dateandtime", String.valueOf(date));
                data.put("consumed", String.valueOf(consumedInDay));
                data.put("goal", "0");
                data.put("type", liqType);
                data.put("amount", "0");
                data.put("dietitian_id",DataFromDatabase.dietitian_id);
                data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
                return data;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setNotificationAlarm() {
        Log.d("water", "createAlarm");
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        Intent waterReceiverIntent = new Intent(requireContext(), WaterNotificationReceiver.class);
        PendingIntent waterReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        long time = calendar.getTimeInMillis() + 60*60*1000; // current time + 1 hour
        long interval = 4*60*60*1000; // 4 hours

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, waterReceiverPendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelNotificationAlarm() {
        Intent waterReceiverIntent = new Intent(requireContext(), WaterNotificationReceiver.class);
        waterReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, waterReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(waterReceiverPendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancelNotificationAlarmAtEndOfDay() {
        Intent waterCancelReceiverIntent = new Intent(requireContext(), WaterNotificationCancelReceiver.class);
        waterCancelReceiverIntent.putExtra("AlarmToCancel", waterReceiverPendingIntent);

        PendingIntent waterCancelReceiverPendingIntent = PendingIntent.getBroadcast(requireContext(), 0, waterCancelReceiverIntent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        }

        long endOfDay = 86400 * 1000; // 24:00:00

        alarmManager.set(AlarmManager.RTC_WAKEUP, endOfDay, waterCancelReceiverPendingIntent);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WaterChannelId", "Water Tracker", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = requireActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    String calculateGoal(int goal) {
        int per = 0;
        try {
            per = consumedInDay * 100 / goal;
        } catch (ArithmeticException e) {
            Log.d("WaterTrackFrag", "Arithmetic Ex, consumedInDay, goal: " + consumedInDay + ", " + goal);
        }
        System.out.println(per);
        System.out.println(consumedInDay);
        System.out.println(goal);
        return per + " %";
    }
    int calculateGoalReturnInt() {
        int per = 0;
        try {
            per = consumedInDay * 100 / goal;

        } catch (ArithmeticException e) {
            per= 1724*100/2423;
            Log.d("WaterTrackFrag", "Arithmetic Ex, consumedInDay, goal: " + consumedInDay + ", " + goal);
        }
        System.out.println(per);
        //System.out.println(consumedInDay);
        //System.out.println(goal);
        return per;
    }
}