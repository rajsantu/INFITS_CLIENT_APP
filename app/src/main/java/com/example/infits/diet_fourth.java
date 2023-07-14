package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.*;

//import sun.bob.mcalendarview.adapters.CalendarAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link diet_fourth#newInstance} factory method to
 * create an instance of this fragment.
 */
public class diet_fourth extends Fragment {
     RecyclerView recyclerView;
     List<Date> dateList;
    CalAdapter adapter;

    ImageView back_btn,view_meal;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public diet_fourth() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment diet_fourth.
     */
    // TODO: Rename and change types and number of parameters
    public static diet_fourth newInstance(String param1, String param2) {
        diet_fourth fragment = new diet_fourth();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_diet_fourth , container , false );
        hooks(view);

        back_btn.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_diet_fourth2_to_fragment_diet_chart));

        view_meal.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_diet_fourth2_to_fragment_diet_third_scrn));

        Date today = new Date();
        dateList = new ArrayList<>();

        // Add some test data to the dateList
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        recyclerView = view.findViewById(R.id.date_recyclerView);

        adapter = new CalAdapter(dateList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        return inflater.inflate(R.layout.fragment_diet_fourth, container, false);
    }

    private void hooks(View view) {
        back_btn = view.findViewById(R.id.back_btn);
        view_meal = view.findViewById(R.id.image);
    }
}
