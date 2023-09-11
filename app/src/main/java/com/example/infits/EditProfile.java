package com.example.infits;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    ImageView male, female,profile_pic, backBtn;
    Button save, editProfile;
    String client_gender;
    String upload_Image;
    String url = String.format("%ssave.php",DataFromDatabase.ipConfig);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toast.makeText(this,DataFromDatabase.clientuserID,Toast.LENGTH_LONG);

        male = findViewById(R.id.gender_male_icon);
        female = findViewById(R.id.gender_female_icon);
        EditText name = findViewById(R.id.name_edt);
        name.setText(DataFromDatabase.name);
        EditText age = findViewById(R.id.age_edt);
        age.setText(DataFromDatabase.age);
        EditText email = findViewById(R.id.email_edt);
        email.setText(DataFromDatabase.email);
        EditText phone = findViewById(R.id.phone_edt);
        EditText location = findViewById(R.id.location_edt);
        location.setText(DataFromDatabase.location);
        EditText weight = findViewById(R.id.weight_edt);
        weight.setText(DataFromDatabase.weight);
        EditText height = findViewById(R.id.height_edt);
        height.setText(DataFromDatabase.height);
        EditText plan = findViewById(R.id.plan_edt);
        plan.setText("Basic");


        phone.setText(DataFromDatabase.mobile);
        profile_pic = findViewById(R.id.dp);
        profile_pic.setImageBitmap(DataFromDatabase.profile);
        backBtn = findViewById(R.id.imgBack);
        editProfile = findViewById(R.id.button_editProfile);


        ImageView name_btn = findViewById(R.id.name_edt_button);
        ImageView age_btn = findViewById(R.id.age_edt_button);
        ImageView email_btn = findViewById(R.id.email_edt_button);
        ImageView phone_btn = findViewById(R.id.phone_edt_button);
        ImageView location_btn = findViewById(R.id.location_edt_button);
        ImageView height_btn = findViewById(R.id.height_edt_button);
        ImageView weight_btn = findViewById(R.id.weight_edt_button);
        Button editProfile = findViewById(R.id.button_editProfile);
        ImageView profile = findViewById(R.id.dp);


        if(DataFromDatabase.gender.equals("M")) {
            male.setImageResource(R.drawable.gender_male_selected);
            female.setImageResource(R.drawable.gender_female);
        } else {
            male.setImageResource(R.drawable.gender_male);
            female.setImageResource(R.drawable.gender_female_selected);
        }

        backBtn.setOnClickListener(v -> this.finish());

        name_btn.setOnClickListener(v -> {
            name.setCursorVisible(true);
            name.setFocusableInTouchMode(true);
            name.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        age_btn.setOnClickListener(v -> {
            age.setCursorVisible(true);
            age.setFocusableInTouchMode(true);
            age.setInputType(InputType.TYPE_CLASS_NUMBER);
        });

        email_btn.setOnClickListener(v -> {
            email.setCursorVisible(true);
            email.setFocusableInTouchMode(true);
            email.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        phone_btn.setOnClickListener(v -> {
            phone.setCursorVisible(true);
            phone.setFocusableInTouchMode(true);
            phone.setInputType(InputType.TYPE_CLASS_PHONE);
        });

        location_btn.setOnClickListener(v -> {
            location.setCursorVisible(true);
            location.setFocusableInTouchMode(true);
            location.setInputType(InputType.TYPE_CLASS_PHONE);
        });

        height_btn.setOnClickListener(v -> {
            height.setCursorVisible(true);
            height.setFocusableInTouchMode(true);
            height.setInputType(InputType.TYPE_CLASS_NUMBER);
        });

        weight_btn.setOnClickListener(v -> {
            weight.setCursorVisible(true);
            weight.setFocusableInTouchMode(true);
            weight.setInputType(InputType.TYPE_CLASS_NUMBER);
        });

        client_gender=DataFromDatabase.gender;
        male.setOnClickListener(v ->  {
            male.setImageResource(R.drawable.gender_male_selected);
            female.setImageResource(R.drawable.gender_female);
            client_gender="M";
        });
        female.setOnClickListener(v ->  {
            male.setImageResource(R.drawable.gender_male);
            female.setImageResource(R.drawable.gender_female_selected);
            client_gender="F";
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        editProfile.setOnClickListener(v -> {

            String Name,Gender,Age,Email,PhoneNo,Location,Height,Weight,Client;

            Name= name.getText().toString();
            Age= age.getText().toString();
            Email= email.getText().toString();
            PhoneNo = phone.getText().toString();
            Location=location.getText().toString();
            Height=height.getText().toString();
            Weight=weight.getText().toString();
            Gender= client_gender;
            Client=DataFromDatabase.clientuserID;

            DataFromDatabase.name = Name;
            DataFromDatabase.email = Email;
            DataFromDatabase.mobile = PhoneNo;
            DataFromDatabase.location = Location;
            DataFromDatabase.age = Age;
            DataFromDatabase.gender  = Gender;
            DataFromDatabase.weight  = Weight;
            DataFromDatabase.height  = Height;

            RequestQueue requestQueue= Volley.newRequestQueue(EditProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
                if(response.equals("failure")){
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Updated Successful",Toast.LENGTH_LONG).show();
                    Log.e("loginInfo",response.toString());
                    Log.d("Response Login",response);
                }
            }, error -> {
                String errorMessage = "Unknown error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                }
                Log.e("VolleyError", errorMessage); // Log the complete error message
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }){
                @Override
                @Nullable
                protected Map<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> data = new HashMap<>();
                    data.put("clientuserID",Client);
                    data.put("name",Name);
                    data.put("email",Email);
                    data.put("mobile",PhoneNo);
                    data.put("gender",Gender);
                    data.put("age",Age);
                    data.put("location",Location);
                    data.put("height",Height);
                    data.put("weight",Weight);
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        });
    }
    private void selectImage() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            Uri uri  = data.getData();
            profile_pic.setImageURI(uri);

            // Convert the URI to a Bitmap
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the Bitmap to the ImageView
            DataFromDatabase.profile = bitmap;
            String Client=DataFromDatabase.clientuserID;

            upload_Image=bitmapToBase64(bitmap);

            // code to upload image in php  starts here

            String url = String.format("%simageUpload.php",DataFromDatabase.ipConfig);

            RequestQueue requestQueue= Volley.newRequestQueue(EditProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
                if(response.equals("failure")){
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Updated Successful",Toast.LENGTH_LONG).show();
                }
            }, error -> {
                String errorMessage = "Unknown error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                }
                Log.e("VolleyError", errorMessage); // Log the complete error message
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }){
                @Override
                @Nullable
                protected Map<String,String> getParams() throws AuthFailureError {
                    HashMap<String,String> data = new HashMap<>();
                    data.put("clientuserID",Client);
                    data.put("imageData",upload_Image);
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            //  code to upload image in php end here
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}