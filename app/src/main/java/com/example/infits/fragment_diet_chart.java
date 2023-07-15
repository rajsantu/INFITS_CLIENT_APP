package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class fragment_diet_chart extends Fragment {
    GaugeSeekBar progressBar;

    ImageView back;
   Button btn_meal_check;



   //for spinner
    Spinner dailySpinner,btn_meal;
    public fragment_diet_chart () {
        // Required empty public constructor
    }



    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_diet_chart , container , false );
        hooks(view);

        //set daily weekly and yearly in spinner
        ArrayAdapter<CharSequence> dailyAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.spinner_diet_daily,R.layout.diet_spinner_dropdown_item );
        dailyAdapter.setDropDownViewResource(R.layout.diet_spinner_layout);
        dailySpinner.setAdapter(dailyAdapter);

        //set breakfast snacks lunch Dinner
        ArrayAdapter<CharSequence> breakfastAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.spinner_diet_breakfast,R.layout.diet_spinner_dropdown_item );
        breakfastAdapter.setDropDownViewResource(R.layout.diet_spinner_layout);
        btn_meal.setAdapter(breakfastAdapter);

        back.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_dashBoardFragment));

        progressBar.setProgress(.75f);
      ///  btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_stepReminderFragment));

        btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_fourth2));




        return view;
    }




    private void hooks(View view) {
        btn_meal_check=view.findViewById (R.id.button6);
        back=view.findViewById(R.id.imageView8);
        progressBar = view.findViewById(R.id.progressBar);
        //for spinner
        dailySpinner = view.findViewById(R.id.diet_btn_daily);
        btn_meal=view.findViewById(R.id.button8);
    }
}