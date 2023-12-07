package com.example.infits;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class cameraStart extends AppCompatActivity {

    SharedPrefManager spf;

    static Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_start);

        spf = new SharedPrefManager(getApplicationContext());

        ActivityResultLauncher<Intent> activityResultLauncher_click_photo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Bundle bundle = result.getData().getExtras();
                    photo = (Bitmap) bundle.get("data");
                    EditProfile.profile_pic.setImageBitmap(photo);
                    DataFromDatabase.profile = photo;
                    spf.createSession(photo);
                    finish();
                }
        );
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher_click_photo.launch(intent1);
    }


    @Override
    public void onBackPressed()
    {
        finish();
    }
}