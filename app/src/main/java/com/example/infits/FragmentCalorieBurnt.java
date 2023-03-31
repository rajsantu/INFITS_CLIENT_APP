package com.example.infits;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCalorieBurnt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCalorieBurnt extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String url = String.format("%scalorieBurnt.php", DataFromDatabase.ipConfig);
    PieChart pieChart;
    ImageView imgBack;
    ArrayList<calorieInfo> calorieInfos;
    int[] colors={Color.parseColor("#FCFF72"),Color.parseColor("#FF6262"),Color.parseColor("#FFA361")};
    RecyclerView calorieRecycleview;
    Button day_btn_calorie,week_btn_calorie,year_btn_calorie;
    TextView totalCalorieValue,caloriedisplaydate;
    public FragmentCalorieBurnt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCalorieBurnt.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCalorieBurnt newInstance(String param1, String param2) {
        FragmentCalorieBurnt fragment = new FragmentCalorieBurnt();
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
        View view=inflater.inflate(R.layout.fragment_calorie_burnt, container, false);
        calorieInfos=new ArrayList<>();
        hooks(view);
        pieChart(30L,40L,40L);
        String date = getCurrentDate(),calorie="823";
//        pastAcivity(date,calorie,"45","42","452","11:10 a.m.","11:10 a.m.","11:10 a.m.","00:18:52","00:18:52","00:18:52");

        setTodayData("day");

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

//        Activity activity = getActivity();
//        if (activity != null) {
//            // Access the Activity's properties or perform actions on it
//            String activityName = activity.getClass().getSimpleName();
//            Toast.makeText(activity, activityName, Toast.LENGTH_SHORT).show();
//            Log.d("CurrentActivity", activityName);
//        }

        return view;
    }

    private void setTodayData(String purpose) {
        String date = getCurrentDate(),calorie="823";
//        pastAcivity(date,calorie,"45","42","452");

        String clientID = "test"; // as it will be in sharedpref using it temporarily
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                String running="0",walking="0",cycling="0",runtime="0",walktime="0",cyclingtime="0",runduration="0",cycleduration="0",walkduration="0";

                                long Total = 0,rduration=0,cduration=0,wduration=0,runcal=0,walkcal=0,cyclingcal=0;

//                                pastAcivity(date,calorie,running,walking,cycling);

//                                JSONArray activitiesArr = jsonObject.getJSONArray("activities");
                                String data = "0";

                                // loop through the activities array and retrieve the data
//                                for(int j=0;j<3;j++) {
                                try {
                                    for(;;) {
//                                    JSONArray activitiesArr = jsonObject.getJSONArray(data);

                                            JSONObject activityObj = jsonObject.getJSONObject(data);
                                            data = Integer.toString(Integer.parseInt(data) + 1);
//                                    for (int i = 0; i < activitiesArr.length(); i++) {
//                                        JSONObject activityObj = activitiesArr.getJSONObject(i);

//                                    int id = activityObj.getInt("id");
//                                    String clientId = activityObj.getString("client_id");
//                                    String dieticianId = activityObj.getString("dietician_id");
                                            String activityName = activityObj.getString("activity_name");
                                            String caloriesBurnt = activityObj.getString("calorie_burnt");
                                            switch (activityName) {
                                                case "running":
                                                    running = caloriesBurnt;
                                                    runcal += Long.parseLong(running);
                                                    runtime = activityObj.getString("time");
                                                    runduration = activityObj.getString("duration");
                                                    rduration += ConvertTimeToInt(runduration);
                                                    break;
                                                case "walking":
                                                    walking = caloriesBurnt;
                                                    walkcal += Long.parseLong(walking);
                                                    walktime = activityObj.getString("time");
                                                    walkduration = activityObj.getString("duration");
                                                    wduration += ConvertTimeToInt(walkduration);
                                                    break;
                                                case "cycling":
                                                    cycling = caloriesBurnt;
                                                    cyclingcal += Long.parseLong(cycling);
                                                    cyclingtime = activityObj.getString("time");
                                                    cycleduration = activityObj.getString("duration");
                                                    cduration += ConvertTimeToInt(cycleduration);
                                                    break;
                                            }
                                            Total += Integer.parseInt(caloriesBurnt);
                                            String duration = activityObj.getString("duration");
                                            String date = activityObj.getString("date");

                                        }
                                    }
                                //as long as the result of php is
                                catch (Exception e){
//                                    for day there is no need to calculate calorie and duration
                                    if(!purpose.equals("day")){
                                        pastAcivity(date,Long.toString(Total),Long.toString(runcal),Long.toString(walkcal),Long.toString(cyclingcal),runtime,walktime,cyclingtime, timeToString(rduration),timeToString(wduration),timeToString(cduration));
                                    }
                                    else
                                        pastAcivity(date,Long.toString(Total),running,walking,cycling,runtime,walktime,cyclingtime,runduration,walkduration,cycleduration);

                                    pieChart(walkcal,runcal,cyclingcal);
                                }

//                                Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onErrorResponse: "+error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID",clientID);
                params.put("date",databaseDate());
                params.put("for",purpose);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private int ConvertTimeToInt(String timeStr){
        String[] timeParts = timeStr.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);
        int durationInSeconds = (hours * 3600) + (minutes * 60) + seconds;
        return durationInSeconds;
    }

    private String timeToString(long durationInSeconds){
//        long durationInSeconds = 1800; // 30 minutes
        int hours = (int) durationInSeconds / 3600;
        int minutes = (int) (durationInSeconds % 3600) / 60;
        int seconds = (int) durationInSeconds % 60;

        String durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return durationString;
    }

    private String databaseDate(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }


    private void hooks(View view){
        pieChart=view.findViewById(R.id.piechart);
        calorieRecycleview=view.findViewById(R.id.calorierecycleView);
        calorieRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        day_btn_calorie=view.findViewById(R.id.day_btn_calorie);
        week_btn_calorie=view.findViewById(R.id.week_btn_calorie);
        year_btn_calorie=view.findViewById(R.id.year_btn_calorie);
        caloriedisplaydate=view.findViewById(R.id.caloriedisplaydate);
        totalCalorieValue=view.findViewById(R.id.totalCalorieValue);
        imgBack=view.findViewById(R.id.calorieImgback);
        day_btn_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonBackground(v);
//                pieChart();
                String date = getCurrentDate(),calorie="823";
                setTodayData("day");
//                pastAcivity(date,calorie,"452","452","452","11:10 a.m.","11:10 a.m.","11:10 a.m.","00:18:52","00:18:52","00:18:52");
            }
        });
        week_btn_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonBackground(v);
//                pieChart();
//                String date = "23 jan,2023",calorie="823";
//                pastAcivity(date,calorie,"452","452","452");
//                pastAcivity(date,calorie,"452","452","452","11:10 a.m.","11:10 a.m.","11:10 a.m.","00:18:52","00:18:52","00:18:52");
                setTodayData("week");

            }
        });
        year_btn_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetButtonBackground(v);
//                pieChart();
//                String date = "23 jan,2023",calorie="823";
//                pastAcivity(date,calorie,"452","452","452","11:10 a.m.","11:10 a.m.","11:10 a.m.","00:18:52","00:18:52","00:18:52");
                setTodayData("year");
            }
        });

    }
//    private void pieChart(){
//        List<PieEntry> entries=new ArrayList<>();
//        entries.add(new PieEntry(40f, getResources().getDrawable(R.drawable.piechart_walk)));
//        entries.add(new PieEntry(30f, getResources().getDrawable(R.drawable.piechart_cycle)));
//        entries.add(new PieEntry(30f, getResources().getDrawable(R.drawable.piechart_run)));
//
//        pieChart.getLegend().setEnabled(false);
//        PieDataSet dataSet = new PieDataSet(entries, "");
//        dataSet.setColors(colors);
//        dataSet.setSliceSpace(2f);
//        PieData data = new PieData(dataSet);
//        pieChart.setDrawHoleEnabled(false);
//        data.setValueTextSize(0f);
//        Description description=new Description();
//        description.setText("");
//        pieChart.setDescription(description);
//        pieChart.setHoleRadius(0f);
//        pieChart.setRotationEnabled(false);
//        pieChart.setData(data);
//        pieChart.animateY(1000, Easing.EaseInOutCubic);
//    }

    private void pieChart(Long walk,Long run,Long cycle){
        List<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry(walk, getResources().getDrawable(R.drawable.piechart_walk)));
        entries.add(new PieEntry(run, getResources().getDrawable(R.drawable.piechart_cycle)));
        entries.add(new PieEntry(cycle, getResources().getDrawable(R.drawable.piechart_run)));

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
    }
    private void pastAcivity(String date,String calorie,String runningk,String walkingk,String cyclingk,String runtime,
                             String walktime,String cyclingtime,String runduration,String walkduration,String cycleduration){
        totalCalorieValue.setText(calorie);
        caloriedisplaydate.setText(date);
        calorieInfos.clear();
        calorieInfos.add(new calorieInfo(R.drawable.baseline_directions_run_24,"Icon","Running",runningk+" kcal",runduration,runtime));
        calorieInfos.add(new calorieInfo(R.drawable.baseline_directions_walk_24,"Icon","Walking",walkingk+" kcal",walkduration,walktime));
        calorieInfos.add(new calorieInfo(R.drawable.baseline_directions_bike_24,"Icon","Cycling",cyclingk+" kcal",cycleduration,cyclingtime));
        CalorieInfoAdapter calorieInfoAdapter=new CalorieInfoAdapter(getContext(),calorieInfos);
        calorieRecycleview.setAdapter(calorieInfoAdapter);
    }

    private String getCurrentDate(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    private void SetButtonBackground(View view){
        switch (view.getId()){
            case R.id.day_btn_calorie:
                day_btn_calorie.setTextColor(Color.WHITE);
                week_btn_calorie.setTextColor(Color.BLACK);
                year_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                week_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                year_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                break;
            case R.id.week_btn_calorie:
                week_btn_calorie.setTextColor(Color.WHITE);
                day_btn_calorie.setTextColor(Color.BLACK);
                year_btn_calorie.setTextColor(Color.BLACK);

                day_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                week_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                year_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                break;
            case R.id.year_btn_calorie:
                year_btn_calorie.setTextColor(Color.WHITE);
                week_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.setTextColor(Color.BLACK);
                day_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                week_btn_calorie.getBackground().setTint(Color.TRANSPARENT);
                year_btn_calorie.getBackground().setTint(Color.parseColor("#ED9B37"));
                break;
            default:
                break;
        }
    }
}