package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;

import java.util.ArrayList;

public class MealTracker extends AppCompatActivity {

    private CardView breakfast,lunch,snack,dinner;
    View bottomSheetN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_tracker);

        //Hooks
        breakfast = findViewById(R.id.meal_op1);
        lunch = findViewById(R.id.meal_op2);
        snack = findViewById(R.id.meal_op3);
        dinner = findViewById(R.id.meal_op4);



        //bottomsheet setting
        bottomSheetN = findViewById(R.id.bottomSheetN);

        BottomSheetBehavior.from(bottomSheetN).setPeekHeight(780);
        BottomSheetBehavior.from(bottomSheetN).setState(BottomSheetBehavior.STATE_COLLAPSED);


        //Each option click listener
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MealtrackerAddMeal.class);
                String message = "Breakfast";
                intent.putExtra("fragment", message);
                startActivity(intent);
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealtrackerAddMeal.class);
                String message = "Lunch";
                intent.putExtra("fragment", message);
                startActivity(intent);
            }
        });
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealtrackerAddMeal.class);
                String message = "Snack";
                intent.putExtra("fragment", message);
                startActivity(intent);
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealtrackerAddMeal.class);
                String message = "Dinner";
                intent.putExtra("fragment", message);
                startActivity(intent);
            }
        });
    }
}