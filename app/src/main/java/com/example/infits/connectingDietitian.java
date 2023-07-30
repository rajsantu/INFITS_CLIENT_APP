package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infits.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class connectingDietitian extends AppCompatActivity {
    EditText verify_code;
    ProgressBar progressBar;
    TextView name,age_gender,email,mobile,dietitian_details;
    Button discard,connect,verify;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_dietitian);
        verify = findViewById(R.id.button5Connect);
        cardView = findViewById(R.id.card70);
        discard = findViewById(R.id.button3);
        connect = findViewById(R.id.button4);
        verify_code = findViewById(R.id.verify);
        progressBar = findViewById(R.id.connectWithDietitianProgress);
        dietitian_details = findViewById(R.id.textView2);
        name = findViewById(R.id.textViewName);
        age_gender = findViewById(R.id.textView5);
        email = findViewById(R.id.textView3);
        mobile = findViewById(R.id.textView4);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromPhpFile();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dietitianUpdate();
            }
        });
    }

    private void fetchDataFromPhpFile() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,String.format("%sverify_1.php",DataFromDatabase.ipConfig),
                response->{
            progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(connectingDietitian.this,response,Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        JSONObject jsonObjectData = jsonArray.getJSONObject(0);
                        dietitian_details.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        discard.setVisibility(View.VISIBLE);
                        connect.setVisibility(View.VISIBLE);
                        name.setText(jsonObjectData.getString("name"));
                        age_gender.setText(jsonObjectData.getString("age")+" |"+jsonObjectData.getString("gender"));
                        email.setText(jsonObjectData.getString("email"));
                        mobile.setText(jsonObjectData.getString("mobile"));

                    } catch (Exception e) {
                        Toast.makeText(connectingDietitian.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                },error->{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(connectingDietitian.this,error.toString(),Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("dietitian_verify_code",verify_code.getText().toString().trim());
                data.put("type","2");
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(connectingDietitian.this).add(stringRequest);
    }
    private void dietitianUpdate(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,String.format("%sdietitianUpdated_1.php",DataFromDatabase.ipConfig),
                response->{
                    progressBar.setVisibility(View.INVISIBLE);

                    try {
                        if (response.equals("Updated")){
                            SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginDetails.edit();
                            editor.putString("verification","1");
                            editor.apply();
                            DataFromDatabase.verification = "1";
                            startActivity(new Intent(connectingDietitian.this,Consultation.class));
                            finish();
                            Toast.makeText(connectingDietitian.this,"Updated",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(connectingDietitian.this,response,Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(connectingDietitian.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                },error->{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(connectingDietitian.this,error.toString(),Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("clientuserID",DataFromDatabase.clientuserID);
                data.put("verification","1");
                data.put("type","2");
                return data;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(connectingDietitian.this).add(stringRequest);
    }
}
