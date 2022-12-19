package com.example.infits;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    ArrayList<FoodItem> foodItems;
    Context context;

    public FoodItemAdapter(ArrayList<FoodItem> foodItems, Context context) {
        this.foodItems = foodItems;
        this.context = context;
    }

    public void setFilteredList(ArrayList<FoodItem> filterList) {
        this.foodItems = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_detail_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem currFood = foodItems.get(position);

        String calorieText = currFood.calorie + " kcal";

        holder.name.setText(currFood.name);
        holder.calorie.setText(calorieText);

        holder.add.setOnClickListener(v -> {
            // update in database
            updateDailyMeals(currFood);
        });
    }

    private void updateDailyMeals(FoodItem foodItem) {
        String dailyMealsUrl = String.format("%supdateDailyMeals.php", DataFromDatabase.ipConfig);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);

        StringRequest dailyMealsRequest = new StringRequest(
                Request.Method.POST,
                dailyMealsUrl,
                response -> {
                    Log.d("FoodItemAdapter", response);
                    ((Activity) context).onBackPressed();
                },
                error -> Log.e("FoodItemAdapter", error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("name", foodItem.name);
                data.put("calorie", foodItem.calorie);
                data.put("protein", foodItem.protein);
                data.put("fibre", foodItem.fibre);
                data.put("carb", foodItem.carb);
                data.put("fat", foodItem.fat);
                data.put("time", time);
                data.put("goal", "2000");

                return data;
            }
        };
        Volley.newRequestQueue(context).add(dailyMealsRequest);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, calorie;
        CardView add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            calorie = itemView.findViewById(R.id.kcal);
            add = itemView.findViewById(R.id.plus);
        }
    }
}
