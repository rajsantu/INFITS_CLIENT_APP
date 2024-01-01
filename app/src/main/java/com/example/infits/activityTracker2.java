package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class activityTracker2 extends Fragment {

    RecyclerView recyclerView;
    List<Date> dateList;
    CalAdapter_at adapter;
    ImageView running_img,walking_img,cycling_img,jumprope_img,trekking_img,skating_img,dancing_img,stair_climbing;
    ImageButton backbutton;

    public activityTracker2 () {
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
        View view = inflater.inflate ( R.layout.fragment_activity_tracker2 , container , false);
        Date today = new Date();
        dateList = new ArrayList<> ();

        // Add some test data to the dateList
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        dateList.add(today);
        //  imageView=view.findViewById ( R.id.image_honey_pancake );

        for (int i = 0; i < 500; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendar.getTime());
        }

        recyclerView = view.findViewById(R.id.date_recyclerView);

        adapter = new CalAdapter_at(dateList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper ();
        snapHelper.attachToRecyclerView(recyclerView);

        // imageView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_diet_fourth2_to_fragment_diet_third_scrn));

        running_img=view.findViewById ( R.id.imageView79 );
        cycling_img=view.findViewById ( R.id.imageView73 );
        walking_img=view.findViewById ( R.id.imageView82 );
        jumprope_img=view.findViewById ( R.id.imageView85 );
        skating_img=view.findViewById ( R.id.imageView89 );
        trekking_img=view.findViewById ( R.id.imageView99 );
        dancing_img=view.findViewById ( R.id.imageView105 );
        stair_climbing=view.findViewById ( R.id.imageView101);
        backbutton=view.findViewById(R.id.activitytrackerimgback);

        running_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activitySecondFragment);
        });
        cycling_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activitythirdfragment);
        });
        walking_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activityfourthfragment);
        });
        jumprope_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activityRopeJump);
        });
        skating_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activitySkating);
        });
        trekking_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activityTrekking);
        });
        stair_climbing.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activityStairClimbing);
        });
        dancing_img.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_activityDancing);
        });
        backbutton.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_activityTracker2_to_dashBoardFragment);
        });

        return  view;
    }
}