package com.example.infits;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static final String LOGIN_PREFS = "LoginPrefs";

    SharedPreferences sharedPreferences;

    TextView reg, fpass;
    Button loginbtn;
    ImageView btnGoogle, btnFacebook, btnTwitter;
    String passwordStr,usernameStr;
    //String url = String.format("%slogin_client.php",DataFromDatabase.ipConfig);
//    String url = "https://infits.in/androidApi/login_client.php";
    String url = "http://192.168.1.10/infits//login_client_1.php";
    RequestQueue queue;


    String google_social_signin_url = String.format("%ssocial_login/loginClientGoogle.php",DataFromDatabase.ipConfig);
    //needs email & token ( google token );
    //email = "vinod.patil.pro@gmail.com"
    //token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI3NDA1MmEyYjY0NDg3NDU3NjRlNzJjMzU5MDk3MWQ5MGNmYjU4NWEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI1MDMyNDgzMzcwMDkta2FzYnM2ZmVzMjlnYjBhcm4wNjNhaWRpazljajU5ODIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI1MDMyNDgzMzcwMDktcnBqMWswZGNiZHMwam1xbjVoOW5wbGRnZGpjZTZwbnEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDk0NzkxNDA3NDQ5MTk0MDgxNjkiLCJlbWFpbCI6InZpbm9kLnBhdGlsLnByb0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlZpbm9kIFBhdGlsIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FFZEZUcDYwU2JwWTJuaFpBTkVHVWR3VzVWOFROOGdLV1dsSHZqaHNwVFljPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IlZpbm9kIiwiZmFtaWx5X25hbWUiOiJQYXRpbCIsImxvY2FsZSI6ImVuLUdCIiwiaWF0IjoxNjc1MzE4MjAzLCJleHAiOjE2NzUzMjE4MDN9.jO7OopE2iv8uU3w0oLLisNkU3fFRfWCb93SGKW5bTpAxsKO6zGmZFVxiSV0m3sEohHnKSjLbOARs1minBoJJ4091ZcPdoNFIy7HIyGzSo7dn71feRMThmaqJKuzcpwb3ZiAWAy6O6DvsYg0RpJUHuW1kFly7FVMQp3prhSI7HUkxGdJtHtQZAVkYn_JJtRcJGngh4aDCB-vdeRChG-86vFMHOqpX9H-6kFasUKFaYy07y48dDHkco7sSSii2Nj34wS-D-ssJg8U7B23jM2T6GRZEGY4GnDIBunManTAHImteiOAuWphnQeKL26mKasaPh9tg68GuRokGsP_yR77TeQ";

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ActivityResultLauncher<Intent> googleSignInActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(Login.this);
                        if(acc!=null){
                            String userEmail = acc.getEmail();
                            String userToken = acc.getIdToken();
                            //perform server operation for login with google authenticated email and token
                            socialLoginForGoogleAuthUserWithToken(userEmail,userToken);
                        }else{
                            Toast.makeText(getApplicationContext(),"something went wrong. User token not generated.",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"something went wrong.",Toast.LENGTH_LONG).show();
                    }
                }
            });

    String facebook_social_signin_url = String.format("%ssocial_login/loginClientFacebook.php",DataFromDatabase.ipConfig);
    //needs email & token ( google token );
    //email = "vinod.patil.pro@gmail.com"
    //token = "token"
    CallbackManager facebookCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg = (TextView) findViewById(R.id.reg);
        fpass = (TextView) findViewById(R.id.fpass);
        loginbtn = (Button) findViewById(R.id.logbtn);

        btnGoogle = (ImageView) findViewById(R.id.google);
        btnTwitter = (ImageView) findViewById(R.id.twitter);
        btnFacebook = (ImageView) findViewById(R.id.facebook);

        queue = Volley.newRequestQueue(this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ireg = new Intent(Login.this, Signup.class);
                startActivity(ireg);
            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ip = new Intent(Login.this, ResetPassword.class);
                startActivity(ip);
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                EditText username= findViewById(R.id.username);
                EditText password= findViewById(R.id.password);
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();

                loginbtn.setClickable(false);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    Toast.makeText(Login.this,response,Toast.LENGTH_SHORT).show();
                    if(response.equals("failure")){
                        Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                        loginbtn.setClickable(true);
                    }else{
                        Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                        Intent id = new Intent(Login.this, DashBoardMain.class);
                        Log.e("loginInfo",response.toString());
                        Log.d("Response Login",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(1);
                            DataFromDatabase.flag=true;
                            DataFromDatabase.clientuserID  = object.getString("clientuserID");
                            DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
                            DataFromDatabase.proUser = !object.getString("verification_code").equals("null") && !object.getString("verification_code").equals("");

                            DataFromDatabase.name = object.getString("name");
                            Log.d("name login",DataFromDatabase.name);

                            SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginDetails.edit();

                            DataFromDatabase.client_id=object.getString("client_id");
                            Log.d("clientID",DataFromDatabase.client_id);
                            DataFromDatabase.dietitian_id=object.getString("dietitian_id");
                            Log.d("dietitianID",DataFromDatabase.dietitian_id);
                            DataFromDatabase.password = object.getString("password");
                            DataFromDatabase.email = object.getString("email");
                            DataFromDatabase.mobile = object.getString("mobile");
                            DataFromDatabase.profilePhoto = object.getString("profilePhoto");
                            DataFromDatabase.location = object.getString("location");
                            DataFromDatabase.age = object.getString("age");
                            DataFromDatabase.gender  = object.getString("gender");
                            DataFromDatabase.weight  = object.getString("weight");
                            DataFromDatabase.height  = object.getString("height");
                            DataFromDatabase.verification  = object.getString("verification");
                            DataFromDatabase.profilePhotoBase = DataFromDatabase.profilePhoto;

                            System.out.println(DataFromDatabase.weight);
                            System.out.println(DataFromDatabase.height);


                            byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
                            DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);
                            Log.d("Login Screen","client user id = "+ DataFromDatabase.clientuserID);

                            editor.putBoolean("hasLoggedIn", true);
                            editor.putBoolean("flag", true);
                            editor.putString("clientuserID",object.getString("clientuserID"));
                            editor.putString("dietitian_id",object.getString("dietitian_id"));
                            editor.putString("client_id",object.getString("client_id"));
                            editor.putString("dietitianuserID",object.getString("dietitianuserID"));
                            editor.putString("name",object.getString("name"));
                            editor.putString("password",object.getString("password"));
                            editor.putString("email",object.getString("email"));
                            editor.putString("mobile",object.getString("mobile"));
                            editor.putString("profilePhoto",object.getString("profilePhoto"));
                            editor.putString("location",object.getString("location"));
                            editor.putString("age",object.getString("age"));
                            editor.putString("gender",object.getString("gender"));
                            editor.putString("weight",object.getString("weight"));
                            editor.putString("height",object.getString("height"));
                            editor.putString("profilePhotoBase",object.getString("profilePhoto"));
                            editor.putBoolean("proUser",DataFromDatabase.proUser);
                            editor.putString("verification",object.getString("verification"));
                            editor.apply();

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity(id);
                    }
                }, error -> {
                    Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
                    loginbtn.setClickable(true);
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        HashMap<String,String> data = new HashMap<>();
                        data.put("userID",usernameStr);
                        data.put("password",passwordStr);
                        return data;
                    }
                };
                queue.add(stringRequest);

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
        });



        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gsc = GoogleSignIn.getClient(Login.this,gso);
                googleSignIn();
            }
        });

        facebookCallbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("email","user_profile");
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResults.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        if(response != null){
                                            Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_LONG).show();
                                            JSONObject obj = response.getJSONObject();
                                            try {
                                                String userEmail = obj.getString("email");
                                                String userToken = loginResults.getAccessToken().getToken();

                                                //Facebook server login with email,  + access token
                                                socialLoginForFacebookAuthUserWithToken(userEmail,userToken);

                                            } catch (JSONException e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage() ,Toast.LENGTH_LONG).show();
                                                throw new RuntimeException(e);
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Graph sql response not received for facebook.",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    @Override
                    public void onCancel() {
//                        Log.e("ltltltltltltltltltl","facebook login canceled");
                        Toast.makeText(getApplicationContext(), "facebook login canceled",Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onError(FacebookException e) {
//                        Log.e("ltltltltltltltltltl", "facebook login failed error");
                        Toast.makeText(getApplicationContext(), "facebook login failed error",Toast.LENGTH_LONG).show();
                    }
                }
        );
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, permissionNeeds);
            }
        });
    }
    private void googleSignIn() {
        Intent googleSignInIntent = gsc.getSignInIntent();
        googleSignInActivityResultLauncher.launch(googleSignInIntent);
    }
    private void socialLoginForGoogleAuthUserWithToken(String userEmail, String userToken) {

        btnGoogle.setClickable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, google_social_signin_url, response -> {
            if(response.equals("failure")){
                Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                btnGoogle.setClickable(true);
            }else{
                Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent id = new Intent(Login.this, DashBoardMain.class);
                Log.d("Response Login",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    DataFromDatabase.flag=true;
                    DataFromDatabase.clientuserID  = object.getString("clientuserID");
                    DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
                    DataFromDatabase.name = object.getString("name");
                    DataFromDatabase.password = object.getString("password");
                    DataFromDatabase.email = object.getString("email");
                    DataFromDatabase.mobile = object.getString("mobile");
                    DataFromDatabase.profilePhoto = object.getString("profilePhoto");
                    DataFromDatabase.location = object.getString("location");
                    DataFromDatabase.age = object.getString("age");
                    DataFromDatabase.gender  = object.getString("gender");
                    DataFromDatabase.weight  = object.getString("weight");
                    DataFromDatabase.height  = object.getString("height");
                    DataFromDatabase.profilePhotoBase = DataFromDatabase.profilePhoto;
                    if (object.getString("verification").equals("0")){
                        DataFromDatabase.proUser = false;
                    }else if (object.getString("verification").equals("1")){
                        DataFromDatabase.proUser = true;
                    }
                    byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
                    DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);

                    SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginDetails.edit();
                    editor.putBoolean("hasLoggedIn", true);
                    editor.putBoolean("flag", true);
                    editor.putString("clientuserID",object.getString("clientuserID"));
                    editor.putString("dietitianuserID",object.getString("dietitianuserID"));
                    editor.putString("name",object.getString("name"));
                    editor.putString("password",object.getString("password"));
                    editor.putString("email",object.getString("email"));
                    editor.putString("mobile",object.getString("mobile"));
                    editor.putString("profilePhoto",object.getString("profilePhoto"));
                    editor.putString("location",object.getString("location"));
                    editor.putString("age",object.getString("age"));
                    editor.putString("gender",object.getString("gender"));
                    editor.putString("weight",object.getString("weight"));
                    editor.putString("height",object.getString("height"));
                    editor.putString("profilePhotoBase",object.getString("profilePhoto"));
                    editor.putBoolean("proUser",DataFromDatabase.proUser);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(gsc != null){
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(id);
                            finish();
                        }
                    });
                }else{
                    startActivity(id);
                    finish();
                }
            }
        }, error -> {
            Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            btnGoogle.setClickable(true);
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                LinkedHashMap<String,String> data = new LinkedHashMap<>();
                data.put("email",userEmail);
                data.put("token",userToken);
                return data;
            }
        };
        queue.add(stringRequest);
    }
    private void socialLoginForFacebookAuthUserWithToken(String userEmail, String userToken) {

        btnFacebook.setClickable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, facebook_social_signin_url, response -> {
            if(response.equals("failure")){
                Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                btnFacebook.setClickable(true);
            }else{
                Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                Intent id = new Intent(Login.this, DashBoardMain.class);
                Log.d("Response Login",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    DataFromDatabase.flag=true;
                    DataFromDatabase.clientuserID  = object.getString("clientuserID");
                    DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
                    DataFromDatabase.name = object.getString("name");
                    DataFromDatabase.password = object.getString("password");
                    DataFromDatabase.email = object.getString("email");
                    DataFromDatabase.mobile = object.getString("mobile");
                    DataFromDatabase.profilePhoto = object.getString("profilePhoto");
                    DataFromDatabase.location = object.getString("location");
                    DataFromDatabase.age = object.getString("age");
                    DataFromDatabase.gender  = object.getString("gender");
                    DataFromDatabase.weight  = object.getString("weight");
                    DataFromDatabase.height  = object.getString("height");
                    DataFromDatabase.profilePhotoBase = DataFromDatabase.profilePhoto;
                    if (object.getString("verification").equals("0")){
                        DataFromDatabase.proUser = false;
                    }else if (object.getString("verification").equals("1")){
                        DataFromDatabase.proUser = true;
                    }
                    byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
                    DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);

                    SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginDetails.edit();
                    editor.putBoolean("hasLoggedIn", true);
                    editor.putBoolean("flag", true);
                    editor.putString("clientuserID",object.getString("clientuserID"));
                    editor.putString("dietitianuserID",object.getString("dietitianuserID"));
                    editor.putString("name",object.getString("name"));
                    editor.putString("password",object.getString("password"));
                    editor.putString("email",object.getString("email"));
                    editor.putString("mobile",object.getString("mobile"));
                    editor.putString("profilePhoto",object.getString("profilePhoto"));
                    editor.putString("location",object.getString("location"));
                    editor.putString("age",object.getString("age"));
                    editor.putString("gender",object.getString("gender"));
                    editor.putString("weight",object.getString("weight"));
                    editor.putString("height",object.getString("height"));
                    editor.putString("profilePhotoBase",object.getString("profilePhoto"));
                    editor.putBoolean("proUser",DataFromDatabase.proUser);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(id);
                finish();
            }
        }, error -> {
            Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            btnFacebook.setClickable(true);
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                LinkedHashMap<String,String> data = new LinkedHashMap<>();
                data.put("email",userEmail);
                data.put("token",userToken);
                return data;
            }
        };
        queue.add(stringRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}