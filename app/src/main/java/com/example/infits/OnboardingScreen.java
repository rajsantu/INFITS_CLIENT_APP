package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.IStatusCallback;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import io.reactivex.rxjava3.annotations.NonNull;

public class OnboardingScreen extends AppCompatActivity {
    ViewPager viewPager;
    SliderOnboardingScreenAdapter sliderOnboardingScreenAdapter;
    WormDotsIndicator wormdotsIndicator;
    CardView skipButton,getStarted;
    private int lastPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewPager = findViewById(R.id.pageView);
        skipButton = findViewById(R.id.skipButton);
        getStarted = findViewById(R.id.getStarted);
        wormdotsIndicator = findViewById(R.id.worm_dots_indicator);
        // calling adapter Starts
        sliderOnboardingScreenAdapter = new SliderOnboardingScreenAdapter(this);
        viewPager.setAdapter(sliderOnboardingScreenAdapter);
        // calling adapter Ends
        wormdotsIndicator.attachTo(viewPager);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        // Skip Button Pressed
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out); // Added
                finish();
            }
        });
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out); // Added
                finish();
            }
        });
    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position){
            if(position>lastPosition){ // page is Scrolled right
                if(position==0){
                    getStarted.setVisibility(View.INVISIBLE);
                    skipButton.setVisibility(View.VISIBLE);
                    lastPosition = position;
                } else if (position==1) {
                    getStarted.setVisibility(View.GONE);
                    skipButton.setVisibility(View.VISIBLE);
                    lastPosition = position;
                }else {
                    getStarted.setVisibility(View.VISIBLE);
                    getStarted.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_slide_in));
                    skipButton.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_slide_out));
                    skipButton.setVisibility(View.INVISIBLE);
                    lastPosition = position;
                }
            } else if (position < lastPosition) { // page is Scrolled Left
                if(lastPosition-position>=2){ // If directly page 1 is opened from the third page
                    getStarted.setVisibility(View.INVISIBLE);
                    getStarted.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_slide_out));
                    skipButton.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_slide_in));
                    skipButton.setVisibility(View.VISIBLE);
                    lastPosition = position;
                }
                if(position==0){
                    getStarted.setVisibility(View.INVISIBLE);
                    skipButton.setVisibility(View.VISIBLE);
                    lastPosition = position;
                } else if (position==1) {
                    getStarted.setVisibility(View.INVISIBLE);
                    getStarted.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_slide_out));
                    skipButton.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_slide_in));
                    skipButton.setVisibility(View.VISIBLE);
                    lastPosition = position;
                }else {
                    getStarted.setVisibility(View.VISIBLE);
                    skipButton.setVisibility(View.INVISIBLE);
                    lastPosition = position;
                }
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}