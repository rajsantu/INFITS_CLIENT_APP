package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Subscription1 extends AppCompatActivity implements PaymentResultListener {

    ImageButton btnclose, btnup, btndown;
    Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription1);

        btnclose = findViewById(R.id.btnclose);
        btnup = findViewById(R.id.btnup);
        btndown = findViewById(R.id.btndown);
        buy = findViewById(R.id.buy_500);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icl = new Intent(Subscription1.this, DashBoardMain.class);
                startActivity(icl);
            }
        });

        btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ac = new Intent(Subscription1.this, Subscription2.class);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                startActivity(ac);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount = "500";

                try {
                    // Rounding off the amount.
                    int amount = Math.round(Float.parseFloat(samount) * 100);

                    // Initialize Razorpay account.
                    Checkout checkout = new Checkout();

                    // Set your id as below
                    checkout.setKeyID("rzp_test_3nZCS38QGE3P3u");

                    // Set image
                    checkout.setImage(R.drawable.infit_splashnew);

                    // Initialize JSON object
                    JSONObject optionsObject = new JSONObject();

                    // Put name
                    optionsObject.put("name", "INFITS");

                    // Put description
                    optionsObject.put("description", "You are paying to Infits");

                    // Set theme color
                    optionsObject.put("theme.color", "");

                    optionsObject.put("image", "https://i.pinimg.com/1200x/0d/3e/04/0d3e047ed58b07c0a139bf40ad8a25a0.jpg");

                    // Put the currency
                    optionsObject.put("currency", "INR");

                    // Put amount
                    optionsObject.put("amount", amount);

                    // Put customer name
                    optionsObject.put("prefill.name", DataFromDatabase.name);

                    // Put mobile number
                    optionsObject.put("prefill.contact", DataFromDatabase.mobile);

                    // Put email
                    optionsObject.put("prefill.email", DataFromDatabase.email);

                    optionsObject.put("send_sms_hash", true);

                    // To retry
                    JSONObject retryObj = new JSONObject();
                    retryObj.put("enabled", true);
                    retryObj.put("max_count", 4);
                    optionsObject.put("retry", retryObj);

                    // Open Razorpay checkout activity
                    checkout.open(Subscription1.this, optionsObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        // This method is called on payment success.
        Toast.makeText(this, "Payment is successful: " + s, Toast.LENGTH_SHORT).show();
        OverviewFragment myFragment = new OverviewFragment();
    }

    @Override
    public void onPaymentError(int i, String s) {
        // This method is called on payment failure.
        Toast.makeText(this, "Payment Failed due to error: " + s, Toast.LENGTH_SHORT).show();
    }
}
