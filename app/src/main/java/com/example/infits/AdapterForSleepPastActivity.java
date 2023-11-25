package com.example.infits;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterForSleepPastActivity extends RecyclerView.Adapter<AdapterForSleepPastActivity.PastVH> {
    private Context context;
    private List<SleepData> sleepDataList;
    private int color;

    public AdapterForSleepPastActivity(Context context, List<SleepData> sleepDataList, int color) {
        this.context = context;
        this.sleepDataList = sleepDataList;
        this.color = color;
    }

    @NonNull
    @Override
    public PastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater la = LayoutInflater.from(context);
        View view = la.inflate(R.layout.past_activity_layout, parent, false);


        return new PastVH(view);
    }

    public AdapterForSleepPastActivity() {
    }

    @Override
    public void onBindViewHolder(@NonNull PastVH holder, int position) {

        SleepData sleepData = sleepDataList.get(position);
        holder.steps.setText(sleepData.getHrsSlept());
        holder.date.setText(sleepData.getDate());
        holder.back.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return sleepDataList.size();
    }

//    public int calculateTotalSleepTimeForCurrentDate() {
//        int totalHours = 0;
//        int totalMinutes = 0;
//
//        // Get the current date in the format used in your 'dates' list
//        String currentDate = getCurrentDate();
//        int entryCount=0;
//
//        if (sleepDataList.isEmpty()) {
//            Log.d("DataAvailability", "No data in sleepDataList");
//            return 0; // No data available
//        }
//
//        for (SleepData sleepData : sleepDataList) {
//            Log.d("SleepData", "Date: " + sleepData.getDate() + ", HrsSlept: " + sleepData.getHrsSlept());
//            if (sleepData.getDate().equals(currentDate)) {
//                entryCount++;
//                // Parse the sleep duration (in hours and minutes) from the SleepData object
//                Log.d("SleepData", "Date: " + sleepData.getDate() + ", HrsSlept: " + sleepData.getHrsSlept());
//                String[] durationParts = sleepData.getHrsSlept().split(":");
//                if (durationParts.length == 2) {
//                    totalHours += Integer.parseInt(durationParts[0]);
//                    totalMinutes += Integer.parseInt(durationParts[1]);
//                }
//            }
//        }
//        // Calculate any overflow of minutes into hours
//        Log.d("DataAvailability", "Entries for " + currentDate + ": " + entryCount);
//        totalHours += totalMinutes / 60;
//        totalMinutes %= 60;
//
//        return totalHours * 60 + totalMinutes; // Total sleep time in minutes
//    }

//    private String getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
//        Date currentDate = new Date();
//        return dateFormat.format(currentDate);
//    }

    public class PastVH extends RecyclerView.ViewHolder {
        TextView steps, date;
        CardView back;

        public PastVH(@NonNull View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.steps_count_pa);
            date = itemView.findViewById(R.id.date_pa);
            back = itemView.findViewById(R.id.back);
        }
    }
}
