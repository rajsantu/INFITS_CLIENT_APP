//package com.example.infits;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.textfield.TextInputLayout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.StringReader;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class Login extends AppCompatActivity {
//    public static final String LOGIN_PREFS = "LoginPrefs";
//
//    SharedPreferences sharedPreferences;
//
//    TextView reg, fpass;
//    Button loginbtn;
//    String passwordStr,usernameStr;
//    String url = String.format("%slogin_client.php",DataFromDatabase.ipConfig);
//    RequestQueue queue;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        reg = (TextView) findViewById(R.id.reg);
//        fpass = (TextView) findViewById(R.id.fpass);
//        loginbtn = (Button) findViewById(R.id.logbtn);
//
//        queue = Volley.newRequestQueue(this);
//
//        reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ireg = new Intent(Login.this, Signup.class);
//                startActivity(ireg);
//            }
//        });
//
//        fpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ip = new Intent(Login.this, ResetPassword.class);
//                startActivity(ip);
//            }
//        });
//
//        loginbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText username= findViewById(R.id.username);
//                EditText password= findViewById(R.id.password);
//                usernameStr = username.getText().toString();
//                passwordStr = password.getText().toString();
//
//                loginbtn.setClickable(false);
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//                    if(response.equals("failure")){
//                        Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
//                        loginbtn.setClickable(true);
//                    }else{
//                        Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
//                        Intent id = new Intent(Login.this, DashBoardMain.class);
//                        Log.d("Response Login",response);
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject object = jsonArray.getJSONObject(0);
//                            DataFromDatabase.flag=true;
//                            DataFromDatabase.clientuserID  = object.getString("clientuserID");
//                            DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
//                            DataFromDatabase.name = object.getString("name");
//                            Log.d("name login",DataFromDatabase.name);
//                            SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
//                            SharedPreferences.Editor editor = loginDetails.edit();
//
//                            DataFromDatabase.password = object.getString("password");
//                            DataFromDatabase.email = object.getString("email");
//                            DataFromDatabase.mobile = object.getString("mobile");
//                            DataFromDatabase.profilePhoto = object.getString("profilePhoto");
//                            DataFromDatabase.location = object.getString("location");
//                            DataFromDatabase.age = object.getString("age");
//                            DataFromDatabase.gender  = object.getString("gender");
//                            DataFromDatabase.weight  = object.getString("weight");
//                            DataFromDatabase.height  = object.getString("height");
//                            DataFromDatabase.profilePhotoBase = DataFromDatabase.profilePhoto;
//
//                            System.out.println(DataFromDatabase.weight);
//                            System.out.println(DataFromDatabase.height);
//
//                            if (object.getString("verification").equals("0")){
//                                DataFromDatabase.proUser = false;
//                            }
//                            if (object.getString("verification").equals("1")){
//                                DataFromDatabase.proUser = true;
//                            }
//                            System.out.println(DataFromDatabase.proUser+" Prouser");
//                            byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
//                            DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);
//                            Log.d("Login Screen","client user id = "+ DataFromDatabase.clientuserID);
//
//                            editor.putBoolean("hasLoggedIn", true);
//                            editor.putBoolean("flag", true);
//                            editor.putString("clientuserID",object.getString("clientuserID"));
//                            editor.putString("dietitianuserID",object.getString("dietitianuserID"));
//                            editor.putString("name",object.getString("name"));
//                            editor.putString("password",object.getString("password"));
//                            editor.putString("email",object.getString("email"));
//                            editor.putString("mobile",object.getString("mobile"));
//                            editor.putString("profilePhoto",object.getString("profilePhoto"));
//                            editor.putString("location",object.getString("location"));
//                            editor.putString("age",object.getString("age"));
//                            editor.putString("gender",object.getString("gender"));
//                            editor.putString("weight",object.getString("weight"));
//                            editor.putString("height",object.getString("height"));
//                            editor.putString("profilePhotoBase",object.getString("profilePhoto"));
//                            editor.putBoolean("proUser",DataFromDatabase.proUser);
//                            editor.apply();
//
//                            finish();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        startActivity(id);
//                    }
//                }, error -> {
//                    Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
//                    loginbtn.setClickable(true);
//                }){
//                    @Override
//                    protected Map<String,String> getParams() throws AuthFailureError{
//                        LinkedHashMap<String,String> data = new LinkedHashMap<>();
//                        data.put("userID",usernameStr);
//                        data.put("password",passwordStr);
//                        return data;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                requestQueue.add(stringRequest);
//            }
//        });
//    }
//
//    private void putDataInPreferences(SharedPreferences.Editor editor) {
//        editor.putBoolean("hasLoggedIn", true);
//
//    }
//}

package com.example.infits;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static final String LOGIN_PREFS = "LoginPrefs";

    SharedPreferences sharedPreferences;
    public static int PERMISSION_ALL = 1;
    TextView reg, fpass;
    Button loginbtn;
    String passwordStr,usernameStr;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    ImageView googlebn, btnFacebook, btnTwitter;
    String google_social_signin_url = String.format("%ssocial_login/loginClientGoogle.php",DataFromDatabase.ipConfig);
    //needs email & token ( google token );
    String url = String.format("%slogin_client.php",DataFromDatabase.ipConfig);
    RequestQueue queue;
    String[] PERMISSIONS={android.Manifest.permission.BODY_SENSORS,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg = (TextView) findViewById(R.id.reg);
        fpass = (TextView) findViewById(R.id.fpass);
        loginbtn = (Button) findViewById(R.id.logbtn);
        googlebn =(ImageView) findViewById(R.id.google);
        btnFacebook=findViewById(R.id.fbbutton);
        queue = Volley.newRequestQueue(this);

        if (!hasPermissions(this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

//        ########################################google login##########################################################


        googlebn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                gsc = GoogleSignIn.getClient(Login.this,gso);
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent, 100);

            }
        });



   //###########################################facebook login#########################################################
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, permissionNeeds);
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
                                                socialLoginForGoogleAuthUserWithToken(userEmail,userToken);

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

//############################################################################################################################

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DashBoardFragment fragment = (DashBoardFragment) fragmentManager.findFragmentById(R.id.trackernav);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username= findViewById(R.id.username);
                EditText password= findViewById(R.id.password);
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();

                loginbtn.setClickable(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    try {
                        JSONArray json_response=new JSONArray(response);
                        JSONObject json_status= (JSONObject) json_response.get(0);
                        if(json_status.getString("Status").equals("Success"))
                        {
                           // System.out.println("Checked");
                            Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                            Intent id = new Intent(Login.this, DashBoardMain.class);
                            Log.d("Response Login",response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(1);
                    if(response.equals("failure")){
                        Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "SatnamVolley: "+ response);
                        loginbtn.setClickable(true);
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
                                Log.d("name login",DataFromDatabase.name);
                                SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                                SharedPreferences.Editor editor = loginDetails.edit();

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

                                System.out.println(DataFromDatabase.weight);
                                System.out.println(DataFromDatabase.height);

                                if (object.getString("verification").equals("0")){
                                    DataFromDatabase.proUser = false;
                                }
                                if (object.getString("verification").equals("1")){
                                    DataFromDatabase.proUser = true;
                                }
                                System.out.println(DataFromDatabase.proUser+" Prouser");
                                byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
                                DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);
                                Log.d("Login Screen","client user id = "+ DataFromDatabase.clientuserID);

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

                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            
//                            if (fragment != null) {
//                                fragment.setProfileImage(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile));
//                            }


                                finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                            startActivity(id);
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                            loginbtn.setClickable(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "SatnamVolley: "+ error);
                    loginbtn.setClickable(true);
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        LinkedHashMap<String,String> data = new LinkedHashMap<>();
                        data.put("userID",usernameStr);
                        data.put("password",passwordStr);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
    public boolean hasPermissions(Context context, String...  permissions) {
        if (context!=null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "permissions GRANTED");
            }
            else {
                Log.d("Permission", "permissions NOT GRANTED");
            }
        }
    }


    private void putDataInPreferences(SharedPreferences.Editor editor) {
        editor.putBoolean("hasLoggedIn", true);
    }
}
}
//############################## after getting result accessing token//////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount acc= task.getResult(ApiException.class);
                String Email= acc.getEmail();
                Log.d("emmmm",Email);
                String Token = acc.getIdToken();
                Log.d("Token",Token);
                socialLoginForGoogleAuthUserWithToken(Email,Token);

            }catch(ApiException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
}

