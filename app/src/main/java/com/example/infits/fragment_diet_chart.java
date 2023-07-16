package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import java.util.ArrayList;

public class fragment_diet_chart extends Fragment implements  AdapterView.OnItemSelectedListener{
    GaugeSeekBar progressBar;

    ImageView back;
   Button btn_meal_check,btn_meal;
    String spinnerItem;

   Spinner customSpinner;

    ArrayList<CustomItem> customList;
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


        customList = getCustomList();
        CustomeAdapterSpinner adapter = new CustomeAdapterSpinner(getContext(), customList);

        if (customSpinner != null) {
            customSpinner.setAdapter(adapter);
            customSpinner.setOnItemSelectedListener(this);
        }

        customSpinner.post(new Runnable() {
            @Override
            public void run() {
                int width = customSpinner.getWidth();
                customSpinner.setDropDownWidth(width);
            }
        });

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomItem selectedItem = (CustomItem) parent.getItemAtPosition(position);
                spinnerItem = selectedItem.getSpinnerItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private ArrayList<CustomItem> getCustomList() {
        customList = new ArrayList<>();

        customList.add(new CustomItem("Dailt"));
        customList.add(new CustomItem("Weekly"));
        customList.add(new CustomItem("Monthly"));
        customList.add(new CustomItem("Yearly"));

        return customList;
    }


    private void hooks(View view) {
        btn_meal_check=view.findViewById (R.id.button6);
        back=view.findViewById(R.id.imageView8);
        progressBar = view.findViewById(R.id.progressBar);
        btn_meal=view.findViewById(R.id.button8);
        customSpinner=view.findViewById(R.id.diet_top_spinner);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CustomItem item = (CustomItem) parent.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}