package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class fragment_diet_chart extends Fragment {
    GaugeSeekBar progressBar;

   Button btn_meal_check;
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


//        btn_meal_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_second);
//            }
//        });
//      btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_second));
//       btn_meal_check.setOnClickListener ( new View.OnClickListener () {
//           @Override
//           public void onClick ( View view ) {
//               Navigation.findNavController ( view ).navigate ( R.id.action_fragment_diet_chart_to_diet_second);
//           }
//       } );

        progressBar = view.findViewById(R.id.progressBar);
        btn_meal_check=view.findViewById ( R.id.button6 );
        progressBar.setProgress(.75f);
      ///  btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_stepReminderFragment));

        btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_fourth2));

        return view;
    }
//    private void hooks(View view) {
//        btn_meal_check=view.findViewById ( R.id.button6 );
//    }
}