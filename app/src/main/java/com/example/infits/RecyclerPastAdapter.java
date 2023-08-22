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

public class RecyclerPastAdapter extends RecyclerView.Adapter<RecyclerPastAdapter.ViewHolder> {
    Context context;
    ArrayList<PastItem> arrPast;
    RecyclerPastAdapter(Context context, ArrayList<PastItem> arrPast){
        this.context = context;
        this.arrPast = arrPast;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.past_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img1.setImageResource(arrPast.get(position).img1);
        holder.img2.setImageResource(arrPast.get(position).img2);
        holder.title.setText(arrPast.get(position).title);
        holder.cal.setText(arrPast.get(position).cal);
        holder.time1.setText(arrPast.get(position).time1);
        holder.time2.setText(arrPast.get(position).time2);
    }

    @Override
    public int getItemCount() {
        return arrPast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, cal, time1, time2;
        ImageView img1, img2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            title = itemView.findViewById(R.id.title);
            cal = itemView.findViewById(R.id.cal);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);

        }
    }
}
