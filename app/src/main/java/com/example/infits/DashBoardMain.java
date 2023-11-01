package com.example.infits;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.fido.fido2.api.common.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardMain extends AppCompatActivity implements DashBoardFragment.OnMenuClicked, UpdateStepCard {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private String[] allPermissions;
    boolean drawerState = false;
    String cardClicked;
    String hours, minutes, secs;
    TextView drawerusername,draweruserplan;
    CircleImageView profilePicNav;
    String Entered;

    boolean isMealNotificationFinished;
    SharedPreferences sharedPreferences;
    String profilePicimg = "https://infits.in/androidApi/upload/default.jpg";
    //String url ="http://192.168.29.222/infits/recipiesDisplay.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_main);
        sharedPreferences = this.getSharedPreferences("mealInfo",MODE_PRIVATE);
        /*
        fragment.getView().post(() -> {
             ImageView imageView = fragment.getView().findViewById(R.id.profile1);
            imageView.setImageResource(R.drawable.profile);
        });*/


        permissionsCheck();

        permissions();



        nav = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer);
        profilePicNav = findViewById(R.id.drawer_profile_pic);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerusername = findViewById(R.id.drawer_username);
                draweruserplan = findViewById(R.id.drawer_user_plan);
                drawerusername.setText(DataFromDatabase.name);
                drawerState = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerState = false;
            }
        });

//        Navigation profile pic
        Glide.with(DashBoardMain.this).load(profilePicimg).fitCenter().into(profilePicNav);

        /*
        if (!DataFromDatabase.proUser){
            Menu nav_Menu = nav.getMenu();
            nav_Menu.findItem(R.id.consul).setVisible(false);
            //nav_Menu.findItem(R.id.message).setVisible(false);
            nav_Menu.findItem(R.id.live).setVisible(false);
            nav_Menu.findItem(R.id.scan).setVisible(false);
        }*/


        cardClicked = getIntent().getStringExtra("notification");

        if(cardClicked != null) {
            if(cardClicked.equals("sleep")) {
                hours = getIntent().getStringExtra("hours");
                minutes = getIntent().getStringExtra("minutes");
                secs = getIntent().getStringExtra("secs");
            }
        }

        nav.setNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.dboard:
                    Intent idb = new Intent(DashBoardMain.this, DashBoardMain.class);
                    startActivity(idb);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    finish();
                    break;

                case R.id.charts:
                    Intent ich = new Intent(DashBoardMain.this, Statistics.class);
                    startActivity(ich);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.live:
                    if (DataFromDatabase.proUser){
                        Intent icL = new Intent(DashBoardMain.this, LiveListAct.class);
                        startActivity(icL);
                    }
                    else {
                        //showDialog();
                        Toast toast = Toast.makeText(this,"Required pro account",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.consul:
                    if (DataFromDatabase.proUser){
                        Intent icl = new Intent(DashBoardMain.this, Consultation.class);
                        startActivity(icl);
                    }
                    else {
                        // showDialog();
                        Toast toast = Toast.makeText(this,"Required pro account",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.setting:
                    Intent ist = new Intent(DashBoardMain.this, Settings.class);
                    startActivity(ist);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                //case R.id.recipes:
                //Intent h = new Intent(DashBoardMain.this, recipies.class);
                //startActivity(h);
                //drawerLayout.closeDrawer(GravityCompat.START);

                //break;

                    /*
                    case R.id.message:
                        Intent imt = new Intent(DashBoardMain.this, ChatArea.class);
                        startActivity(imt);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;*/

                case R.id.scan:
                    Intent iscan = new Intent(DashBoardMain.this,ScanActivity.class);
                    startActivity(iscan);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });


        isMealNotificationFinished = sharedPreferences.getBoolean("mealRunning",true);
        SharedPreferences.Editor editor = (SharedPreferences.Editor) sharedPreferences.edit();


        if(isMealNotificationFinished) {

            isMealNotificationFinished = false;
            editor.putBoolean("mealRunning",isMealNotificationFinished);
            editor.apply();

            long BreakfastTime = 7 * 3600 * 1000 + 30 * 60 * 1000;
            long LunchTime = 13 * 3600 * 1000;
            long SnackTime = 16 * 3600 * 1000 + 30 * 60 * 1000;
            long DinnerTime = 20 * 3600 * 1000;

            long AfterBreakfastTime = 8 * 3600 * 1000;
            long AfterLunchTime = 13 * 3600 * 1000 + 30 * 60 * 1000;
            long AfterSnackTime = 17 * 3600 * 1000;
            long AfterDinnerTime = 20 * 3600 * 1000 + 30 * 60 * 1000;
            createNotificationChannel();

            setMealAlarm(BreakfastTime, "breakfast", 601);
            setMealAlarm(LunchTime, "lunch", 602);
            setMealAlarm(SnackTime,"snack",603);
            setMealAlarm(DinnerTime,"dinner",604);

            setMealAlarm(AfterBreakfastTime, "Afterbreakfast", 605);
            setMealAlarm(AfterLunchTime, "Afterlunch", 606);
            setMealAlarm(AfterSnackTime,"Aftersnack",607);
            setMealAlarm(AfterDinnerTime,"Afterdinner",608);
        }

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CalorieChannelId", "calNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void setMealAlarm(long alarmTime,String text,int alarmCode) {
        long NotificationTimeInMillis = alarmTime;
        long todayTimeInMillis = org.joda.time.LocalDateTime.now().getMillisOfDay();
        long timeDiff;
        if(todayTimeInMillis > NotificationTimeInMillis){
            long oneDayInMillis  = 24*3600*1000;
            timeDiff = oneDayInMillis - (todayTimeInMillis-NotificationTimeInMillis);
        }else {
            timeDiff = NotificationTimeInMillis - todayTimeInMillis;
        }
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

        Intent intent = new Intent(DashBoardMain.this,CalorieNotificationReceiver.class);
        intent.putExtra("Meal",text);
        PendingIntent caloriePendingIntent = PendingIntent.getBroadcast(this,alarmCode,intent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+timeDiff,caloriePendingIntent);

        Log.i("Alarm set at "+timeDiff/1000, "Done!");
    }

    private void permissionsCheck() {
        if(!hasPermissions(DashBoardMain.this,allPermissions)){
            ActivityCompat.requestPermissions(DashBoardMain.this,allPermissions,1234);
        }
    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            allPermissions = new String[]{
                    Manifest.permission.BODY_SENSORS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO
            };
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1234){
            boolean x=false;
            for(int i=0;i<allPermissions.length;i++)
            {
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "All Permissions are required", Toast.LENGTH_SHORT).show();
                }else{
                    x=true;
                }
            }
            if(x){
                Toast.makeText(this, "All permissions are required", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean hasPermissions(Context context, String...  allPermissions){
        if(context!=null && allPermissions !=null){
            for(String allPermit :allPermissions){
                if(ActivityCompat.checkSelfPermission(context,allPermit)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("Weight",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("weight", "0");
        myEdit.putString("weightChangeDate", "");

        myEdit.apply();
    }

    @Override
    public void menuClicked() {
        if(!drawerState) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public String getCardClicked() {
        return cardClicked;
    }

    public String getSleepTime() {
        return String.format(Locale.getDefault(), "%s:%s:%s", hours, minutes, secs);
    }

    @Override
    public void updateStepCardData(Intent intent) {
        Log.wtf("mainFrag", "entered");
        DashBoardFragment.updateStepCard(intent);
    }

    //  private void showDialog() {
    //  final Dialog dialog = new Dialog(this);dialog.setCancelable(true);
    //  dialog.setContentView(R.layout.referralcodedialog);
    //  final EditText referralCode = dialog.findViewById(R.id.referralcode);
    //  ImageView checkReferral = dialog.findViewById(R.id.checkReferral);
    //  checkReferral.setOnClickListener(vi->{

    //     //String referralUrl = String.format("%sverify.php",DataFromDatabase.ipConfig);
    //     String urlRefer = "https://infits.in/androidApi/verify.php";
    //       StringRequest stringRequest = new StringRequest(Request.Method.POST,urlRefer,
    //             response->{
    //                 Log.d("DietitianVerification", response);
    ///
    //                if(response.equals("found")) {
    //                   showSuccessDialog();
    //                   System.out.println("Verified");
    //               }else {
    //                   System.out.println("Not verified");
    //                   showFailureDialog();
    //               }

    //           },error->{

    //   }){
    //      @Nullable
    //      @Override
    //      protected Map<String, String> getParams() throws AuthFailureError {
    //
    //          Map<String,String> data = new HashMap<>();
    //
    //          //data.put("clientID",DataFromDatabase.clientuserID);
    //          Entered = referralCode.getText().toString();
    //          data.put("dietitian_verify_code",Entered);
    //          data.put("type","1");


    //         return data;
    //      }
    //   };
    //    Volley.newRequestQueue(this).add(stringRequest);
    //     stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
    //             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    //             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //     dialog.dismiss();
    //  });
    //  dialog.show();
    //  }

    // private void showSuccessDialog() {
    //   Dialog dialog = new Dialog(this);
    //  dialog.setContentView(R.layout.referral_congratulation);
    // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    //  ImageView btn = dialog.findViewById(R.id.btn);


    //   btn.setOnClickListener(v -> {
    //      dialog.dismiss();

    //   });

    //  dialog.show();
    //  }

    // private void showFailureDialog() {
    //  Dialog dialog = new Dialog(this);
    // dialog.setContentView(R.layout.wrong_referral_dialog);
    //  dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    // Button btn = dialog.findViewById(R.id.try_again);

    // btn.setOnClickListener(v -> dialog.dismiss());

    //  dialog.show();
    // }
}