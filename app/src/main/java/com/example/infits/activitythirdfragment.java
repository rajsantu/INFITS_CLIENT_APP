package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class activitythirdfragment extends Fragment {

    Button btn_setgoal, btn_start_trd;
    ImageView back_button;

    public activitythirdfragment() {
        // Required empty public constructor
    }


    public static activitythirdfragment newInstance(String param1, String param2) {
        activitythirdfragment fragment = new activitythirdfragment();
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
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_activitythirdfragment, container, false);


        Dialog dialog=new Dialog (this.getContext ());
        dialog.setContentView ( R.layout.activity_trck_popup );
        btn_start_trd=view.findViewById ( R.id.imageView86_trd);
        btn_setgoal=view.findViewById ( R.id.imageView74 );
        back_button=view.findViewById( R.id.imageView73);
        btn_setgoal.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                dialog.show ();
            }
        } );

        btn_start_trd.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Navigation.findNavController(v).navigate(R.id.action_activitythirdfragment_to_cycling_frag2);
            }
        } );

        back_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Navigation.findNavController(v).navigate(R.id.action_activitythirdfragment_to_activityTracker2);
            }
        } );

        return view;

    }
}