package com.example.infits;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InAppNotification extends AppCompatActivity {

    TextView noNotifications;
    RecyclerView notificationRV;
    String inAppUrl = String.format("%sgetInAppNotifications.php", DataFromDatabase.ipConfig);

    Date date = new Date();

    ArrayList<InAppNotificationData> inAppData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_notification);

        noNotifications = findViewById(R.id.no_notifications);
        notificationRV = findViewById(R.id.in_app_notification_rv);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(InAppNotification.this, DashBoardMain.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        getInAppNotifications();
    }

    private void getInAppNotifications() {
        StringRequest inAppRequest = new StringRequest(
                Request.Method.POST,
                inAppUrl,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("inApp");

                        if(array.length() != 0) {
                            noNotifications.setVisibility(View.GONE);
                            notificationRV.setVisibility(View.VISIBLE);
                        } else {
                            noNotifications.setVisibility(View.VISIBLE);
                            notificationRV.setVisibility(View.GONE);
                        }

                        for(int i = 0; i < array.length(); i++) {
                            JSONObject currObject = array.getJSONObject(i);

                            String type = currObject.getString("type");
                            String date = currObject.getString("date");
                            String time = currObject.getString("time");
                            String text = currObject.getString("text");

                            Log.i("Data from server", "type:"+type+" date:"+date+" time:"+time+" text:"+text);

                            InAppNotificationData inAppNotificationData = new InAppNotificationData(type, date, time, text);
                            inAppData.add(inAppNotificationData);

                        }

                        InAppNotificationAdapter inAppNotificationAdapter = new InAppNotificationAdapter(inAppData, this);

                        notificationRV.setAdapter(inAppNotificationAdapter);
                        notificationRV.setLayoutManager(new LinearLayoutManager(this));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> logError(error)
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("dateandtime", String.valueOf(date));
                return data;
            }
        };
        Volley.newRequestQueue(this).add(inAppRequest);
    }

    private void logError(VolleyError error) {
        Log.e("InAppNotifications", error.toString());
    }
}