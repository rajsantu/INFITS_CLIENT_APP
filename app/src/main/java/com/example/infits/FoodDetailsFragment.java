package com.example.infits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
 * Use the {@link FoodDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailsFragment extends Fragment {

    ImageView imgBack;
    EditText searchBar;
    RecyclerView foodDetailsRV;

    ArrayList<FoodItem> foodItems = new ArrayList<>();
    FoodItemAdapter foodItemAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDetailsFragment newInstance(String param1, String param2) {
        FoodDetailsFragment fragment = new FoodDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_food_details, container, false);

        imgBack = view.findViewById(R.id.imgback);
        searchBar = view.findViewById(R.id.searchView);
        foodDetailsRV = view.findViewById(R.id.foodDetailsRV);

        imgBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getFoodItems();

        return view;
    }

    private void filterList(String input) {
        ArrayList<FoodItem> filterList = new ArrayList<>();

        for(FoodItem foodItem: foodItems) {
            if(foodItem.name.toLowerCase().contains(input.toLowerCase())) {
                filterList.add(foodItem);
            }
        }

        foodItemAdapter.setFilteredList(filterList);
    }

    private void getFoodItems() {
        String getFoodUrl = String.format("%sgetFoodItems.php", DataFromDatabase.ipConfig);

        StringRequest getFoodRequest = new StringRequest(
                Request.Method.GET,
                getFoodUrl,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray foodArray = object.getJSONArray("food");

                        for(int i = 0; i < foodArray.length(); i++) {
                            JSONObject currFood = foodArray.getJSONObject(i);

                            String name = currFood.getString("name");
                            String calorie = currFood.getString("calorie");
                            String protein = currFood.getString("protein");
                            String fibre = currFood.getString("fibre");
                            String carb = currFood.getString("carb");
                            String fat = currFood.getString("fat");

                            FoodItem foodItem = new FoodItem(name, calorie, protein, fibre, carb, fat);
                            foodItems.add(foodItem);
                        }

                        foodItemAdapter = new FoodItemAdapter(foodItems, requireContext());
                        foodDetailsRV.setLayoutManager(new LinearLayoutManager(requireContext()));
                        foodDetailsRV.setAdapter(foodItemAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("FoodDetailsFragment",error.toString())
        );
        Volley.newRequestQueue(requireContext()).add(getFoodRequest);
    }
}