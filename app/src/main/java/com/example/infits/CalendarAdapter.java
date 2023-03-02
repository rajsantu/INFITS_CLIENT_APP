package com.example.infits;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    private List<Date> dates;

    public CalendarAdapter(List<Date> dates) {
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = dates.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        holder.dayOfWeek.setText(new SimpleDateFormat("E", Locale.getDefault()).format(date).substring(0,1));
        holder.dateOfMonth.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.monthName.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(date));

        // Check if the current date being bound is today's date
        Calendar today = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            holder.itemView.setBackgroundResource(R.drawable.rounded_background);
            holder.dayOfWeek.setTextColor(Color.WHITE);
            holder.dateOfMonth.setTextColor(Color.WHITE);
            holder.monthName.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.selector_rounded_background_grey);
            holder.dayOfWeek.setTextColor(Color.BLACK);
            holder.dateOfMonth.setTextColor(Color.BLACK);
            holder.monthName.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayOfWeek;
        TextView dateOfMonth;
        TextView monthName;

        public ViewHolder(View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.day_of_week);
            dateOfMonth = itemView.findViewById(R.id.date_of_month);
            monthName = itemView.findViewById(R.id.month_name);
        }
    }
}
