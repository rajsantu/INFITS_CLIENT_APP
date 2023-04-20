package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealtrackerAddMeal extends AppCompatActivity {

    String url = String.format("%sgetFoodItems.php",DataFromDatabase.ipConfig);
    String url1 = String.format("%sgetFavouriteFoodItems.php", DataFromDatabase.ipConfig);
    String heading;

    private TextView title,recentTextView,FavouritesTextview,frequentTextView;;
    View underlineFrequent,underlineRecent,underlineFavourites;
    SearchView searchBreakfast;
    RecyclerView breakfastitems;
    JSONObject mainJSONobj = new JSONObject();
    JSONArray jsonArray= new JSONArray();
    RequestQueue queue,requestQueue;
    ArrayList<addmealInfo> addmealInfos = new ArrayList<>();
    ImageView calorieImgback;
//    AddMealAdapter addMealAdapter;
    MealtrackerAddMealAdapter addMealAdapter;
    ArrayList<addmealInfo> filteredlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealtracker_add_meal);


        //Hooks
        title = findViewById(R.id.title);
//        Toast.makeText(this, "title", Toast.LENGTH_SHORT).show();
        underlineFrequent = findViewById(R.id.underlineFrequent);
        underlineRecent = findViewById(R.id.underlineRecent);
        underlineFavourites = findViewById(R.id.underlineFavourites);
//        Toast.makeText(this, "underline", Toast.LENGTH_SHORT).show();
        recentTextView = findViewById(R.id.recentTextView);
        FavouritesTextview = findViewById(R.id.FavouritesTextview);
        frequentTextView = findViewById(R.id.frequentTextView);
//        Toast.makeText(this, "textview", Toast.LENGTH_SHORT).show();

        searchBreakfast=findViewById(R.id.searchBreakfast);


        breakfastitems=findViewById(R.id.breakfastitems);
        calorieImgback=findViewById(R.id.calorieImgback);
        breakfastitems.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));


        //setting title
        heading = getIntent().getStringExtra("fragment");
        title.setText(heading);



        //basic actions
        AddFrequentMeal();
        calorieImgback.setOnClickListener(v -> onBackPressed());


        searchBreakfast.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                filter(newText);
                return false;
            }
        });

        recentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnderLineLayout(v);
                for (int i=0;i<addmealInfos.size();i++) {
                    AddRecentMeal(addmealInfos.get(i).mealname, addmealInfos.get(i).mealcalorie
                            , addmealInfos.get(i).protein, addmealInfos.get(i).carb, addmealInfos.get(i).protein);
                }
            }
        });
        frequentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnderLineLayout(v);
                AddFrequentMeal();

            }
        });
        FavouritesTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnderLineLayout(v);
                AddFavouritesMeal();

            }
        });



    }

    private void filter(String text) {
        filteredlist = new ArrayList<addmealInfo>();

        for (addmealInfo item : addmealInfos) {
            if (item.mealname.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Log.d("Error","No Data Found");
//            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            addMealAdapter.filterList(filteredlist);
        }
    }
    private void AddRecentMeal(String name,String calorieValue,String protin,String carb,String fat){
        addmealInfos.clear();
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("RecentMeal", Context.MODE_PRIVATE);

            if(sharedPreferences.contains("RecentMealInfo")) {
                JSONObject jsonObject = new JSONObject(sharedPreferences.getString("RecentMealInfo", ""));
                JSONArray jsonArray1 = jsonObject.getJSONArray("RecentMealInfo");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                    addmealInfos.add(new addmealInfo(Integer.parseInt(jsonObject1.getString("icon")),"BreakFast",jsonObject1.getString("name"),
                            jsonObject1.getString("calorie"),jsonObject1.getString("protin"),jsonObject1.getString("carb"),
                            jsonObject1.getString("fat")));
                }
            }
            addMealAdapter=new MealtrackerAddMealAdapter(getApplicationContext(),addmealInfos);
            breakfastitems.setAdapter(addMealAdapter);
        }catch (JSONException jsonException){
            Log.d("JSONException",jsonException.toString());
        }
    }
    private void AddFrequentMeal(){
//        Toast.makeText(this, "Freq", Toast.LENGTH_SHORT).show();
        addmealInfos.clear();
        queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("food");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String name=jsonObject1.getString("name");
                    String calorie=jsonObject1.getString("calorie")+"  kcal";
                    String carb=jsonObject1.getString("carb");
                    String protein=jsonObject1.getString("protein");
                    String fat=jsonObject1.getString("fat");
//                    Log.d("error123", name);

                    addmealInfos.add(new addmealInfo(R.drawable.donutimg_full,heading,name,calorie,carb,protein,fat));
                }
//                Log.d("error123", addmealInfos.toString());
                addMealAdapter=new MealtrackerAddMealAdapter(this,addmealInfos);
                breakfastitems.setAdapter(addMealAdapter);
            }catch (JSONException e){
                Log.d("error123", e.toString());
                e.printStackTrace();
            }

        },error -> {
            Log.d("error123", error.toString());
            Toast.makeText(this,error.toString().trim(),Toast.LENGTH_SHORT).show();
        }){

        };
        queue.add(stringRequest);

    }
    private void AddFavouritesMeal(){
        addmealInfos.clear();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("favouriteFoodItems");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String name=jsonObject1.getString("nameofFoodItem");
                    String calorie=jsonObject1.getString("calorie")+"  kcal";
                    String carb=jsonObject1.getString("carb");
                    String protein=jsonObject1.getString("protein");
                    String fat=jsonObject1.getString("fat");
                    addmealInfos.add(new addmealInfo(R.drawable.hotdog,heading,name,calorie,carb,protein,fat));
                }
                addMealAdapter=new MealtrackerAddMealAdapter(this,addmealInfos);
                breakfastitems.setAdapter(addMealAdapter);
            }catch (Exception e){
                Log.d("JSONException",e.toString());
            }
        },error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientuserID", "test");
                return data;
            }
        };
//        for(int j=0;j<addmealInfos.size();j++){
//            if(addmealInfos.get(j).mealname.indexOf())
//        }
        requestQueue.add(stringRequest1);

    }
    private void UnderLineLayout(View view){
        switch (view.getId()){
            case R.id.frequentTextView:
                underlineFrequent.setVisibility(View.VISIBLE);
                underlineRecent.setVisibility(View.INVISIBLE);
                underlineFavourites.setVisibility(View.INVISIBLE);
                break;
            case R.id.recentTextView:
                underlineRecent.setVisibility(View.VISIBLE);
                underlineFrequent.setVisibility(View.INVISIBLE);
                underlineFavourites.setVisibility(View.INVISIBLE);
                break;
            case R.id.FavouritesTextview:
                underlineFavourites.setVisibility(View.VISIBLE);
                underlineRecent.setVisibility(View.INVISIBLE);
                underlineFrequent.setVisibility(View.INVISIBLE);
                break;
            default:
                underlineFrequent.setVisibility(View.VISIBLE);
                underlineRecent.setVisibility(View.INVISIBLE);
                underlineFavourites.setVisibility(View.INVISIBLE);
                break;
        }
    }
}