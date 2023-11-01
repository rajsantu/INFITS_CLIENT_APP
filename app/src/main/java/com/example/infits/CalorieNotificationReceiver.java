package com.example.infits;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.abhinav.progress_view.ProgressData;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CalorieNotificationReceiver extends BroadcastReceiver {
    //String url = String.format("%scalorieTracker.php", DataFromDatabase.ipConfig);
    String url = "https://infits.in/androidApi/calorieTracker.php";
    AlarmManager alarmManager;
    int currentCalorie;
    int calorieGoal;
    int calDiff,carbDiff,proDiff,fatDiff,fibDiff;

    boolean isMealNotificationFinished;
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        String Time = intent.getStringExtra("Meal");

        switch (Time) {
            case "breakfast":
                breakfast(context);
                break;
            case "lunch":
                lunch(context);
                break;
            case "snack":
                snack(context);
                break;
            case "dinner":
                dinner(context);
                break;
            case "Afterbreakfast":
                Afterbreakfast(context);
                break;
            case "Afterlunch":
                Afterlunch(context);
                break;
            case "Aftersnack":
                Aftersnack(context);
                break;
            case "Afterdinner":
                Afterdinner(context);
                break;
        }
//        currentCalorie = intent.getIntExtra("CCalorie",0);
//        calorieGoal = intent.getIntExtra("CGoal",0);
//
//        Log.i("Received", "onReceive:"+"\n"+" CCal="+currentCalorie+"\n"+" calGoal="+calorieGoal);
//        Toast.makeText(context, "recieved", Toast.LENGTH_SHORT).show();
//        int calDiff = calorieGoal - currentCalorie;
//
//        if(currentCalorie<calorieGoal){
//
//            String contentText = "You've consumed "+currentCalorie+" cals "+"\n"+"You have "+calDiff+" cals left to consumed for today";
//
//            Intent resultIntent = new Intent(context, SplashScreen.class);
//            resultIntent.putExtra("notification", "weight");
//
//            PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                    context, 1, resultIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//            );
//
//            Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
//                    .setContentTitle("It's time to eat!")
//                    .setContentText(contentText)
//                    .setSmallIcon(R.mipmap.logo)
//                    .setAutoCancel(true)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setContentIntent(resultPendingIntent)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(contentText))
//                    .build();
//
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            managerCompat.notify(1, notification);
//            Log.d("Calorie()", "set");
//        }else {
//
//            String contentText = "You have reached your goal!";
//
//            Notification notification = new NotificationCompat.Builder(context,"CalorieChannelId")
//                    .setContentTitle("Calorie Reminder"+"\n"+"Well Done!")
//                    .setContentText(contentText)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setSmallIcon(R.mipmap.logo)
//                    .setAutoCancel(true)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText(contentText))
//                    .build();
//
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//            managerCompat.notify(1,notification);
//
//            PendingIntent caloriePendingIntent = PendingIntent.getBroadcast(context,500,intent,PendingIntent.FLAG_IMMUTABLE);
//            alarmManager.cancel(caloriePendingIntent);
//        }
    }

    private void Afterdinner(Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("CalTracker Data Bro", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");

                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
                        String carbgoal = goalsObject.getString("CarbsGoal");
                        String fibergoal = goalsObject.getString("FiberGoal");
                        String proteingoal = goalsObject.getString("ProteinGoal");
                        String fatgoal = goalsObject.getString("FatsGoal");


                        JSONObject valuesObject = dataObject.getJSONObject("Values");
                        String calories = valuesObject.getString("Calories");
                        String carbs = valuesObject.getString("carbs");
                        String fiber = valuesObject.getString("fiber");
                        String protein = valuesObject.getString("protein");
                        String fat = valuesObject.getString("fat");

                        //All parsing
                        currentCalorie = Integer.parseInt(calories);
                        calorieGoal = Integer.parseInt(calorieconsumegoal);
                        calDiff =calorieGoal - currentCalorie;

                        carbDiff = Integer.parseInt(carbgoal) - Integer.parseInt(carbs);
                        fibDiff = Integer.parseInt(fibergoal) - Integer.parseInt(fiber);
                        proDiff = Integer.parseInt(proteingoal) - Integer.parseInt(protein);
                        fatDiff = Integer.parseInt(fatgoal) - Integer.parseInt(fat);

                        String calText = " and "+calDiff+"kcal of calorie";
                        String carbText = carbDiff+"g of carbs,";
                        String fibreText = " "+fibDiff+"g of fibre";
                        String fatText = " "+fatDiff+"g of fats,";
                        String proteinText = " "+proDiff+"g of protein,";

                        if(carbDiff<=0){
                            carbText ="";
                        }
                        if(calDiff<=0){
                            calText ="";
                        }
                        if(fatDiff<=0){
                            fatText ="";
                        }
                        if(proDiff<=0){
                            proteinText ="";
                        }
                        if(fibDiff<=0){
                            fibreText ="";
                        }

                        //  Toast.makeText(getContext(), "calConsumed : "+calConsumed +" /"+calories, Toast.LENGTH_SHORT).show();
                        String contentText = "You have " +carbText+ proteinText + fatText + fibreText + calText +" left to be consumed for the day";

                        Intent resultIntent = new Intent(context, SplashScreen.class);
                        resultIntent.putExtra("notification", "calorie");

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                                context, 608, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        );
                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                                .setContentTitle("Nutrition reminder")
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(contentText))
                                .build();

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        managerCompat.notify(1, notification);

                        isMealNotificationFinished = false;
                        sharedPreferences = context.getSharedPreferences("mealInfo",context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = (SharedPreferences.Editor) sharedPreferences.edit();
                        editor.putBoolean("mealRunning",isMealNotificationFinished);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("today",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(context).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void Aftersnack(Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("CalTracker Data Bro", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");

                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
                        String carbgoal = goalsObject.getString("CarbsGoal");
                        String fibergoal = goalsObject.getString("FiberGoal");
                        String proteingoal = goalsObject.getString("ProteinGoal");
                        String fatgoal = goalsObject.getString("FatsGoal");


                        JSONObject valuesObject = dataObject.getJSONObject("Values");
                        String calories = valuesObject.getString("Calories");
                        String carbs = valuesObject.getString("carbs");
                        String fiber = valuesObject.getString("fiber");
                        String protein = valuesObject.getString("protein");
                        String fat = valuesObject.getString("fat");

                        //All parsing
                        currentCalorie = Integer.parseInt(calories);
                        calorieGoal = Integer.parseInt(calorieconsumegoal);
                        calDiff =calorieGoal - currentCalorie;

                        carbDiff = Integer.parseInt(carbgoal) - Integer.parseInt(carbs);
                        fibDiff = Integer.parseInt(fibergoal) - Integer.parseInt(fiber);
                        proDiff = Integer.parseInt(proteingoal) - Integer.parseInt(protein);
                        fatDiff = Integer.parseInt(fatgoal) - Integer.parseInt(fat);

                        String calText = " and "+calDiff+"kcal of calorie";
                        String carbText = carbDiff+"g of carbs,";
                        String fibreText = " "+fibDiff+"g of fibre";
                        String fatText = " "+fatDiff+"g of fats,";
                        String proteinText = " "+proDiff+"g of protein,";

                        if(carbDiff<=0){
                            carbText ="";
                        }
                        if(calDiff<=0){
                            calText ="";
                        }
                        if(fatDiff<=0){
                            fatText ="";
                        }
                        if(proDiff<=0){
                            proteinText ="";
                        }
                        if(fibDiff<=0){
                            fibreText ="";
                        }

                        //  Toast.makeText(getContext(), "calConsumed : "+calConsumed +" /"+calories, Toast.LENGTH_SHORT).show();
                        String contentText = "You have " +carbText+ proteinText + fatText + fibreText + calText +" left to be consumed for the day";

                        Intent resultIntent = new Intent(context, SplashScreen.class);
                        resultIntent.putExtra("notification", "calorie");

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                                context, 607, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        );
                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                                .setContentTitle("Nutrition reminder")
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(contentText))
                                .build();

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        managerCompat.notify(1, notification);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("today",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(context).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void Afterlunch(Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("CalTracker Data Bro", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");

                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
                        String carbgoal = goalsObject.getString("CarbsGoal");
                        String fibergoal = goalsObject.getString("FiberGoal");
                        String proteingoal = goalsObject.getString("ProteinGoal");
                        String fatgoal = goalsObject.getString("FatsGoal");


                        JSONObject valuesObject = dataObject.getJSONObject("Values");
                        String calories = valuesObject.getString("Calories");
                        String carbs = valuesObject.getString("carbs");
                        String fiber = valuesObject.getString("fiber");
                        String protein = valuesObject.getString("protein");
                        String fat = valuesObject.getString("fat");

                        //All parsing
                        currentCalorie = Integer.parseInt(calories);
                        calorieGoal = Integer.parseInt(calorieconsumegoal);
                        calDiff =calorieGoal - currentCalorie;

                        carbDiff = Integer.parseInt(carbgoal) - Integer.parseInt(carbs);
                        fibDiff = Integer.parseInt(fibergoal) - Integer.parseInt(fiber);
                        proDiff = Integer.parseInt(proteingoal) - Integer.parseInt(protein);
                        fatDiff = Integer.parseInt(fatgoal) - Integer.parseInt(fat);

                        String calText = " and "+calDiff+"kcal of calorie";
                        String carbText = carbDiff+"g of carbs,";
                        String fibreText = " "+fibDiff+"g of fibre";
                        String fatText = " "+fatDiff+"g of fats,";
                        String proteinText = " "+proDiff+"g of protein,";

                        if(carbDiff<=0){
                            carbText ="";
                        }
                        if(calDiff<=0){
                            calText ="";
                        }
                        if(fatDiff<=0){
                            fatText ="";
                        }
                        if(proDiff<=0){
                            proteinText ="";
                        }
                        if(fibDiff<=0){
                            fibreText ="";
                        }

                        //  Toast.makeText(getContext(), "calConsumed : "+calConsumed +" /"+calories, Toast.LENGTH_SHORT).show();
                        String contentText = "You have " +carbText+ proteinText + fatText + fibreText + calText +" left to be consumed for the day";

                        Intent resultIntent = new Intent(context, SplashScreen.class);
                        resultIntent.putExtra("notification", "calorie");

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                                context, 606, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        );
                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                                .setContentTitle("Nutrition reminder")
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(contentText))
                                .build();

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        managerCompat.notify(1, notification);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("today",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(context).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //    private void dinner(Context context) {
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                response -> {
//                    Log.d("CalTracker Data Bro", response);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject dataObject = jsonObject.getJSONObject("Data");
//
//                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
//                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
//
//                        JSONObject valuesObject = dataObject.getJSONObject("Values");
//                        String calories = valuesObject.getString("Calories");
//
//                        //All parsing
//                        currentCalorie = Integer.parseInt(calories);
//                        calorieGoal = Integer.parseInt(calorieconsumegoal);
//                        int calDiff = calorieGoal - currentCalorie;
//
//                        String contentText = "You've consumed " + currentCalorie + " cals " + "\n" + "You have " + calDiff + " cals left to consumed for today";
//
//                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
//                                .setContentTitle("It's time to Dinner!")
//                                .setContentText(contentText)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setSmallIcon(R.mipmap.logo)
//                                .setAutoCancel(true)
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(contentText))
//                                .build();
//
//                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        managerCompat.notify(1, notification);
//                        Intent intent = new Intent(context,SplashScreen.class);
//
//                        isMealNotificationFinished = false;
//                        sharedPreferences = context.getSharedPreferences("mealInfo",context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = (SharedPreferences.Editor) sharedPreferences.edit();
//                        editor.putBoolean("mealRunning",isMealNotificationFinished);
//                        editor.apply();
//
//                        Toast.makeText(context, "isMealNotificationFinished ="+isMealNotificationFinished, Toast.LENGTH_SHORT).show();
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                LocalDateTime now = LocalDateTime.now();// gets the current date and time
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
//                data.put("clientID", DataFromDatabase.clientuserID);
//                data.put("today",dtf.format(now));
//                return data;
//            }
//        };
//        Volley.newRequestQueue(context).add(request);
//        request.setRetryPolicy(new DefaultRetryPolicy(50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//
//    private void snack(Context context) {
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                response -> {
//                    Log.d("CalTracker Data Bro", response);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject dataObject = jsonObject.getJSONObject("Data");
//
//                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
//                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
//
//                        JSONObject valuesObject = dataObject.getJSONObject("Values");
//                        String calories = valuesObject.getString("Calories");
//
//                        //All parsing
//                        currentCalorie = Integer.parseInt(calories);
//                        calorieGoal = Integer.parseInt(calorieconsumegoal);
//                        int calDiff = calorieGoal - currentCalorie;
//                        String contentText = "You've consumed " + currentCalorie + " cals " + "\n" + "You have " + calDiff + " cals left to consumed for today";
//
//                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
//                                .setContentTitle("It's time to have some Snack!")
//                                .setContentText(contentText)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setSmallIcon(R.mipmap.logo)
//                                .setAutoCancel(true)
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(contentText))
//                                .build();
//
//                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        managerCompat.notify(1, notification);
//                        Intent intent = new Intent(context,SplashScreen.class);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                LocalDateTime now = LocalDateTime.now();// gets the current date and time
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
//                data.put("clientID", DataFromDatabase.clientuserID);
//                data.put("today",dtf.format(now));
//                return data;
//            }
//        };
//        Volley.newRequestQueue(context).add(request);
//        request.setRetryPolicy(new DefaultRetryPolicy(50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//
//    private void lunch(Context context) {
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                response -> {
//                    Log.d("CalTracker Data Bro", response);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject dataObject = jsonObject.getJSONObject("Data");
//
//                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
//                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
//
//                        JSONObject valuesObject = dataObject.getJSONObject("Values");
//                        String calories = valuesObject.getString("Calories");
//
//                        //All parsing
//                        currentCalorie = Integer.parseInt(calories);
//                        calorieGoal = Integer.parseInt(calorieconsumegoal);
//                        int calDiff = calorieGoal - currentCalorie;
//                        String contentText = "You've consumed " + currentCalorie + " cals " + "\n" + "You have " + calDiff + " cals left to consumed for today";
//
//                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
//                                .setContentTitle("It's time to Lunch!")
//                                .setContentText(contentText)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setSmallIcon(R.mipmap.logo)
//                                .setAutoCancel(true)
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(contentText))
//                                .build();
//
//                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        managerCompat.notify(1, notification);
//                        Intent intent = new Intent(context,SplashScreen.class);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                LocalDateTime now = LocalDateTime.now();// gets the current date and time
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
//                data.put("clientID", DataFromDatabase.clientuserID);
//                data.put("today",dtf.format(now));
//                return data;
//            }
//        };
//        Volley.newRequestQueue(context).add(request);
//        request.setRetryPolicy(new DefaultRetryPolicy(50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//
//    private void breakfast(Context context) {
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                response -> {
//                    Log.d("CalTracker Data Bro", response);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject dataObject = jsonObject.getJSONObject("Data");
//
//                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
//                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
//
//                        JSONObject valuesObject = dataObject.getJSONObject("Values");
//                        String calories = valuesObject.getString("Calories");
//
//                        //All parsing
//                        currentCalorie = Integer.parseInt(calories);
//                        calorieGoal = Integer.parseInt(calorieconsumegoal);
//
//                        String contentText = "You've consumed " + currentCalorie + " cals " + "\n" + "You have " + calDiff + " cals left to consumed for today";
//
//                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
//                                .setContentTitle("It's time to Breakfast!")
//                                .setContentText(contentText)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setSmallIcon(R.mipmap.logo)
//                                .setAutoCancel(true)
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(contentText))
//                                .build();
//
//                          NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//                           if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                              return;
//                         }
//                         managerCompat.notify(1, notification);
//                        Intent intent = new Intent(context,SplashScreen.class);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                LocalDateTime now = LocalDateTime.now();// gets the current date and time
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
//                data.put("clientID", DataFromDatabase.clientuserID);
//                data.put("today",dtf.format(now));
//                return data;
//            }
//        };
//        Volley.newRequestQueue(context).add(request);
//        request.setRetryPolicy(new DefaultRetryPolicy(50000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }Calorie Reminder
    private void Afterbreakfast(Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("CalTracker Data Bro", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataObject = jsonObject.getJSONObject("Data");

                        JSONObject goalsObject = dataObject.getJSONObject("Goals");
                        String calorieconsumegoal = goalsObject.getString("CalorieConsumeGoal");
                        String carbgoal = goalsObject.getString("CarbsGoal");
                        String fibergoal = goalsObject.getString("FiberGoal");
                        String proteingoal = goalsObject.getString("ProteinGoal");
                        String fatgoal = goalsObject.getString("FatsGoal");


                        JSONObject valuesObject = dataObject.getJSONObject("Values");
                        String calories = valuesObject.getString("Calories");
                        String carbs = valuesObject.getString("carbs");
                        String fiber = valuesObject.getString("fiber");
                        String protein = valuesObject.getString("protein");
                        String fat = valuesObject.getString("fat");

                        //All parsing
                        currentCalorie = Integer.parseInt(calories);
                        calorieGoal = Integer.parseInt(calorieconsumegoal);
                        calDiff =calorieGoal - currentCalorie;

                        carbDiff = Integer.parseInt(carbgoal) - Integer.parseInt(carbs);
                        fibDiff = Integer.parseInt(fibergoal) - Integer.parseInt(fiber);
                        proDiff = Integer.parseInt(proteingoal) - Integer.parseInt(protein);
                        fatDiff = Integer.parseInt(fatgoal) - Integer.parseInt(fat);

                        String calText = " and "+calDiff+"kcal of calorie";
                        String carbText = carbDiff+"g of carbs,";
                        String fibreText = " "+fibDiff+"g of fibre";
                        String fatText = " "+fatDiff+"g of fats,";
                        String proteinText = " "+proDiff+"g of protein,";

                        if(carbDiff<=0){
                            carbText ="";
                        }
                        if(calDiff<=0){
                            calText ="";
                        }
                        if(fatDiff<=0){
                            fatText ="";
                        }
                        if(proDiff<=0){
                            proteinText ="";
                        }
                        if(fibDiff<=0){
                            fibreText ="";
                        }

                        //  Toast.makeText(getContext(), "calConsumed : "+calConsumed +" /"+calories, Toast.LENGTH_SHORT).show();
                        String contentText = "You have " +carbText+ proteinText + fatText + fibreText + calText +" left to be consumed for the day";

                        Intent resultIntent = new Intent(context, SplashScreen.class);
                        resultIntent.putExtra("notification", "calorie");

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                                context, 605, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        );
                        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                                .setContentTitle("Nutrition reminder")
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.mipmap.logo)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(contentText))
                                .build();

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        managerCompat.notify(1, notification);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.e("CalTracker Data Bro", error.toString())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                LocalDateTime now = LocalDateTime.now();// gets the current date and time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
                data.put("clientID", DataFromDatabase.clientuserID);
                data.put("today",dtf.format(now));
                return data;
            }
        };
        Volley.newRequestQueue(context).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void dinner(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "calorie");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 604, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                .setContentTitle("Calorie Reminder")
                .setContentText("It's time to have your dinner!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It's time to have your dinner!"))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification);


        Log.i("TAG", "isMealNotificationFinished ="+isMealNotificationFinished);

    }

    private void snack(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "calorie");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 603, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                .setContentTitle("Calorie Reminder")
                .setContentText("It's time to have your snack!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It's time to have your snack!"))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification);

        Log.i("TAG", "isMealNotificationFinished ="+isMealNotificationFinished);

    }

    private void lunch(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "calorie");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 602, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                .setContentTitle("Calorie Reminder")
                .setContentText("It's time to have your lunch!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It's time to have your lunch!"))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification);

        Log.i("TAG", "isMealNotificationFinished ="+isMealNotificationFinished);
    }

    private void breakfast(Context context) {
        Intent resultIntent = new Intent(context, SplashScreen.class);
        resultIntent.putExtra("notification", "calorie");

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 601, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(context, "CalorieChannelId")
                .setContentTitle("Calorie Reminder")
                .setContentText("It's time to have your breakfast!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.logo)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It's time to have your breakfast!"))
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification);

        Log.i("TAG", "isMealNotificationFinished ="+isMealNotificationFinished);
    }
}
