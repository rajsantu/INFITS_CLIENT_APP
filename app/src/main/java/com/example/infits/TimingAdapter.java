package com.example.infits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimingAdapter extends RecyclerView.Adapter<TimingAdapter.CustomViewHolder> {
    private List<String> stringList;

    public TimingAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.timing_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        String data = stringList.get(position % stringList.size());
        holder.textView.setText(data);
    }

    @Override
    public int getItemCount() {

        return stringList.size();

//        return Integer.MAX_VALUE;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.timing_textView);
        }
    }
}