package com.example.infits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class NumberedAdapter extends RecyclerView.Adapter<NumberedAdapter.ViewHolder> {

    private List<String> mData;
//    private int mInterval;
//    private static int mSelectedPosition = -1;

    public NumberedAdapter(List<String> data) {
        mData = data;
//        mInterval = interval;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
//        int positionInList = position % mData.size();
//        int minutesValue = positionInList * mInterval;
//        holder.bind(String.valueOf(minutesValue));

//        if(position > mData.size()){
//            position = position% mData.size();
//        }

        String number = mData.get(position%mData.size());

        holder.bind(number);

//        mSelectedPosition = holder.getLayoutPosition();
    }


    @Override
    public int getItemCount() {

//        return mData.size();

        return Integer.MAX_VALUE;
    }

    public String getNumber(int selectedPosition) {
        return mData.get(selectedPosition%mData.size());
//        return ""+selectedPosition;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDataTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDataTextView = itemView.findViewById(R.id.hours_textView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getLayoutPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        int oldSelectedPosition = mSelectedPosition;
//                        mSelectedPosition = position;
//                        notifyItemChanged(oldSelectedPosition);
//                        notifyItemChanged(mSelectedPosition);
//                    }
//                }
//            });
        }

        public void bind(String data) {
            mDataTextView.setText(data);
        }


    }
}