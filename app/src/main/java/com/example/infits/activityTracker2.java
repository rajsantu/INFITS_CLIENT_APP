package com.example.infits;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class activityTracker2 extends Fragment {

    private ArrayList<ActivityItem> arrItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<Date> dateList;
    private CalAdapter_at adapter;

    public activityTracker2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_tracker2, container, false);

        // Set up recyclerView2 and populate arrItems
        recyclerView2 = view.findViewById(R.id.constraintLayout2);
        recyclerView2.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        arrItems.add(new ActivityItem(R.drawable.runningimg, "Running", R.drawable.arrow_right));
        arrItems.add(new ActivityItem(R.drawable.cycling_img, "Cycling", R.drawable.arrow_right));
        arrItems.add(new ActivityItem(R.drawable.walkingimg, "Walking", R.drawable.arrow_right));

        ActivityAdapter activityAdapter = new ActivityAdapter(requireContext(), arrItems);
        recyclerView2.setAdapter(activityAdapter);

        // Set up recyclerView and populate dateList
        recyclerView = view.findViewById(R.id.date_recyclerView);
        Date today = new Date();
        dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        adapter = new CalAdapter_at(dateList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Set OnClickListener for textView52
        TextView viewAllTextView = view.findViewById(R.id.textView52);
        viewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to navigate to the activity_past.xml activity
                Intent intent = new Intent(requireContext(), PastActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
