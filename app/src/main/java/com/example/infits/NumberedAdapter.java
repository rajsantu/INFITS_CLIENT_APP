package com.example.infits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter<NumberedAdapter.ViewHolder>{

    private List<String> mData;
    private boolean mIsHours;

    public NumberedAdapter(List<String> data, boolean isHours) {
        mData = data;
        mIsHours = isHours;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mIsHours) {
            holder.bind(String.valueOf(position + 1));
        } else {
            holder.bind(String.valueOf(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDataTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDataTextView = itemView.findViewById(R.id.hours_textView);
        }

        public void bind(String data) {
            mDataTextView.setText(data);
        }
    }
}
