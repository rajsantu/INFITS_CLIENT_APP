package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import Utility.ToastUtils;



public class Calorie_meal_info_with_photo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView mealName, calorieValue, carbsValue, fatValue, proteinValue;
    //String url = String.format("%sgetFavouriteFoodItems.php", DataFromDatabase.ipConfig);
    String url = "https://infits.in/androidApi/getFavouriteFoodItems.php";
    //String url1 = String.format("%saddFavouriteFoodItems.php", DataFromDatabase.ipConfig);
    String url1 = "https://infits.in/androidApi/addFavouriteFoodItems.php";
    //String url2 = String.format("%sdeleteFavouriteFoodItems.php", DataFromDatabase.ipConfig);
    String url2 = "https://infits.in/androidApi/deleteFavouriteFoodItems.php";
    String TAG = "mealInfoWithPhoto";
    ImageView calorieImgback;
    boolean IsFavourite = false;

    ArrayList<String> mealInfotransfer;
    JSONObject mainJSONobj;
    JSONArray mainJsonArray;
    SharedViewModel sharedViewModel;

    View bottomSheetN;
    //    ArrayList<Todays_BreakFast_info> todays_breakFast_infos= new ArrayList<>();
    String Meal_Type;
    NumberPicker quantityPicker, sizePicker;
    RequestQueue requestQueue, requestQueue1, requestQueue2;
    ImageButton uparrow1, uparrow2, downarrow1, downarrow2, FavouriteMealButton;
    TextView AddToMealTrackerButton;
    String[] numberPicker1List = new String[]{"0.5", "1", "1.5", "2"};
    String[] numberPicker2List = new String[]{"Small", "Regular", "Large"};
    int REQUEST_IMAGE_CAPTURE = 1;

//    Adapter_Todays_BreakFast adapter_todays_breakFast;

    public Calorie_meal_info_with_photo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mealInfoWithPhoto.
     */
    // TODO: Rename and change types and number of parameters
    public static mealInfoWithPhoto newInstance(String param1, String param2) {
        mealInfoWithPhoto fragment = new mealInfoWithPhoto();
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

        View view = inflater.inflate(R.layout.fragment_calorie_meal_info_with_photo, container, false);
        hooks(view);
//        if (todays_breakFast_infos == null) {
//            todays_breakFast_infos = new ArrayList<>();
//        }
        sharedViewModel= new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        BottomSheetBehavior.from(bottomSheetN).setPeekHeight(1520);
        BottomSheetBehavior.from(bottomSheetN).setState(BottomSheetBehavior.STATE_COLLAPSED);


        Bundle args = getArguments();
        mainJSONobj = new JSONObject();
//        Toast.makeText(requireContext(), "Bundle", Toast.LENGTH_SHORT).show();
        if (args != null) {
            mealInfotransfer = args.getStringArrayList("mealInfotransfer");
        } else {
            mealInfotransfer = new ArrayList<>(Arrays.asList("sheera", "Dinner", "190 kcal", "29.6", "1.9", "7.4", "2131230991", "1"));
            Log.d(TAG, "onCreateView: NULL bundle");
        }

        getFavouriteFoodItems();
        setupNumberPicker(quantityPicker, numberPicker1List, uparrow1, downarrow1);
        setupNumberPicker(sizePicker, numberPicker2List, uparrow2, downarrow2);
        //set TextView
        mainJsonArray = new JSONArray();
//        Toast.makeText(getContext(), "JsonArray", Toast.LENGTH_SHORT).show();
        mealName.setText(mealInfotransfer.get(0));
        Meal_Type = mealInfotransfer.get(1);
        calorieValue.setText(mealInfotransfer.get(2));
        carbsValue.setText(mealInfotransfer.get(3));
        proteinValue.setText(mealInfotransfer.get(4));
        fatValue.setText(mealInfotransfer.get(5));


        //handling on backpressed
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                FragmentManager fm = getParentFragmentManager();
//                if(fm.getBackStackEntryCount() > 1)   fm.popBackStack();
//                requireActivity().onBackPressed();
//            }
//        };
//
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        //delete shared preference

        DeleteSharedPreference();
//        Add Recent Meal Data
        AddingDataForRecentMeal();

        //TakeaPhotoButton
//        AddPhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(requireActivity(), CameraForMealTracker.class);
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("mealName", mealName.getText().toString());
//                    jsonObject.put("Meal_Type", Meal_Type.toString());
//                    jsonObject.put("calorieValue", calorieValue.getText().toString());
//                    jsonObject.put("carbsValue", carbsValue.getText().toString());
//                    jsonObject.put("fatValue", fatValue.getText().toString());
//                    jsonObject.put("proteinValue", proteinValue.getText().toString());
//                    jsonObject.put("Quantity", numberPicker1List[quantityPicker.getValue()]);
//                    jsonObject.put("Size", numberPicker2List[sizePicker.getValue()]);
//                    intent.putExtra("mealInfoForPhoto", jsonObject.toString());
//
//                    //Sharedpref
//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("mealInfoForPhoto", jsonObject.toString());
//                    editor.apply();
//
//
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Log.d("Exception", e.toString());
//                }
////
//            }
//        });


        //FavouriteMealButton'
        AddToMealTrackerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityValue = String.valueOf(quantityPicker.getValue());
                String sizeValue = numberPicker2List[sizePicker.getValue()];
//                 Create a Todays_BreakFast_info object with the meal details
//                todays_breakFast_infos = new ArrayList<>(); with this line whenever the button is clicked new Array List is created  that's why any previously loaded added is lost
                Todays_BreakFast_info mealInformation = new Todays_BreakFast_info(
//                         Set your meal's icon here
                        mealName.getText().toString(),
                        calorieValue.getText().toString(),
                        fatValue.getText().toString(),
                        proteinValue.getText().toString(),
                        carbsValue.getText().toString(),
                        quantityValue,
                        sizeValue);
                sharedViewModel.addMeal(mealInformation);
//                todays_breakFast_infos.add(mealInfo);
//                adapter_todays_breakFast = new Adapter_Todays_BreakFast(getContext(), todays_breakFast_infos);
//                adapter_todays_breakFast.addItem(mealInfo);
//                Log.d("item added","data size:"+ todays_breakFast_infos.size());
//                 Navigate to the fragment with the RecyclerView
                Navigation.findNavController(v).navigate(R.id.action_calorie_meal_info_with_photo2_to_fragmentTodays_BreakFast);
            }


        });

        FavouriteMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsFavourite == false) {
                    AddFavourite();

                } else {
                    RemoveFavourite();
                }
            }
        });
        return view;
    }

    private void setupNumberPicker(NumberPicker numberPicker, String[] values, ImageButton upButton, ImageButton downButton) {
        numberPicker.setDisplayedValues(values);
        numberPicker.setMaxValue(values.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setValue(1);

        if (upButton != null) {
            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberPicker.setValue(numberPicker.getValue() + 1);
                    updateNutritionalValues();
                    Log.d("TAG", "up arrow clicked.New value" + numberPicker.getValue());
                }
            });
        }

        if (downButton != null) {
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberPicker.setValue(numberPicker.getValue() - 1);
                    updateNutritionalValues();
                    Log.d("TAG", "down arrow clicked.New value" + numberPicker.getValue());
                }
            });
        }

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateNutritionalValues();
                Log.d("TAG", "numberPicker scrolled");
            }
        });
    }

    private void updateNutritionalValues() {
        String calorieString = mealInfotransfer.get(2).replaceAll("[^\\d.]", ""); // Remove non-numeric characters
        double calorie = Double.parseDouble(calorieString);

        String carbsString = mealInfotransfer.get(3).replaceAll("[^\\d.]", "");
        double carbs = Double.parseDouble(carbsString);

        String proteinString = mealInfotransfer.get(4).replaceAll("[^\\d.]", "");
        double protein = Double.parseDouble(proteinString);

        String fatString = mealInfotransfer.get(5).replaceAll("[^\\d.]", "");
        double fat = Double.parseDouble(fatString);

        int selectedQuantityFromPicker = quantityPicker.getValue();
        int selectedSizeFromPicker = sizePicker.getValue();

        double selectedQuantity = Double.parseDouble(numberPicker1List[selectedQuantityFromPicker]);
        double selectedSize = 1.0;

        if (selectedSizeFromPicker >= 0 && selectedSizeFromPicker < numberPicker2List.length) {
            selectedSize = Double.parseDouble(numberPicker2List[selectedSizeFromPicker]
                    .toLowerCase()
                    .replace("small", "0.5")
                    .replace("regular", "1")
                    .replace("large", "2"));
        }
        double newCaloriesValue = calorie * selectedQuantity * selectedSize;
        double newCarbsValue = carbs * selectedQuantity * selectedSize;
        double newProteinValue = protein * selectedQuantity * selectedSize;
        double newFatsValue = fat * selectedQuantity * selectedSize;

//        set the text view of nutrients with updated values
        calorieValue.setText(String.format("%.2f", newCaloriesValue));
        carbsValue.setText(String.format("%.2f", newCarbsValue));
        proteinValue.setText(String.format("%.2f", newProteinValue));
        fatValue.setText(String.format("%.2f", newFatsValue));

    }


    private void DeleteSharedPreference() {

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("tracker", "RecentMealInfo");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0L, 59 * 1000, pendingIntent);
    }

    private void AddingDataForRecentMeal() {
        try {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RecentMeal", MODE_PRIVATE);
            if (sharedPreferences.contains("RecentMealInfo")) {
                JSONObject jsonObject2 = new JSONObject(sharedPreferences.getString("RecentMealInfo", ""));
                JSONArray jsonArray1 = jsonObject2.getJSONArray("RecentMealInfo");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    if (jsonArray1.getString(i).contains(mealName.getText().toString()) == false) {
                        mainJsonArray.put(jsonArray1.getJSONObject(i));
                    }
                }
            }
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name", mealName.getText().toString());
            jsonObject1.put("calorie", calorieValue.getText().toString());
            jsonObject1.put("protin", proteinValue.getText().toString());
            jsonObject1.put("carb", carbsValue.getText().toString());
            jsonObject1.put("fat", fatValue.getText().toString());
            jsonObject1.put("icon", mealInfotransfer.get(6));
            mainJsonArray.put(jsonObject1);
            mainJSONobj.put("RecentMealInfo", mainJsonArray);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("RecentMealInfo", mainJSONobj.toString());
            editor.commit();
            Log.d("RecentMeal", sharedPreferences.getString("RecentMealInfo", ""));


        } catch (Exception exception) {
            Log.d("Exception", exception.toString());
        }
    }

    private void AddFavourite() {
        requestQueue1 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, response -> {
            Log.d("response123", response);
            if (response.equals("success")) {
                Toast.makeText(getContext(), "Food added to favourite successfully", Toast.LENGTH_LONG).show();
                IsFavourite = true;
                FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favourite_clicked));
            } else if (response.equals("failed")) {
                Toast.makeText(getContext(), "failed to add Food to favourite", Toast.LENGTH_LONG).show();
                FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favorite));
                IsFavourite = false;
            }

        }, error -> {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String user = "test";
//                if(!DataFromDatabase.clientuserID.isEmpty()) user = DataFromDatabase.clientuserID;
                Map<String, String> params = new HashMap<>();
                params.put("clientuserID", DataFromDatabase.clientuserID);
                params.put("FavouriteFoodName", mealName.getText().toString());
                params.put("calorie", calorieValue.getText().toString());
                params.put("protein", proteinValue.getText().toString());
                params.put("carb", carbsValue.getText().toString());
                params.put("fat", fatValue.getText().toString());
                return params;
            }

        };
        requestQueue1.add(stringRequest1);

    }

    private void RemoveFavourite() {
        requestQueue2 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, response -> {
            Log.d("response123", response);
            if (response.equals("success")) {
                Toast.makeText(getContext(), "Food removed from favourite successfully", Toast.LENGTH_LONG).show();
                IsFavourite = false;
                FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favorite));
            } else if (response.equals("failed")) {
                Toast.makeText(getContext(), "failed to remove Food from favourite", Toast.LENGTH_LONG).show();
                FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favourite_clicked));
                IsFavourite = true;
            }

        }, error -> {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String user = "test";
//                if(!DataFromDatabase.clientuserID.isEmpty()) user = DataFromDatabase.clientuserID;
                Map<String, String> params = new HashMap<>();
                params.put("clientuserID", user);
                params.put("FavouriteFoodName", mealName.getText().toString());
                return params;
            }

        };
        requestQueue2.add(stringRequest2);

    }

    private void hooks(View view) {


        mealName = view.findViewById(R.id.mealName);
        fatValue = view.findViewById(R.id.fatValue);
        calorieValue = view.findViewById(R.id.calorieValue);
        proteinValue = view.findViewById(R.id.proteinValue);
        carbsValue = view.findViewById(R.id.carbsValue);

        bottomSheetN = view.findViewById(R.id.bottomSheetN);

        quantityPicker = view.findViewById(R.id.numberPicker1);
        sizePicker = view.findViewById(R.id.numberPicker2);
        uparrow1 = view.findViewById(R.id.uparrow1);
        downarrow1 = view.findViewById(R.id.downarrow1);
        uparrow2 = view.findViewById(R.id.uparrow2);
        downarrow2 = view.findViewById(R.id.downarrow2);
        AddToMealTrackerButton = view.findViewById(R.id.AddToMealTrackerButton);


        FavouriteMealButton = view.findViewById(R.id.favouriteMealButton);

        //calorieImgback
        calorieImgback = view.findViewById(R.id.calorieImgback);
        calorieImgback.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    public void getFavouriteFoodItems() {
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("favouriteFoodItems");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.getString("nameofFoodItem").equals(mealName.getText().toString()) == true) {
                        IsFavourite = true;
                        FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favourite_clicked));
                        break;
                    } else {
                        FavouriteMealButton.setImageDrawable(getActivity().getDrawable(R.drawable.favorite));
//                        break;
                    }
                }
            } catch (JSONException e) {
                Log.d("JSONException", e.toString());
            }
        }, error -> {
//            Toast.showToast(requireActivity(),error.toString());
//            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
//                data.put("clientuserID", DataFromDatabase.clientuserID);
                data.put("clientuserID", "test");
                return data;
            }
        };
        requestQueue.add(stringRequest);
    }


}