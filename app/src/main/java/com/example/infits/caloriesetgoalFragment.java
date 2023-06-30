package com.example.infits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinav.progress_view.ProgressData;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link caloriesetgoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class caloriesetgoalFragment extends Fragment {

    SeekBar calorieconsume,calorieburn,carbs,fiber,protein,fat;
    TextView calconsumeTV,calBurnTV,carbTV,fiberTV,proteinTV,fatTV;
    CardView setgoal;

    Integer CCcurrentProgress,CBcurrentProgress,fibercurrentProgress,carbcurrentProgress,fatcurrentProgress,proteincurrentProgress;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_caloriesetgoal, container, false);
        View view = inflater.inflate(R.layout.fragment_caloriesetgoal, container, false);
        pastActivity();

        hooks(view);


        calorieconsume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calconsumeTV.setText(progress+" Kcal");
                CCcurrentProgress = progress;
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
                CBcurrentProgress = progress;
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
                carbcurrentProgress = progress;
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
                fibercurrentProgress = progress;
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
                proteincurrentProgress = progress;
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
                fatcurrentProgress = progress;
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

                if (CCcurrentProgress==0 && CBcurrentProgress==0 &&
                    carbcurrentProgress==0 && fibercurrentProgress==0 &&
                    proteincurrentProgress==0 && fatcurrentProgress==0){
                    Toast.makeText(getContext(), "Progess shoudnt be 0", Toast.LENGTH_SHORT).show();
                }

                String url = String.format("%sClient_GoalsCalorie.php", DataFromDatabase.ipConfig);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.d("Calorie set Goal Bro", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject goalsObject = jsonObject.getJSONObject("Goals");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, error -> Log.e("calorie set Goal Error::", error.toString())) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        LocalDateTime now = LocalDateTime.now();// gets the current date and time
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                        data.put("clientID", DataFromDatabase.clientuserID);
                        data.put("dateandtime",dtf.format(now));
                        data.put("client_id",DataFromDatabase.client_id);
                        data.put("clientuserID",DataFromDatabase.clientuserID);
                        data.put("calorieConsumeGoal",Integer.toString(CCcurrentProgress));
                        data.put("calorieBurnGoal",Integer.toString(CBcurrentProgress));
                        data.put("carbsGoal",Integer.toString(carbcurrentProgress));
                        data.put("fiberGoal",Integer.toString(fibercurrentProgress));
                        data.put("proteinGoal",Integer.toString(proteincurrentProgress));
                        data.put("fatGoal",Integer.toString(fatcurrentProgress));
                        data.put("operationToDo","add");
                        data.put("calorieConsumed","2210");
                        data.put("dietitian_id",DataFromDatabase.dietitian_id);
                        data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
                        return data;
                    }
                };
                Volley.newRequestQueue(requireContext()).add(request);
                request.setRetryPolicy(new DefaultRetryPolicy(6000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    public void pastActivity(){

        String url = String.format("%sClient_GoalsCalorie.php", DataFromDatabase.ipConfig);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Calorie Goals Data Bro", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject goalsObject = jsonObject.getJSONObject("Goals");
                        // Extract values from the "values" object
                        JSONObject valuesObject = goalsObject.getJSONObject("values");
                        String clientUserID = valuesObject.getString("clientuserID");
                        String dietitianUserID = valuesObject.getString("dietitianuserID");
                        String calorieConsumeGoal = valuesObject.getString("calorieConsumeGoal");
                        String calorieBurnGoal = valuesObject.getString("calorieBurnGoal");
                        String carbsGoal = valuesObject.getString("CarbsGoal");
                        String fatGoal = valuesObject.getString("fatGoal");
                        String proteinGoal = valuesObject.getString("Protein");
                        String fiberGoal = valuesObject.getString("Fiber");
                        //Seek bar progress
                        calorieconsume.setProgress(Integer.parseInt(calorieConsumeGoal));
                        calorieburn.setProgress(Integer.parseInt(calorieBurnGoal));
                        carbs.setProgress(Integer.parseInt(carbsGoal));
                        fiber.setProgress(Integer.parseInt(fiberGoal));
                        protein.setProgress(Integer.parseInt(proteinGoal));
                        fat.setProgress(Integer.parseInt(fatGoal));
                        //Text View
                        calconsumeTV.setText(Integer.parseInt(calorieConsumeGoal) + " Kcal");
                        calBurnTV.setText(Integer.parseInt(calorieBurnGoal) + " Kcal");
                        carbTV.setText(Integer.parseInt(carbsGoal) + " g");
                        fiberTV.setText(Integer.parseInt(fiberGoal) + " g");
                        proteinTV.setText(Integer.parseInt(proteinGoal) + " g");
                        fatTV.setText(Integer.parseInt(fatGoal) + " g");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("calorie Goal Data Bro", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("dateandtime",dtf.format(now));
                data.put("client_id","1");
                data.put("clientuserID","dev");
                data.put("calorieConsumeGoal","3000");
                data.put("calorieBurnGoal","1500");
                data.put("carbsGoal","450");
                data.put("fiberGoal","400");
                data.put("proteinGoal","300");
                data.put("fatGoal","200");
                data.put("operationToDo","get");
                data.put("calorieConsumed","2210");
                data.put("dietitian_id","19");
                data.put("dietitianuserID","infits");
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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