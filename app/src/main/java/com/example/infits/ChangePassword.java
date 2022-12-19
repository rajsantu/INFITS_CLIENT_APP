package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    TextInputEditText newPass, confirmPass;
    Button create;

    String email;
    String url = String.format("%supdatePassword.php",DataFromDatabase.ipConfig);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        hooks();

        email = getIntent().getStringExtra("email");

        create.setOnClickListener(v -> {
            String nPass = newPass.getText().toString();
            String cPass = confirmPass.getText().toString();

            if(nPass.isEmpty() || cPass.isEmpty()) {
                Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_LONG).show();
            } else if(!nPass.equals(cPass)) {
                Toast.makeText(this, "Confirmed password does not match entered password", Toast.LENGTH_LONG).show();
            } else {
                updateDatabase(nPass);
            }
        });
    }

    private void updateDatabase(String pass) {
        StringRequest request = new StringRequest(
                Request.Method.POST, url,
                response -> {
                    if(response.equals("success")) {
                        Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Log.d("ChangePassword", "response: " + response);
                        Toast.makeText(this, "Update failed. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("ChangePassword", "error: " + error);
                    Toast.makeText(this, "Update failed. Please try again later.", Toast.LENGTH_LONG).show();
                }
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", email);
                data.put("pass", pass);
                return data;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void hooks() {
        newPass = findViewById(R.id.new_pass);
        confirmPass = findViewById(R.id.confirm_pass);
        create = findViewById(R.id.createBtn);
    }
}