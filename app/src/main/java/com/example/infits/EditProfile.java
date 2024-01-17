package com.example.infits;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    ImageView backBtn, profile_pic, male, female;
    EditText name, age, email, phone, location, plan, height, weight, about;
    Button editProfile;
    String client_gender;
    String upload_Image;
    String url = String.format("%ssave.php", DataFromDatabase.ipConfig);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backBtn = findViewById(R.id.backBtn_EditProfile);
        profile_pic = findViewById(R.id.dp_EditProfile);
        name = findViewById(R.id.name_edt_EditProfile);
        male = findViewById(R.id.gender_male_icon_EditProfile);
        female = findViewById(R.id.gender_female_icon_EditProfile);
        age = findViewById(R.id.age_edt_EditProfile);
        email = findViewById(R.id.email_edt_EditProfile);
        phone = findViewById(R.id.phone_edt_EditProfile);
        location = findViewById(R.id.location_edt_EditProfile);
        plan = findViewById(R.id.plan_edt_EditProfile);
        height = findViewById(R.id.height_edt_EditProfile);
        weight = findViewById(R.id.weight_edt_EditProfile);
        about = findViewById(R.id.about_edt_EditProfile);
        editProfile = findViewById(R.id.button_save_EditProfile);


        if (DataFromDatabase.gender.equals("M")) {
            male.setImageResource(R.drawable.gender_male_selected);
            female.setImageResource(R.drawable.gender_female);
        } else {
            male.setImageResource(R.drawable.gender_male);
            female.setImageResource(R.drawable.gender_female_selected);
        }

        backBtn.setOnClickListener(v -> this.finish());

        client_gender = DataFromDatabase.gender;
        male.setOnClickListener(v -> {
            male.setImageResource(R.drawable.gender_male_selected);
            female.setImageResource(R.drawable.gender_female);
            client_gender = "M";
        });
        female.setOnClickListener(v -> {
            male.setImageResource(R.drawable.gender_male);
            female.setImageResource(R.drawable.gender_female_selected);
            client_gender = "F";
        });

        profile_pic.setOnClickListener(v -> selectImage());

        editProfile.setOnClickListener(v -> {

            String Name, Gender, Age, Email, PhoneNo, Location, Height, Weight, Plan, About, Client;

            Name = name.getText().toString();
            Gender = client_gender;
            Age = age.getText().toString();
            Email = email.getText().toString();
            PhoneNo = phone.getText().toString();
            Location = location.getText().toString();
            Height = height.getText().toString();
            Weight = weight.getText().toString();
            Plan = plan.getText().toString();
            About = about.getText().toString();
            Client = DataFromDatabase.clientuserID;

            DataFromDatabase.name = Name;
            DataFromDatabase.email = Email;
            DataFromDatabase.mobile = PhoneNo;
            DataFromDatabase.location = Location;
            DataFromDatabase.age = Age;
            DataFromDatabase.gender = Gender;
            DataFromDatabase.weight = Weight;
            DataFromDatabase.height = Height;
            DataFromDatabase.plan = Plan;
            DataFromDatabase.about = About;
            RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("failure")) {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Updated Successful", Toast.LENGTH_LONG).show();
                    Log.e("loginInfo", response);
                    Log.d("Response Login", response);
                }
            }, error -> {
                String errorMessage = "Unknown error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                }
                Log.e("VolleyError", errorMessage); // Log the complete error message
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

            }) {
                @NonNull
                @Override
                public Map<String, String> getParams() {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("clientuserID", Client);
                    data.put("name", Name);
                    data.put("email", Email);
                    data.put("mobile", PhoneNo);
                    data.put("gender", Gender);
                    data.put("age", Age);
                    data.put("location", Location);
                    data.put("height", Height);
                    data.put("weight", Weight);
                    data.put("plan", Plan);
                    data.put("about", About);
                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        });
    }

    private void selectImage() {
        ImagePicker.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            Uri uri = data.getData();
            profile_pic.setImageURI(uri);

            // Convert the URI to a Bitmap
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the Bitmap to the ImageView
            DataFromDatabase.profile = bitmap;
            String Client = DataFromDatabase.clientuserID;

            upload_Image = bitmapToBase64(bitmap);

            // code to upload image in php  starts here

            String url = String.format("%simageUpload.php", DataFromDatabase.ipConfig);

            RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);

            StringRequest stringRequest = getStringRequest(url, Client);
            requestQueue.add(stringRequest);
            //  code to upload image in php end here
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private StringRequest getStringRequest(String url, String Client) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            if (response.equals("failure")) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Updated Successful", Toast.LENGTH_LONG).show();
            }
        }, error -> {
            String errorMessage = "Unknown error";
            if (error.networkResponse != null && error.networkResponse.data != null) {
                errorMessage = new String(error.networkResponse.data);
            }
            Log.e("VolleyError", errorMessage); // Log the complete error message
            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("clientuserID", Client);
                data.put("imageData", upload_Image);
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}