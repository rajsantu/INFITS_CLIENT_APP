package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class activityfourthfragment extends Fragment {



    Button btn_setgoal,btn_start;

    public activityfourthfragment() {
        // Required empty public constructor
    }


    public static activityfourthfragment newInstance(String param1, String param2) {
        activityfourthfragment fragment = new activityfourthfragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_activityfourthfragment, container, false);


        Dialog dialog=new Dialog (this.getContext ());
        dialog.setContentView ( R.layout.activity_trck_popup );
        btn_setgoal=view.findViewById ( R.id.imageView74 );
        btn_start=view.findViewById ( R.id.imageView86_walk_start);
        btn_setgoal.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                dialog.show ();
            }
        } );


        btn_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Navigation.findNavController(v).navigate(R.id. action_activityfourthfragment_to_walking_frag2);
            }
        } );



        return view;
    }
}