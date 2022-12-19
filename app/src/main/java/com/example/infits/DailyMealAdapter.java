package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyMealAdapter extends RecyclerView.Adapter<DailyMealAdapter.viewHolder> {
    ArrayList<DailyMeal> dailyMeals;
    Context context;

    public DailyMealAdapter(ArrayList<DailyMeal> dailyMeals, Context context) {
        this.dailyMeals = dailyMeals;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dailymeals,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DailyMeal currMeal = dailyMeals.get(position);

        String timeText = getTimeText(currMeal.time);

        holder.time.setText(timeText);
        holder.name.setText(currMeal.name);
        holder.amount.setText(currMeal.calorie);
    }

    private String getTimeText(String time) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String timeText = "";
        try {
            Date mealTime = parser.parse(time);
            Date twelvePM = parser.parse("12:00");
            Date sixPM = parser.parse("18:00");

            assert mealTime != null;
            if(mealTime.before(twelvePM)) {
                timeText = "Breakfast";
            } else if(mealTime.before(sixPM)) {
                timeText = "Lunch";
            } else {
                timeText = "Dinner";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeText;
    }

    @Override
    public int getItemCount() {
        return dailyMeals.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView time,name ,amount,totalAmount;
        ImageView image;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            image = itemView.findViewById(R.id.image);
        }
    }
}
