package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class ScanResult extends AppCompatActivity {

    TextView productName,quantity,fat,calories,carb,protein,fiber;
    TextView fat_per_serving,calories_per_serving,carb_per_serving,protein_per_serving,fiber_per_serving;

    ImageView image,notFound;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();

        String barcode = intent.getStringExtra("barcode");

        image = findViewById(R.id.image);

        quantity = findViewById(R.id.quantity);

        productName = findViewById(R.id.product_name);
        carb = findViewById(R.id.per_100g_title_carb);
        fat = findViewById(R.id.per_100g_title_fat);
        protein = findViewById(R.id.per_100g_title_protein);
        calories = findViewById(R.id.per_100g_title_energy);
        fiber = findViewById(R.id.per_100g_title_fiber);
        calories_per_serving = findViewById(R.id.per_serving_energy);
        fat_per_serving = findViewById(R.id.per_serving_fat);
        carb_per_serving = findViewById(R.id.per_serving_carb);
        protein_per_serving = findViewById(R.id.per_serving_protein);
        fiber_per_serving = findViewById(R.id.per_serving_fiber);

//energy-kcal_serving

        notFound = findViewById(R.id.not_found);

        String url = String.format("https://world.openfoodfacts.org/api/v0/product/%s.json",barcode);
        StringRequest scanResult = new StringRequest(Request.Method.GET,url, response -> {

            System.out.println(response);
            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("1")){
                    JSONObject product = jsonResponse.getJSONObject("product");

                    Toast.makeText(this, jsonResponse.getString("code"), Toast.LENGTH_SHORT).show();
                    Glide.with(getApplicationContext()).load(product.getString("image_small_url")).into(image);
                    productName.append(" "+product.getString("product_name"));
                    quantity.setText(product.getString("serving_size"));
                    JSONObject nutrients = product.getJSONObject("nutriments");
                    calories.setText(nutrients.getString("energy-kcal_100g"));
                    carb.setText(nutrients.getString("carbohydrates_100g"));
                    fat.setText(nutrients.getString("fat_100g"));
                    protein.setText(nutrients.getString("proteins_100g"));
                    fiber.setText(nutrients.getString("fiber_100g"));
                    fat_per_serving.setText(nutrients.getString("fat_serving"));
                    calories_per_serving.setText(nutrients.getString("energy-kcal_serving"));
                    carb_per_serving.setText(nutrients.getString("carbohydrates_serving"));
                    protein_per_serving.setText(nutrients.getString("proteins_serving"));
                    fiber_per_serving.setText(nutrients.getString("fiber_serving"));
                }
                if (jsonResponse.getString("status").equals("0")){
                            notFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        },
                error -> {

                });
        Volley.newRequestQueue(getApplicationContext()).add(scanResult);

//        next = findViewById(R.id.nextbtn);

//        next.setOnClickListener(v->{
//            Intent intentToQuestion = new Intent(getApplicationContext(),QuestionsAfterScan.class);
//            startActivity(intentToQuestion);
//        });

    }
}