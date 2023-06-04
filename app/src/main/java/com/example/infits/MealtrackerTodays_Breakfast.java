package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MealtrackerTodays_Breakfast extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    ImageView calorieImgback;
    LinearLayout linear_layout1, linear_layout2,rcview;

    MealtrackerFinalAdapter mealtrackerFinalAdapter;


    ArrayList<Todays_BreakFast_info> todays_breakFast_infos;
    private static final String ARG_PARAM2 = "param2";

    TextView DoneButtonView,headerTitle;
    String url = String.format("%ssaveMeal.php", DataFromDatabase.ipConfig);

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView_Todays_breakfast;
    SimpleDateFormat todayDate;
    SimpleDateFormat todayTime;
    Date date;

    public void FragmentTodays_BreakFast() {
        // Required empty public constructor
    }
    public static MealtrackerTodays_Breakfast newInstance(String param1, String param2) {
        MealtrackerTodays_Breakfast fragment = new MealtrackerTodays_Breakfast();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        todays_breakFast_infos = new ArrayList<>();
        todays_breakFast_infos.clear();

        View view = inflater.inflate(R.layout.fragment_mealtracker_todays__breakfast, container, false);
        todayDate = new SimpleDateFormat("d MMM yyyy");

        todayTime = new SimpleDateFormat("h.m.s a");

        date=new Date();

        //set correct header title
        headerTitle = view.findViewById(R.id.header_title);
//        doneMeal = view.findViewById(R.id.done_meal);
        headerTitle.setText(getMeal()); //open after connected
//        doneMeal.setText(getMeal());

        //recycleview
        rcview = view.findViewById(R.id.rcview);
        recyclerView_Todays_breakfast = view.findViewById(R.id.recyclerView_Todays_breakfast);
        recyclerView_Todays_breakfast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //Correcting the Sharedpref
//        correctPref();

        //displaying data
        DisplayDataInList();
//        todays_breakFast_infos.clear();

        mealtrackerFinalAdapter = new MealtrackerFinalAdapter(getContext(), todays_breakFast_infos);
        recyclerView_Todays_breakfast.setAdapter(mealtrackerFinalAdapter);

        //backbutton
        calorieImgback = view.findViewById(R.id.calorieImgback);
        calorieImgback.setOnClickListener(v -> requireActivity().onBackPressed());

        //DoneButtonView
        linear_layout1 = view.findViewById(R.id.linear_layout1);
        linear_layout2 = view.findViewById(R.id.linear_layout2);

        DoneButtonView = view.findViewById(R.id.DoneButtonView);
        DoneButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    linear_layout1.setVisibility(View.GONE);
                    linear_layout2.setVisibility(View.VISIBLE);
                    AddDatatoTable(view);
                } catch (Exception e) {
                    Log.d("Exception123", e.toString());
                }
            }
        });

        //delete shared preference

//        DeleteSharedPreference();
        return view;
    }

    public void AddDatatoTable(View view) {
        try {
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                // Get the SharedPreferences object
                String prefName = obj.getString("image");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BitMapInfo", Context.MODE_PRIVATE);

// Retrieve the value with the specified key, or return a default value if the key doesn't exist
                String myValue = sharedPreferences.getString(prefName, "image");
                obj.remove("image");
                obj.put("image",myValue);
                Log.d("TAG", "AddDatatoTable: got "+obj.getString("image"));
            }
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String mealName=jsonObject1.getString("mealName");
            String Meal_Type=jsonObject1.getString("Meal_Type");

            SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("BitMapInfo", MODE_PRIVATE);
            Log.d("lastBreakFast", sharedPreferences1.getString("ClickedPhoto",""));
            String base64String= sharedPreferences1.getString("ClickedPhoto","");






            RequestQueue queue= Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Log.d("responseCalorie", response);

                if (response.contains("true")) {
                    DeleteSharedPreference();
                    linear_layout2.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getContext(), "Error with database", Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(() -> {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MealTrackerFragment mealTrackerFragment = new MealTrackerFragment();
                    fragmentTransaction.add(R.id.frameLayout, mealTrackerFragment).commit();
                }, 2000);
            },

                    error -> {
                        Log.d("AddDatatoTable", "AddDatatoTable: "+error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    String timeString = todayTime.format(date);
                    String dateString = todayDate.format(date);
                    data.put("name", mealName);
//                    data.put("image", base64String);
                    data.put("date", dateString);
                    data.put("time", timeString);
                    //timeMeal is a Meal_Type
                    data.put("timeMeal", Meal_Type);
                    data.put("description","");
//                    data.put("clientID", DataFromDatabase.clientuserID.toString());
                    data.put("clientID", DataFromDatabase.clientuserID);
                    data.put("position",String.valueOf(jsonArray.length()-1));
                    data.put("jsonArray", jsonArray.toString());
                    return data;
                }



            };

            queue.add(stringRequest);


        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }




    private String getMeal() {
        try {
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            JSONObject jsonObject1 = jsonArray.getJSONObject(jsonArray.length() - 1);
            return jsonObject1.getString("Meal_Type");
        }
        catch (Exception e){
            Log.d("getMeal: ",e.toString());
        }
        return null;
    }

    public void DisplayDataInList() {
        try {

            // Set the Bitmap as the Drawable of the ImageView

//        holder.addmealIcon.setImageDrawable(new BitmapDrawable(context.getResources(), decodedBitmap));
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                todays_breakFast_infos.add(new Todays_BreakFast_info(getContext().getDrawable(R.drawable.pizza_img),
//                        todays_breakFast_infos.add(new Todays_BreakFast_info(decodedBitmap,
                        jsonObject1.getString("mealName"),
                        jsonObject1.getString("calorieValue"),
                        jsonObject1.getString("carbsValue"),
                        jsonObject1.getString("fatValue"),
                        jsonObject1.getString("proteinValue"),
                        jsonObject1.getString("Quantity"),
                        jsonObject1.getString("Size")));
            }

        } catch (Exception e) {
            Log.d("Displaydatainlist", e.toString());
        }
    }

    private void DeleteSharedPreference() {
        Log.d("TAG", "DeleteSharedPreference: " + "Deleted");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // remove all data from shared preferences
        editor.apply(); // commit the changes
        sharedPreferences = getActivity().getSharedPreferences("BitMapInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}