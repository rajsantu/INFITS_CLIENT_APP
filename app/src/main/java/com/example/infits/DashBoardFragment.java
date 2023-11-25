package com.example.infits;
import static com.example.infits.StepTrackerFragment.goalVal;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class DashBoardFragment extends Fragment {

    //String urlRefer = String.format("%sverify.php",DataFromDatabase.ipConfig);
    String urlRefer = "https://infits.in/androidApi/verify.php";


    String Entered;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:S", Locale.getDefault());

    SimpleDateFormat caloriedateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:S", Locale.getDefault());


    String url = String.format("%sdashboard.php",DataFromDatabase.ipConfig);
    //String url = "https://infits.in/androidApi/dashboard.php";


    // String url = String.format("%sDashboard.php",DataFromDatabase.ipConfig);
    //String url1 = String.format("%sgetDietitianDetail.php", DataFromDatabase.ipConfig);
    String url1 = "https://infits.in/androidApi/getDietitianDetail.php";
    //String url1 = "https://infits.in/androidApi/getDietitianDetail.php";

    //No such file!
    // String url1 = String.format("%sprofilePicture.php", DataFromDatabase.ipConfig);

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
    TextView diet_date,meal_tracker_text;
    static TextView stepsProgressPercent;
    RequestQueue queue;
    ImageButton sidemenu, notifmenu;
    CardView stepcard, heartcard, watercard, sleepcard, weightcard, caloriecard,dietcard,goProCard,mealTrackerCard,workout_card;
    Button btnsub, btnsub1;
    TextView name,date, workout_date, consul_date;
    ImageView profile;

    CardView consultation_gopro_btn, diet_chart_gopro_btn,meal_tracker_gopro_btn;

    TextView consultation_text, diet_chart_text;
    ImageView pro_identifier;

    static ProgressBar stepsProgressBar;

    ImageView menuBtn, notificationBell, notificationBellUpdate;
    int waterGoal = 0, waterConsumed = 0;

    String dietitianID,dietitianuserID,dietitianName;

    public interface OnMenuClicked {
        void menuClicked();
    }

    OnMenuClicked onMenuClicked;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private final String urlDt = String.format("%sgetDietitianDetail.php",DataFromDatabase.ipConfig);
    private final String urlDt = "https://infits.in/androidApi/getDietitianDetail.php";

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

//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                startActivity(new Intent(getActivity(),DashBoardMain.class));
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        myEdit.apply();

        SharedPreferences prefs = requireContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        String clientuserID = prefs.getString("clientuserID", DataFromDatabase.clientuserID);

        // Dashboard Profile pic from server
        ImageView profileImageView = view.findViewById(R.id.profile1);
        String DashboardprofilePic = "http://192.168.1.9/infits/upload/default.jpg";
        Glide.with(this).load(DashboardprofilePic).fitCenter().into(profileImageView);

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
        }, error -> Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show()) {
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

        name.setText("Hi " + DataFromDatabase.name);
        date.setText(sf.format(dateToday));

        meal_date.setText(sf.format(dateToday));
        diet_date.setText(sf.format(dateToday));
        workout_date.setText(sf.format(dateToday));
        consul_date.setText(sf.format(dateToday));


        menuBtn.setOnClickListener(v -> onMenuClicked.menuClicked());


        if (DataFromDatabase.proUser){

            consultation_gopro_btn.setVisibility(View.GONE);
            meal_tracker_gopro_btn.setVisibility(View.GONE);
            diet_chart_gopro_btn.setVisibility(View.GONE);

            consul_date.setVisibility(View.VISIBLE);
            meal_date.setVisibility(View.VISIBLE);
            diet_date.setVisibility(View.VISIBLE);
            consultation_text.setVisibility(View.VISIBLE);
            meal_tracker_text.setVisibility(View.VISIBLE);
            diet_chart_text.setVisibility(View.VISIBLE);

            pro_identifier.setVisibility(View.VISIBLE);
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

        stepcard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_stepTrackerFragment));

        SharedPreferences stepPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        float stepGoal = stepPrefs.getFloat("goal", 0f);
        int stepPercent = stepGoal == 0 ? 0 : (int) ((FetchTrackerInfos.currentSteps * 100) / stepGoal);
        String stepText = stepGoal == 0 ? "----------" : (int) stepGoal + " Steps";
        String stepPercentText = stepPercent + "%";
        Log.d("frag", String.valueOf(FetchTrackerInfos.currentSteps));
        Log.d("frag", String.valueOf(stepGoal));
        Log.d("frag", String.valueOf(stepPercent));
        if (FetchTrackerInfos.currentSteps > 1)
            stepstv.setText(FetchTrackerInfos.currentSteps+" steps");
        else stepstv.setText("--------");
        stepsProgressPercent.setText(stepPercentText);
        stepsProgressBar.setProgress(stepPercent);

        sleepcard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_sleepTrackerFragment));

        SharedPreferences sleepPrefs = requireActivity().getSharedPreferences("sleepPrefs", Context.MODE_PRIVATE);
        String sleepGoalText = sleepPrefs.getString("goal", "8") + " Hours";
        String sleepText = sleepPrefs.getString("hours", "00") + " hr " + sleepPrefs.getString("minutes", "00") + " mins";

        sleepGoaltv.setText(sleepGoalText);
        sleeptv.setText(sleepText);

        watercard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_waterTrackerFragment));
        getLatestWaterData();

        weightcard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_weightTrackerFragment));
        getLatestWeightData();

        SharedPreferences weightPrefs = requireContext().getSharedPreferences("weightPrefs", Context.MODE_PRIVATE);

        caloriecard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_calorieTrackerFragment));
        getLatestCalorieData();

        workout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_activityTracker2);
            }
        });

        mealTrackerCard.setOnClickListener(v->{
            updateVerification();
            if (DataFromDatabase.proUser){
                //Intent intent = new Intent(getActivity(),Meal_main.class);
                //requireActivity().finish();
                //startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_mealTracker);
            }
            else {
                showDialog();
            }
        });

        goProCard.setOnClickListener(v->{
            if (DataFromDatabase.proUser){
                if (DataFromDatabase.verification.equals("0")){
                    startActivity(new Intent(getActivity(), connectingDietitian.class));
                }else {
                    updateVerification();
                    Intent intent = new Intent(getActivity(),Consultation.class);
                    requireActivity().finish();
                    startActivity(intent);
                }

                //Toast.makeText(getContext(),"Consultation card clicked",Toast.LENGTH_SHORT).show();
            }
            else {
                showDialog();
            }
        });

        heartcard.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_heartRate));

        //Include this!!
        //dietcard.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_fragment_diet_chart));

        dietcard.setOnClickListener(v->{
            if (DataFromDatabase.proUser){
                updateVerification();
                //Intent intent = new Intent(getActivity(),Meal_main.class);
                //requireActivity().finish();
                //startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_fragment_diet_chart);
            }
            else {
                showDialog();
            }
        });

        if (DataFromDatabase.proUser){
            StringRequest dietitianDetails = new StringRequest(Request.Method.POST,urlDt,response -> {
                System.out.println(response);
                Log.d("dietitiandetails",response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject array = object.getJSONObject("dietitiandetails");
                    DataFromDatabase.flag = true;
                    DataFromDatabase.dietitianuserID = array.getString("dietitianuserID");
                    byte[] qrimage = Base64.decode(array.getString("profilePhoto"), 0);
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
                jsonException.printStackTrace();
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
                        stepstv.setText(R.string.No_data);
                    }if (waterStr.equals("null")){
                        glassestv.setText(R.string.No_data);
                    }if (waterGoal.equals("null")){
                        glassesGoaltv.setText(R.string.No_data);
                    }if (sleephrsStr.equals("null")){
                        sleeptv.setText(R.string.No_data);
                    }if (sleepGoal.equals("null")){
                        sleepGoaltv.setText(R.string.No_data);
                    }if (weightStr.equals("null")){
                        weighttv.setText(R.string.No_data);
                    }if (weightGoal.equals("null")){
                        weightGoaltv.setText(R.string.No_data);
                    }

                    steps_update(stepsStr,stepsGoal);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //else if (response.equals("failure")){
            else{
                Log.d("clientMetrics","failure");
                Toast.makeText(getContext(), "ClientMetrics failed", Toast.LENGTH_SHORT).show();
            }
        },error -> Log.d("dashBoardFrag", error.toString()))
        {
            @Nullable
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

    public void setProfileImage(Drawable drawable) {
        profile.setImageDrawable(drawable);
    }


    private void getLatestCalorieData() {
        //String calorieUrl = String.format("%sgetLatestCalorieData.php", DataFromDatabase.ipConfig);
        String calorieUrl = "http://192.168.1.9/infits/getLatestCalorieData.php";
        calorietv.setText("------");
        calorieGoaltv.setText("2000 Kcal");

        StringRequest calorieRequest = new StringRequest( Request.Method.POST, calorieUrl,
                response -> {
                    Log.d("DashBoardFragment", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("food");

                        if (array.isNull("Calories")) {
                            calorietv.setText("0 kcal");
                            String calorieGoalText = array.getString("goal") + " kcal";
                            calorieGoaltv.setText(calorieGoalText);
                        } else {
                            String calorieValueText = array.getString("Calories") + " kcal";
                            String calorieGoalText = array.getString("goal") + " kcal";

                            calorietv.setText(calorieValueText);
                            calorieGoaltv.setText(calorieGoalText);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("DashBoardFragment", error.toString())
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Date date = new Date();
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("dateandtime",caloriedateFormat.format(date));

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(calorieRequest);
        calorieRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getLatestWeightData(){
        //String Url = String.format("%sgetLatestWeightData.php", DataFromDatabase.ipConfig);
        String Url = "https://infits.in/androidApi/getLatestWeightData.php";
        weighttv.setText("------");
        weightGoaltv.setText("70 Kg");

        StringRequest calorieRequest = new StringRequest( Request.Method.POST, Url,
                response -> {
                    Log.d("DashBoardFragment Weight", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("weight");

                        if(array.length() == 0) {
                            weighttv.setText("----------");
                            weightGoaltv.setText("----------");
                        }else{
                            String weight = array.getString("weight");
                            String Goal = array.getString("goal");
                            String bmi = array.getString("bmi");

                            weighttv.setText((weight) + " Kg");
                            weightGoaltv.setText((Goal) + " Kg");
                            DataFromDatabase.weightStr = weight;
                            DataFromDatabase.weightGoal = Goal;
                            DataFromDatabase.bmi = bmi;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("DashBoardFragment", error.toString())
        ) {
            @Nullable
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

        //profile = view.findViewById(R.id.profile1);

        /*
        if (DataFromDatabase.profile != null) {
            profile.setImageBitmap(DataFromDatabase.profile);
        } else {
            if (getActivity() != null) {
                fragment.setProfileImage(ContextCompat.getDrawable(getActivity(), R.drawable.profile));
            }
        }*/

        //profile.setImageBitmap(DataFromDatabase.profile);

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

        //Dates
        meal_date = view.findViewById(R.id.date_meal);
        diet_date = view.findViewById(R.id.date_diet);
        workout_date = view.findViewById(R.id.workout_date);
        consul_date = view.findViewById(R.id.consultation_date);

        //goPro buttons
        consultation_gopro_btn=view.findViewById(R.id.consultation_gopro_btn);
        diet_chart_gopro_btn=view.findViewById(R.id.diet_chart_gopro_btn);
        meal_tracker_gopro_btn=view.findViewById(R.id.meal_tracker_go_pro_btn);

        //Pro textviews
        consultation_text=view.findViewById(R.id.consultation_txt);
        meal_tracker_text=view.findViewById(R.id.meal_track_txt);
        diet_chart_text=view.findViewById(R.id.diet_chart_txt);
        pro_identifier=view.findViewById(R.id.pro_identifier);

        //CardView
        stepcard = view.findViewById(R.id.stepcard);
        heartcard = view.findViewById(R.id.heartcard);
        watercard = view.findViewById(R.id.watercard);
        sleepcard = view.findViewById(R.id.sleepcard);
        weightcard = view.findViewById(R.id.weightcard);
        caloriecard = view.findViewById(R.id.caloriecard);
        dietcard = view.findViewById(R.id.dietcardPro);


        goProCard = view.findViewById(R.id.proCrad);
        mealTrackerCard = view.findViewById(R.id.meal_tracker);
        diet_date = view.findViewById(R.id.date_diet);
        workout_card = view.findViewById(R.id.workout_card);
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

            //String referralUrl = String.format("%sverify.php",DataFromDatabase.ipConfig);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,urlRefer,
                    response->{
                        Log.d("DietitianVerification", response);

                        if(response.equals("found")) {
                            showSuccessDialog();
                            System.out.println("Verified");
                        }else {
                            System.out.println("Not verified");
                            showFailureDialog();
                        }

                    },error->{

            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> data = new HashMap<>();

                    //data.put("clientID",DataFromDatabase.clientuserID);
                    Entered = referralCode.getText().toString();
                    data.put("dietitian_verify_code",Entered);
                    data.put("type","1");


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

    private void showFailureDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.wrong_referral_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btn = dialog.findViewById(R.id.try_again);

        btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showSuccessDialog() {
        getUpdatedDietitianData();
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_congratulation);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btn = dialog.findViewById(R.id.btn);
        consultation_gopro_btn.setVisibility(View.GONE);
        meal_tracker_gopro_btn.setVisibility(View.GONE);
        diet_chart_gopro_btn.setVisibility(View.GONE);

        consul_date.setVisibility(View.VISIBLE);
        meal_date.setVisibility(View.VISIBLE);
        diet_date.setVisibility(View.VISIBLE);
        consultation_text.setVisibility(View.VISIBLE);
        meal_tracker_text.setVisibility(View.VISIBLE);
        diet_chart_text.setVisibility(View.VISIBLE);

        pro_identifier.setVisibility(View.VISIBLE);


        btn.setOnClickListener(v -> {
            dialog.dismiss();
            updateVerification();

        });

        dialog.show();
    }
    private void getUpdatedDietitianData(){
        final Dialog dialog = new Dialog(getActivity());
        final EditText referralCode = dialog.findViewById(R.id.referralcode);
        String Url = String.format("%sgetUpdatedDietitianData.php", DataFromDatabase.ipConfig);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Url, response -> {
            Log.d("updatedDietitianData", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("DietitianData")) {
                    JSONObject dietitianData = jsonObject.getJSONObject("DietitianData");
                    DataFromDatabase.dietitian_id = dietitianData.getString("dietitianID");
                    DataFromDatabase.dietitianuserID = dietitianData.getString("dietitianuserID");
                    System.out.println(DataFromDatabase.dietitian_id);
                    System.out.println(DataFromDatabase.dietitianuserID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {

            Log.d("updatedDietitianData Err",error.toString());
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("verify_code",Entered);
                return data;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void updateVerification(){
        final Dialog dialog = new Dialog(getActivity());
        final EditText referralCode = dialog.findViewById(R.id.referralcode);

        //String url = String.format("%sdietitianUpdated.php",DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/dietitianUpdated.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response->{
                    Log.d("DietitianUpdated", response);

                    if(response.equals("Updated")) {
                        System.out.println("Updated");
                        DataFromDatabase.proUser = true;

                    }else {
                        System.out.println("Not verified");
                    }

                },error->{

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> data = new HashMap<>();

                data.put("clientuserID",DataFromDatabase.clientuserID);
                data.put("referralCode",Entered);
                data.put("dietitianID",DataFromDatabase.dietitian_id);
                data.put("dietitianuserID",DataFromDatabase.dietitianuserID);
                data.put("type","1");
                data.put("verification","0");


                return data;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
        //String url = String.format("%sgetLatestWaterdt.php", DataFromDatabase.ipConfig);
        String url = "https://infits.in/androidApi/getLatestWaterdt.php";
        glassestv.setText("----------");
        glassesGoaltv.setText("8 Glasses");
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("dashboardFrag", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("water");

                        if(array.length() == 0) {
                            glassestv.setText("----------");
                            glassesGoaltv.setText("----------");
                        }else{
                            String waterGoalStr = array.getString("goal");
                            String waterConsumedStr = array.getString("drinkConsumed");

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
                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("dateandtime",dateFormat.format(date));
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