package com.example.infits;

import static com.example.infits.fragment_diet_chart.getRecipeName;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

//import sun.bob.mcalendarview.adapters.CalendarAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link diet_fourth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class diet_fourth extends Fragment implements CalMealScheduleAdapter.OnDateSelectedListener {
     RecyclerView recyclerView,recyclerView2;
     private List<Date> dateList;
    CalMealScheduleAdapter adapter;

    ImageView back_btn;

    ProgressBar progressBar;
//    int calories_count,protein_count,carbs_count,fats_counts = 0;

    List<MealScheduleItemClass> itemClasses;
    MealScheduleAdapter mealScheduleAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public diet_fourth() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment diet_fourth.
     */
    // TODO: Rename and change types and number of parameters
    public static diet_fourth newInstance(String param1, String param2) {
        diet_fourth fragment = new diet_fourth();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_diet_fourth , container , false );
        hooks(view);

        // for date viewing in recyclerView
        Date today = new Date();
        dateList = new ArrayList<> ();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        recyclerView = view.findViewById(R.id.recyclerViewMealSchedule);
        adapter = new CalMealScheduleAdapter(dateList,requireActivity(),this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper ();
        snapHelper.attachToRecyclerView(recyclerView);

        //for recyclerView2
        recyclerView2 = view.findViewById(R.id.mealScheduleRecylerView);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(requireActivity());
        recyclerView2.setLayoutManager(layoutManager2);

        back_btn.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_diet_fourth2_to_fragment_diet_chart));
        Format fdn = new SimpleDateFormat("EEEE");
        req(fdn.format(new Date()).toLowerCase());
        return view;
    }

    private void hooks(View view) {
        back_btn = view.findViewById(R.id.back_btn);
        progressBar = view.findViewById(R.id.mealScheduleProgress);

        itemClasses = new ArrayList<>();
    }

    private void req(String day){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(requireActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, String.format("%sgetDietChart.php",DataFromDatabase.ipConfig) ,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject data_response = new JSONObject(response);
                            String data = data_response.getString("data");
                            JSONArray jsonArrayData = new JSONArray(data);
                            JSONObject jsonObjectData = jsonArrayData.getJSONObject(0);
                            JSONObject day_name = new JSONObject(jsonObjectData.getString(day));

                            addDetails(day_name,"breakfast","breakfast_morning","breakfast_after","breakfast_morning","breakfast_after");

                            addDetails(day_name,"lunch","afternoon","Afternoon time");

                            addDetails(day_name,"snacks","High Tea and Snacks","Snacks time");

                            addDetails(day_name,"dinner","night","late_night","night","late night");

                            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutTwo,"Today's Meal Nutrition",""));
                            //layout 3
                            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutThree,R.drawable.fire,"Calories","300kCal",""));
                            //layout 3
                            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutThree,R.drawable.fish,"Protein","300 g",""));
                            //layout 3
                            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutThree,R.drawable.carbs_diet_chart,"Carbs","300 g",""));
                            //layout 3
                            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutThree,R.drawable.fats,"Fats","210 g",""));

                             mealScheduleAdapter = new MealScheduleAdapter(itemClasses);
                             recyclerView2.setAdapter(mealScheduleAdapter);


                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(),e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(),"Connectivity Issue", Toast.LENGTH_SHORT).show();
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("dietitian_id","6");
                data.put("client_id","1");
                data.put("type","1");
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
        //for dinner and breakfast
        private void addDetails(JSONObject day_name,String key1,String key2,String key3,String time1,String time2){
        try {
            JSONObject key1_data = new JSONObject(day_name.getString(key1));
            JSONArray key2_data = new JSONArray(key1_data.getString(key2));
            JSONArray key3_data = new JSONArray(key1_data.getString(key3));

            //layout 2 Breakfast
            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutTwo,key1,key2_data.length()+key3_data.length()+" meals"));

            for (int i = 0; i < key2_data.length(); i++) {
                itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutOne,R.drawable.blue_pancake,getRecipeName(key2_data.getString(i)),time1));
            }
            for (int i = 0; i < key3_data.length(); i++) {
                itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutOne,R.drawable.blue_pancake,getRecipeName(key3_data.getString(i)),time2));
            }
        }catch (Exception e){
            Toast.makeText(requireActivity(),"addBreakFast " + e.toString(), Toast.LENGTH_SHORT).show();
        }
}

        //for snacks lunch
        private void addDetails(JSONObject day_name,String meal_name,String key,String time){
        try {
            JSONObject meal_name_data = new JSONObject(day_name.getString(meal_name));
            JSONArray key_data = new JSONArray(meal_name_data.getString(key));
            itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutTwo,meal_name,key_data.length()+" meals"));
            for (int i = 0; i < key_data.length(); i++) {
                itemClasses.add(new MealScheduleItemClass(MealScheduleItemClass.LayoutOne,R.drawable.blue_pancake,getRecipeName(key_data.getString(i)),time));
            }
        }catch (Exception e){
            Toast.makeText(requireActivity(),"addBreakFast2 " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void reqToCount(JSONArray key_data) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(requireActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, String.format("%sgetDietChart.php",DataFromDatabase.ipConfig) ,
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArrayData = new JSONArray(response);
                            for (int i = 0 ; i<jsonArrayData.length();i++){
                                    JSONObject jsonObject = new JSONObject(jsonArrayData.getString(i));
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("drecipe_nutritional_information"));
//                                    calories_count = calories_count+Integer.parseInt(jsonObject1.getString("Calories"));
                            }

                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(),e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(),"Connectivity Issue", Toast.LENGTH_SHORT).show();
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("dietitian_id","6");
                data.put("client_id","1");
                data.put("type","3");
                data.put("recipe_id[]",key_data.toString().replace("[","").replace("]",""));
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSelected(Date date) {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( date );
        itemClasses.clear();
        mealScheduleAdapter.notifyDataSetChanged();
        req(dayName(new SimpleDateFormat ( "E" , Locale.getDefault () ).format ( date )));
    }
    private String dayName(String str){
        switch (str){
            case "Mon" : return "monday";
            case "Tue" : return "tuesday";
            case "Wed" : return "wednesday";
            case "Thu" : return "thursday";
            case "Fri" : return "friday";
            case "Sat" : return "saturday";
            case "Sun" : return "sunday";
            default: return  "monday";
        }

    }
}
