package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.MemoryFile;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTodays_BreakFast#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTodays_BreakFast extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    ImageView calorieImgback;
    LinearLayout linear_layout1, linear_layout2;
    Todays_BreakFast_info todays_breakFast_info;
    ArrayList<Todays_BreakFast_info> todays_breakFast_infos;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView DoneButtonView,headerTitle,doneMeal;
    //String url = String.format("%ssaveMeal.php", DataFromDatabase.ipConfig);
    String url = "https://infits.in/androidApi/saveMeal.php";

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView_Todays_breakfast;
    SimpleDateFormat todayDate;
    SimpleDateFormat todayTime;
    Date date;
    public FragmentTodays_BreakFast() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTodays_BreakFast.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTodays_BreakFast newInstance(String param1, String param2) {
        FragmentTodays_BreakFast fragment = new FragmentTodays_BreakFast();
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
        todays_breakFast_infos = new ArrayList<>();
        todays_breakFast_infos.clear();

        View view = inflater.inflate(R.layout.fragment_todays__break_fast, container, false);
        todayDate = new SimpleDateFormat("d MMM yyyy");

        todayTime = new SimpleDateFormat("h.m.s a");

        date=new Date();

        //set correct header title
        headerTitle = view.findViewById(R.id.header_title);
//        doneMeal = view.findViewById(R.id.done_meal);
        headerTitle.setText(getMeal());
//        doneMeal.setText(getMeal());

        //recycleview
        recyclerView_Todays_breakfast = view.findViewById(R.id.recyclerView_Todays_breakfast);
        recyclerView_Todays_breakfast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DisplayDataInList();
//        todays_breakFast_infos.clear();

        Adapter_Todays_BreakFast adapter_todays_breakFast = new Adapter_Todays_BreakFast(getContext(), todays_breakFast_infos);
        recyclerView_Todays_breakfast.setAdapter(adapter_todays_breakFast);

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
                    AddDatatoTable();


                } catch (Exception e) {
                    Log.d("Exception123", e.toString());
                }
            }
        });

        //delete shared preference

        DeleteSharedPreference();
        return view;
    }

    public void AddDatatoTable() {
        try {
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", Context.MODE_PRIVATE);
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            JSONObject jsonObject1 = jsonArray.getJSONObject(jsonArray.length() - 1);
            String mealName=jsonObject1.getString("mealName");
            String Meal_Type=jsonObject1.getString("Meal_Type");

            SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("BitMapInfo",Context.MODE_PRIVATE);
            Log.d("lastBreakFast", sharedPreferences1.getString("ClickedPhoto","").toString());
            String base64String=sharedPreferences1.getString("ClickedPhoto","").toString();




            RequestQueue queue=Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Log.d("responseCalorie",response.toString());
                if (response.equals("updated")) {
                    linear_layout2.setVisibility(View.GONE);

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        CalorieTrackerFragment calorieTrackerFragment = new CalorieTrackerFragment();
                        fragmentTransaction.add(R.id.frameLayout, calorieTrackerFragment).commit();
                    }
                }, 2000);
            },

                    error -> {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    String timeString = todayTime.format(date);
                    String dateString = todayDate.format(date);
                    data.put("name", mealName.toString());
                    data.put("image", base64String);
                    data.put("date", dateString);
                    data.put("time", timeString);
                    //timeMeal is a Meal_Type
                    data.put("timeMeal", Meal_Type);
                    data.put("description","");
                    data.put("clientID", DataFromDatabase.clientuserID.toString());
                    data.put("position",String.valueOf(jsonArray.length()-1));
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
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", Context.MODE_PRIVATE);
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            JSONObject jsonObject1 = jsonArray.getJSONObject(jsonArray.length() - 1);
            String Meal_Type=jsonObject1.getString("Meal_Type");
            return Meal_Type;
        }
        catch (Exception e){
            Log.d("getMeal: ","Json shared meal error");
        }
        return null;
    }

    public void DisplayDataInList() {
        try {
            sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", Context.MODE_PRIVATE);

//            //        holder.addmealIcon.
//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BitMapInfo", Context.MODE_PRIVATE);
//            String base64String = sharedPreferences.getString("ClickedPhoto", null);
//
//            // Decode the base64 string to a Bitmap object
//            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
//            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Set the Bitmap as the Drawable of the ImageView

//        holder.addmealIcon.setImageDrawable(new BitmapDrawable(context.getResources(), decodedBitmap));
            SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("BitMapInfo", MODE_PRIVATE);
            Log.d("lastBreakFast", sharedPreferences1.getString("ClickedPhoto",""));
            String base64String= sharedPreferences1.getString("ClickedPhoto","");
            byte[] imageAsBytes = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
            Bitmap   myImage= BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("TodaysBreakFast", ""));
            JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                todays_breakFast_infos.add(new Todays_BreakFast_info(myImage,
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
            Log.d("Exception", e.toString());
        }
    }

    private void DeleteSharedPreference() {

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("tracker", "TodaysBreakFast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0L, 59 * 1000, pendingIntent);
    }
}