package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Landing1 extends AppCompatActivity {

    ImageButton b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing1);

        b=(ImageButton) findViewById(R.id.circleButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing1.this, Landing2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out); // Added
            }
        });
    }
}
