package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.animation.Animator; // Animator Import
import android.graphics.BitmapFactory;
import android.media.MediaPlayer; // Media Import
import android.net.Uri; // URI import
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView; // VideoView Import

public class SplashScreen extends AppCompatActivity {

    Intent intent;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // installing SplashScreen Starts
        androidx.core.splashscreen.SplashScreen splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        // installing SplashScreen Ends
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // VideoView for the SplashScreen Animation Starts
        videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.logo_animation_splash_screen;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        // // VideoView for the SplashScreen Animation Ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                boolean isLoggedIn = loginDetails.getBoolean("hasLoggedIn", false);
                // Shared preferences for showing the OnBoarding Screen Starts.
                SharedPreferences OnBoardingScreenSharedPreferences = getSharedPreferences("OnBoardingScreen",MODE_PRIVATE);
                boolean isFirstTimeAppOpen = OnBoardingScreenSharedPreferences.getBoolean("isFirstTimeAppOpen",true);
                // Shared preferences for showing the OnBoarding Screen Continues in "else if" Part.

                if(isLoggedIn) { // Condition 1: If the user is already loggedin,he will directed to DashBoardMain.
                    intent = new Intent(SplashScreen.this, DashBoardMain.class);
                    setDataFromDatabase(loginDetails);
                    String cameFromNotification = getIntent().getStringExtra("notification");
                    intent.putExtra("notification", cameFromNotification);
                    if(cameFromNotification != null) {
                        if(cameFromNotification.equals("sleep")) {
                            intent.putExtra("hours", getIntent().getStringExtra("hours"));
                            intent.putExtra("minutes", getIntent().getStringExtra("minutes"));
                            intent.putExtra("secs", getIntent().getStringExtra("secs"));
                        }
                    }

                }

                else if(isFirstTimeAppOpen){ // Case 2: If the the user is first time user of the application, he will directed to OnboardingScreen.
                    SharedPreferences.Editor editor = OnBoardingScreenSharedPreferences.edit();
                    editor.putBoolean("isFirstTimeAppOpen",false);
                    editor.apply();
                    intent = new Intent(getApplicationContext(),OnboardingScreen.class);
                }
                else { // Case 3: If the the user is occasional user of the application but he is loggedout, he will directed to the Login Activity.
                    intent = new Intent(getApplicationContext(),Login.class);
                }

                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out); // Added
                finish();
            }
        }, 2900); // Changed to 2900 ms.
    }

    private void setDataFromDatabase(SharedPreferences prefs) {
        DataFromDatabase.flag = prefs.getBoolean("flag", true);
        DataFromDatabase.clientuserID = prefs.getString("clientuserID", "");
        DataFromDatabase.client_id = prefs.getString("client_id", "");
        DataFromDatabase.dietitian_id = prefs.getString("dietitian_id","");
        DataFromDatabase.dietitianuserID = prefs.getString("dietitianuserID", "");
        DataFromDatabase.name = prefs.getString("name", "");
        DataFromDatabase.password = prefs.getString("password", "");
        DataFromDatabase.email = prefs.getString("email", "");
        DataFromDatabase.mobile = prefs.getString("mobile", "");
        DataFromDatabase.profilePhoto = prefs.getString("profilePhoto", "");
        DataFromDatabase.location = prefs.getString("location", "");
        DataFromDatabase.age = prefs.getString("age", "");
        DataFromDatabase.gender = prefs.getString("gender", "");
        DataFromDatabase.weight = prefs.getString("weight", "");
        DataFromDatabase.height = prefs.getString("height", "");
        DataFromDatabase.profilePhotoBase = prefs.getString("profilePhotoBase", "");
        DataFromDatabase.proUser = prefs.getBoolean("proUser", false);
        byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
        DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);
        DataFromDatabase.verification = prefs.getString("verification","");
    }
}