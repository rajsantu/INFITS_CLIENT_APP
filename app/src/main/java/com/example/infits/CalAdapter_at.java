package com.example.infits;
import android.content.Context;
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
//public class CalAdapter_at {
//}
class CalAdapter_at  extends RecyclerView.Adapter< CalAdapter_at .ViewHolder> {

    private List<Date> dates;
    private Date selectedDate;

    OnDateSelectedListener listener;

    public  CalAdapter_at  ( List<Date> dates ) {
        this.dates = dates;
    }

    public  CalAdapter_at ( Context context , List<Date> dates , OnDateSelectedListener listener ) {
        // ...
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.calendar_item , parent , false );
        return new ViewHolder ( view );


    }

    @Override
    public void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
        Date date = dates.get ( position );
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime ( date );
        holder.dayOfWeek.setText ( new SimpleDateFormat ( "E" , Locale.getDefault () ).format ( date ).substring ( 0 , 1 ) );
        holder.dateOfMonth.setText ( String.valueOf ( calendar.get ( Calendar.DAY_OF_MONTH ) ) );
        holder.monthName.setText ( new SimpleDateFormat ( "MMM" , Locale.getDefault () ).format ( date ) );


        // Check if the current date being bound is today's date
        Calendar today = Calendar.getInstance ();
        if (selectedDate != null && selectedDate.equals ( date )) {
            holder.itemView.setBackgroundResource ( R.drawable.cal_bg_atracker);
            holder.dayOfWeek.setTextColor ( Color.WHITE );
            holder.dateOfMonth.setTextColor ( Color.WHITE );
            holder.monthName.setTextColor ( Color.WHITE );
        } else if (selectedDate == null && calendar.get ( Calendar.YEAR ) == today.get ( Calendar.YEAR )
                && calendar.get ( Calendar.MONTH ) == today.get ( Calendar.MONTH )
                && calendar.get ( Calendar.DAY_OF_MONTH ) == today.get ( Calendar.DAY_OF_MONTH )) {
            holder.itemView.setBackgroundResource ( R.drawable.cal_bg_atracker );
            holder.dayOfWeek.setTextColor ( Color.WHITE );
            holder.dateOfMonth.setTextColor ( Color.WHITE );
            holder.monthName.setTextColor ( Color.WHITE );
        } else {
            holder.itemView.setBackgroundResource ( R.drawable.btnbg_gray );
            holder.dayOfWeek.setTextColor ( Color.BLACK );
            holder.dateOfMonth.setTextColor ( Color.BLACK );
            holder.monthName.setTextColor ( Color.BLACK );
        }
    }


    @Override
    public int getItemCount () {

        return dates.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayOfWeek;
        TextView dateOfMonth;
        TextView monthName;

        public ViewHolder ( View itemView ) {
            super ( itemView );
            dayOfWeek = itemView.findViewById ( R.id.day_of_week );
            dateOfMonth = itemView.findViewById ( R.id.date_of_month );
            monthName = itemView.findViewById ( R.id.month_name );

            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick ( View v ) {
                    selectedDate = dates.get ( getAdapterPosition () );
                    notifyDataSetChanged ();
                    if (listener != null) {
                        listener.onDateSelected ( selectedDate );
                    }
                }
            } );
        }
    }

    public String getSelectedDate () {
        // Update selectedDate to current time if it's null
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance ().getTime ();
        }

        // Format the selected date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat ( "EEEE, MMMM d, yyyy" , Locale.getDefault () );
        String formattedDate = formatter.format ( selectedDate );

        return formattedDate;
    }

    public interface OnDateSelectedListener {
        void onDateSelected ( Date date );
    }


}