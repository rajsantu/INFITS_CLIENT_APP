package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class fragment_diet_chart extends Fragment implements PopupMenu.OnMenuItemClickListener {
    GaugeSeekBar progressBar;

    ImageView back;
   Button btn_meal_check,btn_meal;

   Button diet_choose_top_btn;
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

        diet_choose_top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //diet_choose_top_btn.setText();
                showPopup(v);
            }
        });

        return view;
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(getContext(),v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.diet_popup_menu);
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                diet_choose_top_btn.setText("Daily");
                return true;
            case R.id.item2:
                diet_choose_top_btn.setText("Weekly");
                return true;
            case R.id.item3:
                diet_choose_top_btn.setText("Monthly");
                return true;
            case R.id.item4:
                diet_choose_top_btn.setText("Yearly");
                return true;
            default:
                diet_choose_top_btn.setText("Choose");
                return false;
        }
    }

    private void hooks(View view) {
        btn_meal_check=view.findViewById (R.id.button6);
        back=view.findViewById(R.id.imageView8);
        progressBar = view.findViewById(R.id.progressBar);
        btn_meal=view.findViewById(R.id.button8);
        diet_choose_top_btn=view.findViewById(R.id.diet_btn_daily);
    }
}