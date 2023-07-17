package com.example.infits;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.TextView;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class fragment_diet_chart extends Fragment {
    GaugeSeekBar progressBar,progressBarProtein,progressBarCarbs,progressBarFats;

    ImageView back,gaugeSeekMiddleIcon;
   Button btn_meal_check;

    //for calories protein carbs fats click listener
    AppCompatButton btn_calories,btn_proteins,btn_carbs,btn_fats;
    TextView gaugeSeekMiddleText,gaugeSeekMiddleTextValue,gaugeSeekMiddleTextUnit;

   //for spinner
    Spinner dailySpinner,btn_meal;
    public fragment_diet_chart () {
        // Required empty public constructor
    }



    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

    }

    @SuppressLint("UseCompatLoadingForDrawables")
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

        //for calorie button click listener
        btn_calories.setOnClickListener(view1 -> {
            btn_proteins.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_calories.setBackground(getResources().getDrawable(R.drawable.btn_bg_calorie));
            btn_carbs.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_fats.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            gaugeSeekMiddleIcon.setBackground(getResources().getDrawable(R.drawable.fire_diet_chart));
            gaugeSeekMiddleText.setText("Calories");
            gaugeSeekMiddleTextUnit.setText("kcal");
            gaugeSeekMiddleTextValue.setText("1050");
            progressBarCarbs.setVisibility(View.INVISIBLE);
            progressBarProtein.setVisibility(View.INVISIBLE);
            progressBarFats.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        });

        //for protein button click listener
        btn_proteins.setOnClickListener(view1 -> {
            btn_proteins.setBackground(getResources().getDrawable(R.drawable.btn_bg_protein));
            btn_calories.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_carbs.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_fats.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            gaugeSeekMiddleIcon.setBackground(getResources().getDrawable(R.drawable.fish));
            gaugeSeekMiddleText.setText("Protein");
            gaugeSeekMiddleTextUnit.setText("g");
            gaugeSeekMiddleTextValue.setText("256");
            progressBar.setVisibility(View.INVISIBLE);
            progressBarCarbs.setVisibility(View.INVISIBLE);
            progressBarFats.setVisibility(View.INVISIBLE);
            progressBarProtein.setVisibility(View.VISIBLE);

        });

        //for carbs click listener
        btn_carbs.setOnClickListener(view1 -> {
            btn_proteins.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_calories.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_carbs.setBackground(getResources().getDrawable(R.drawable.btn_bg_carbs));
            btn_fats.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            gaugeSeekMiddleIcon.setBackground(getResources().getDrawable(R.drawable.carbs_diet_chart));
            gaugeSeekMiddleText.setText("Carbs");
            gaugeSeekMiddleTextUnit.setText("g");
            gaugeSeekMiddleTextValue.setText("512");
            progressBar.setVisibility(View.INVISIBLE);
            progressBarProtein.setVisibility(View.INVISIBLE);
            progressBarFats.setVisibility(View.INVISIBLE);
            progressBarCarbs.setVisibility(View.VISIBLE);
        });

        //for fats click listener
        btn_fats.setOnClickListener(view1 -> {
            btn_proteins.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_calories.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_carbs.setBackground(getResources().getDrawable(R.drawable.protien_bg));
            btn_fats.setBackground(getResources().getDrawable(R.drawable.btn_bg_fats));
            gaugeSeekMiddleIcon.setBackground(getResources().getDrawable(R.drawable.fats));
            gaugeSeekMiddleText.setText("Fats");
            gaugeSeekMiddleTextUnit.setText("g");
            gaugeSeekMiddleTextValue.setText("200");
            progressBar.setVisibility(View.INVISIBLE);
            progressBarProtein.setVisibility(View.INVISIBLE);
            progressBarCarbs.setVisibility(View.INVISIBLE);
            progressBarFats.setVisibility(View.VISIBLE);
        });

        back.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_dashBoardFragment));

        progressBar.setProgress(.75f);
      ///  btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_stepReminderFragment));

        btn_meal_check.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_chart_to_diet_fourth2));




        return view;
    }




    private void hooks(View view) {
        btn_meal_check=view.findViewById (R.id.button6);
        back=view.findViewById(R.id.imageView8DietChart);
        progressBar = view.findViewById(R.id.progressBarDietChart);
        dailySpinner = view.findViewById(R.id.diet_btn_daily);
        btn_meal=view.findViewById(R.id.button8);
        btn_calories = view.findViewById(R.id.button);
        btn_proteins = view.findViewById(R.id.button3);
        btn_carbs = view.findViewById(R.id.button4DietChart);
        btn_fats = view.findViewById(R.id.button5DietChart);
        gaugeSeekMiddleIcon = view.findViewById(R.id.view);
        gaugeSeekMiddleText = view.findViewById(R.id.textView4DietChart);
        gaugeSeekMiddleTextValue = view.findViewById(R.id.textView5DietChart);
        gaugeSeekMiddleTextUnit = view.findViewById(R.id.textView6DietChart);
        progressBarProtein = view.findViewById(R.id.progressBarProteinDietChart);
        progressBarCarbs = view.findViewById(R.id.progressBarCarbsDietChart);
        progressBarFats = view.findViewById(R.id.progressBarFatsDietChart);
    }
}