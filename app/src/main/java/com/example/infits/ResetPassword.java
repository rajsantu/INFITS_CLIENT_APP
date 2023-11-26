package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResetPassword extends AppCompatActivity {


    ImageView back_login;
    Button sendMailBtn;
    EditText mailET;
    String url = String.format("%ssendMail.php",DataFromDatabase.ipConfig);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        back_login = findViewById(R.id.back_login);
        sendMailBtn = findViewById(R.id.sendMailBtn);
        mailET = findViewById(R.id.mailID);

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mailET.getText().toString().isEmpty()) {
                    Toast.makeText(ResetPassword.this, "please enter your E-mail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPassword.this, "Sending...", Toast.LENGTH_SHORT).show();
                    String email=mailET.getText().toString();
                    String otp=getOTP();
                    send(email,otp);
                }
            }
        });
    }
    private void send(String email,String otp)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response ->
        {
                Toast.makeText(ResetPassword.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetPassword.this, EnterOTP.class);
                intent.putExtra("otp", otp);
                intent.putExtra("email", email);
                startActivity(intent);
                } ,
                error -> {
                    Log.d("Data",error.toString().trim());
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> dataVol = new HashMap<>();
                dataVol.put("email", email);
                dataVol.put("otp", otp);
                return dataVol;
            }
        };
        Volley.newRequestQueue(ResetPassword.this).add(stringRequest);
    }
    private String getOTP() {
        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        return String.valueOf(otp);
    }
}