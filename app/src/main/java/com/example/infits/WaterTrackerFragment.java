package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class WaterTrackerFragment extends Fragment {

    ImageView imgback, addliq, decliq, reminder;

    LottieAnimationView lottieAnimationViewWater;
    TextView waterGoalPercent, wgoal3, consumed, consumed2;
    TextView waterGoal,waterGoal2;
    String liqType = "water", liqAmt;
    Button setgoal;
    RecyclerView rc ;
    float goalWater;
    static int goal = 1800;


    List<String> pastDrink;   //for storing previous drinkConsumed
    Set<String> setPastDrink;

    ProgressBar progressBar;
    int consumedInDay;

    AlarmManager alarmManager;
    PendingIntent waterReceiverPendingIntent;
    Calendar calendar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final int progr = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.getDefault());
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    Date date = new Date();
    SharedPreferences goalPreference;
    AtomicInteger selected_glassNumber = new AtomicInteger();

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

        goalPreference = getContext().getApplicationContext().getSharedPreferences("perGlassInMl", MODE_PRIVATE);
//        setPastDrink = preConsumed.getStringSet("Prev_Array",new LinkedHashSet<>());
//
//        if(setPastDrink.size() >0){
//            pastDrink = new ArrayList<>(setPastDrink);
//            for (int i=0;i<pastDrink.size();i++) {
//                Log.i("pastDrink["+i+"]", "val on retrieve :" + pastDrink.get(i));
//            }
//        }else {
//            Toast.makeText(getContext(), "pastDrink is null", Toast.LENGTH_SHORT).show();
//        }


        addliq = view.findViewById(R.id.addliq);
        decliq = view.findViewById(R.id.decliq);
        imgback = view.findViewById(R.id.imgback);
        lottieAnimationViewWater = view.findViewById(R.id.waterAnimation);
        waterGoalPercent = view.findViewById(R.id.water_goal_percent);
        wgoal3 = view.findViewById(R.id.wgoal3);
        setgoal = view.findViewById(R.id.setgoal_watertracker);
        consumed = view.findViewById(R.id.water_consumed);
//        consumed2 = view.findViewById(R.id.water_consumed2);
        waterGoal = view.findViewById(R.id.water_goal);
//        waterGoal2 = view.findViewById(R.id.water_goal2);
        reminder = view.findViewById(R.id.reminder);
        rc = view.findViewById(R.id.past_activity);

        getLatestWaterData();
        //updateProgressBar();

        if (DataFromDatabase.waterGoal.equals(null) || DataFromDatabase.waterGoal.equalsIgnoreCase("null")) {
            waterGoal.setText("0 ml");
            waterGoal2.setText("0 ml");
        } else {
            waterGoal.setText(DataFromDatabase.waterGoal + " ml");
            waterGoal2.setText(DataFromDatabase.waterGoal + " ml");
            try {
                goal = Integer.parseInt(DataFromDatabase.waterGoal);
                // Log.d("Goal",String.valueOf(goal));
            } catch (NumberFormatException ex) {
                goal = 1800;
                waterGoal.setText(1800 + " ml");
                waterGoal2.setText(1800 + " ml");
                // Log.d("Goal",String.valueOf(goal));
                System.out.println(ex);
            }
        }

        if (DataFromDatabase.waterStr.equals(null) || DataFromDatabase.waterStr.equalsIgnoreCase("null")) {
            consumed.setText("0 ml");
            consumed2.setText("0 ml");
        } else {
            consumed.setText(DataFromDatabase.waterStr + " ml");  // waterStr = waterConsumed
            consumed2.setText(DataFromDatabase.waterStr + " ml");  // waterStr = waterConsumed
            try {
                consumedInDay = Integer.parseInt(DataFromDatabase.waterStr);
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
            } catch (NumberFormatException ex) {
                consumedInDay = 0;
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
            }
        }

        calendar = Calendar.getInstance();
        Log.d("water", "currHour: " + calendar.get(Calendar.HOUR_OF_DAY));

        createNotificationChannel();
        waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));


        pastActivity();

        setgoal.setOnClickListener(view1 -> {

            final Dialog dialog = new Dialog(getActivity());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.watergoaldialog);
            Slider goal_slider = dialog.findViewById(R.id.slider);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ImageView up_arrow = dialog.findViewById(R.id.number_Picker_up_button);
            ImageView down_arrow = dialog.findViewById(R.id.number_Picker_down_button);
            ImageView close_dialog = dialog.findViewById(R.id.close_btn);
            TextView numberPickerText = dialog.findViewById(R.id.current_number_picked);
            TextView selectedGoal = dialog.findViewById(R.id.selected_text);
            TextView selectedGlass = dialog.findViewById(R.id.selected_glass_number);
            TextView textIndicator = dialog.findViewById(R.id.valueIndicator);
            TextView r1 = dialog.findViewById(R.id.r1);
            TextView r2 = dialog.findViewById(R.id.r2);
            TextView r3 = dialog.findViewById(R.id.r3);
            TextView r4 = dialog.findViewById(R.id.r4);
            TextView r5 = dialog.findViewById(R.id.r5);
            TextView r6 = dialog.findViewById(R.id.r6);
            TextView r7 = dialog.findViewById(R.id.r7);
            close_dialog.setOnClickListener(v -> dialog.dismiss());
            final Boolean[] isGlassSelected = {false};

            selectedGoal.setText(goal_slider.getValue() +" L");

            goal_slider.setCustomThumbDrawable(R.drawable.thumb_for_water_draw);
            goal_slider.addOnChangeListener((Slider slider, float value, boolean fromUser) -> {
                if(isGlassSelected[0]){
                    selectedGlass.setText((int) value+" Glass");
                    int pickedNumber = Integer.parseInt(numberPickerText.getText().toString());
                    DecimalFormat df = new DecimalFormat("0.0");

                    selectedGoal.setText(df.format(value*pickedNumber/1000)+ " L");
                }else {
                    DecimalFormat df = new DecimalFormat("0.0");
                    selectedGoal.setText(df.format(slider.getValue())+ " L");
                    int pickedNumber = Integer.parseInt(numberPickerText.getText().toString());
                    selected_glassNumber.set((int) ((value * 1000) / pickedNumber));
                    selectedGlass.setText(selected_glassNumber +" Glass");
                }
            });

            up_arrow.setOnClickListener(v -> {
                int pickedNumber = Integer.parseInt(numberPickerText.getText().toString());
                int incrementNumber = pickedNumber + 100;
                // Toast.makeText(getContext(), "clicked: "+incrementNumber, Toast.LENGTH_SHORT).show();
                numberPickerText.setText(String.valueOf(incrementNumber));
                if (isGlassSelected[0]) {
                    goal_slider.setValueFrom(0);
                    goal_slider.setValueTo(10);
                    goal_slider.setValue(0);
                    goal_slider.setStepSize(1);
                } else {
                    float val = Float.parseFloat((numberPickerText.getText().toString()));
                    float rangeVal = (val / 1000) * 2;
                    float interval = (rangeVal * 10 - rangeVal * 1) / 6;
                    DecimalFormat df = new DecimalFormat("0.0");

                    r1.setText(df.format(rangeVal * 1) + " L");
                    r2.setText(df.format(rangeVal + interval * 1) + " L");
                    r3.setText(df.format(rangeVal + interval * 2) + " L");
                    r4.setText(df.format(rangeVal + interval * 3) + " L");
                    r5.setText(df.format(rangeVal + interval * 4) + " L");
                    r6.setText(df.format(rangeVal + interval * 5) + " L");
                    r7.setText(df.format(rangeVal * 10) + " L");

                    goal_slider.setValueFrom(rangeVal);
                    goal_slider.setValueTo(rangeVal * 10);
                    goal_slider.setValue(rangeVal);
                    DecimalFormat df1 = new DecimalFormat("0.0");
                    goal_slider.setStepSize(Float.parseFloat(df1.format(val / 1000)));
                }
            });

            down_arrow.setOnClickListener(v -> {
                if (Integer.parseInt(numberPickerText.getText().toString()) > 100) {
                    int pickedNumber = Integer.parseInt(numberPickerText.getText().toString());
                    int DecNumber = pickedNumber - 100;
                    //  Toast.makeText(getContext(), "clicked: " + incrementNumber, Toast.LENGTH_SHORT).show();
                    numberPickerText.setText(String.valueOf(DecNumber));

                    if(isGlassSelected[0]) {
                        goal_slider.setValueFrom(0);
                        goal_slider.setValueTo(10);
                        goal_slider.setValue(0);
                        goal_slider.setStepSize(1);
                    } else {
                        float val = Float.parseFloat((numberPickerText.getText().toString()));
                        float rangeVal = (val / 1000) * 2;

                        float interval = (rangeVal * 10 - rangeVal * 1) / 6;

                        DecimalFormat df = new DecimalFormat("0.0");

                        r1.setText(df.format(rangeVal * 1) + " L");
                        r2.setText(df.format(rangeVal + interval * 1) + " L");
                        r3.setText(df.format(rangeVal + interval * 2) + " L");
                        r4.setText(df.format(rangeVal + interval * 3) + " L");
                        r5.setText(df.format(rangeVal + interval * 4) + " L");
                        r6.setText(df.format(rangeVal + interval * 5) + " L");
                        r7.setText(df.format(rangeVal * 10) + " L");

                        goal_slider.setValueFrom(rangeVal);
                        goal_slider.setValueTo(rangeVal * 10);
                        goal_slider.setValue(rangeVal);
                        DecimalFormat df1 = new DecimalFormat("0.0");
                        goal_slider.setStepSize(Float.parseFloat(df1.format(val / 1000)));
                    }
                }
            });

            selectedGlass.setOnClickListener(view2 -> {
                isGlassSelected[0] = true;
                Drawable background = getContext().getDrawable(R.drawable.selecter_shape);
                selectedGlass.setBackground(background);
                selectedGoal.setBackgroundColor(Color.WHITE);
                textIndicator.setText("in glasses");
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.GONE);
                r5.setVisibility(View.GONE);
                r6.setVisibility(View.GONE);
                r7.setVisibility(View.GONE);
                goal_slider.setValueFrom(0);
                goal_slider.setValueTo(10);
                goal_slider.setValue(0);
                goal_slider.setStepSize(1);

            });

            selectedGoal.setOnClickListener(view22 -> {
                isGlassSelected[0] = false;
                Drawable background = getContext().getDrawable(R.drawable.selecter_shape);
                selectedGoal.setBackground(background);
                selectedGlass.setBackground(null);
                textIndicator.setText("in liters");
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                r5.setVisibility(View.VISIBLE);
                r6.setVisibility(View.VISIBLE);
                r7.setVisibility(View.VISIBLE);
                float val = Float.parseFloat((numberPickerText.getText().toString()));
                float rangeVal = (val / 1000) * 2;
                float interval = (rangeVal * 10 - rangeVal * 1) / 6;
                DecimalFormat df = new DecimalFormat("0.0");

                r1.setText(df.format(rangeVal * 1) + " L");
                r2.setText(df.format(rangeVal + interval * 1) + " L");
                r3.setText(df.format(rangeVal + interval * 2) + " L");
                r4.setText(df.format(rangeVal + interval * 3) + " L");
                r5.setText(df.format(rangeVal + interval * 4) + " L");
                r6.setText(df.format(rangeVal + interval * 5) + " L");
                r7.setText(df.format(rangeVal * 10) + " L");

                goal_slider.setValueFrom(rangeVal);
                goal_slider.setValueTo(rangeVal * 10);
                goal_slider.setValue(rangeVal);
                DecimalFormat df1 = new DecimalFormat("0.0");
                goal_slider.setStepSize(Float.parseFloat(df1.format(val / 1000)));

            });

            Button setGoalBtn = dialog.findViewById(R.id.set_water_goal);  // Save Button in SetGoal Fragment
            setGoalBtn.setOnClickListener(v -> {
                SharedPreferences.Editor editor = goalPreference.edit();
                editor.putInt("glassPerServe",Integer.parseInt(numberPickerText.getText().toString()));
                editor.apply();
                int goalInt = goal;
                if(isGlassSelected[0]){
                    goalInt = (int) (goal_slider.getValue() * Integer.parseInt(numberPickerText.getText().toString()));

                }else {
                    goalInt = (int) (goal_slider.getValue() * 1000);
                }
                final String[] goaltxt = {String.valueOf(goalInt)};

                goal = Integer.parseInt(goaltxt[0]);
                Log.d("Goal",String.valueOf(goal));

                waterGoal.setText(goaltxt[0] + " ml");
                waterGoal2.setText(goaltxt[0] + " ml");
                waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));
                consumedInDay = 0;

                String url = String.format("%supdatewatergoal.php",DataFromDatabase.ipConfig);
                // String url =  DataFromDatabase.ipConfig +"watergoalupdate.php";
                // String url = "https://infits.in/androidApi/updatewatergoal.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {

                            try {
                                Log.i("response from server", "onResponse: "+response);
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");

                                int newGoal = jsonObject.getInt("goal");

                                // Update the UI with the new goal value if the operation was successful
                                if (goal!=newGoal) {
                                    goaltxt[0] = (String.valueOf(newGoal));
                                    waterGoalPercent.setText(String.valueOf(calculateGoal(newGoal)));
                                }
                                getLatestWaterData();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d("response;;", "JSON parsing error."+e);
                            }
                        },
                        error -> {
                            Log.e("response from server ;", "error:" + error);
                        }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        // int amt = (int) Float.parseFloat(choosed.getText().toString());
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
                        data.put("glassGoal",String.valueOf(selected_glassNumber));
                        data.put("glassConsumed","0");


                        Log.i("goal uploaded", "data uploaded date="+sdf.format(date)+"\n"+
                                "goal:"+goal+"\n"+
                                "clientuserID:"+DataFromDatabase.clientuserID+"\n"+
                                "type:"+"water"+"\n"+
                                "amount:"+ (consumedInDay)+"\n"+
                                "drinkConsumed:"+(consumedInDay)+"\n"+
                                "client_id:"+DataFromDatabase.client_id+"\n"+
                                "dietitian_id:"+DataFromDatabase.dietitian_id+"\n"+
                                "dietitianuserID:"+DataFromDatabase.dietitianuserID+"\n"+
                                "glassGoal:"+String.valueOf(selected_glassNumber)+"\n"+
                                "glassConsumed:"+"0");

                        return data;
                    }
                };
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                SharedPreferences notificationPrefs = requireActivity().getSharedPreferences("notificationDetails", MODE_PRIVATE);
                boolean waterNotificationPermission = notificationPrefs.getBoolean("waterSwitch", false);

                if (false /*waterNotificationPermission*/) {
                    setNotificationAlarm();
                    cancelNotificationAlarmAtEndOfDay();
                }

                dialog.dismiss();
            });

            dialog.show();
            waterGoalPercent.setText(String.valueOf(calculateGoal(goal)));

        });

        //for adding liquid
        addliq.setOnClickListener(v -> {
            final Boolean[] isGlassSelected = {false};
            final Dialog dialog = new Dialog(getActivity());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_add_liquid);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            int glassPerServe = goalPreference.getInt("glassPerServe",200);

            Button addDrank = dialog.findViewById(R.id.addbtn);

            Slider slider = dialog.findViewById(R.id.slider);

            TextView choosed = dialog.findViewById(R.id.liqamt);// choosed = choosedLiquedAmount
            TextView choosed_glass = dialog.findViewById(R.id.selected_glass_number);// choosed = choosedLiquedAmount

            final int[] val = new int[1];


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

            slider.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    if(isGlassSelected[0]){
                        val[0] = ((int) slider.getValue())*glassPerServe;
                        choosed.setText(val[0]+" ml");
                        choosed_glass.setText((int) value+" Glass");
                        Log.d("Water", String.valueOf(val[0]));
                    }else {
                        val[0] = (int) slider.getValue();
                        choosed.setText(String.valueOf((float) val[0])+" ml");
                        choosed_glass.setText((int) (value/glassPerServe)+" Glass");
                        Log.d("Water", String.valueOf(val[0]));
                    }

                }
            });
            slider.setCustomThumbDrawable(R.drawable.thumb_for_water_draw);

            choosed.setOnClickListener(v1 -> {
                isGlassSelected[0] = false;
                v1.setBackground(getContext().getDrawable(R.drawable.selecter_shape));
                choosed_glass.setBackground(null);
                slider.setValue(0);
                slider.setValueFrom(0);
                slider.setValueTo(1000);
                slider.setStepSize(50);
            });

            choosed_glass.setOnClickListener(v2 -> {
                isGlassSelected[0] = true;
                v2.setBackground(getContext().getDrawable(R.drawable.selecter_shape));
                choosed.setBackground(null);
                slider.setValue(0);
                slider.setValueFrom(0);
                slider.setValueTo(10);
                slider.setStepSize(1);
            });


            choosed.setText(String.valueOf(slider.getValue()));

            addDrank.setOnClickListener(v1 -> {
                if(goal == 0)
                    Toast.makeText(getContext(), "Daily goal not set", Toast.LENGTH_SHORT).show();
                else {
                    int selectedDrink =(int) slider.getValue();

                    if(isGlassSelected[0]==true){
                        selectedDrink = ((int)slider.getValue())*glassPerServe;
                    }


                    dialog.dismiss();

                    String url = String.format("%supdateWatertracker.php",DataFromDatabase.ipConfig);

                    // String url = "https://infits.in/androidApi/updateWatertracker.php";

                    int finalSelectedDrink = selectedDrink;
                    StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                        Log.e("drunk", "onCreateView: "+response );
                        Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                        pastActivity();
                        getLatestWaterData();
                    }, error -> {
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        Log.i("error", "response:"+error);
                        Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            //  int amt = (int) Float.parseFloat(choosed.getText().toString());
                            Map<String, String> data = new HashMap<>();
                            data.put("client_id", DataFromDatabase.client_id);
                            data.put("clientuserID", DataFromDatabase.clientuserID);
                            data.put("dateandtime", sdf.format(date));
                            data.put("drinkConsumed", String.valueOf(finalSelectedDrink));
                            data.put("goal", String.valueOf(goal));
                            data.put("type", liqType);
                            data.put("amount", String.valueOf(finalSelectedDrink));
                            data.put("dietitian_id",DataFromDatabase.dietitian_id);
                            data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
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
                }
            });

            dialog.show();

        });

        //for deducting liquid
        decliq.setOnClickListener(v -> {
            if(consumed.getText().toString().equals("0 ml")){
                Toast.makeText(getContext(), "Not consumed anything!", Toast.LENGTH_SHORT).show();
            }else{
                String url = String.format("%supdateWatertrackerDec.php",DataFromDatabase.ipConfig);
                // String url = "http://192.168.152.231/infits/test.php";
                StringRequest demoRequest = new StringRequest(Request.Method.POST, url, response -> {
                    try{
                        Log.i("dec response", "response: "+response);
                        JSONObject object = new JSONObject(response);
                        String msg = object.getString("message");
                        if(msg.equals("deleted")){
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            getLatestWaterData();
                            pastActivity();
                            Log.i("dec response", "response: "+response);
                            //Toast.makeText(getContext(), "response "+ response, Toast.LENGTH_SHORT).show();
                        } else if (msg.equals("Zeroamount")) {
                            Log.i("dec response ", "conumedDrink is 0!");
                        }
                    }catch (JSONException e){
                        Log.i("json error while dec", "error response:"+response);
                        Log.e("json error while dec","Error: "+e.toString());
                    }
                }, error -> Toast.makeText(getContext(), "error"+error, Toast.LENGTH_SHORT).show()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        data.put("client_id", DataFromDatabase.client_id);
                        data.put("clientuserID", DataFromDatabase.clientuserID);
                        data.put("dateandtime", sdf.format(date));
                        data.put("goal", String.valueOf(goal));
                        data.put("dietitian_id",DataFromDatabase.dietitian_id);
                        data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
                        return data;
                    }
                };

                Volley.newRequestQueue(getContext()).add(demoRequest);
                demoRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

        return view;
    }

    private void updateProgressBar() {

        progressBar.setProgress(progr);
    }
    private void pastActivity(){


        ArrayList<String> dates = new ArrayList<>(); // ArrayList for past Activity
        ArrayList<String> datas = new ArrayList<>(); // ArrayList for past Activity
        ArrayList<String> fetchedDateswater = new ArrayList<>();
        fetchedDateswater.clear();
        String url = String.format("%spastActivitywater.php", DataFromDatabase.ipConfig);
        //String url = "https://infits.in/androidApi/pastActivitywater.php";

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {

                System.out.println("water data="+response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("water");
                Log.d("arraylength",String.valueOf(jsonArray.length()));
                Log.d("watervalue:",jsonArray.toString());
                int glassPerServe = goalPreference.getInt("glassPerServe",200);
                for (int i=1;i<jsonArray.length();i++){
                    fetchedDateswater.add(jsonArray.getJSONObject(i).getString("date"));
                    int consumedTodayInGlass = (Integer.parseInt(jsonArray.getJSONObject(i).getString("water")))/glassPerServe;
                    datas.add(consumedTodayInGlass+ " Glasses");
                    Log.d("watt",datas.toString());
                }

                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(), fetchedDateswater, datas, Color.parseColor("#76A5FF"));
                rc.setLayoutManager(new LinearLayoutManager(getContext()));
                rc.setAdapter(ad);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("pastActivity", "pastActivity: "+e );
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
        String url = String.format("%sgetLatestWaterdt.php", DataFromDatabase.ipConfig);
        // String url = "https://infits.in/androidApi/getLatestWaterdt.php";

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

                            waterGoal.setText(goal + "ml");
                            waterGoal2.setText(goal + " ml");
                            try {
                                waterGoalPercent.setText((consumedInDay * 100) / goal + " %");
                            }catch (ArithmeticException e){
                                Toast.makeText(getContext(), "Cannot be divided by zero: error"+ e, Toast.LENGTH_SHORT).show();
                            }

                            consumed.setText(consumedInDay + " ml");
                            consumed2.setText(consumedInDay + "ml");
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
                            consumed.setText(String.valueOf(consumedInDay)+ " ml");
                            consumed2.setText(String.valueOf(consumedInDay)+ "ml");
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
        String url = String.format("%swatertracker.php",DataFromDatabase.ipConfig);
        //  String url = "https://infits.in/androidApi/watertracker.php";
        StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
            consumed.setText(consumedInDay +" ml");
            consumed2.setText(consumedInDay +" ml");
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