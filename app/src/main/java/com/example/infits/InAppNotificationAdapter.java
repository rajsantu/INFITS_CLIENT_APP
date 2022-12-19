package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InAppNotificationAdapter extends RecyclerView.Adapter<InAppNotificationAdapter.ViewHolder> {

    private ArrayList<InAppNotificationData> data;
    private Context context;

    public InAppNotificationAdapter(ArrayList<InAppNotificationData> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_app_notification_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InAppNotificationData currData = data.get(position);

        holder.date.setText(currData.date);
        holder.time.setText(currData.time);
        holder.text.setText(currData.text);

        switch (currData.type) {
            case "step" :
                holder.vectorImg.setBackground(AppCompatResources.getDrawable(context, R.drawable.vector_steps));
                holder.cardBg.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.stepNotification));
                break;
            case "water" :
                holder.vectorImg.setBackground(AppCompatResources.getDrawable(context, R.drawable.vector_water));
                holder.cardBg.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.waterNotification));
                break;
            case "sleep" :
                holder.vectorImg.setBackground(AppCompatResources.getDrawable(context, R.drawable.vector_sleep));
                holder.cardBg.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.sleepNotification));
                break;
            case "weight" :
                holder.vectorImg.setBackground(AppCompatResources.getDrawable(context, R.drawable.vector_weight));
                holder.cardBg.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.weightNotification));
                break;
            case "calorie" :
                holder.vectorImg.setBackground(AppCompatResources.getDrawable(context, R.drawable.vector_calorie));
                holder.cardBg.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.calorieNotification));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, text, time;
        CardView cardBg;
        ImageView vectorImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.in_app_item_date);
            time = itemView.findViewById(R.id.in_app_item_time);
            text = itemView.findViewById(R.id.in_app_item_text);
            cardBg = itemView.findViewById(R.id.in_app_item_cv);
            vectorImg = itemView.findViewById(R.id.in_app_item_iv);
        }
    }
}
