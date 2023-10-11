
package com.example.infits;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

public class CameraForMealTracker extends AppCompatActivity {

    byte[] photoArr;

    Bitmap food_eaten_photoBitmap;
    int SELECT_PICTURE=200;
    SurfaceView surfaceView;

    com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource cameraSourceFlash;


    RelativeLayout camera_layout;

    ImageView saveImageButton, gallery,food_eaten_photo;
    String jsonObject;
    Button takePhoto;
    BarcodeDetector barcodeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_for_meal_tracker);


        Intent getIntent = getIntent();
        jsonObject=getIntent.getStringExtra("mealInfoForPhoto");



        surfaceView = findViewById(R.id.camera_screen);

        takePhoto = findViewById(R.id.take);
        takePhoto.setEnabled(true);
        takePhoto.setVisibility(View.VISIBLE);


        saveImageButton = findViewById(R.id.saveImageButton);
        saveImageButton.setEnabled(false);

        gallery = findViewById(R.id.gallery);

        camera_layout = findViewById(R.id.camera_layout);



        food_eaten_photo = findViewById(R.id.food_eaten_photo);



        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        cameraSourceFlash = new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO)
                .setFlashMode(Camera.Parameters.FLASH_MODE_AUTO)
                .build();

//        saveImageButton.setOnClickListener(v -> {
//            try {
//                JSONObject jsonObject1=new JSONObject(getIntent.getStringExtra("mealInfoForPhoto"));
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                food_eaten_photoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//                String base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//
////                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, Activity_Todays_Breakfast.class);
////                try {
////                    JSONArray jsonArray = new JSONArray(jsonObject);
////                    // Do something with the JSONArray...
////                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
////                } catch (JSONException e) {
////                    Toast.makeText(this, "NotDone", Toast.LENGTH_SHORT).show();
////                    e.printStackTrace();
////                }
//                intent.putExtra("mealInfoForPhoto", jsonObject.toString());
//                intent.putExtra("fragment","MealtrackerTodays_Breakfast.class");
////
////
////                for saving bitmap
//                SharedPreferences sharedPreferences=getSharedPreferences("BitMapInfo", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//
//                editor.putString("ClickedPhoto",base64String);
//                editor.commit();
//
//                Log.d("ClickedPhoto", jsonObject1.toString());
//                startActivity(intent);
//            }catch (Exception e){
//                Log.d("Exception123",e.toString());
//                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//        });

        saveImageButton.setOnClickListener(v -> {
            try {
                JSONObject jsonObject1 = new JSONObject(getIntent.getStringExtra("mealInfoForPhoto"));

                // Convert bitmap to base64 string
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                food_eaten_photoBitmap.compress(Bitmap.CompressFormat.JPEG, 1, outputStream);
                String base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                // Add base64 string to jsonObject1
//                jsonObject1.put("image", base64String);
                String uniqueID = UUID.randomUUID().toString();
                jsonObject1.put("image",base64String);

                Intent intent = new Intent(this, Activity_Todays_Breakfast.class);
                intent.putExtra("mealInfoForPhoto", jsonObject1.toString());
                intent.putExtra("fragment", "MealtrackerTodays_Breakfast.class");
                // Save base64 string in shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("BitMapInfo", Context.MODE_PRIVATE);
//                SharedPreferences sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ClickedPhoto", base64String);
                editor.putString(uniqueID, base64String);
                editor.commit();

                Log.d("jsonobj1", jsonObject1.toString());

                startActivity(intent);
            } catch (Exception e) {
                Log.d("Exception123", e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

            }
        });


        takePhoto.setOnClickListener(v -> {
            saveImageButton.setVisibility(View.VISIBLE);
            saveImageButton.setEnabled(true);
            cameraSourceFlash.takePicture(new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource.ShutterCallback() {
                @Override
                public void onShutter() {

                }
            }, new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource.PictureCallback() {
                @Override
                public void onPictureTaken(@NonNull byte[] bytes) {

                    photoArr = bytes;

                    food_eaten_photoBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    surfaceView.setVisibility(View.INVISIBLE);
                    food_eaten_photo.setVisibility(View.VISIBLE);
//                    food_eaten_photo.setRotationX(90);
                    food_eaten_photo.setImageBitmap(food_eaten_photoBitmap);


                }
            });
        });

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(CameraForMealTracker.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSourceFlash.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(CameraForMealTracker.this, new
                                String[]{Manifest.permission.CAMERA}, 201);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSourceFlash.stop();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
                        surfaceView.setVisibility(View.INVISIBLE);
                        surfaceView.setBackgroundColor(Color.BLACK);
                        food_eaten_photo.setVisibility(View.VISIBLE);
                        saveImageButton.setVisibility(View.VISIBLE);
                        saveImageButton.setEnabled(true);
                        takePhoto.setEnabled(false);
                        takePhoto.setVisibility(View.INVISIBLE);
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        food_eaten_photo.setImageBitmap(image);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // update the preview image in the layout
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.setVisibility(View.GONE);
        Log.e("TAG", "On Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.setVisibility(View.VISIBLE);
        Log.e("TAG", "On Resume");
    }

}
