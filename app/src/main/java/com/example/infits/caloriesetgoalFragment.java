package com.example.infits;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link caloriesetgoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class caloriesetgoalFragment extends Fragment {

    SeekBar calorieconsume,calorieburn,carbs,fiber,protein,fat;
    TextView calconsumeTV,calBurnTV,carbTV,fiberTV,proteinTV,fatTV;
    CardView setgoal;

    ImageView confirmed;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public caloriesetgoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment caloriesetgoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static caloriesetgoalFragment newInstance(String param1, String param2) {
        caloriesetgoalFragment fragment = new caloriesetgoalFragment();
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
        //return inflater.inflate(R.layout.fragment_caloriesetgoal, container, false);
        View view = inflater.inflate(R.layout.fragment_caloriesetgoal, container, false);
        hooks(view);

        calorieconsume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calconsumeTV.setText(progress+" Kcal");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calorieburn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calBurnTV.setText(progress+" Kcal");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        carbs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                carbTV.setText(progress+" g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        fiber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fiberTV.setText(progress+" g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        protein.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                proteinTV.setText(progress+" g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        fat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fatTV.setText(progress+" g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmed.setVisibility(View.VISIBLE);
                setgoal.setVisibility(View.GONE);
            }
        });
        confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmed.setVisibility(View.GONE);
                setgoal.setVisibility(View.VISIBLE);
            }
        });



        return view;
    }

    private void hooks(View view) {
        //All TextViews
        calconsumeTV = view.findViewById(R.id.setgoalcalorieconsumeTV);
        calBurnTV = view.findViewById(R.id.setgoalcalorieburnTV);
        carbTV =  view.findViewById(R.id.setgoalcarbsTV);
        fiberTV = view.findViewById(R.id.setgoalfiberTV);
        proteinTV = view.findViewById(R.id.setgoalProteinTV);
        fatTV = view.findViewById(R.id.setgoalfatTV);

        //All Seekbars
        calorieconsume = view.findViewById(R.id.calorieconsumeseekbar);
        calorieburn = view.findViewById(R.id.calorieburnseekbar);
        carbs = view.findViewById(R.id.carbsseekbar);
        fiber = view.findViewById(R.id.fiberseekbar);
        protein = view.findViewById(R.id.proteinseekbar);
        fat = view.findViewById(R.id.fatseekbar);


        setgoal = view.findViewById(R.id.caloriesetgoal);
        confirmed = view.findViewById(R.id.calsetgoalconfirmed);

    }
}