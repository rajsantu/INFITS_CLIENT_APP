package com.example.infits;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class connectingDietitian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_connetcing_dietitian);

        Button verifyButton = findViewById(R.id.button5);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromPhpFile();
            }
        });
    }

    private void fetchDataFromPhpFile() {
        EditText verificationEditText = findViewById(R.id.verify);
        String verificationCode = verificationEditText.getText().toString().trim();

        String url = "http://192.168.18.6/verify.php?verification_code=" + verificationCode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.has("error")) {

                                TextView textViewName = findViewById(R.id.textViewName);
                                TextView textViewMobile = findViewById(R.id.textViewMobile);

                                textViewName.setText(response.getString("name"));
                                textViewMobile.setText(response.getString("mobile"));

                                findViewById(R.id.card70).setVisibility(View.VISIBLE);

                                TextView errorTextView = findViewById(R.id.errorTextView);
                                errorTextView.setVisibility(View.GONE);
                            } else {

                                String errorMessage = response.getString("error");

                                TextView errorTextView = findViewById(R.id.errorTextView);
                                errorTextView.setText(errorMessage);
                                errorTextView.setVisibility(View.VISIBLE);

                                findViewById(R.id.card70).setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
