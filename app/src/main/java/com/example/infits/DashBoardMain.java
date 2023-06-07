package com.example.infits;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class DashBoardMain extends AppCompatActivity implements DashBoardFragment.OnMenuClicked, UpdateStepCard {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private String[] allPermissions;
    boolean drawerState = false;
    String cardClicked;
    String hours, minutes, secs;
    TextView drawerusername,draweruserplan;


//    String url ="http://192.168.1.7/infits/recipiesDisplay.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_main);

        /*
        fragment.getView().post(() -> {
             ImageView imageView = fragment.getView().findViewById(R.id.profile1);
            imageView.setImageResource(R.drawable.profile);
        });*/


        permissionsCheck();

        permissions();



        nav = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer);

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
                        Intent icL = new Intent(DashBoardMain.this, LiveListAct.class);
                        startActivity(icL);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.consul:
                        Intent icl = new Intent(DashBoardMain.this, Consultation.class);
                        startActivity(icl);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                case R.id.setting:
                    Intent ist = new Intent(DashBoardMain.this, Settings.class);
                    startActivity(ist);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

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
}