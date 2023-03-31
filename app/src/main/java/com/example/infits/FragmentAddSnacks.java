package com.example.infits;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddSnacks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddSnacks extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String url = String.format("%sgetFoodItems.php",DataFromDatabase.ipConfig);
    String url1 = String.format("%sgetFavouriteFoodItems.php", DataFromDatabase.ipConfig);
    String mealType="Snacks";
    RequestQueue queue,requestQueue;

    ArrayList<addmealInfo> filteredlist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AutoCompleteTextView snacksSpinner;
    ArrayAdapter<String> arrayAdapter;
    ImageView underlineFrequent,underlineRecent,underlineFavourites;
    TextView recentTextView,FavouritesTextview,frequentTextView;
    SearchView searchsnacks;
    RecyclerView snacksitems;
    ArrayList<addmealInfo> addmealInfos;
    ImageView calorieImgback;
    String[] calorieDropDownitems={"Yesterday","Today","Tomorrow"};
    AddMealAdapter addMealAdapter;
    public FragmentAddSnacks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddSnacks.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddSnacks newInstance(String param1, String param2) {
        FragmentAddSnacks fragment = new FragmentAddSnacks();
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
        View view=inflater.inflate(R.layout.fragment_add_snacks, container, false);
        addmealInfos=new ArrayList<>();
        hooks(view);
        AddFrequentMeal();
        calorieImgback.setOnClickListener(v -> requireActivity().onBackPressed());
        snacksSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSuggestion = (String) parent.getItemAtPosition(position);
                AddFrequentMeal();
            }
        });
        searchsnacks.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
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
        addMealAdapter=new AddMealAdapter(getActivity(),addmealInfos);
        snacksitems.setAdapter(addMealAdapter);
        return view;
    }
    private void filter(String text) {
        filteredlist = new ArrayList<>();

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
    private void hooks(View view){
        snacksSpinner=view.findViewById(R.id.snacksSpinner);
        arrayAdapter=new ArrayAdapter<>(requireActivity(), R.layout.dropdownitems,calorieDropDownitems);
        snacksSpinner.setAdapter(arrayAdapter);
        underlineFrequent=view.findViewById(R.id.underlineFrequent);
        underlineRecent=view.findViewById(R.id.underlineRecent);
        underlineFavourites=view.findViewById(R.id.underlineFavourites);
        recentTextView=view.findViewById(R.id.recentTextView);
        FavouritesTextview=view.findViewById(R.id.FavouritesTextview);
        frequentTextView=view.findViewById(R.id.frequentTextView);
        searchsnacks=view.findViewById(R.id.searchsnacks);
        snacksitems=view.findViewById(R.id.snacksitems);
        calorieImgback=view.findViewById(R.id.calorieImgback);
        snacksitems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }
    private void AddRecentMeal(String name,String calorieValue,String protin,String carb,String fat){
        addmealInfos.clear();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RecentMeal", Context.MODE_PRIVATE);

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
            addMealAdapter=new AddMealAdapter(getActivity(),addmealInfos);
            snacksitems.setAdapter(addMealAdapter);
        }catch (JSONException jsonException){
            Log.d("JSONException",jsonException.toString());
        }
    }
    private void AddFrequentMeal(){
        addmealInfos.clear();
        queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,response -> {
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
                    addmealInfos.add(new addmealInfo(R.drawable.hotdog,mealType,name,calorie,carb,protein,fat));
                }

                addMealAdapter=new AddMealAdapter(getActivity(),addmealInfos);
                snacksitems.setAdapter(addMealAdapter);
            }catch (JSONException e){
                e.printStackTrace();

            }

        },error -> {
            Log.d("error123", error.toString());
            Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
        }){

        };
        queue.add(stringRequest);

    }
    private void AddFavouritesMeal(){
        addmealInfos.clear();
        requestQueue = Volley.newRequestQueue(getContext());
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
                    addmealInfos.add(new addmealInfo(R.drawable.hotdog,mealType,name,calorie,carb,protein,fat));
                }
                addMealAdapter=new AddMealAdapter(getActivity(),addmealInfos);
                snacksitems.setAdapter(addMealAdapter);
            }catch (Exception e){
                Log.d("JSONException",e.toString());
            }
        },error -> {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientuserID", DataFromDatabase.clientuserID);
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