package com.example.infits;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class MealInfoAdapter extends RecyclerView.Adapter<MealInfoAdapter.ViewHolder> {

    Context context;
    List<calorieconsumedInfo> calorieconsumedInfoList;
    public MealInfoAdapter(Context context, List<calorieconsumedInfo> calorieconsumedInfoList){
        this.context=context;
        this.calorieconsumedInfoList=calorieconsumedInfoList;
    }
    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.caloriechilditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        holder.mealName.setText(calorieconsumedInfoList.get(position).mealName);
        holder.mealIcon.setImageDrawable(context.getDrawable(calorieconsumedInfoList.get(position).mealIcon));
        holder.mealcalorieValue.setText(calorieconsumedInfoList.get(position).mealcalorieValue);
        holder.mealQuantity.setText(calorieconsumedInfoList.get(position).mealQuantity);
        holder.mealSize.setText(calorieconsumedInfoList.get(position).mealSize);
        holder.mealTime.setText(calorieconsumedInfoList.get(position).mealTime);
        String mealType=calorieconsumedInfoList.get(position).mealType;
        String color="";
        switch (mealType){
            case "BreakFast":
                color="#FF6262";
                break;
            case "Lunch":
                color="#FFA361";
                break;
            case "Dinner":
                color="#F9FC88";
                break;
            case "Snacks":
                color="#ACAFFD";
                break;
        }
        holder.lineView.setBackgroundColor(Color.parseColor(color));
        Drawable drawable=context.getDrawable(R.drawable.concentrci_circle);
        drawable.setTint(Color.parseColor(color));
        holder.concentricCircleView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return calorieconsumedInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mealName,mealcalorieValue,mealQuantity,mealSize,mealTime;
        ImageView mealIcon,concentricCircleView;
        View lineView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mealName=itemView.findViewById(R.id.mealName);
            mealcalorieValue=itemView.findViewById(R.id.mealcalorieValue);
            mealQuantity=itemView.findViewById(R.id.mealQuantity);
            mealSize=itemView.findViewById(R.id.mealSize);
            mealTime=itemView.findViewById(R.id.mealTime);
            mealIcon=itemView.findViewById(R.id.mealIcon);
            concentricCircleView=itemView.findViewById(R.id.concentricCircleView);
            lineView=itemView.findViewById(R.id.lineView);
        }
    }
}
