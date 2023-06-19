package com.example.infits;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.abhinav.progress_view.ProgressData;
import com.abhinav.progress_view.ProgressView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class CalorieTrackerFragment extends Fragment {
    ProgressView progressView1,progressView2,progressView3,progressView4;

    CircularProgressIndicator circularProgressIndicatorCC,circularProgressIndicatorCB;

    View bottomSheetN;
    ImageView imgBack;

    int calgoal;
    int calConsumed;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    CardView calorieConsumed,calorieBurnt,addBreakfast,addLunch,addSnacks,addDinner;
    private String mParam2;

    public  CalorieTrackerFragment(){
        // Required Empty Constructor.
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalorieTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalorieTrackerFragment newInstance(String param1, String param2) {
        CalorieTrackerFragment fragment = new CalorieTrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calorie_tracker, container, false);

        String url = String.format("%scalorieTracker.php", DataFromDatabase.ipConfig);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("calorieData Chusko Bro", response);

                    try {
                        JSONObject jsonArray = new JSONObject(response);
                        JSONObject array = jsonArray.getJSONObject("Values");


                        if(array.length() != 0) {
                            String caloriesConsumed = array.getString("caloriesConsumed");
                            calConsumed = Integer.parseInt(caloriesConsumed);
                            String Goal = array.getString("goal");
                            calgoal = Integer.parseInt(Goal);
                            String calorieBurnt = array.getString("calorieBurnt");
                            String carbs = array.getString("carbs");
                            String fiber = array.getString("fiber");
                            String protein = array.getString("protein");
                            String fat = array.getString("fat");


                           // goal = Integer.parseInt(waterGoalStr);
                            // consumedInDay = Integer.parseInt(waterConsumedStr);

                            //waterGoal.setText(goal + " ml");
                            //waterGoalPercent.setText((consumedInDay * 100) / goal + " %");
                            //consumed.setText(consumedInDay + " ml");
                            //lottieAnimationViewWater.setAnimation(R.raw.water_loading_animation_bottle);
                            //int durationOfAnimationFromLottie = 6000;
                            //int durationOfWaterAnimation = (durationOfAnimationFromLottie*calculateGoalReturnInt()/100)-500;
                            //lottieAnimationViewWater.playAnimation();
                            //new Handler().postDelayed(new Runnable() {
                              //                            @Override
                                //                          public void run() {
                                  //                            lottieAnimationViewWater.pauseAnimation();
                                    //                      }
                                      //                },durationOfWaterAnimation
                            //);
                            //consumed.setText(String.valueOf(consumedInDay));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("dashboardFrag", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("today",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        hooks(view);
        ProgressData progressData1 = new ProgressData("view1",60f,100f,R.color.progressGreenColor);
        progressView1.setData(progressData1);

        ProgressData progressData2 = new ProgressData("view1",60f,100f,R.color.progressRedColor);
        progressView2.setData(progressData2);

        ProgressData progressData3 = new ProgressData("view1",60f,100f,R.color.progressPurpleColor);
        progressView3.setData(progressData3);

        ProgressData progressData4 = new ProgressData("view1",60f,100f,R.color.progressBlueColor);
        progressView4.setData(progressData4);

        circularProgressIndicatorCC.setProgress(calConsumed,calgoal);
        circularProgressIndicatorCB.setProgress(7000,10000);

        BottomSheetBehavior.from(bottomSheetN).setPeekHeight(350);
        BottomSheetBehavior.from(bottomSheetN).setState(BottomSheetBehavior.STATE_COLLAPSED);

        System.out.println(calConsumed);
        System.out.println(calgoal);

        calorieConsumed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_calorieConsumedFragment);
            }
        });
        calorieBurnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_calorieBurntFragment);
            }
        });

        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_tocalorieAddBreakFastFragment); // To be continued...
            }
        });
        addLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_tocalorieAddLunchFragment); // To be continued...
            }
        });
        addSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_tocalorieAddSnacksFragment); // To be continued...
            }
        });
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_tocalorieAddDinnerFragment); // To be continued...
            }
        });


//        dailyMeals = new ArrayList<>();
//        goalValue = 0f;
//        calorieValue = 0f;
//        proteinValue = 0f;
//        fibreValue = 0f;
//        carbValue = 0f;
//        fatValue = 0f;
//
//        getDailyMeals();

//        log.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_foodDetailsFragment));


//        reminder.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_calorieReminderFragment));

        return view;
    }

    private void hooks(View view) {
//        log = view.findViewById(R.id.log);
        imgBack = view.findViewById(R.id.imgback);
//        reminder = view.findViewById(R.id.reminder);
//        dailyMealsRecycle = view.findViewById(R.id.dailyMealsRecycle);
//        kcalEatenTV = view.findViewById(R.id.kcalEaten);
//        kcalLeftTV = view.findViewById(R.id.kcalLeft);
//        kcalPB = view.findViewById(R.id.calorie_progress_bar);
//        carbTV = view.findViewById(R.id.carbLeft);
//        carbPB = view.findViewById(R.id.carbPB);
//        fibreTV = view.findViewById(R.id.fibreLeft);
//        fibrePB = view.findViewById(R.id.fibrePB);
//        proteinTV = view.findViewById(R.id.proteinLeft);
//        proteinPB = view.findViewById(R.id.proteinPB);
//        fatTV = view.findViewById(R.id.fatLeft);
//        fatPB = view.findViewById(R.id.fatPB);
        progressView1 = view.findViewById(R.id.progressView1);
        progressView2 = view.findViewById(R.id.progressView2);
        progressView3 = view.findViewById(R.id.progressView3);
        progressView4 = view.findViewById(R.id.progressView4);
        circularProgressIndicatorCC = view.findViewById(R.id.circular_progressCC);
        circularProgressIndicatorCB = view.findViewById(R.id.circular_progressCB);
        bottomSheetN = view.findViewById(R.id.bottomSheetN);
        addBreakfast = view.findViewById(R.id.add_breakfast);
        addLunch = view.findViewById(R.id.add_lunch);
        addSnacks = view.findViewById(R.id.add_snack);
        addDinner = view.findViewById(R.id.add_dinner);
        calorieConsumed=view.findViewById(R.id.calorieConsumed);
        calorieBurnt=view.findViewById(R.id.calorieBurnt);
        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

    }
}
