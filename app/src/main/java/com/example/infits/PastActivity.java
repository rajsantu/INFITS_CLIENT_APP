package com.example.infits;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class PastActivity extends AppCompatActivity {

    private ArrayList<PastItem> arrpast = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerPastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);

        recyclerView = findViewById(R.id.recyclerPast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerPastAdapter(this, arrpast);
        recyclerView.setAdapter(adapter);

        fetchPastItems();
    }

    private void fetchPastItems() {
        String url = "http://192.168.18.6/get_past_activities.php";


        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            arrpast.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("categories");
                                String cal = jsonObject.getString("calories");
                                String time1 = jsonObject.getString("runtime");
                                String time2 = jsonObject.getString("date");

                                arrpast.add(new PastItem(0, 0, title, cal, time1, time2));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PastActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}


