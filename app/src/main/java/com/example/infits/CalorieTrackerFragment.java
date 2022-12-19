package com.example.infits;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalorieTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalorieTrackerFragment extends Fragment {

    RecyclerView dailyMealsRecycle;
    ImageView log;
    ImageView imgBack, reminder;
    TextView kcalEatenTV, kcalLeftTV, carbTV, fibreTV, proteinTV, fatTV;
    CircularProgressIndicator kcalPB;
    ProgressBar carbPB, fibrePB, proteinPB, fatPB;

    ArrayList<DailyMeal> dailyMeals;
    float goalValue = 0f;
    float calorieValue = 0f;
    float proteinValue = 0f;
    float fibreValue = 0f;
    float carbValue = 0f;
    float fatValue = 0f;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalorieTrackerFragment() {
        // Required empty public constructor
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

//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                startActivity(new Intent(getActivity(),DashBoardMain.class));
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calorie_tracker, container, false);

        hooks(view);

        dailyMeals = new ArrayList<>();
        goalValue = 0f;
        calorieValue = 0f;
        proteinValue = 0f;
        fibreValue = 0f;
        carbValue = 0f;
        fatValue = 0f;

        getDailyMeals();

        log.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_foodDetailsFragment));

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        reminder.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_calorieTrackerFragment_to_calorieReminderFragment));

        return view;
    }

    private void hooks(View view) {
        log = view.findViewById(R.id.log);
        imgBack = view.findViewById(R.id.imgback);
        reminder = view.findViewById(R.id.reminder);
        dailyMealsRecycle = view.findViewById(R.id.dailyMealsRecycle);
        kcalEatenTV = view.findViewById(R.id.kcalEaten);
        kcalLeftTV = view.findViewById(R.id.kcalLeft);
        kcalPB = view.findViewById(R.id.calorie_progress_bar);
        carbTV = view.findViewById(R.id.carbLeft);
        carbPB = view.findViewById(R.id.carbPB);
        fibreTV = view.findViewById(R.id.fibreLeft);
        fibrePB = view.findViewById(R.id.fibrePB);
        proteinTV = view.findViewById(R.id.proteinLeft);
        proteinPB = view.findViewById(R.id.proteinPB);
        fatTV = view.findViewById(R.id.fatLeft);
        fatPB = view.findViewById(R.id.fatPB);
    }

    private void getDailyMeals() {
        String dailyMealsUrl = String.format("%sgetDailyMeals.php", DataFromDatabase.ipConfig);

        StringRequest dailyMealsRequest = new StringRequest(
                Request.Method.POST,
                dailyMealsUrl,
                response -> {
                    Log.d("CalorieTrackerFragment", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray mealArray = object.getJSONArray("food");
                        for(int i = 0; i < mealArray.length(); i++) {
                            JSONObject currMeal = mealArray.getJSONObject(i);

                            String name = currMeal.getString("name");
                            String calorie = currMeal.getString("calorie");
                            String protein = currMeal.getString("protein");
                            String fibre = currMeal.getString("fibre");
                            String carb = currMeal.getString("carb");
                            String fat = currMeal.getString("fat");
                            String time = currMeal.getString("time");
                            String goal = currMeal.getString("goal");

                            DailyMeal dailyMeal = new DailyMeal(name, calorie, protein, fibre, carb, fat, time, goal);
                            dailyMeals.add(dailyMeal);

                            calorieValue += Float.parseFloat(calorie);
                            proteinValue += Float.parseFloat(protein);
                            fibreValue += Float.parseFloat(fibre);
                            carbValue += Float.parseFloat(carb);
                            fatValue += Float.parseFloat(fat);
                            goalValue = Float.parseFloat(goal);
                        }

                        DailyMealAdapter adapter = new DailyMealAdapter(dailyMeals,getContext());

                        dailyMealsRecycle.setHasFixedSize(true);
                        dailyMealsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        dailyMealsRecycle.setAdapter(adapter);

                        setProgressBars();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("CalorieTrackerFragment", error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(dailyMealsRequest);
    }

    private void setProgressBars() {
        kcalEatenTV.setText(String.valueOf((int) calorieValue));
        kcalLeftTV.setText(String.valueOf((int) Math.max(goalValue - calorieValue, 0)));
        kcalPB.setProgress((int) ((calorieValue * 100) / goalValue));

        carbTV.setText((int) Math.max(goalValue - carbValue, 0) + " g left");
        carbPB.setProgress((int) ((carbValue * 100) / goalValue));

        fibreTV.setText((int) Math.max(goalValue - fibreValue, 0) + " g left");
        fibrePB.setProgress((int) ((fibreValue * 100) / goalValue));

        proteinTV.setText((int) Math.max(goalValue - proteinValue, 0) + " g left");
        proteinPB.setProgress((int) ((proteinValue * 100) / goalValue));

        fatTV.setText((int) Math.max(goalValue - fatValue, 0) + " g left");
        fatPB.setProgress((int) ((fatValue * 100) / goalValue));
    }
}