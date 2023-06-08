package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class activity_trck_popup extends AppCompatActivity {

    ImageView set_goal;
    EditText goal_value_txt;
    String goal_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trck_popup);
        set_goal = findViewById(R.id.imageView89);
        goal_value_txt = findViewById(R.id.textView88);

        set_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goal_value = goal_value_txt.getText().toString();
                Toast.makeText(getApplicationContext(), goal_value, Toast.LENGTH_SHORT).show();
                String goal = goal_value;
                String url=String.format("%srunningTrackerSetGoal.php",DataFromDatabase.ipConfig);

                StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
                    if (response.equals("updated")){
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(response);
                        Toast.makeText(getApplicationContext(), "Not working" + String.valueOf(response), Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> data = new HashMap<>();
                        data.put("client_id",DataFromDatabase.client_id);
                        data.put("clientuserID",DataFromDatabase.clientuserID);
                        data.put("distance","0");
                        data.put("calories", "0");
                        data.put("runtime", "0");
                        data.put("goal", goal);
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDateTime now = LocalDateTime.now();
                        data.put("date",dtf.format(now));
                        return data;
                    }
                };

                Volley.newRequestQueue(getApplicationContext().getApplicationContext()).add(request);
                Toast.makeText(getApplicationContext(),"check 1",Toast.LENGTH_SHORT).show();

//                Navigation.findNavController(v).navigate(R.id.action_activity_trck_popup_to_activitySecondFragment);
            }
        });

    }
}