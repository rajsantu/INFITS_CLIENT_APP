package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    Context context;
    ArrayList<ActivityItem> arrItems;

    ActivityAdapter(Context context, ArrayList<ActivityItem> arrItems){
        this.context = context;
        this.arrItems = arrItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView80.setImageResource(arrItems.get(position).imageResource);
        holder.textView84.setText(arrItems.get(position).title);
        holder.imageView81.setImageResource(arrItems.get(position).arrowImageResource);

    }

    @Override
    public int getItemCount() {
        return arrItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView84;
        ImageView imageView80, imageView81;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView84 = itemView.findViewById(R.id.textView84);
            imageView80 = itemView.findViewById(R.id.imageView80);
            imageView81 = itemView.findViewById(R.id.imageView81);

        }
    }
}
