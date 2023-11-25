package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Signup extends AppCompatActivity {

    TextView term, memlog;
    Button signbtn;
    RadioButton agreeToCondition, maleRB, femaleRB;
    ProgressBar progressBar;

    char gender = 'M';

    EditText fullName,userName,emailID,password,phoneNo, age, height, weight,referredCode;
    //referred_code is code shared by other use
    //referralcode is user referral code when he/she wants to share

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        agreeToCondition = findViewById(R.id.agree);
        term = (TextView) findViewById(R.id.term);
        memlog = (TextView) findViewById(R.id.memlog);
        signbtn = (Button) findViewById(R.id.signbtn);
        fullName = findViewById(R.id.new_name);
        userName = findViewById(R.id.new_user_name);
        emailID = findViewById(R.id.new_email);
        password = findViewById(R.id.new_password);
        phoneNo = findViewById(R.id.new_phone_number);
        age = findViewById(R.id.age_background);
        height = findViewById(R.id.new_height);
        weight = findViewById(R.id.new_weight);
        referredCode = findViewById(R.id.new_referral_code);
        maleRB = findViewById(R.id.male_rb);
        femaleRB = findViewById(R.id.female_rb);
        progressBar = findViewById(R.id.signUpProgress);

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Signup.this, TermsAndConditions.class);
                startActivity(it);
            }
        });

        memlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilo = new Intent(Signup.this, Login.class);
                startActivity(ilo);
            }
        });

        if (agreeToCondition.isSelected()){
            signbtn.setClickable(true);
        }

        maleRB.setOnClickListener(v -> gender = 'M');
        femaleRB.setOnClickListener(v -> gender = 'F');
        signbtn.setOnClickListener(v -> {
            signbtn.setClickable(false);
            generateReferral();
        });
    }

    private boolean checkIfFieldsAreFilled(
            String userID, String passwordStr, String emailStr, String phoneStr,
            String fullNameStr, String ageStr, String heightStr, String weightStr) {
        return !userID.equals("") && !passwordStr.equals("") && !emailStr.equals("") && !phoneStr.equals("") &&
                !fullNameStr.equals("") && !ageStr.equals("") && !heightStr.equals("") && !weightStr.equals("") ;
    }

    private void generateReferral() {
        Random random = new Random();
        //if username length equals to 3
        if (userName.getText().toString().trim().length() == 3 && !userName.getText().toString().trim().contains(" ")){
            StringBuilder referralCode = new StringBuilder(userName.getText().toString().substring(0, 3).toUpperCase());
            for(int i = 0; i < 5; i++) {
                referralCode.append(random.nextInt(10));
            }
            signUpWithReferral(referralCode.toString());
        }else if (userName.getText().toString().trim().length() >= 4 && !userName.getText().toString().trim().contains(" ")){
            StringBuilder referralCode = new StringBuilder(userName.getText().toString().substring(0, 4).toUpperCase());
            for(int i = 0; i < 4; i++) {
                referralCode.append(random.nextInt(10));
            }
            signUpWithReferral(referralCode.toString());
        }else {
            signbtn.setClickable(true);
            Toast.makeText(Signup.this,"Username must be greater than 3 letter",Toast.LENGTH_SHORT).show();
        }
    }

    private void signUpWithReferral(String referralCode){
        progressBar.setVisibility(View.VISIBLE);
        String userID = userName.getText().toString();
        String passwordStr = password.getText().toString();
        String emailStr = emailID.getText().toString();
        String phoneStr = phoneNo.getText().toString();
        String fullNameStr = fullName.getText().toString();
        String ageStr = age.getText().toString();
        String heightStr = height.getText().toString();
        String weightStr = weight.getText().toString();
        String referredCodeStr = referredCode.getText().toString();
        if(!checkIfFieldsAreFilled(userID, passwordStr, emailStr, phoneStr, fullNameStr, ageStr, heightStr, weightStr)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else {

            String url = String.format("%sregister_client_with_referral.php",DataFromDatabase.ipConfig);
            //StringRequest stringRequest = new StringRequest(Request.Method.POST,String.format("%sregister_client_with_referral.php",DataFromDatabase.ipConfig), response -> {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
                if (response != null && !response.contains("Duplicate entry")){
                    progressBar.setVisibility(View.INVISIBLE);
                    signbtn.setClickable(true);
                    Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_LONG).show();
                    Intent id = new Intent(getApplicationContext(), Login.class);
                    startActivity(id);
                    finish();
                }
                else{
                    if (response != null && response.contains("client.PRIMARY")){
                        progressBar.setVisibility(View.INVISIBLE);
                        signbtn.setClickable(true);
                        Toast.makeText(getApplicationContext(),"Username already exits",Toast.LENGTH_LONG).show();
                    }else {
                        if (response != null && response.contains("Duplicate entry")) generateReferral();
                    }
                }
            },error -> {
                signbtn.setClickable(true);
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage = "Unknown error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                }
                Log.e("VolleyError", errorMessage); // Log the complete error message
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();})
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> data = new HashMap<>();
                    data.put("userID",userID);
                    data.put("password",passwordStr);
                    data.put("email",emailStr);
                    data.put("name",fullNameStr);
                    data.put("phone",phoneStr);
                    data.put("gender",String.valueOf(gender));
                    data.put("age",ageStr);
                    data.put("height",heightStr);
                    data.put("weight",weightStr);
                    data.put("referredCode",referredCodeStr);
                    data.put("verification","0");
                    data.put("dietitian_id","-1");
                    data.put("dietitianuserID","-1");
                    data.put("userid",userID);
                    data.put("referralCode",referralCode);
                    data.put("activeUsers","none");

                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        Log.d("Request Data", entry.getKey() + ": " + entry.getValue());
                    }

                    return data;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(stringRequest);
        }
    }
}