package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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

        Dialog dialog=new Dialog (this.getContext ());
        dialog.setContentView ( R.layout.activity_trck_popup );
        run_goal_btn =view.findViewById ( R.id.imageView74_btn );
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

                String goal = "5";
                String url=String.format("%srunningTrackerSetGoal.php",DataFromDatabase.ipConfig);

                StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
                    if (response.equals("updated")){
                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(response);
                        Toast.makeText(getActivity(), "Not working" + String.valueOf(response), Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> data = new HashMap<>();
                        LocalDateTime now = LocalDateTime.now();// gets the current date and time
                        DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                        data.put("client_id",DataFromDatabase.client_id);
                        data.put("clientuserID",DataFromDatabase.clientuserID);
                        data.put("distance","0");
                        data.put("calories", "0");
                        data.put("runtime", "0");
                        data.put("duration","0");
                        data.put("dateandtime",DTF.format(now));
                        data.put("goal", goal);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        data.put("date",dtf.format(now));
                        return data;
                    }
                };

                Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
                Toast.makeText(getActivity(),"check 1",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_running_frag1);
            }
        } );

        return view;
    }

}