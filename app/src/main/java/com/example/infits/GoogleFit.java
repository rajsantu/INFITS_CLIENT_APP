package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFit extends AppCompatActivity {
    private final int GOOGLE_FIT_REQUEST_CODE = 1;

    FitnessOptions fitnessOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_fit);

        googleFit();
    }

    private void googleFit() {
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = getGoogleAccount();

        if(!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(this, GOOGLE_FIT_REQUEST_CODE, account, fitnessOptions);
        } else {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusYears(1);
            long endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond();
            long startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond();

            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_HEART_RATE_BPM)
                    .setTimeRange(startSeconds, endSeconds, TimeUnit.SECONDS)
                    .bucketByTime(1, TimeUnit.DAYS)
                    .build();

            Fitness.getHistoryClient(this, account)
                    .readData(readRequest)
                    .addOnSuccessListener(dataReadResponse -> {
                        if(!dataReadResponse.getBuckets().isEmpty()) {
                            List<Bucket> bucketList = dataReadResponse.getBuckets();

                            if (!bucketList.isEmpty()) {
                                for (Bucket bucket : bucketList) {
                                    DataSet heartData = bucket.getDataSet(DataType.TYPE_HEART_RATE_BPM);
                                    if(heartData != null) getDataFromDataReadResponse(heartData);
                                    else Log.i("getHistoryClient()", "heartData null");
                                }
                            }
                        } else {
                            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("googleFit", "OnFailure()"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_FIT_REQUEST_CODE) {
            Log.d("onActivityResult()", "here");
            System.out.println(data.getData());
        }

    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(this, fitnessOptions);
    }

    private void getDataFromDataReadResponse(DataSet dataSet) {
        List<DataPoint> dataPoints = dataSet.getDataPoints();

        for(DataPoint dp: dataPoints) {
            Log.d("ReadResponse", "name: " + dp.getDataType().getName());
            String startTime = Instant.ofEpochSecond(dp.getStartTime(TimeUnit.SECONDS))
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().toString();
            String endTime = Instant.ofEpochSecond(dp.getEndTime(TimeUnit.SECONDS))
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().toString();
            Log.d("ReadResponse", "startTime: " + startTime);
            Log.d("ReadResponse", "endTime: " + endTime);

            for(Field field: dp.getDataType().getFields()) {
                Log.d("ReadResponse", "field: " + field);
            }
        }
    }
}