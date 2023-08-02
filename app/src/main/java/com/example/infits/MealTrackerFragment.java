package com.example.infits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealTrackerFragment extends Fragment {

    private CardView breakfast,lunch,snack,dinner;
    private RecyclerView rv;
    List<addmealInfo> addmealInfos;
    View bottomSheetN;
    RequestQueue queue;
    //String url=  String.format("%smealTrackerRecentShared.php",DataFromDatabase.ipConfig);
    String url = "https://infits.in/androidApi/mealTrackerRecentShared.php";
    ImageView imageback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MealTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MealTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MealTrackerFragment newInstance(String param1, String param2) {
        MealTrackerFragment fragment = new MealTrackerFragment();
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
        View view = inflater.inflate(R.layout.fragment_meal_tracker, container, false);
        Log.d("TAG", "onCreateView: ");

        hooks(view);
        setData();

        Bundle bundle=new Bundle();


        imageback.setOnClickListener(v -> {
            if(getArguments() != null && getArguments().getBoolean("notification") /* coming from notification */) {
                startActivity(new Intent(getActivity(),DashBoardMain.class));
                requireActivity().finish();
            } else {
                Navigation.findNavController(v).navigate(R.id.action_mealTracker_to_dashBoardFragment);
            }
        });

        breakfast.setOnClickListener(view1 -> {
            String message = "Breakfast";
            bundle.putString("fragment",message);
            Navigation.findNavController(view1).navigate(R.id.action_mealTracker_to_addmealFragment, bundle);
        });

        lunch.setOnClickListener(view12 -> {
            String message = "Lunch";
            bundle.putString("fragment",message);
            Navigation.findNavController(view12).navigate(R.id.action_mealTracker_to_addmealFragment, bundle);
        });

        snack.setOnClickListener(view13 -> {
            String message = "Snack";
            bundle.putString("fragment",message);
            Navigation.findNavController(view13).navigate(R.id.action_mealTracker_to_addmealFragment, bundle);
        });

        dinner.setOnClickListener(view14 -> {
            String message = "Dinner";
            bundle.putString("fragment",message);
            Navigation.findNavController(view14).navigate(R.id.action_mealTracker_to_addmealFragment, bundle);
        });

        return view;
    }

    private void hooks(View v) {

        breakfast = v.findViewById(R.id.meal_op1);
        lunch = v.findViewById(R.id.meal_op2);
        snack = v.findViewById(R.id.meal_op3);
        dinner = v.findViewById(R.id.meal_op4);
        rv = v.findViewById(R.id.recyclerview);
        imageback = v.findViewById(R.id.imgback);

        bottomSheetN = v.findViewById(R.id.bottomSheetN);

        BottomSheetBehavior.from(bottomSheetN).setPeekHeight(780);
        BottomSheetBehavior.from(bottomSheetN).setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void setData(){
        addmealInfos = new ArrayList<>();

        queue = Volley.newRequestQueue(getContext());
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = formatter.format(today);
//        System.out.println("Today's date is: " + formattedDate);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url, response -> {
            Log.d("Recent meal Data Bro", response);
            //if(!response.equals("0 results"))
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    // Loop through each object in the array and do something with it
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String name=jsonObject1.getString("name");
                        String meal=jsonObject1.getString("meal");
                        String calorie=jsonObject1.getString("Calories")+"  kcal";
                        //must accept these changes and overwrite older!!
                        String carb=String.valueOf(jsonObject1.getInt("carbs"));
                        String protein=String.valueOf(jsonObject1.getInt("protein"));
                        String fat=String.valueOf(jsonObject1.getInt("fat"));
                        addmealInfo mealarr = new addmealInfo(R.drawable.donutimg_full,meal,name,calorie,carb,protein,fat);


                        String dateandtimeString = jsonObject1.getString("dateandtime");
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dateTime = dateFormat.parse(dateandtimeString);
                        // Format the time portion
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = timeFormat.format(dateTime);

                        //String timeString = jsonObject1.getString("time");
                        //String[] parts = timeString.split("\\s+"); // split time and AM/PM
                        //String[] parts = timeString.split("\\s+"); // split time and AM/PM
                        //String[] timeParts = parts[0].split("\\."); // split time into hours, minutes, and seconds
                        //String[] timeParts = parts[0].split("\\."); // split time into hours, minutes, and seconds
                        //String timeWithoutSeconds = timeParts[0] + "." + timeParts[1] + " " + parts[1]; // concatenate hours, minutes, and AM/PM
                        //String timeWithoutSeconds = timeParts[0] + "." + timeParts[1] + " " + parts[1]; // concatenate hours, minutes, and AM/PM

                        //System.out.println(timeWithoutSeconds); // output: 9.20 AM

                        //mealarr.setTime(timeWithoutSeconds);
                        mealarr.setTime(formattedTime);
                        addmealInfos.add(mealarr);
                        //                    Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                        // etc. - replace these with the column names you want to retrieve
                    }
                    Log.d("TAG", "itemList size: " + addmealInfos.size());

                    MealTrackerSharedMealAdapter adapter = new MealTrackerSharedMealAdapter(getContext(), addmealInfos);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv.setAdapter(adapter);

                    //                if(!response.toString().equals("0 results")){
                    //                    JSONObject jsonObject = new JSONObject(response);
                    //                    String name = jsonObject.getString("name");
                    //
                    //                }

                } catch (Exception e) {
                    Log.d("TAG", "setData: " + e);
                }
        },error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientuserID", DataFromDatabase.clientuserID);
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                data.put("dateandtime",dtf.format(now));
                //data.put("date",formattedDate);
//                data.put("date","25 apr 2023");
                return data;
            }
        };
        queue.add(stringRequest);

    }
}