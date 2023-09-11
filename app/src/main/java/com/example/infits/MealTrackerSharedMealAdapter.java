package com.example.infits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//public class MealTrackerSharedMealAdapter {
//}
public class MealTrackerSharedMealAdapter extends RecyclerView.Adapter<MealTrackerSharedMealAdapter.ViewHolder> {
    private List<addmealInfo> itemList;
    ArrayList<String> mealInfotransfer;
    Context context;

    public MealTrackerSharedMealAdapter(Context context, List<addmealInfo> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shared_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        SharedMeal item = itemList.get(position);
        String calorie = itemList.get(position).mealcalorie;
        String newcal = calorie.replace(" kcal","");
//        Log.d("TAG", "onBindViewHolder: " +name);
        holder.name.setText(itemList.get(position).mealname);
        holder.streak.setText(newcal);
        holder.time.setText(itemList.get(position).time);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "clicked for "+name, Toast.LENGTH_SHORT).show();
                mealInfotransfer = new ArrayList<>();
                Bundle bundle=new Bundle();
                int icon=itemList.get(position).mealIocn;
                String Meal_Name=itemList.get(position).mealname;
                String Meal_type=itemList.get(position).mealType;
                String calorie=itemList.get(position).mealcalorie;
                String carbs=itemList.get(position).carb;
                String protin=itemList.get(position).protein;
                String fat=itemList.get(position).fat;
//                String fiber = addmealInfos.get(position).getFiber();
                mealInfotransfer.add(Meal_Name);
                mealInfotransfer.add(Meal_type);
                mealInfotransfer.add(calorie);
                mealInfotransfer.add(carbs);
                mealInfotransfer.add(protin);
                mealInfotransfer.add(fat);
                mealInfotransfer.add(String.valueOf(icon));
                mealInfotransfer.add("1");
                Log.d("mealInfotransfer",mealInfotransfer.toString());

                bundle.putStringArrayList("mealInfotransfer",mealInfotransfer);
                Intent intent = new Intent(context, Activity_Todays_Breakfast.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bundle",bundle);
                intent.putExtra("fragment", "mealinfowithphoto");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.d("TAG", "itemcount: "+ itemList.size());
        return itemList.size();
//        return 3;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,streak,time;
        private CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            streak = itemView.findViewById(R.id.streak);
            time = itemView.findViewById(R.id.time);
            cv = itemView.findViewById(R.id.click);
        }
    }
}

