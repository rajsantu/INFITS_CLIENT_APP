package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

public class LiveSession extends AppCompatActivity {

    Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);
        customDialog = new Dialog(this);
        upcomingStreamLive();
    }

    private void upcomingStreamLive() {
//        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

        customDialog.setContentView(R.layout.upcoming_streaming_live_layout);
        //customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView confirmCard = customDialog.findViewById(R.id.upcoming_stream_live);
        customDialog.show();
    }
}