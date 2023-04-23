//package com.example.infits;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.hardware.Camera;
//import android.hardware.camera2.CameraAccessException;
//import android.hardware.camera2.CameraCharacteristics;
//import android.hardware.camera2.CameraDevice;
//import android.hardware.camera2.CameraManager;
//import android.media.AudioManager;
//import android.media.ToneGenerator;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.ParcelFileDescriptor;
//import android.util.Base64;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.gms.vision.CameraSource;
//import com.google.android.gms.vision.Detector;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;
//
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileDescriptor;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class CameraForMealTracker extends AppCompatActivity {
//
//    String url = String.format("%ssaveMeal.php",DataFromDatabase.ipConfig);
//
//    byte[] photoArr;
//
//    Bitmap foodEatenPhoto;
//
//    SurfaceView surfaceView;
//
//    TextView meal,datetime;
//
//    EditText name,description;
//
//    com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource cameraSourceFlash;
//
//    CameraSource cameraSource;
//
//    LinearLayout result;
//
//    RelativeLayout camera_layout;
//
//    ImageView turn_on_flash, turn_off_flash,foodEaten;
//
//    Button takePhoto, retakePhoto, saveMeal;
//
//    BarcodeDetector barcodeDetector;
//
//    String mealString;
//
//    ToneGenerator toneGen1;
//    TextView barcodeText;
//    String barcodeData;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera_for_meal_tracker);
//
//        Intent getIntent = getIntent();
//
//        mealString = getIntent.getStringExtra("meal");
//
//        String positionString = getIntent.getStringExtra("position");
//
//        surfaceView = findViewById(R.id.camera_screen);
//
//        takePhoto = findViewById(R.id.take);
//
//        retakePhoto = findViewById(R.id.retake);
//
//        turn_on_flash = findViewById(R.id.flash_on_btn);
//
//        turn_off_flash = findViewById(R.id.flash_off_btn);
//
//        camera_layout = findViewById(R.id.camera_layout);
//
//        result = findViewById(R.id.result);
//
//        foodEaten = findViewById(R.id.food_eaten);
//
//        name = findViewById(R.id.name);
//
//        meal = findViewById(R.id.meal);
//
//        datetime = findViewById(R.id.date_and_time);
//
//        saveMeal = findViewById(R.id.save_meal);
//
//        description = findViewById(R.id.enter_description);
//
//        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
//
//        barcodeText = findViewById(R.id.barcodeText);
//
//        meal.setText(mealString);
//
//        Date date = new Date();
//
//        String dateAndTime = new SimpleDateFormat("d MMM yyyy, h.m a").format(date);
//
//        SimpleDateFormat todayDate = new SimpleDateFormat("d MMM yyyy");
//
//        SimpleDateFormat todayTime = new SimpleDateFormat("h.m.s a");
//
//        barcodeDetector = new BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.ALL_FORMATS)
//                .build();
//
//        retakePhoto.setOnClickListener(v -> {
//            camera_layout.setVisibility(View.VISIBLE);
//            result.setVisibility(View.GONE);
//        });
//
//        cameraSourceFlash = new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource
//                .Builder(this, barcodeDetector)
//                .setRequestedPreviewSize(1920, 1080)
//                .setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO)
//                .setFlashMode(Camera.Parameters.FLASH_MODE_AUTO)
//                .build();
//
//        turn_on_flash.setOnClickListener(v -> {
//            cameraSourceFlash.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//            turn_on_flash.setVisibility(View.GONE);
//            turn_off_flash.setVisibility(View.VISIBLE);
//        });
//
//        turn_off_flash.setOnClickListener(v -> {
//            cameraSourceFlash.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            turn_on_flash.setVisibility(View.VISIBLE);
//            turn_off_flash.setVisibility(View.GONE);
//        });
//
//        takePhoto.setOnClickListener(v -> {
//            cameraSourceFlash.takePicture(new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource.ShutterCallback() {
//                @Override
//                public void onShutter() {
//
//                }
//            }, new com.google.android.gms.samples.vision.barcodereader.ui.camera.CameraSource.PictureCallback() {
//                @Override
//                public void onPictureTaken(@NonNull byte[] bytes) {
//
//                    photoArr = bytes;
//
//                    foodEatenPhoto = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//
//                    foodEaten.setImageBitmap(foodEatenPhoto);
//
//                    camera_layout.setVisibility(View.GONE);
//
//                    result.setVisibility(View.VISIBLE);
//
//                }
//            });
//        });
//
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(CameraForMealTracker.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        cameraSourceFlash.start(surfaceView.getHolder());
//                    } else {
//                        ActivityCompat.requestPermissions(CameraForMealTracker.this, new
//                                String[]{Manifest.permission.CAMERA}, 201);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cameraSourceFlash.stop();
//            }
//        });
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if (barcodes.size() != 0) {
//
//
//                    barcodeText.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            if (barcodes.valueAt(0).email != null) {
//                                barcodeText.removeCallbacks(null);
//                                barcodeData = barcodes.valueAt(0).email.address;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                            } else {
//
//                                barcodeData = barcodes.valueAt(0).displayValue;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        datetime.setText(dateAndTime);
//
//        saveMeal.setOnClickListener(v->{
//            if(name.getText().toString().isEmpty()){
//                Toast.makeText(this, "Please enter the name of the food" , Toast.LENGTH_SHORT).show();
//            }
//            else{
//                StringRequest saveMealToDb = new StringRequest(Request.Method.POST,url, response->{
//
//                    if (response.equals("updated")){
//                        Intent intent = new Intent(getApplicationContext(),Meal_main.class);
//                        intent.putExtra("position", positionString);
//                        intent.putExtra("name", name.getText().toString());
//                        intent.putExtra("description", description.getText().toString());
//                        intent.putExtra("photo",photoArr);
//                        intent.putExtra("meal",mealString);
//                        setResult(RESULT_OK,intent);
//                        finish();
//                    }
//
//                    System.out.println(response);
//
//                },error -> {
//
//                }){
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                        foodEatenPhoto.compress(Bitmap.CompressFormat.JPEG,50, outputStream);
//                        String base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//
//                        String nameString = name.getText().toString();
//                        String descriptionString = description.getText().toString();
//
//                        Map<String,String> data = new HashMap<>();
//
//                        String timeString = todayTime.format(date);
//                        String dateString = todayDate.format(date);
//
//                        data.put("image",base64String);
//                        data.put("description",descriptionString);
//                        data.put("name",nameString);
//                        data.put("date",dateString);
//                        data.put("time",timeString);
//                        data.put("timeMeal",meal.getText().toString());
//                        data.put("clientID","Azarudeen");
//                        data.put("position",positionString);
//
//                        return data;
//                    }
//                };
//                Volley.newRequestQueue(getApplicationContext()).add(saveMealToDb);
//            }
//        });
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        surfaceView.setVisibility(View.GONE);
//        Log.e("TAG", "On Pause");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        surfaceView.setVisibility(View.VISIBLE);
//        Log.e("TAG", "On Resume");
//    }
//}

package com.example.infits;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;

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

        import com.google.android.gms.vision.barcode.Barcode;
        import com.google.android.gms.vision.barcode.BarcodeDetector;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.ByteArrayOutputStream;
        import java.io.FileDescriptor;
        import java.io.IOException;
        import java.util.ArrayList;
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
                food_eaten_photoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                String base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                // Add base64 string to jsonObject1
//                jsonObject1.put("image", base64String);
                String uniqueID = UUID.randomUUID().toString();
                jsonObject1.put("image",uniqueID);

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
