package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class fragment_diet_chart extends Fragment {
    GaugeSeekBar progressBar;

    ImageView back;
   Button btn_meal_check,btn_meal;
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

        back.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_dashBoardFragment));

        progressBar.setProgress(.75f);
      ///  btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_stepReminderFragment));

        btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_fourth2));

        btn_meal.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_second_frag));

        return view;
    }
    private void hooks(View view) {
        btn_meal_check=view.findViewById (R.id.button6);
        back=view.findViewById(R.id.imageView8);
        progressBar = view.findViewById(R.id.progressBar);
        btn_meal=view.findViewById(R.id.button8);
    }
}