package com.example.infits;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Fragment_diet_third_scrn extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<DataModel> dataList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
        fetchDataFromApi();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diet_third_scrn, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DataModel selectedData = dataList.get(position);
                // Handle item click, e.g., show details for the selectedData
            }
        });

        return rootView;
    }

    private void fetchDataFromApi() {
        String apiUrl = "http://www.db4free.net/path/to/d_recipe_data.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseApiResponse(response);
                        adapter.notifyDataSetChanged(); // Update the RecyclerView
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }
        );

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    private void parseApiResponse(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dietitianId = jsonObject.getString("dietitian_id");
                String recipeName = jsonObject.getString("recipe_name");
                String ingredients = jsonObject.getString("drecipe_ingredients");
                String recipeInstructions = jsonObject.getString("recipe_recipe");
                String nutritionalInfo = jsonObject.getString("recipe_nutritional_information");
                String recipeImage = jsonObject.getString("drecipe_img");
                String recipeCategory = jsonObject.getString("recipe_category");

                dataList.add(new DataModel(
                        dietitianId, recipeName, ingredients,
                        recipeInstructions, nutritionalInfo,
                        recipeImage, recipeCategory
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
