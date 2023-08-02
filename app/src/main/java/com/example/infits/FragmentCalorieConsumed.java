package com.example.infits;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalorieConsumed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalorieConsumed extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PieChart pieChart;
    ImageView imgBack;
    ArrayList<calorieInfo> calorieInfos;
    ArrayList<calorieconsumedInfo> BreakFast;
    ArrayList<calorieconsumedInfo> Lunch;
    ArrayList<calorieconsumedInfo> Dinner;
    ArrayList<calorieconsumedInfo> Snacks;
    int[] colors={Color.parseColor("#FCFF72"),Color.parseColor("#ACAFFD"),Color.parseColor("#FF6262"),Color.parseColor("#FFA361")   };
    RecyclerView calorieRecycleview;
    Button day_btn_calorie,week_btn_calorie,month_btn_calorie;
    Button ButtonID;
    TextView totalCalorieValue,caloriedisplaydate;
    String    breakfastcalorieconsumed;

    SimpleDateFormat caloriedateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:S", Locale.getDefault());

    public FragmentCalorieConsumed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalorieConsumed.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalorieConsumed newInstance(String param1, String param2) {
        FragmentCalorieConsumed fragment = new FragmentCalorieConsumed();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_calorie_consumed, container, false);
        calorieInfos=new ArrayList<>();
        BreakFast=new ArrayList<>();
        Lunch=new ArrayList<>();
        Dinner=new ArrayList<>();
        Snacks=new ArrayList<>();
        hooks(view);
        //pieChart();
        pastAcivity();
        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }
    private void hooks(View view){
        pieChart=view.findViewById(R.id.piechart);
        calorieRecycleview=view.findViewById(R.id.calorierecycleView);
        calorieRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        day_btn_calorie=view.findViewById(R.id.day_btn_calorie);
        week_btn_calorie=view.findViewById(R.id.week_btn_calorie);
        month_btn_calorie=view.findViewById(R.id.month_btn_calorie);
        caloriedisplaydate=view.findViewById(R.id.caloriedisplaydate);
        totalCalorieValue=view.findViewById(R.id.totalCalorieValue);
        imgBack=view.findViewById(R.id.calorieImgback);
        Date dateToday = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd,yyyy");
        caloriedisplaydate.setText(sf.format(dateToday));
        day_btn_calorie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonID = day_btn_calorie;
                SetButtonBackground(v);
                calorieInfos.clear();
                BreakFast.clear();
                Lunch.clear();
                Dinner.clear();
                Snacks.clear();
                pastAcivity();

            }
        });
        week_btn_calorie.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                ButtonID = week_btn_calorie;
                SetButtonBackground(v);
                calorieInfos.clear();
                BreakFast.clear();
                Lunch.clear();
                Dinner.clear();
                Snacks.clear();
                pastAcivity();

                //pieChart();

            }
        });
        month_btn_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonID = month_btn_calorie;
                SetButtonBackground(v);
                calorieInfos.clear();
                BreakFast.clear();
                Lunch.clear();
                Dinner.clear();
                Snacks.clear();
                pastAcivity();
            }
        });
    }
    private void pieChart(){
        List<PieEntry> entries=new ArrayList<>();
        //entries.add(new PieEntry(49,"D"));
        //entries.add(new PieEntry(15,"B"));
        //entries.add(new PieEntry(20,"S"));
        //entries.add(new PieEntry(11,"L"));


        pieChart.getLegend().setEnabled(false);
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2f);
        PieData data = new PieData(dataSet);
        pieChart.setDrawHoleEnabled(false);
        data.setValueTextSize(0f);


        Description description=new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setHoleRadius(0f);
        pieChart.setRotationEnabled(false);
        pieChart.setData(data);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.setEntryLabelTextSize(25f);
        Typeface typeface=Typeface.defaultFromStyle(Typeface.BOLD);

        pieChart.setEntryLabelTypeface(typeface);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.WHITE);
    }
    private void pastAcivity(){
        Date dateToday = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd,yyyy");
        caloriedisplaydate.setText(sf.format(dateToday));
        calorieInfos.clear();

        //String calorieUrl = String.format("%scalorieConsumed.php", DataFromDatabase.ipConfig);
        String calorieUrl = "https://infits.in/androidApi/calorieConsumed.php";

        StringRequest calorieRequest = new StringRequest( Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("CalorieConsumed Data Bro", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("value");


                        JSONObject valueObject = object.getJSONObject("value");
                        List<PieEntry> entries = new ArrayList<>();
                        // Set a description for the chart
                        Description description = new Description();
                        description.setText("Total calorie");
                        pieChart.setDescription(description);
                        // Parse Breakfast data
                        JSONObject breakfastObject = valueObject.getJSONObject("BreakFast");
                        String breakfastCalorieConsumed = breakfastObject.getString("MealType_caloriesconsumed");

                        JSONArray breakfastDataArray = breakfastObject.getJSONArray("data");
                        if (breakfastDataArray.length() > 0) {
                            JSONObject breakfastData = breakfastDataArray.getJSONObject(0);
                            String breakfastName = breakfastData.getString("name");
                            String breakfastCalories = breakfastData.getString("Calories");
                            String dateandtime = breakfastData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            String formattedTime = timeFormat.format(dateTime);
                            calorieInfos.clear();
                            CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
                            calorieRecycleview.setAdapter(calorieInfoAdapter);
                            calorieInfos.add(new calorieInfo(R.string.breakfast,"String","BREAKFAST",breakfastCalorieConsumed,dateandtime,formattedTime,BreakFast));


                            // Create a list of PieEntry objects to hold the data
                            entries.add(new PieEntry(Float.parseFloat(breakfastCalorieConsumed), "B"));

                            // Create a PieDataSet with the entries and a label
                            PieDataSet dataSet = new PieDataSet(entries, "B");

                            //Attri
                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getLegend().setEnabled(false);
                            dataSet.setColors(colors);
                            dataSet.setSliceSpace(2f);
                            PieData data = new PieData(dataSet);
                            pieChart.setDrawHoleEnabled(false);
                            data.setValueTextSize(0f);
                            pieChart.setHoleRadius(0f);
                            pieChart.setRotationEnabled(false);
                            pieChart.setData(data);
                            pieChart.animateY(1000, Easing.EaseInOutCubic);
                            pieChart.setEntryLabelTextSize(25f);
                            Typeface typeface=Typeface.defaultFromStyle(Typeface.BOLD);

                            pieChart.setEntryLabelTypeface(typeface);
                            pieChart.setDrawEntryLabels(true);
                            pieChart.setEntryLabelColor(Color.WHITE);
                        }

                        // Parse Lunch data
                        JSONObject lunchObject = valueObject.getJSONObject("Lunch");
                        String lunchCaloriesConsumed = lunchObject.getString("MealType_caloriesconsumed");

                        JSONArray lunchDataArray = lunchObject.getJSONArray("data");
                        if (lunchDataArray.length() > 0) {
                            JSONObject lunchData = lunchDataArray.getJSONObject(0);
                            String lunchName = lunchData.getString("name");
                            String lunchCalories = lunchData.getString("Calories");
                            String dateandtime = lunchData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            String formattedTime = timeFormat.format(dateTime);
                            CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
                            calorieRecycleview.setAdapter(calorieInfoAdapter);
                            calorieInfos.add(new calorieInfo(R.string.lunch,"String","LUNCH",lunchCaloriesConsumed,dateandtime,formattedTime,Lunch));
                            // Access other properties as needed
                            // Create a list of PieEntry objects to hold the data
                            entries.add(new PieEntry(Float.parseFloat(lunchCaloriesConsumed), "L"));

                            // Create a PieDataSet with the entries and a label
                            PieDataSet dataSet = new PieDataSet(entries, "L");

                            //Attri
                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getLegend().setEnabled(false);
                            dataSet.setColors(colors);
                            dataSet.setSliceSpace(2f);
                            PieData data = new PieData(dataSet);
                            pieChart.setDrawHoleEnabled(false);
                            data.setValueTextSize(0f);
                            pieChart.setHoleRadius(0f);
                            pieChart.setRotationEnabled(false);
                            pieChart.setData(data);
                            pieChart.animateY(1000, Easing.EaseInOutCubic);
                            pieChart.setEntryLabelTextSize(25f);
                            Typeface typeface=Typeface.defaultFromStyle(Typeface.BOLD);

                            pieChart.setEntryLabelTypeface(typeface);
                            pieChart.setDrawEntryLabels(true);
                            pieChart.setEntryLabelColor(Color.WHITE);
                        }

                        // Parse Snacks data
                        JSONObject snacksObject = valueObject.getJSONObject("Snacks");
                        String snacksCaloriesConsumed = snacksObject.getString("MealType_caloriesconsumed");

                        JSONArray snacksDataArray = snacksObject.getJSONArray("data");
                        if (snacksDataArray.length() > 0) {
                            JSONObject snacksData = snacksDataArray.getJSONObject(0);
                            String snacksName = snacksData.getString("name");
                            String snacksCalories = snacksData.getString("Calories");
                            String dateandtime = snacksData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            String formattedTime = timeFormat.format(dateTime);
                            CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
                            calorieRecycleview.setAdapter(calorieInfoAdapter);
                            calorieInfos.add(new calorieInfo(R.string.snacks,"String","SNACKS",snacksCaloriesConsumed,dateandtime,formattedTime,Snacks));
                            // Access other properties as needed
                            // Create a list of PieEntry objects to hold the data
                            entries.add(new PieEntry(Float.parseFloat(snacksCaloriesConsumed), "S"));

                            // Create a PieDataSet with the entries and a label
                            PieDataSet dataSet = new PieDataSet(entries, "S");

                            //Attri
                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getLegend().setEnabled(false);
                            dataSet.setColors(colors);
                            dataSet.setSliceSpace(2f);
                            PieData data = new PieData(dataSet);
                            pieChart.setDrawHoleEnabled(false);
                            data.setValueTextSize(0f);
                            pieChart.setHoleRadius(0f);
                            pieChart.setRotationEnabled(false);
                            pieChart.setData(data);
                            pieChart.animateY(1000, Easing.EaseInOutCubic);
                            pieChart.setEntryLabelTextSize(25f);
                            Typeface typeface=Typeface.defaultFromStyle(Typeface.BOLD);

                            pieChart.setEntryLabelTypeface(typeface);
                            pieChart.setDrawEntryLabels(true);
                            pieChart.setEntryLabelColor(Color.WHITE);
                        }

                        // Parse Dinner data
                        JSONObject dinnerObject = valueObject.getJSONObject("Dinner");
                        String dinnerCaloriesConsumed = dinnerObject.getString("MealType_caloriesconsumed");

                        JSONArray dinnerDataArray = dinnerObject.getJSONArray("data");
                        if (dinnerDataArray.length() > 0) {
                            JSONObject dinnerData = dinnerDataArray.getJSONObject(0);
                            String dinnerName = dinnerData.getString("name");
                            String dinnerCalories = dinnerData.getString("Calories");
                            String dateandtime = dinnerData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            String formattedTime = timeFormat.format(dateTime);
                            CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
                            calorieRecycleview.setAdapter(calorieInfoAdapter);
                            calorieInfos.add(new calorieInfo(R.string.dinner,"String","DINNER",dinnerCaloriesConsumed,dateandtime,formattedTime,Dinner));

                            // Access other properties as needed
                            // Create a list of PieEntry objects to hold the data
                            entries.add(new PieEntry(Float.parseFloat(dinnerCaloriesConsumed), "D"));

                            // Create a PieDataSet with the entries and a label
                            PieDataSet dataSet = new PieDataSet(entries, "D");

                            //Attri
                            PieData pieData = new PieData(dataSet);
                            pieChart.setData(pieData);
                            pieChart.getLegend().setEnabled(false);
                            dataSet.setColors(colors);
                            dataSet.setSliceSpace(2f);
                            PieData data = new PieData(dataSet);
                            pieChart.setDrawHoleEnabled(false);
                            data.setValueTextSize(0f);
                            pieChart.setHoleRadius(0f);
                            pieChart.setRotationEnabled(false);
                            pieChart.setData(data);
                            pieChart.animateY(1000, Easing.EaseInOutCubic);
                            pieChart.setEntryLabelTextSize(25f);
                            Typeface typeface=Typeface.defaultFromStyle(Typeface.BOLD);

                            pieChart.setEntryLabelTypeface(typeface);
                            pieChart.setDrawEntryLabels(true);
                            pieChart.setEntryLabelColor(Color.WHITE);
                        }

                        String totalCaloriesConsumed = valueObject.getString("total_caloriesconsumed");
                        totalCalorieValue.setText(totalCaloriesConsumed);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("CalorieConsumed Data Bro", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("date",caloriedateFormat.format(date));
                //data.put("for", "today");
                if (ButtonID==week_btn_calorie){
                    data.put("for", "week");
                } else if (ButtonID==month_btn_calorie) {
                    data.put("for", "month");
                } else if (ButtonID==day_btn_calorie) {
                    data.put("for", "today");
                }else
                    data.put("for", "today");

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //calorieInfos.add(new calorieInfo(R.string.breakfast,"String","BREAKFAST","452 Kcal","00:18:52","11:10 a.m.",BreakFast));
        //calorieInfos.add(new calorieInfo(R.string.lunch,"String","LUNCH","452 kcal","00:18:52","11:10 a.m.",Lunch));
        //calorieInfos.add(new calorieInfo(R.string.snacks,"String","SNACKS","452 kcal","00:18:52","11:10 a.m.",Snacks));
        //calorieInfos.add(new calorieInfo(R.string.dinner,"String","DINNER","452 kcal","00:18:52","11:10 a.m.",Dinner));
        BreakFast.clear();
        Lunch.clear();
        Snacks.clear();
        Dinner.clear();
        BreakFastInfo();
        LunchInfo();
        SnacksInfo();
        DinnerInfo();

        //CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
        //calorieRecycleview.setAdapter(calorieInfoAdapter);

    }

    private void BreakFastInfo(){
        String mealType="BreakFast";
        //String calorieUrl = String.format("%scalorieConsumed.php", DataFromDatabase.ipConfig);
        String calorieUrl = "https://infits.in/androidApi/calorieConsumed.php";

        StringRequest calorieRequest = new StringRequest(Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("Breakfast Info Bro", response);

                    try {
                        JSONObject object = new JSONObject(response);

                        JSONObject valueObject = object.getJSONObject("value");
                        JSONObject breakfastObject = valueObject.getJSONObject("BreakFast");

                        JSONArray breakfastDataArray = breakfastObject.getJSONArray("data");
                        for (int i = 0; i < breakfastDataArray.length(); i++) {
                            JSONObject breakfastData = breakfastDataArray.getJSONObject(i);
                            String breakfastName = breakfastData.getString("name");
                            String breakfastCalories = breakfastData.getString("Calories");
                            String dateandtime = breakfastData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String Time = timeFormat.format(dateTime);
                            BreakFast.add(new calorieconsumedInfo(R.drawable.istockphoto_860224944_612x612_removebg_preview_1, mealType,
                                    breakfastName, breakfastCalories, "1", "Small", Time));
                        }
                        String totalCaloriesConsumed = valueObject.getString("total_caloriesconsumed");
                        totalCalorieValue.setText(totalCaloriesConsumed);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Breakfast Info Bro", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("date", caloriedateFormat.format(date));
                if (ButtonID == week_btn_calorie) {
                    data.put("for", "week");
                } else if (ButtonID == month_btn_calorie) {
                    data.put("for", "month");
                } else if (ButtonID == day_btn_calorie) {
                    data.put("for", "today");
                } else {
                    data.put("for", "today");
                }
                return data;
            }

        };

        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void LunchInfo(){
        String mealType="Lunch";
        //String calorieUrl = String.format("%scalorieConsumed.php", DataFromDatabase.ipConfig);
        String calorieUrl = "https://infits.in/androidApi/calorieConsumed.php";

        StringRequest calorieRequest = new StringRequest(Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("Lunch Info Bro", response);

                    try {
                        JSONObject object = new JSONObject(response);

                        JSONObject valueObject = object.getJSONObject("value");
                        JSONObject lunchObject = valueObject.getJSONObject("Lunch");

                        JSONArray lunchDataArray = lunchObject.getJSONArray("data");
                        for (int i = 0; i < lunchDataArray.length(); i++) {
                            JSONObject lunchData = lunchDataArray.getJSONObject(i);
                            String lunchName = lunchData.getString("name");
                            String lunchCalories = lunchData.getString("Calories");
                            String dateandtime = lunchData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String Time = timeFormat.format(dateTime);
                            Lunch.add(new calorieconsumedInfo(R.drawable.istockphoto_860224944_612x612_removebg_preview_1, mealType,
                                    lunchName, lunchCalories, "1", "Small", Time));
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Lunch Info Bro", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("date", caloriedateFormat.format(date));
                if (ButtonID == week_btn_calorie) {
                    data.put("for", "week");
                } else if (ButtonID == month_btn_calorie) {
                    data.put("for", "month");
                } else if (ButtonID == day_btn_calorie) {
                    data.put("for", "today");
                } else {
                    data.put("for", "today");
                }
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void SnacksInfo(){
        String mealType = "Snacks";
        String calorieUrl = String.format("%scalorieConsumed.php", DataFromDatabase.ipConfig);

        StringRequest calorieRequest = new StringRequest(Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("Snacks Info Bro", response);

                    try {
                        JSONObject object = new JSONObject(response);

                        JSONObject valueObject = object.getJSONObject("value");
                        JSONObject snacksObject = valueObject.getJSONObject("Snacks");

                        JSONArray snacksDataArray = snacksObject.getJSONArray("data");
                        for (int i = 0; i < snacksDataArray.length(); i++) {
                            JSONObject snacksData = snacksDataArray.getJSONObject(i);
                            String snackName = snacksData.getString("name");
                            String snackCalories = snacksData.getString("Calories");
                            String dateandtime = snacksData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String Time = timeFormat.format(dateTime);
                            Snacks.add(new calorieconsumedInfo(R.drawable.istockphoto_860224944_612x612_removebg_preview_1, mealType,
                                    snackName, snackCalories, "1", "Small", Time));
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Snacks Info Bro", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("date", caloriedateFormat.format(date));
                if (ButtonID == week_btn_calorie) {
                    data.put("for", "week");
                } else if (ButtonID == month_btn_calorie) {
                    data.put("for", "month");
                } else if (ButtonID == day_btn_calorie) {
                    data.put("for", "today");
                } else {
                    data.put("for", "today");
                }
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    private void DinnerInfo(){
        String mealType="Dinner";
        String calorieUrl = String.format("%scalorieConsumed.php", DataFromDatabase.ipConfig);

        StringRequest calorieRequest = new StringRequest(Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("Dinner Info Bro", response);

                    try {
                        JSONObject object = new JSONObject(response);

                        JSONObject valueObject = object.getJSONObject("value");
                        JSONObject dinnerObject = valueObject.getJSONObject("Dinner");

                        JSONArray dinnerDataArray = dinnerObject.getJSONArray("data");
                        for (int i = 0; i < dinnerDataArray.length(); i++) {
                            JSONObject dinnerData = dinnerDataArray.getJSONObject(i);
                            String dinnerName = dinnerData.getString("name");
                            String dinnerCalories = dinnerData.getString("Calories");
                            String dateandtime = dinnerData.getString("dateandtime");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dateTime = dateFormat.parse(dateandtime);
                            // Format the time portion
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            String Time = timeFormat.format(dateTime);
                            Dinner.add(new calorieconsumedInfo(R.drawable.istockphoto_860224944_612x612_removebg_preview_1, mealType,
                                    dinnerName, dinnerCalories, "1", "Small", Time));
                        }
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Dinner Info Bro", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("date", caloriedateFormat.format(date));
                if (ButtonID == week_btn_calorie) {
                    data.put("for", "week");
                } else if (ButtonID == month_btn_calorie) {
                    data.put("for", "month");
                } else if (ButtonID == day_btn_calorie) {
                    data.put("for", "today");
                } else {
                    data.put("for", "today");
                }
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void SetButtonBackground(View view){
        switch (view.getId()){
            case R.id.day_btn_calorie:
                day_btn_calorie.setTextColor(Color.WHITE);
                week_btn_calorie.setTextColor(Color.BLACK);
                month_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                week_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                month_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                break;
            case R.id.week_btn_calorie:
                week_btn_calorie.setTextColor(Color.WHITE);
                day_btn_calorie.setTextColor(Color.BLACK);
                month_btn_calorie.setTextColor(Color.BLACK);

                day_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                week_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                month_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                break;
            case R.id.month_btn_calorie:
                month_btn_calorie.setTextColor(Color.WHITE);
                week_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                week_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                month_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                break;
            default:
                break;
        }
    }
}