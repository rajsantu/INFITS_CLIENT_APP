package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Fitbit extends AppCompatActivity {
    final String client_id = "", client_secret = "", response_type = "code", scope = "heartrate", redirect_uri = "http://localhost";
    String authCode = "", accessToken = "";
    final String authorization = Arrays.toString(Base64.getEncoder().encode((client_id + ":" + client_secret).getBytes(StandardCharsets.UTF_8)));

    final String loginUrl = "https://accounts.fitbit.com/login";
    final String authorizeUrl = String.format(
            "https://www.fitbit.com/oauth2/authorize?client_id=%s&response_type=%s&scope=%s&redirect_uri=%s",
            client_id, response_type, scope, redirect_uri
    );
    final String oauth2tokenUrl = String.format(
            "https://api.fitbit.com/oauth2/token?code=%s&grant_type=authorization_code&redirect_uri=%s",
            authCode, redirect_uri
    );
    final String getDataUrl = String.format(
            "https://api.fitbit.com/1/user/-/profile.json?authorization=Bearer %s",
            authorization
    );

    WebView webView;

    public Fitbit() throws UnsupportedEncodingException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit);

        System.out.println(authorizeUrl);

        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(loginUrl);
        webView.setWebViewClient(new WebViewClient());
    }

    private void authenticate() {
        // auth code will get appended to the redirect_uri followed by #_=_
        // we need that
        StringRequest request = new StringRequest(
                Request.Method.GET, authorizeUrl,
                response -> {
                    authCode = response;
                    if(true /*success*/) oauth2token();
                },
                error -> Log.e("authenticate()", "error: " + error)
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void oauth2token() {
        StringRequest request = new StringRequest(
                Request.Method.GET, oauth2tokenUrl,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        accessToken = object.getString("access_token");

                        getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("oauth2token()", "error: " + error)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + authCode);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.GET, getDataUrl,
                response -> {
                    // get the data
                },
                error -> Log.e("getData()", "error: " + error)
        );
        Volley.newRequestQueue(this).add(request);
    }
}