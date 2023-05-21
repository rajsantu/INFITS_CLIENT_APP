package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tenclouds.gaugeseekbar.GaugeSeekBar;

public class activitySecondFragment extends Fragment {

    GaugeSeekBar progressBar;
    Button run_goal_btn,btn_start;

    public activitySecondFragment() {
        // Required empty public constructor
    }


    public static activitySecondFragment newInstance(String param1, String param2) {
        activitySecondFragment fragment = new activitySecondFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_activity_second, container, false);
        // progressBar = view.findViewById(R.id.progressBar111);
        //   progressBar.setProgress(.75f);
        Dialog dialog=new Dialog (this.getContext ());
        dialog.setContentView ( R.layout.activity_trck_popup );
        run_goal_btn=view.findViewById ( R.id.imageView74_btn );
        btn_start=view.findViewById ( R.id.imageView86 );
        run_goal_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                dialog.show ();
            }
        } );

        btn_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_running_frag1);
            }
        } );



        return view;
    }

}