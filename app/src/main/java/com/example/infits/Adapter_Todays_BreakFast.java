package com.example.infits;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.PublicKey;
import java.util.ArrayList;

public class Adapter_Todays_BreakFast extends RecyclerView.Adapter<Adapter_Todays_BreakFast.ViewHolder> {

    Context context;
    ArrayList<Todays_BreakFast_info> todays_breakFast_infos;

    public Adapter_Todays_BreakFast(Context context,ArrayList<Todays_BreakFast_info> todays_breakFast_infos){
        this.todays_breakFast_infos=todays_breakFast_infos;
        this.context=context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.todays_breakfast_detail,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.icon.setImageDrawable(todays_breakFast_infos.get(position).icon);
//        holder.icon.setImageBitmap(todays_breakFast_infos.get(position).icon);
        holder.mealName.setText(todays_breakFast_infos.get(position).mealName);
        holder.calorieValue.setText(todays_breakFast_infos.get(position).calorieValue);
        holder.fatvalue.setText(todays_breakFast_infos.get(position).fatvalue);
        holder.carbsValue.setText(todays_breakFast_infos.get(position).carbsValue);
        holder.quantityValue.setText(todays_breakFast_infos.get(position).quantityValue);
        holder.sizeValue.setText(todays_breakFast_infos.get(position).sizeValue);
    }

    @Override
    public int getItemCount() {
        return todays_breakFast_infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView mealName, calorieValue, fatvalue, protinValue, carbsValue,  quantityValue, sizeValue;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            icon=itemView.findViewById(R.id.mealImage);
            mealName=itemView.findViewById(R.id.mealName);
            calorieValue=itemView.findViewById(R.id.calorieValue);
            fatvalue=itemView.findViewById(R.id.fatValue);
            protinValue=itemView.findViewById(R.id.protinValue);
            carbsValue=itemView.findViewById(R.id.carbsValue);
            quantityValue=itemView.findViewById(R.id.quantityValue);
            sizeValue=itemView.findViewById(R.id.sizeValue);
        }
    }
}
