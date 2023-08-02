package com.example.infits;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Instantiate the fragment
        OverviewFragment myFragment = new OverviewFragment();

        // Add the fragment to the container view
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ClassBeta, myFragment)
                .commit();
    }
}
