package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import org.joda.time.LocalDateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activitySecondFragment extends Fragment {

    GaugeSeekBar progressBar;
    Button run_goal_btn,btn_start;
    TextView textView71, textView72, textView73;
    ImageView back_button;

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
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.activity_trck_popup);
        run_goal_btn = view.findViewById(R.id.imageView74_btn);
        btn_start = view.findViewById(R.id.imageView86);
        textView71 = view.findViewById(R.id.textView71);
        textView72 = view.findViewById(R.id.textView72);
        textView73 = view.findViewById(R.id.textView73);
        back_button=view.findViewById(R.id.imageView73);
        run_goal_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                dialog.show ();
            }
        } );

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_activityTracker2);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goal = "5";
                String url = "https://infits.in/androidApi/runningTracker.php";

                StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String calories = jsonObject.getString("calories");
                        String distance = jsonObject.getString("distance");
                        String runtime = jsonObject.getString("runtime");

                        textView71.setText(calories);
                        textView72.setText(distance);
                        textView73.setText(runtime);

                        Toast.makeText(getContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        Date now = new Date();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                        data.put("client_id", DataFromDatabase.client_id);
                        data.put("clientuserID", DataFromDatabase.clientuserID);
                        data.put("distance", "0");
                        data.put("calories", "0");
                        data.put("runtime", "0");
                        data.put("duration", "0");
                        data.put("dateandtime", dtf.format((TemporalAccessor) now));
                        data.put("goal", goal);
                        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        data.put("date", dtf2.format((TemporalAccessor) now));
                        return data;
                    }
                };

                Volley.newRequestQueue(getContext()).add(request);
                Navigation.findNavController(v).navigate(R.id.action_activitySecondFragment_to_running_frag1);
            }
        });

        return view;
    }

}