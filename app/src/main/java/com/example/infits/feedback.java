package com.example.infits;

        import androidx.annotation.Dimension;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;

        import android.app.Dialog;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;
        import android.widget.ImageView;
        import android.view.View;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.widget.RatingBar;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.iarcuschin.simpleratingbar.SimpleRatingBar;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class feedback extends AppCompatActivity{

    private SimpleRatingBar ratingBar1,ratingBar2;
    String url = String.format("%sfeedback.php", DataFromDatabase.ipConfig);
    private EditText editText;
    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar1 = findViewById(R.id.bar1);
        ratingBar2 = findViewById(R.id.bar2);
        editText = findViewById(R.id.user_feedback);
        frameLayout = findViewById(R.id.submitbtn);

//         ratingBar1.setOnClickListener(new View.OnClickListener() {
//
//             @Override
//             public void onClick(View view) {
//                 Float star = ratingBar1.getRating();
//                 Toast.makeText(MainActivity.this, Float.toString(star), Toast.LENGTH_SHORT).show();
//             }
//         });

//        ratingBar2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Float star = ratingBar2.getRating();
//                Toast.makeText(MainActivity.this, Float.toString(star), Toast.LENGTH_SHORT).show();
//            }
//        });
//
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDetails();
            }
        });


    }

    public void sendDetails(){
        String clientID= "test",dieticianID="John_wayne";
        Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
        String feedback = editText.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),"Thank-you For your Feedback",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
//                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error req generate",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("app_rating",Float.toString(ratingBar2.getRating()));
                params.put("dietitian_rating",Float.toString(ratingBar1.getRating()));
                params.put("feedback",feedback);
                params.put("clientID",clientID);
                params.put("dieticianID",dieticianID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}