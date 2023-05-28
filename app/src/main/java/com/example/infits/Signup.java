package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Signup extends AppCompatActivity {

    TextView term, memlog;
    Button signbtn;
    RadioButton agreeToCondition, maleRB, femaleRB;

    String url = String.format("%sregister_client.php",DataFromDatabase.ipConfig);
    char gender = 'M';

    EditText fullName,userName,emailID,password,phoneNo, age, height, weight;

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
        maleRB = findViewById(R.id.male_rb);
        femaleRB = findViewById(R.id.female_rb);

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
            System.out.println(gender);

            String userID = userName.getText().toString();
            String passwordStr = password.getText().toString();
            String emailStr = emailID.getText().toString();
            String phoneStr = phoneNo.getText().toString();
            String fullNameStr = fullName.getText().toString();
            String ageStr = age.getText().toString();
            String heightStr = height.getText().toString();
            String weightStr = weight.getText().toString();

            if(!checkIfFieldsAreFilled(userID, passwordStr, emailStr, phoneStr, fullNameStr, ageStr, heightStr, weightStr)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
                    System.out.println(response);
//                        Log.i(response, "onCreate: ");
                    if (response != null && response.equals("success")){
                        Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();
                        Intent id = new Intent(getApplicationContext(), Login.class);
                        startActivity(id);
                    }
                    else{


                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();}){
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
                        data.put("weight",heightStr);
                        data.put("height",weightStr);
                        data.put("verification","0");
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);




                generateReferral();
            }
        });
    }

    private boolean checkIfFieldsAreFilled(
            String userID, String passwordStr, String emailStr, String phoneStr,
            String fullNameStr, String ageStr, String heightStr, String weightStr
    ) {
        return !userID.equals("") && !passwordStr.equals("") && !emailStr.equals("") && !phoneStr.equals("") &&
                !fullNameStr.equals("") && !ageStr.equals("") && !heightStr.equals("") && !weightStr.equals("");
    }

    private void generateReferral() {
        Random random = new Random();
        StringBuilder referralCode = new StringBuilder(userName.getText().toString().substring(0, 3));

        for(int i = 0; i < 5; i++) {
            int num = random.nextInt(10);
            referralCode.append(num);
        }

        addToReferralTable(String.valueOf(referralCode));
    }

    private void addToReferralTable(String referralCode) {
        String referralUrl = String.format("%supdateReferralTable.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> Log.d("Signup", "addToReferralTable: " + response),
                error -> Log.e("Signup", "addToReferralTable: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", userName.getText().toString());
                data.put("referralCode", referralCode);
                data.put("activeUsers", "none");

                return data;
            }
        };
        Volley.newRequestQueue(this).add(referralRequest);
    }
}