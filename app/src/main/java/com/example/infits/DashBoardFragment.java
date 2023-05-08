package com.example.infits;

import static com.example.infits.StepTrackerFragment.goalVal;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashBoardFragment extends Fragment {

    String urlRefer = String.format("%sverify.php",DataFromDatabase.ipConfig);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    String url = String.format("%sdashboard.php",DataFromDatabase.ipConfig);


    DataFromDatabase dataFromDatabase;
    TextView stepstv;
    TextView glassestv;
    TextView glassesGoaltv;
    TextView sleeptv;
    TextView sleepGoaltv;
    TextView weighttv;
    TextView weightGoaltv;
    TextView calorietv;
    TextView calorieGoaltv;
    TextView bpmtv;
    TextView bpmUptv;
    TextView bpmDowntv;
    TextView meal_date;
    TextView diet_date;
    String url1 = String.format("%sprofilePicture.php", DataFromDatabase.ipConfig);

    static TextView stepsProgressPercent;
    RequestQueue queue;
    ImageButton sidemenu, notifmenu;
    CardView stepcard, heartcard, watercard, sleepcard, weightcard, caloriecard,dietcard,goProCard,mealTrackerCard,dietCardPro;
    Button btnsub, btnsub1;
    TextView name,date;
    ImageView profile;

    static ProgressBar stepsProgressBar;

    ImageView menuBtn, notificationBell, notificationBellUpdate;
    int waterGoal = 0, waterConsumed = 0;

    public interface OnMenuClicked {
        void menuClicked();
    }

    OnMenuClicked onMenuClicked;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String urlDt = String.format("%sgetDietitianDetail.php",DataFromDatabase.ipConfig);

    public DashBoardFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
          onMenuClicked = (OnMenuClicked) context;
    }

    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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

//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                startActivity(new Intent(getActivity(),DashBoardMain.class));
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        hooks(view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DateForSteps", Context.MODE_PRIVATE);

        Date dateForSteps = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

        System.out.println(simpleDateFormat.format(dateForSteps));

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("date", simpleDateFormat.format(dateForSteps));
        myEdit.putBoolean("verified",false);
        myEdit.commit();

        SharedPreferences prefs = requireContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        String clientuserID = prefs.getString("clientuserID", DataFromDatabase.clientuserID);

        ImageView profileImageView = view.findViewById(R.id.profile1);

        // Execute the query using a Volley StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, response -> {
            try {
                JSONObject responseJson = new JSONObject(response);

                String profilePhoto = responseJson.getString("profile");
                // decode the base64 string to a byte array
                byte[] decodedBytes = Base64.decode(profilePhoto, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (decodedBitmap != null) {
                    profileImageView.setImageBitmap(decodedBitmap);
                } else {
                    Log.e("TAG", "Failed to decode bitmap.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ProfilePhotoFetcher", "JSON parsing error: " + e.getMessage());
                // Handle the JSONException here
            }
        }, error -> {
            Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("userID",clientuserID);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        Date dateToday = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd,yyyy");

        name.setText(DataFromDatabase.name);
        date.setText(sf.format(dateToday));

//        meal_date.setText(sf.format(dateToday));
//        diet_date.setText(sf.format(dateToday));

        menuBtn.setOnClickListener(v -> onMenuClicked.menuClicked());

        if (DataFromDatabase.proUser){
            goProCard.setVisibility(View.GONE);
            mealTrackerCard.setVisibility(View.VISIBLE);
            dietCardPro.setVisibility(View.GONE);
            dietcard.setVisibility(View.VISIBLE);
        }
        if (!DataFromDatabase.proUser){
            goProCard.setVisibility(View.VISIBLE);
            mealTrackerCard.setVisibility(View.GONE);
            dietCardPro.setVisibility(View.VISIBLE);
            dietcard.setVisibility(View.GONE);
        }

        SharedPreferences inAppPrefs = requireActivity().getSharedPreferences("inAppNotification", Context.MODE_PRIVATE);
        boolean newNotification = inAppPrefs.getBoolean("newNotification", false);

        if(newNotification) {
            notificationBellUpdate.setVisibility(View.VISIBLE);
        } else {
            notificationBellUpdate.setVisibility(View.GONE);
        }

        notificationBell.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), InAppNotification.class);
            startActivity(intent);

            SharedPreferences.Editor inAppEditor = inAppPrefs.edit();
            inAppEditor.putBoolean("newNotification", false);
            inAppEditor.apply();

            requireActivity().finish();
        });

        stepcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_stepTrackerFragment);
            }
        });

        SharedPreferences stepPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        float steps = stepPrefs.getFloat("steps", 0);
        float stepGoal = stepPrefs.getFloat("goal", 0f);
        int stepPercent = stepGoal == 0 ? 0 : (int) ((steps * 100) / stepGoal);
        String stepText = stepGoal == 0 ? "----------" : (int) stepGoal + " Steps";
        String stepPercentText = stepPercent + "%";
        Log.d("frag", String.valueOf(steps));
        Log.d("frag", String.valueOf(stepGoal));
        Log.d("frag", String.valueOf(stepPercent));

        stepstv.setText(stepText);
        stepsProgressPercent.setText(stepPercentText);
        stepsProgressBar.setProgress(stepPercent);

        sleepcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_sleepTrackerFragment);
            }
        });

        SharedPreferences sleepPrefs = requireActivity().getSharedPreferences("sleepPrefs", Context.MODE_PRIVATE);
        String sleepGoalText = sleepPrefs.getString("goal", "8") + " Hours";
        String sleepText = sleepPrefs.getString("hours", "00") + " hr " + sleepPrefs.getString("minutes", "00") + " mins";

        sleepGoaltv.setText(sleepGoalText);
        sleeptv.setText(sleepText);

        watercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_waterTrackerFragment);
            }
        });
        getLatestWaterData();

        weightcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_weightTrackerFragment);
            }
        });

        SharedPreferences weightPrefs = requireContext().getSharedPreferences("weightPrefs", Context.MODE_PRIVATE);
        int weightGoalVal = weightPrefs.getInt("goal", 70);
        int weightVal = weightPrefs.getInt("weight", 0);
        String weightGoalText = weightGoalVal == 0 ? "----------" : weightGoalVal + " KG";
        String weightText = weightVal == 0 ? "----------" : weightVal + " KiloGrams";

        weightGoaltv.setText(weightGoalText);
        weighttv.setText(weightText);

        caloriecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_calorieTrackerFragment);
            }
        });

        getLatestCalorieData();

        mealTrackerCard.setOnClickListener(v->{
//            Intent intent = new Intent(getActivity(),Meal_main.class);
//            requireActivity().finish();
//            startActivity(intent);
        });

        heartcard.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_heartRate);
        });

        dietcard.setOnClickListener(v->{
//            Intent intent = new Intent(getActivity(),Diet_plan_main_screen.class);
//            requireActivity().finish();
//            startActivity(intent);
        });

        if (DataFromDatabase.proUser){
            StringRequest dietitianDetails = new StringRequest(Request.Method.POST,urlDt,response -> {
                System.out.println(response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    DataFromDatabase.flag = true;
                    DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
                    byte[] qrimage = Base64.decode(object.getString("profilePhoto"), 0);
                    DataFromDatabase.dtPhoto = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            },error -> {

            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> data = new HashMap<>();

                    data.put("userID",prefs.getString("dietitianuserID", DataFromDatabase.dietitianuserID));

                    return data;
                }
            };

            Volley.newRequestQueue(getContext()).add(dietitianDetails);
        }

        goProCard.setOnClickListener(v->{
            showDialog();
        });

        dietCardPro.setOnClickListener(v->{
            showDialog();
        });

        queue = Volley.newRequestQueue(getContext());
        Log.d("ClientMetrics","before");

        StringRequest stringRequestHeart = new StringRequest(Request.Method.POST,String.format("%sheartrate.php",DataFromDatabase.ipConfig),response -> {

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArray = jsonResponse.getJSONArray("heart");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                bpmtv.setText(jsonObject.getString("avg"));
                bpmDowntv.setText(jsonObject.getString("min"));
                bpmUptv.setText(jsonObject.getString("max"));
            }catch (JSONException jsonException){
                System.out.println(jsonException);
            }
        },error -> {

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("userID",clientuserID);
                return data;
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequestHeart);

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,url, response -> {
            if (!response.equals("failure")){
                Log.d("ClientMetrics","success");
                Log.d("response",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    String stepsStr = object.getString("steps");
                    String stepsGoal = object.getString("stepsgoal");
                    String waterStr = object.getString("waterConsumed");
                    DataFromDatabase.waterStr = waterStr;
                    String waterGoal = object.getString("watergoal");
                    DataFromDatabase.waterGoal = waterGoal;
                    String sleephrsStr = object.getString("sleephrs");
                    String sleepminsStr = object.getString("sleepmins");
                    String sleepGoal = object.getString("sleepgoal");
                    String weightStr = object.getString("weight");
                    DataFromDatabase.weightStr = weightStr;
                    String weightGoal = object.getString("weightgoal");
                    DataFromDatabase.weightGoal = weightGoal;
                    stepstv.setText(Html.fromHtml(String.format("<strong>%s</strong> steps",stepsStr)));
                    glassestv.setText(Html.fromHtml(String.format("<strong>%s</strong> ml",waterStr)));
                    glassesGoaltv.setText(Html.fromHtml(String.format("<strong>%s ml</strong>",waterGoal)));
                    sleeptv.setText(Html.fromHtml(String.format("<strong>%s</strong> hr <strong>%s</strong> mins",sleephrsStr,sleepminsStr)));
                    sleepGoaltv.setText(Html.fromHtml(String.format("<strong>%s Hours</strong>",sleepGoal)));
                    weighttv.setText(Html.fromHtml(String.format("<strong>%s </strong>KiloGrams",weightStr)));
                    weightGoaltv.setText(Html.fromHtml(String.format("<strong>%s KG</strong>",weightGoal)));

                    if (stepsStr.equals("null")){
                        stepstv.setText("no data available");
                    }if (waterStr.equals("null")){
                        glassestv.setText("no data available");
                    }if (waterGoal.equals("null")){
                        glassesGoaltv.setText("no data available");
                    }if (sleephrsStr.equals("null")){
                        sleeptv.setText("no data available");
                    }if (sleepGoal.equals("null")){
                        sleepGoaltv.setText("no data available");
                    }if (weightStr.equals("null")){
                        weighttv.setText("no data available");
                    }if (weightGoal.equals("null")){
                        weightGoaltv.setText("no data available");
                    }

                    steps_update(stepsStr,stepsGoal);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (response.equals("failure")){
                Log.d("clientMetrics","failure");
                Toast.makeText(getContext(), "ClientMetrics failed", Toast.LENGTH_SHORT).show();
            }
        },error -> Log.d("dashBoardFrag", error.toString()))
        {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("clientID", DataFromDatabase.client_id);
                return data;
            }
        };

        // RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // requestQueue.add(stringRequest);
        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue1.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        Log.d("ClientMetrics","at end");

        DashBoardMain dashBoardMain = (DashBoardMain) requireActivity();
        String cardClicked = dashBoardMain.getCardClicked();

        if(cardClicked != null) {
            switch (cardClicked) {
                case "step":
                    Bundle stepBundle = new Bundle();
                    stepBundle.putBoolean("notification", true);
                    Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_stepTrackerFragment, stepBundle);
                    break;
                case "sleep":
                    Bundle sleepBundle = new Bundle();
                    sleepBundle.putBoolean("notification", true);
                    sleepBundle.putString("sleepTime", dashBoardMain.getSleepTime());
                    Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_sleepTrackerFragment, sleepBundle);
                    break;
                case "weight": Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_weightTrackerFragment);
                    break;
                case "heart": Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_heartRate);
                    break;
                case "calorie": Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_calorieTrackerFragment);
                    break;
                case "water":
                    Bundle waterBundle = new Bundle();
                    waterBundle.putBoolean("notification", true);
                    Navigation.findNavController(requireActivity(), R.id.trackernav).navigate(R.id.action_dashBoardFragment_to_waterTrackerFragment, waterBundle);
            }
        }
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        ImageView imageView = view.findViewById(R.id.profile1);
//        imageView.setImageResource(R.drawable.profile);
//    }

    public void setProfileImage(Drawable drawable) {
        profile.setImageDrawable(drawable);
    }

    private void getLatestCalorieData() {
        String calorieUrl = String.format("%sgetLatestCalorieData.php", DataFromDatabase.ipConfig);

        StringRequest calorieRequest = new StringRequest(
                Request.Method.POST,
                calorieUrl,
                response -> {
                    Log.d("DashBoardFragment", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("food");

                        if(array.length() == 0) {
                            calorietv.setText("----------");
                            calorieGoaltv.setText("----------");
                        } else {
                            String calorieGoalText = array.getJSONObject(0).getString("goal") + " kcal";
                            String calorieValueText = array.getJSONObject(0).getString("calorie") + " kcal";

                            calorietv.setText(calorieValueText);
                            calorieGoaltv.setText(calorieGoalText);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("DashBoardFragment", error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void hooks(View view) {
        menuBtn = view.findViewById(R.id.hamburger_menu);
        notificationBell = view.findViewById(R.id.dashboard_bell);
        notificationBellUpdate = view.findViewById(R.id.dashboard_bell_update);

        name = view.findViewById(R.id.nameInDash);
        date = view.findViewById(R.id.date);

//        profile = view.findViewById(R.id.profile1);
//
////        if (DataFromDatabase.profile != null) {
////            profile.setImageBitmap(DataFromDatabase.profile);
////        } else {
////            if (getActivity() != null) {
////                fragment.setProfileImage(ContextCompat.getDrawable(getActivity(), R.drawable.profile));
////            }
////        }
//
//        profile.setImageBitmap(DataFromDatabase.profile);

        stepstv = view.findViewById(R.id.steps);
        glassestv = view.findViewById(R.id.glasses);
        glassesGoaltv = view.findViewById(R.id.glassesGoal);
        sleeptv = view.findViewById(R.id.sleep);
        sleepGoaltv = view.findViewById(R.id.sleepgoal);
        weighttv = view.findViewById(R.id.weight);
        weightGoaltv = view.findViewById(R.id.weightGoal);
        calorietv = view.findViewById(R.id.calorie);
        calorieGoaltv = view.findViewById(R.id.GoalCalorie);
        bpmtv = view.findViewById(R.id.bpm);
        bpmUptv = view.findViewById(R.id.bpmUp);
        bpmDowntv = view.findViewById(R.id.bpmDown);
        meal_date = view.findViewById(R.id.date_meal);

        stepcard = view.findViewById(R.id.stepcard);
        heartcard = view.findViewById(R.id.heartcard);
        watercard = view.findViewById(R.id.watercard);
        sleepcard = view.findViewById(R.id.sleepcard);
        weightcard = view.findViewById(R.id.weightcard);
        caloriecard = view.findViewById(R.id.caloriecard);
        dietcard = view.findViewById(R.id.dietcard);
        goProCard = view.findViewById(R.id.proCrad);
        mealTrackerCard = view.findViewById(R.id.meal_tracker);
        dietCardPro = view.findViewById(R.id.dietcardPro);
        diet_date = view.findViewById(R.id.date_diet);

        stepsProgressPercent = view.findViewById(R.id.steps_progress_percent);
        stepsProgressBar = view.findViewById(R.id.steps_progress_bar);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.referralcodedialog);
        final EditText referralCode = dialog.findViewById(R.id.referralcode);
        ImageView checkReferral = dialog.findViewById(R.id.checkReferral);
        checkReferral.setOnClickListener(vi->{

            StringRequest stringRequest = new StringRequest(Request.Method.POST,urlRefer,response->{

            },error->{

            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> data = new HashMap();

                    data.put("clientID",DataFromDatabase.clientuserID);
                    data.put("referal_code",referralCode.getText().toString());


                    return data;
                }
            };
             Volley.newRequestQueue(getContext()).add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void updateStepCard(Intent intent) {
        Log.wtf("dashboard", "entered");
        if (intent.getExtras() != null) {
            float steps = intent.getIntExtra("steps",0);
            final float[] goalPercent = new float[1];

            Log.i("StepTracker","Countdown seconds remaining:" + steps);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                goalPercent[0] = ((steps/goalVal)*100)/100;
                stepsProgressBar.setProgress((int) goalPercent[0]);
                stepsProgressPercent.setText(String.valueOf((int) goalPercent[0]));

                System.out.println("steps: " + steps);
                System.out.println("goalVal: " + goalVal);
                System.out.println("goalPercent: " + goalPercent[0]);
            },2000);
        }
    }

    public void getLatestWaterData() {
        String url = String.format("%sgetLatestWaterdt.php", DataFromDatabase.ipConfig);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("dashboardFrag", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("water");

                        if(array.length() != 0) {
                            String waterGoalStr = array.getJSONObject(0).getString("goal");
                            String waterConsumedStr = array.getJSONObject(0).getString("drinkConsumed");
                            waterGoal = Integer.parseInt(waterGoalStr) / 250;
                            waterConsumed = Integer.parseInt(waterConsumedStr) / 250;  // 250 ml = 1 glass

                            glassestv.setText(waterConsumed + " Glasses");
                            glassesGoaltv.setText(waterGoal + " Glasses");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("dashboardFrag", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date= new Date();
                data.put("clientID", DataFromDatabase.client_id);
                data.put("date",dateFormat.format(date));
                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void steps_update(String steps , String goal)
    {
        int step1=Integer.parseInt(steps);
        int goal1= Integer.parseInt(goal);

        int stepPercent= (int) (step1 * 100)/goal1;
        String stepPercentText = stepPercent + "%";
        stepsProgressPercent.setText(stepPercentText);
        stepsProgressBar.setProgress(stepPercent);


    }


}