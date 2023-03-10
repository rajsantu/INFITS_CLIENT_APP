////package com.example.infits;
////
////import android.app.Dialog;
////import android.graphics.Color;
////import android.graphics.drawable.AnimationDrawable;
////import android.os.Bundle;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////import androidx.fragment.app.Fragment;
////import androidx.fragment.app.FragmentManager;
////import androidx.navigation.Navigation;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////
////import android.os.Handler;
////import android.os.Looper;
////import android.os.Message;
////import android.util.Log;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.Button;
////import android.widget.ImageButton;
////import android.widget.ImageView;
////import android.widget.LinearLayout;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import com.android.volley.AuthFailureError;
////import com.android.volley.Request;
////import com.android.volley.toolbox.StringRequest;
////import com.android.volley.toolbox.Volley;
////import com.polidea.rxandroidble3.RxBleClient;
////import com.polidea.rxandroidble3.RxBleConnection;
////import com.polidea.rxandroidble3.RxBleDevice;
////import com.tenclouds.gaugeseekbar.GaugeSeekBar;
////import com.txusballesteros.SnakeView;
////
////import org.json.JSONArray;
////import org.json.JSONException;
////import org.json.JSONObject;
////
////import java.io.UnsupportedEncodingException;
////import java.nio.charset.StandardCharsets;
////import java.text.SimpleDateFormat;
////import java.util.ArrayList;
////import java.util.Calendar;
////import java.util.HashMap;
////import java.util.Map;
////import java.util.UUID;
////
////import io.reactivex.rxjava3.core.Observable;
////import io.reactivex.rxjava3.disposables.Disposable;
/////// import added by skp
////import java.util.Calendar;
////import devs.mulham.horizontalcalendar.HorizontalCalendar;
////import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
/////**
//// * A simple {@link Fragment} subclass.
//// * Use the {@link HeartRate#newInstance} factory method to
//// * create an instance of this fragment.
//// */
////public class HeartRate extends Fragment {
////
////    float val = 0f;
////
////    float value_wave = 1f;
////
////    boolean wheel = false;
////
////    Button startListening;
////    ImageView heartImageView;
////    ImageButton imgBack;
////
//////    SnakeView snakeView;
////
////    private Observable<RxBleConnection> connectionObservable;
////    private Observable<byte[]> notificationObservable;
////
////    TextView result_from,measuring,deviceName,min,avg,max;
////
////    LinearLayout after_measured,after_measured_title;
////
////    String value;
////
////    Disposable disposable;
////    RxBleDevice device;
////    RxBleClient rxBleClient;
////
////    // TODO: Rename parameter arguments, choose names that match
////    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////    private static final String ARG_PARAM1 = "param1";
////    private static final String ARG_PARAM2 = "param2";
////
////    // TODO: Rename and change types of parameters
////    private String mParam1;
////    private String mParam2;
////    private Disposable connectionDisposable;
////
////    public HeartRate() {
////        // Required empty public constructor
////    }
////
////    /**
////     * Use this factory method to create a new instance of
////     * this fragment using the provided parameters.
////     *
////     * @param param1 Parameter 1.
////     * @param param2 Parameter 2.
////     * @return A new instance of fragment HeartRate.
////     */
////    // TODO: Rename and change types and number of parameters
////    public static HeartRate newInstance(String param1, String param2) {
////        HeartRate fragment = new HeartRate();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
////        return fragment;
////    }
////
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout for this fragment
////        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
////
////        RecyclerView pastActivity = view.findViewById(R.id.past_activity);    // skp chng
////
////        ArrayList<String> dates = new ArrayList<>();
////        ArrayList<String> datas = new ArrayList<>();
////
////        String url = String.format("%sheartrate.php",DataFromDatabase.ipConfig);
////
////        max = view.findViewById(R.id.max);
////        avg = view.findViewById(R.id.avg);
////        min = view.findViewById(R.id.min);
////        imgBack = view.findViewById(R.id.back_heart);   //skp chng
////
////        min.setText(DataFromDatabase.bpmDown);
////        avg.setText(DataFromDatabase.bpm);
////        max.setText(DataFromDatabase.bpmUp);
////
////
////
//////        /* starts before 1 month from now */
//////        Calendar startDate = Calendar.getInstance();
//////        startDate.add(Calendar.MONTH, -1);
//////
//////        /* ends after 1 month from now */
//////        Calendar endDate = Calendar.getInstance();
//////        endDate.add(Calendar.MONTH, 1);
//////
//////        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
//////        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendarView)
//////                // on below line we are adding a range
//////                // as start date and end date to our calendar.
//////                  .range(startDate, endDate)
//////                // on below line we are providing a number of dates
//////                // which will be visible on the screen at a time.
//////                .datesNumberOnScreen(5)
//////                // at last we are calling a build method
//////                // to build our horizontal recycler view.
//////                .build();
//////        // on below line we are setting calendar listener to our calendar view.
//////        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//////            @Override
//////            public void onDateSelected(Calendar date, int position) {
//////                // on below line we are printing date
//////                // in the logcat which is selected.
//////                Log.e("TAG", "CURRENT DATE IS " + date);
//////            }
//////        });
////
////        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
////            try {
////                JSONObject jsonObject = new JSONObject(response);
////                JSONArray jsonArray = jsonObject.getJSONArray("heart");
////                for (int i = 0;i<jsonArray.length();i++){
////                    JSONObject object = jsonArray.getJSONObject(i);
////                    String data = object.getString("avg");
////                    String date = object.getString("date");
////                    dates.add(date);
////                    datas.add(data);
////                    System.out.println(datas.get(i));
////                    System.out.println(dates.get(i));
////                }
////                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(),dates,datas, Color.parseColor("#F1699E"));
//////                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
//////                pastActivity.setAdapter(ad);                                                            //skp chng
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        },error -> {
////            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
////            Log.d("Error",error.toString());
////        }){
////            @Nullable
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String,String> data = new HashMap<>();
////                data.put("clientID",DataFromDatabase.clientuserID);
////                return data;
////            }
////        };
////
////        Volley.newRequestQueue(getActivity()).add(stringRequest);
////
////        rxBleClient = RxBleClient.create(getContext());
//////        result_from = view.findViewById(R.id.result_from);
//////        after_measured = view.findViewById(R.id.after_measured);
//////        after_measured_title = view.findViewById(R.id.after_measured_title);
//////        deviceName = view.findViewById(R.id.deviceName);
////
//////        startListening = view.findViewById(R.id.startListening);
//////        heartImageView = view.findViewById(R.id.heart_anime);
////
////        AnimationDrawable heartAnimation = (AnimationDrawable) heartImageView.getBackground();
////
////       // measuring = view.findViewById(R.id.measuring);
////        try {
////            device = rxBleClient.getBleDevice(DataFromDatabase.macAddress);
////            connectionObservable = prepareConnectionObservable();
////            deviceName.append(" "+device.getName());
////        }catch (NullPointerException ex){
////            startListening.setClickable(false);
////            startListening.setOnClickListener(null);
////            startListening.setEnabled(false);
////            deviceName.append(" nill");
////            Toast.makeText(getContext(),"No device is connected",Toast.LENGTH_SHORT).show();
////        }
////        startListening.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                spinRotate();
////                wheel = true;
////                result_from.setText("000");
////                startListening.setVisibility(View.GONE);
////                heartAnimation.start();
////                measuring.setVisibility(View.VISIBLE);
////                after_measured.setVisibility(View.GONE);
////                after_measured_title.setVisibility(View.GONE);
//////                connectionDisposable = device.establishConnection(false)
//////                        .flatMapSingle(rxBleConnection -> rxBleConnection.readCharacteristic(convertFromInteger(0x2A37)))
//////                        .subscribe(
//////                                characteristicValue -> {
//////                                    value = byteArrayToHex(characteristicValue);
//////                                    String name = new String(characteristicValue);
//////                                    System.out.println("Value  "+value);
//////                                    System.out.println(name);
//////                                },
//////                                throwable -> {
//////                                    // Handle an error here.
//////                                    System.out.println(throwable);
//////                                }
//////                        );
////                disposable = connectionObservable
////                    .flatMap(rxBleConnection -> rxBleConnection.setupNotification(convertFromInteger(0x2A37)))
////                    .flatMap(notificationObservable -> notificationObservable)
////                    .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);
////                Handler handler = new Handler();
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        result_from.setText(value);
////                        heartAnimation.stop();
////                        heartAnimation.selectDrawable(0);
////                        heartAnimation.setVisible(true,true);
////                        disposable.dispose();
////                        measuring.setVisibility(View.GONE);
////                        after_measured.setVisibility(View.VISIBLE);
////                        after_measured_title.setVisibility(View.VISIBLE);
////                        startListening.setVisibility(View.VISIBLE);
//////                        snakeView.setVisibility(View.GONE);
////                        wheel = false;
////                        System.out.println("Help");
////                    }
////                }, 10000);
////            }
////
////            private void onNotificationSetupFailure(Throwable throwable) {
////                final Dialog dialog = new Dialog(getContext());
//////                dialog.requestWindowFeature(Window.);
////                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
////                dialog.setCancelable(true);
////                dialog.setContentView(R.layout.referralcodedialog);
////                dialog.show();
////            }
////
////            private void onNotificationReceived(byte[] bytes) {
////                final StringBuilder stringBuilder = new StringBuilder(bytes.length);
////                for(byte byteChar : bytes){
////                    stringBuilder.append(byteChar);
////                    System.out.print(byteChar + " ");
////                }
////
////
//////                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
//////                        stringBuilder.toString());
////                try {
////                    System.out.println(new String(bytes,"UTF-8")+"   "+stringBuilder.toString()+"   ");
////                } catch (UnsupportedEncodingException e) {
////                    e.printStackTrace();
////                }
////                System.out.print(bytes+"   ");
////                value = String.valueOf(bytes[1]);
////                System.out.println(value);
//////                System.out.print();
////                System.out.println("     Heart " + new String(bytes, StandardCharsets.UTF_16LE));
////
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        result_from.setText(value);
////                    }
////                },1000);
////            }
////        });
////
////        imgBack.setOnClickListener(v -> {
////            Navigation.findNavController(v).navigate(R.id.action_heartRate_to_dashBoardFragment);
////            FragmentManager manager = getActivity().getSupportFragmentManager();
////            manager.popBackStack();
////        });
////
////        return view;
////    }
////
////    public UUID convertFromInteger(int i) {
////        final long MSB = 0x0000000000001000L;
////        final long LSB = 0x800000805f9b34fbL;
////        long value = i & 0xFFFFFFFF;
////        return new UUID(MSB | (value << 32), LSB);
////    }
////    private String byteArrayToHex ( byte[] a){
////        StringBuilder sb = new StringBuilder(a.length * 2);
////        for (byte b : a)
////            sb.append(String.format("%02x", b & 0xff));
////        return sb.toString();
////    }
////    public void spinRotate(){
////        val = 0;
//////        new Handler().post(new Runnable() {
//////            @Override
//////            public void run() {
//////            while (wheel){
//////                    if (value_wave <=100){
//////                        snakeView.addValue(value_wave++);
//////                    }
//////                    else{
//////                        value_wave = 1f;
//////                    }
//////                try {
//////                    Thread.sleep(100);
//////                } catch (InterruptedException e) {
//////                    e.printStackTrace();
//////                }
//////                }
//////            }
//////        });
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                while (wheel){
//////                    snakeView.addValue(value_wave++);
//////                    snakeView.addValue(value_wave++);
////                    System.out.println("Hi");
////                    if (val >= 1.0f){
////                        val = 0;
////                        System.out.println("inside if");
////                    }
////                    else{
////                        val = (float) (val+0.1f);
////                        System.out.println("inside else" + val);
//////                        gaugeSeekBar.setProgress(val);
////                        try {
////                            Thread.sleep(100);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////            }
////        }).start();
////
////
////    }
////    private Observable<RxBleConnection> prepareConnectionObservable() {
////        return device
////                .establishConnection(false);
////    }
////}
//
//
//        package com.example.infits;
//
//        import android.app.Dialog;
//        import android.graphics.Color;
//        import android.graphics.drawable.AnimationDrawable;
//        import android.os.Bundle;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//        import androidx.fragment.app.Fragment;
//        import androidx.fragment.app.FragmentManager;
//        import androidx.navigation.Navigation;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import android.os.Handler;
//        import android.os.Looper;
//        import android.os.Message;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Button;
//        import android.widget.ImageButton;
//        import android.widget.ImageView;
//        import android.widget.LinearLayout;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import com.android.volley.AuthFailureError;
//        import com.android.volley.Request;
//        import com.android.volley.toolbox.StringRequest;
//        import com.android.volley.toolbox.Volley;
//        import com.jjoe64.graphview.GraphView;
//        import com.jjoe64.graphview.series.DataPoint;
//        import com.jjoe64.graphview.series.LineGraphSeries;
//        import com.polidea.rxandroidble3.RxBleClient;
//        import com.polidea.rxandroidble3.RxBleConnection;
//        import com.polidea.rxandroidble3.RxBleDevice;
//        import com.tenclouds.gaugeseekbar.GaugeSeekBar;
//        import com.txusballesteros.SnakeView;
//
//        import org.json.JSONArray;
//        import org.json.JSONException;
//        import org.json.JSONObject;
//
//        import java.io.UnsupportedEncodingException;
//        import java.nio.charset.StandardCharsets;
//        import java.text.SimpleDateFormat;
//        import java.util.ArrayList;
//        import java.util.Calendar;
//        import java.util.HashMap;
//        import java.util.Map;
//        import java.util.UUID;
//
//        import io.reactivex.rxjava3.core.Observable;
//        import io.reactivex.rxjava3.disposables.Disposable;
//
//
//        import java.util.Calendar;
//
//        import devs.mulham.horizontalcalendar.HorizontalCalendar;
//        import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HeartRate#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HeartRate extends Fragment {
//
//    float val = 0f;
//
//    float value_wave = 1f;
//
//    boolean wheel = false;
//
//    Button startListening;
//    ImageView heartImageView;
//    ImageButton imgBack;
//
////    SnakeView snakeView;
//
//    private Observable<RxBleConnection> connectionObservable;
//    private Observable<byte[]> notificationObservable;
//
//    TextView result_from,measuring,deviceName,min,avg,max;
//
//    LinearLayout after_measured,after_measured_title;
//
//    String value;
//
//    Disposable disposable;
//    RxBleDevice device;
//    RxBleClient rxBleClient;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private Disposable connectionDisposable;
//
//    public HeartRate() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HeartRate.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HeartRate newInstance(String param1, String param2) {
//        HeartRate fragment = new HeartRate();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
//
//        RecyclerView pastActivity = view.findViewById(R.id.past_activity);
//
//        GraphView graph = view.findViewById(R.id.graph); //suraj changes
////        int[] Data= {0,2,3,5,10,46,100,145,146,147,148,149,150,151,152,152,151,150,149,148,147,53,8,10,102,156,189,50,63,67,69,72,75,78,82,85,
////                90,95,99,105,110,115,121,126,132,137,143,
////                148,153,157,162,165,168,170,173,174,175,176,
////                177,177,177,176,176,175,173,172,171,169,168,
////                167,166,164,161,155,147,136,123,111,101,92,84,
////                78,74,70,67,65,64,62,61,61,60,60,59,59,58,58,
////                58,57,57,57,56,56,56,56,56,56,56,55,55,55,55,
////                55,54,54,};
//
//        ArrayList<String> dates = new ArrayList<>();
//        ArrayList<String> datas = new ArrayList<>();
//
//        String url = String.format("%sheartrate.php",DataFromDatabase.ipConfig);
//
////        max = view.findViewById(R.id.max);
////        avg = view.findViewById(R.id.avg);
////        min = view.findViewById(R.id.min);
//        imgBack = view.findViewById(R.id.back_heart);
////form a seriese (curve for graph)
//
//        LineGraphSeries<DataPoint> series= new LineGraphSeries<> ();
//        double y;
//        for (int x=0;x<90;x++){
//            y=Math.sin ( 80*x*0.2 )-2*Math.sin(x*0.2);
//            series.appendData ( new DataPoint ( x,y ), true, 90);
//         //   series.appendData(new DataPoint(i,Data[i]),true,90);
//        }
//        graph.addSeries ( series  );
//
//        series.setColor(Color.RED);
//        series.setDrawDataPoints ( true );
//        series.setThickness ( 5 );
//        series.setDataPointsRadius (2);
//
//        // activate horizontal zooming and scrolling
//        graph.getViewport().setScalable(true);
//        // activate horizontal scrolling
//        graph.getViewport().setScrollable(true);
//        // activate horizontal and vertical zooming and scrolling
//        graph.getViewport().setScalableY(true);
//        // activate vertical scrolling
//        graph.getViewport().setScrollableY(true);
//
//
//        //яку частину графіка виводити
//        // set manual X bounds
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(90);
//        // set manual Y bounds
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(5);
//
//
//    //        min.setText(DataFromDatabase.bpmDown);
////        avg.setText(DataFromDatabase.bpm);
////        max.setText(DataFromDatabase.bpmUp);
//
////        /* starts before 1 month from now */ calender horizontal view
////        Calendar startDate = Calendar.getInstance();
////        startDate.add(Calendar.MONTH, -1);
////
////        /* ends after 1 month from now */
////        Calendar endDate = Calendar.getInstance();
////        endDate.add(Calendar.MONTH, 1);
////
////        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
////        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
////                // on below line we are adding a range
////                // as start date and end date to our calendar.
////                .range(startDate, endDate)
////                // on below line we are providing a number of dates
////                // which will be visible on the screen at a time.
////                .datesNumberOnScreen(5)
////                // at last we are calling a build method
////                // to build our horizontal recycler view.
////                .build();
////
////
////
////
////        // on below line we are setting calendar listener to our calendar view.
////        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
////            @Override
////            public void onDateSelected(Calendar date, int position) {
////                // on below line we are printing date
////                // in the logcat which is selected.
////                Log.e("TAG", "CURRENT DATE IS " + date);
////            }
////        });
//
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONArray("heart");
//                for (int i = 0;i<jsonArray.length();i++){
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String data = object.getString("avg");
//                    String date = object.getString("date");
//                    dates.add(date);
//                    datas.add(data);
//                    System.out.println(datas.get(i));
//                    System.out.println(dates.get(i));
//                }
//                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(),dates,datas, Color.parseColor("#F1699E"));
//                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
//                pastActivity.setAdapter(ad);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        },error -> {
//            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//            Log.d("Error",error.toString());
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> data = new HashMap<>();
//                data.put("clientID",DataFromDatabase.clientuserID);
//                return data;
//            }
//        };
//
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
//
//        rxBleClient = RxBleClient.create(getContext());
//        result_from = view.findViewById(R.id.result_from);
////        after_measured = view.findViewById(R.id.after_measured);
////        after_measured_title = view.findViewById(R.id.after_measured_title);
//        deviceName = view.findViewById(R.id.deviceName);
//
//        startListening = view.findViewById(R.id.startListening);
//        heartImageView = view.findViewById(R.id.heart_anime);
//
//        AnimationDrawable heartAnimation = (AnimationDrawable) heartImageView.getBackground();
//
//        measuring = view.findViewById(R.id.measuring);
//        try {
//            device = rxBleClient.getBleDevice(DataFromDatabase.macAddress);
//            connectionObservable = prepareConnectionObservable();
//            deviceName.append(" "+device.getName());
//        }catch (NullPointerException ex){
//            startListening.setClickable(false);
//            startListening.setOnClickListener(null);
//            startListening.setEnabled(false);
//            deviceName.append(" nill");
//            Toast.makeText(getContext(),"No device is connected",Toast.LENGTH_SHORT).show();
//        }
//        startListening.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                spinRotate();
//                wheel = true;
//                result_from.setText("000");
//                startListening.setVisibility(View.GONE);
//                heartAnimation.start();
//                measuring.setVisibility(View.VISIBLE);
//                after_measured.setVisibility(View.GONE);
//                after_measured_title.setVisibility(View.GONE);
////                connectionDisposable = device.establishConnection(false)
////                        .flatMapSingle(rxBleConnection -> rxBleConnection.readCharacteristic(convertFromInteger(0x2A37)))
////                        .subscribe(
////                                characteristicValue -> {
////                                    value = byteArrayToHex(characteristicValue);
////                                    String name = new String(characteristicValue);
////                                    System.out.println("Value  "+value);
////                                    System.out.println(name);
////                                },
////                                throwable -> {
////                                    // Handle an error here.
////                                    System.out.println(throwable);
////                                }
////                        );
//                disposable = connectionObservable
//                        .flatMap(rxBleConnection -> rxBleConnection.setupNotification(convertFromInteger(0x2A37)))
//                        .flatMap(notificationObservable -> notificationObservable)
//                        .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        result_from.setText(value);
//                        heartAnimation.stop();
//                        heartAnimation.selectDrawable(0);
//                        heartAnimation.setVisible(true,true);
//                        disposable.dispose();
//                        measuring.setVisibility(View.GONE);
//                        after_measured.setVisibility(View.VISIBLE);
//                        after_measured_title.setVisibility(View.VISIBLE);
//                        startListening.setVisibility(View.VISIBLE);
////                        snakeView.setVisibility(View.GONE);
//                        wheel = false;
//                        System.out.println("Help");
//                    }
//                }, 10000);
//            }
//
//            private void onNotificationSetupFailure(Throwable throwable) {
//                final Dialog dialog = new Dialog(getContext());
////                dialog.requestWindowFeature(Window.);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.referralcodedialog);
//                dialog.show();
//            }
//
//            private void onNotificationReceived(byte[] bytes) {
//                final StringBuilder stringBuilder = new StringBuilder(bytes.length);
//                for(byte byteChar : bytes){
//                    stringBuilder.append(byteChar);
//                    System.out.print(byteChar + " ");
//                }
//
//
////                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
////                        stringBuilder.toString());
//                try {
//                    System.out.println(new String(bytes,"UTF-8")+"   "+stringBuilder.toString()+"   ");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.print(bytes+"   ");
//                value = String.valueOf(bytes[1]);
//                System.out.println(value);
////                System.out.print();
//                System.out.println("     Heart " + new String(bytes, StandardCharsets.UTF_16LE));
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        result_from.setText(value);
//                    }
//                },1000);
//            }
//        });
//
//        imgBack.setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_heartRate_to_dashBoardFragment);
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            manager.popBackStack();
//        });
//
//        return view;
//    }
//
//    public UUID convertFromInteger(int i) {
//        final long MSB = 0x0000000000001000L;
//        final long LSB = 0x800000805f9b34fbL;
//        long value = i & 0xFFFFFFFF;
//        return new UUID(MSB | (value << 32), LSB);
//    }
//    private String byteArrayToHex ( byte[] a){
//        StringBuilder sb = new StringBuilder(a.length * 2);
//        for (byte b : a)
//            sb.append(String.format("%02x", b & 0xff));
//        return sb.toString();
//    }
//    public void spinRotate(){
//        val = 0;
////        new Handler().post(new Runnable() {
////            @Override
////            public void run() {
////            while (wheel){
////                    if (value_wave <=100){
////                        snakeView.addValue(value_wave++);
////                    }
////                    else{
////                        value_wave = 1f;
////                    }
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                }
////            }
////        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (wheel){
////                    snakeView.addValue(value_wave++);
////                    snakeView.addValue(value_wave++);
//                    System.out.println("Hi");
//                    if (val >= 1.0f){
//                        val = 0;
//                        System.out.println("inside if");
//                    }
//                    else{
//                        val = (float) (val+0.1f);
//                        System.out.println("inside else" + val);
////                        gaugeSeekBar.setProgress(val);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
//    private Observable<RxBleConnection> prepareConnectionObservable() {
//        return device
//                .establishConnection(false);
//    }
//}
//package com.example.infits;
//
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.polidea.rxandroidble3.RxBleClient;
//import com.polidea.rxandroidble3.RxBleConnection;
//import com.polidea.rxandroidble3.RxBleDevice;
//import com.tenclouds.gaugeseekbar.GaugeSeekBar;
//import com.txusballesteros.SnakeView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.disposables.Disposable;
///// import added by skp
//import java.util.Calendar;
//import devs.mulham.horizontalcalendar.HorizontalCalendar;
//import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HeartRate#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HeartRate extends Fragment {
//
//    float val = 0f;
//
//    float value_wave = 1f;
//
//    boolean wheel = false;
//
//    Button startListening;
//    ImageView heartImageView;
//    ImageButton imgBack;
//
////    SnakeView snakeView;
//
//    private Observable<RxBleConnection> connectionObservable;
//    private Observable<byte[]> notificationObservable;
//
//    TextView result_from,measuring,deviceName,min,avg,max;
//
//    LinearLayout after_measured,after_measured_title;
//
//    String value;
//
//    Disposable disposable;
//    RxBleDevice device;
//    RxBleClient rxBleClient;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private Disposable connectionDisposable;
//
//    public HeartRate() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HeartRate.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HeartRate newInstance(String param1, String param2) {
//        HeartRate fragment = new HeartRate();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
//
//        RecyclerView pastActivity = view.findViewById(R.id.past_activity);    // skp chng
//
//        ArrayList<String> dates = new ArrayList<>();
//        ArrayList<String> datas = new ArrayList<>();
//
//        String url = String.format("%sheartrate.php",DataFromDatabase.ipConfig);
//
//        max = view.findViewById(R.id.max);
//        avg = view.findViewById(R.id.avg);
//        min = view.findViewById(R.id.min);
//        imgBack = view.findViewById(R.id.back_heart);   //skp chng
//
//        min.setText(DataFromDatabase.bpmDown);
//        avg.setText(DataFromDatabase.bpm);
//        max.setText(DataFromDatabase.bpmUp);
//
//
//
////        /* starts before 1 month from now */
////        Calendar startDate = Calendar.getInstance();
////        startDate.add(Calendar.MONTH, -1);
////
////        /* ends after 1 month from now */
////        Calendar endDate = Calendar.getInstance();
////        endDate.add(Calendar.MONTH, 1);
////
////        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
////        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this,R.id.calendarView)
////                // on below line we are adding a range
////                // as start date and end date to our calendar.
////                  .range(startDate, endDate)
////                // on below line we are providing a number of dates
////                // which will be visible on the screen at a time.
////                .datesNumberOnScreen(5)
////                // at last we are calling a build method
////                // to build our horizontal recycler view.
////                .build();
////        // on below line we are setting calendar listener to our calendar view.
////        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
////            @Override
////            public void onDateSelected(Calendar date, int position) {
////                // on below line we are printing date
////                // in the logcat which is selected.
////                Log.e("TAG", "CURRENT DATE IS " + date);
////            }
////        });
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONArray("heart");
//                for (int i = 0;i<jsonArray.length();i++){
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String data = object.getString("avg");
//                    String date = object.getString("date");
//                    dates.add(date);
//                    datas.add(data);
//                    System.out.println(datas.get(i));
//                    System.out.println(dates.get(i));
//                }
//                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(),dates,datas, Color.parseColor("#F1699E"));
////                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
////                pastActivity.setAdapter(ad);                                                            //skp chng
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        },error -> {
//            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//            Log.d("Error",error.toString());
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> data = new HashMap<>();
//                data.put("clientID",DataFromDatabase.clientuserID);
//                return data;
//            }
//        };
//
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
//
//        rxBleClient = RxBleClient.create(getContext());
////        result_from = view.findViewById(R.id.result_from);
////        after_measured = view.findViewById(R.id.after_measured);
////        after_measured_title = view.findViewById(R.id.after_measured_title);
////        deviceName = view.findViewById(R.id.deviceName);
//
////        startListening = view.findViewById(R.id.startListening);
////        heartImageView = view.findViewById(R.id.heart_anime);
//
//        AnimationDrawable heartAnimation = (AnimationDrawable) heartImageView.getBackground();
//
//       // measuring = view.findViewById(R.id.measuring);
//        try {
//            device = rxBleClient.getBleDevice(DataFromDatabase.macAddress);
//            connectionObservable = prepareConnectionObservable();
//            deviceName.append(" "+device.getName());
//        }catch (NullPointerException ex){
//            startListening.setClickable(false);
//            startListening.setOnClickListener(null);
//            startListening.setEnabled(false);
//            deviceName.append(" nill");
//            Toast.makeText(getContext(),"No device is connected",Toast.LENGTH_SHORT).show();
//        }
//        startListening.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                spinRotate();
//                wheel = true;
//                result_from.setText("000");
//                startListening.setVisibility(View.GONE);
//                heartAnimation.start();
//                measuring.setVisibility(View.VISIBLE);
//                after_measured.setVisibility(View.GONE);
//                after_measured_title.setVisibility(View.GONE);
////                connectionDisposable = device.establishConnection(false)
////                        .flatMapSingle(rxBleConnection -> rxBleConnection.readCharacteristic(convertFromInteger(0x2A37)))
////                        .subscribe(
////                                characteristicValue -> {
////                                    value = byteArrayToHex(characteristicValue);
////                                    String name = new String(characteristicValue);
////                                    System.out.println("Value  "+value);
////                                    System.out.println(name);
////                                },
////                                throwable -> {
////                                    // Handle an error here.
////                                    System.out.println(throwable);
////                                }
////                        );
//                disposable = connectionObservable
//                    .flatMap(rxBleConnection -> rxBleConnection.setupNotification(convertFromInteger(0x2A37)))
//                    .flatMap(notificationObservable -> notificationObservable)
//                    .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        result_from.setText(value);
//                        heartAnimation.stop();
//                        heartAnimation.selectDrawable(0);
//                        heartAnimation.setVisible(true,true);
//                        disposable.dispose();
//                        measuring.setVisibility(View.GONE);
//                        after_measured.setVisibility(View.VISIBLE);
//                        after_measured_title.setVisibility(View.VISIBLE);
//                        startListening.setVisibility(View.VISIBLE);
////                        snakeView.setVisibility(View.GONE);
//                        wheel = false;
//                        System.out.println("Help");
//                    }
//                }, 10000);
//            }
//
//            private void onNotificationSetupFailure(Throwable throwable) {
//                final Dialog dialog = new Dialog(getContext());
////                dialog.requestWindowFeature(Window.);
//                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.referralcodedialog);
//                dialog.show();
//            }
//
//            private void onNotificationReceived(byte[] bytes) {
//                final StringBuilder stringBuilder = new StringBuilder(bytes.length);
//                for(byte byteChar : bytes){
//                    stringBuilder.append(byteChar);
//                    System.out.print(byteChar + " ");
//                }
//
//
////                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
////                        stringBuilder.toString());
//                try {
//                    System.out.println(new String(bytes,"UTF-8")+"   "+stringBuilder.toString()+"   ");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                System.out.print(bytes+"   ");
//                value = String.valueOf(bytes[1]);
//                System.out.println(value);
////                System.out.print();
//                System.out.println("     Heart " + new String(bytes, StandardCharsets.UTF_16LE));
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        result_from.setText(value);
//                    }
//                },1000);
//            }
//        });
//
//        imgBack.setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_heartRate_to_dashBoardFragment);
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            manager.popBackStack();
//        });
//
//        return view;
//    }
//
//    public UUID convertFromInteger(int i) {
//        final long MSB = 0x0000000000001000L;
//        final long LSB = 0x800000805f9b34fbL;
//        long value = i & 0xFFFFFFFF;
//        return new UUID(MSB | (value << 32), LSB);
//    }
//    private String byteArrayToHex ( byte[] a){
//        StringBuilder sb = new StringBuilder(a.length * 2);
//        for (byte b : a)
//            sb.append(String.format("%02x", b & 0xff));
//        return sb.toString();
//    }
//    public void spinRotate(){
//        val = 0;
////        new Handler().post(new Runnable() {
////            @Override
////            public void run() {
////            while (wheel){
////                    if (value_wave <=100){
////                        snakeView.addValue(value_wave++);
////                    }
////                    else{
////                        value_wave = 1f;
////                    }
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                }
////            }
////        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (wheel){
////                    snakeView.addValue(value_wave++);
////                    snakeView.addValue(value_wave++);
//                    System.out.println("Hi");
//                    if (val >= 1.0f){
//                        val = 0;
//                        System.out.println("inside if");
//                    }
//                    else{
//                        val = (float) (val+0.1f);
//                        System.out.println("inside else" + val);
////                        gaugeSeekBar.setProgress(val);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();
//
//
//    }
//    private Observable<RxBleConnection> prepareConnectionObservable() {
//        return device
//                .establishConnection(false);
//    }
//}

package com.example.infits;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.RxBleConnection;
import com.polidea.rxandroidble3.RxBleDevice;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;
import com.txusballesteros.SnakeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;


import java.util.Calendar;

//import devs.mulham.horizontalcalendar.HorizontalCalendar;
//import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeartRate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartRate extends Fragment {

    float val = 0f;
    LineChart mpLineChart;
    float value_wave = 1f;

    boolean wheel = false;

    Button startListening,  button_start;
    ImageView heartImageView;
    ImageButton imgBack;

//    SnakeView snakeView;

    private Observable<RxBleConnection> connectionObservable;
    private Observable<byte[]> notificationObservable;


    TextView result_from,measuring,deviceName,min,avg,max,textView;

    LinearLayout after_measured,after_measured_title;

    String value;

    Disposable disposable;
    RxBleDevice device;
    RxBleClient rxBleClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Disposable connectionDisposable;

    public HeartRate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeartRate.
     */
    // TODO: Rename and change types and number of parameters
    public static HeartRate newInstance(String param1, String param2) {
        HeartRate fragment = new HeartRate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

   // String json_url =" http://192.168.43.70:8090/getinfo.php";


// ...

    // Get a reference to the left YAxis object and customize it

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_heart_rate, container, false);
//     RequestQueue mQueue = Volley.newRequestQueue ( this );

        Context context = getContext();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String json_url ="http://192.168.43.70:8090/infits/heartTodayGraph.php";
//        Connection connection = DriverManager.getConnection("http://192.168.43.70:8090/infits/heartTodayGraph.php", "username", "password");

        ArrayList<Entry> entries = new ArrayList<>();
        RecyclerView pastActivity = view.findViewById(R.id.past_activity);
        mpLineChart=(LineChart) view.findViewById(R.id.line_chart);
        LineDataSet lineDataSet1=new LineDataSet(dataValues1(),"Data Set 1");
       // LineDataSet lineDataSet1 = new LineDataSet(entries, "My Data");
      // LineDataSet lineDataSet1=new LineDataSet("Data Set 1");
        lineDataSet1.setColor ( Color.rgb(239,101,159) );
        lineDataSet1.setDrawCircles ( false );

        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data1=new LineData(dataSets);
        mpLineChart.setData(data1);
        mpLineChart.invalidate();
        //
        mpLineChart.setDrawGridBackground(false);
        lineDataSet1.setColor ( Color.RED );
        lineDataSet1.setColor ( Color.rgb(239,101,159) );
        lineDataSet1.setDrawCircles ( false );
        int alpha = (int)(0.5 * 255.0f);
        lineDataSet1.setColor(Color.argb(alpha,255,96,155));
        lineDataSet1.setCircleColor ( Color.argb(0,255,96,155) );
        lineDataSet1.setCircleHoleColor ( Color.argb(0,255,96,155) );
        lineDataSet1.setCircleHoleRadius ( 1 );
        lineDataSet1.setCircleRadius ( 1);
        //mpLineChart.setBackgroundColor(Color.GREEN);
        //GraphView graph = view.findViewById(R.id.graph); //suraj changes
//        int[] Data= {0,2,3,5,10,46,100,145,146,147,148,149,150,151,152,152,151,150,149,148,147,53,8,10,102,156,189,50,63,67,69,72,75,78,82,85,
//                90,95,99,105,110,115,121,126,132,137,143,
//                148,153,157,162,165,168,170,173,174,175,176,
//                177,177,177,176,176,175,173,172,171,169,168,
//                167,166,164,161,155,147,136,123,111,101,92,84,
//                78,74,70,67,65,64,62,61,61,60,60,59,59,58,58,
//                58,57,57,57,56,56,56,56,56,56,56,55,55,55,55,
//                55,54,54,};

        Legend legend=mpLineChart.getLegend ();
        legend.setEnabled ( false );


        XAxis xAxis=mpLineChart.getXAxis ();
        YAxis yAxisLeft=mpLineChart.getAxisLeft ();
        YAxis yAxisRight=mpLineChart.getAxisRight ();
      //  xAxis.setValueFormatter( new XAxisValueFormatter () );
        //mpLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri"}));

        mpLineChart.getXAxis().setDrawGridLines(false);
        mpLineChart.getAxisLeft ().setDrawAxisLine ( false );
        yAxisLeft.setDrawAxisLine ( false );
        mpLineChart.getDescription().setEnabled(false);
       lineDataSet1.setDrawValues(false);
      //  yAxisLeft.setValueFormatter ( new MyAxisValueFormatter () );


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mpLineChart.getXAxis ().setDrawAxisLine ( false ); // X axis line removed
      mpLineChart.getAxisLeft().setDrawLabels(false);
//      lineDataSet1.setCircleSize ( 60 );
        lineDataSet1.setDrawCircles ( true );
        lineDataSet1.setDrawCircleHole ( true );
        lineDataSet1.setCircleColor ( Color.RED );
        lineDataSet1.setCircleHoleColor ( Color.RED );
        lineDataSet1.setCircleHoleRadius ( 2 );
        lineDataSet1.setCircleRadius ( 2 );
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> datas = new ArrayList<>();

        String url = String.format("%sheartrate.php",DataFromDatabase.ipConfig);
//        ArrayList<Entry> entries = new ArrayList<>();
// Instantiate a new LineDataSet object with a label for the data
//        LineDataSet lineDataSet = new LineDataSet(entries, "My Data");
// Instantiate a new LineData object and set the LineDataSet on it
       // LineData lineData = new LineData(lineDataSet1);

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest ( Request.Method.POST , json_url ,  null ,
                new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse ( JSONObject response ) {
//                                try {
//                                    Name.setText  (response.getString ( "Name" ));
//                                    Email.setText (response.getString ( "value" ) );
//                                    Password.setText ( response.getString ( "time" ) );
//                                } catch (JSONException e) {
//                                    e.printStackTrace ();
////                                }
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                float xValue = jsonObject.optInt ("value");
//                                float yValue = jsonObject.optInt ("time");
//                                entries.add(new Entry(xValue, yValue));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//// Notify the LineData object that the Entry objects have been updated
//                      //  lineDataSet1.notifyDataChanged();
//
//                        // Invalidate the LineChart to trigger a redraw
//                    mpLineChart.invalidate();

                        try {
                            JSONArray jsonArray = response.getJSONArray ( "heart" );
                            ArrayList<Entry> dataVals=new ArrayList<Entry>();

                            String[] timeArray = new String[jsonArray.length()];
                            String[] valueArray = new String[jsonArray.length()];
                            for(int i=0; i<jsonArray.length ();i++)
                            {
                               // JSONArray jsonObject = jsonArray.getJSONArray ( i );
                                JSONObject heart = jsonArray.getJSONObject (i);


                                String time=heart.getString ("time"  );
                               // dataVals.add(new Entry(valueArray,timeArray) );
                                //String  time=heart.getString ( "time" );
                                valueArray[i] = heart.getString ( "value" );
                                timeArray[i] = heart.getString ( "time" );

//                                mpLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{time+""}));
//
                                mpLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(timeArray));
                                mpLineChart.getAxisRight().setValueFormatter(new IndexAxisValueFormatter(valueArray));

                               mpLineChart.getData().notifyDataChanged();

                               mpLineChart.notifyDataSetChanged();
                                //mpLineChart.getAxisRight().setValueFormatter(new IndexAxisValueFormatter(new String[]{value+""}));

//                                        Email.setText (response.getString ( "value" ) );
//                                        Password.setText ( response.getString ( "time" ) );
//                                        Email.append ( value+" " );
//                                        Password.append ( time+" " );



                            }

                        } catch (JSONException e) {
                            e.printStackTrace ();
                        }

                    }
                } , new Response.ErrorListener () {
            @Override
            public void onErrorResponse ( VolleyError error ) {
               // Toast.makeText ( HeartRate.this,"Something went wrong",Toast.LENGTH_SHORT ).show ();
                error.printStackTrace ();
            }
        } );
        mQueue.add (jsonObjectRequest );
        //MySingleton.getInstance ( MainActivity.this ).addToRequestque ( jsonObjectRequest ) ;


//        max = view.findViewById(R.id.max);
//        avg = view.findViewById(R.id.avg);
//        min = view.findViewById(R.id.min);
        imgBack = view.findViewById(R.id.back_heart);
      //form a seriese (curve for graph)

      /*  LineGraphSeries<DataPoint> series= new LineGraphSeries<> ();
        double y;
        for (int x=0;x<90;x++){
            y=Math.sin ( 80*x*0.2 )-2*Math.sin(x*0.2);
            series.appendData ( new DataPoint ( x,y ), true, 90);
            //   series.appendData(new DataPoint(i,Data[i]),true,90);
        }
        graph.addSeries ( series  );

        series.setColor(Color.rgb(239, 101, 159));
        series.setDrawDataPoints ( true );
        series.setThickness ( 5 );
        series.setDataPointsRadius (2);

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);
        // activate vertical scrolling
        graph.getViewport().setScrollableY(true);


        //яку частину графіка виводити
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(90);
        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);*/


        //        min.setText(DataFromDatabase.bpmDown);
//        avg.setText(DataFromDatabase.bpm);
//        max.setText(DataFromDatabase.bpmUp);

//        /* starts before 1 month from now */ calender horizontal view
//        Calendar startDate = Calendar.getInstance();
//        startDate.add(Calendar.MONTH, -1);
//
//        /* ends after 1 month from now */
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.MONTH, 1);
//
//        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
//        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
//                // on below line we are adding a range
//                // as start date and end date to our calendar.
//                .range(startDate, endDate)
//                // on below line we are providing a number of dates
//                // which will be visible on the screen at a time.
//                .datesNumberOnScreen(5)
//                // at last we are calling a build method
//                // to build our horizontal recycler view.
//                .build();
//
//
//
//
//        // on below line we are setting calendar listener to our calendar view.
//        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar date, int position) {
//                // on below line we are printing date
//                // in the logcat which is selected.
//                Log.e("TAG", "CURRENT DATE IS " + date);
//            }
//        });


      //button_start=button_start.findViewById ( R.id.start_title);
      Button button_start=(Button) view.findViewById ( R.id.start_title );
      TextView avg_hr=(TextView) view.findViewById ( R.id.avg_hr_digit ) ;
        TextView max_hr=(TextView) view.findViewById ( R.id.max_hr_digit ) ;
        TextView min_hr=(TextView) view.findViewById (R.id.min_hr_digit) ;
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///final String originalText = textView.findViewById ( R.id.avg_hr_digit ).toString();
                // final String originalText = textView1.getText().toString();
                avg_hr.setText("__");
                max_hr.setText("__");
                min_hr.setText("__");
                button_start.setText ( "Measuring" );
                avg_hr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        avg_hr.setText("89");
                        max_hr.setText("124");
                        min_hr.setText("69");

                        button_start.setText ( "RESTART" );

                    }
                }, 10000);
            }
        });

        min.setText(DataFromDatabase.bpmDown);
        avg.setText(DataFromDatabase.bpm);
        max.setText(DataFromDatabase.bpmUp);
        int noOfDays=10;
        ArrayList<String> fetchedDatesHeart=new ArrayList<>();
        fetchedDatesHeart.clear();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            try {
                Log.d("response123",response.toString());
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("heart");
                Log.d("arraylength",String.valueOf(jsonArray.length()));
                for (int i=0;i<jsonArray.length();i++){
                    fetchedDatesHeart.add(jsonArray.getJSONObject(i).getString("date"));
                }
                for (int i=0;i<noOfDays;i++){
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -i);
                    Log.d("featched",fetchedDatesHeart.toString());
                    Log.d("currentInstance",dateFormat.format(cal.getTime()).toString());
                    if(fetchedDatesHeart.contains(dateFormat.format(cal.getTime()).toString())==true){
                        int index=fetchedDatesHeart.indexOf(dateFormat.format(cal.getTime()));
                        Log.d("index",String.valueOf(index));
                        JSONObject object=jsonArray.getJSONObject(index);
                        dates.add(dateFormat.format(cal.getTime()));
                        String data=object.getString("avg").toString();
                        datas.add(data);
                    }
                    else {
                        dates.add(dateFormat.format(cal.getTime()));
                        datas.add("0");
                    }
                }
//                for (int i = 0;i<jsonArray.length();i++){
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String data = object.getString("avg");
//                    String date = object.getString("date");
//                    dates.add(date);
//                    datas.add(data);
//                    System.out.println(datas.get(i));
//                    System.out.println(dates.get(i));
//                }
                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(),dates,datas, Color.parseColor("#F1699E"));
                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
                pastActivity.setAdapter(ad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            if (getActivity() != null) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
            Log.d("Error", error.toString());
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("clientID",DataFromDatabase.clientuserID);
                return data;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);

        rxBleClient = RxBleClient.create(getContext());
        result_from = view.findViewById(R.id.result_from);
//        after_measured = view.findViewById(R.id.after_measured);
//        after_measured_title = view.findViewById(R.id.after_measured_title);
        deviceName = view.findViewById(R.id.deviceName);

        startListening = view.findViewById(R.id.startListening);
        heartImageView = view.findViewById(R.id.heart_anime);

        AnimationDrawable heartAnimation = (AnimationDrawable) heartImageView.getBackground();

        measuring = view.findViewById(R.id.measuring);
        try {
            // device = rxBleClient.getBleDevice(DataFromDatabase.macAddress);
            connectionObservable = prepareConnectionObservable();
            deviceName.append(" "+device.getName());
        }catch (NullPointerException ex){
            startListening.setClickable(false);
            startListening.setOnClickListener(null);
            startListening.setEnabled(false);
            deviceName.append(" nill");
            Toast.makeText(getContext(),"No device is connected",Toast.LENGTH_SHORT).show();
        }
        startListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinRotate();
                wheel = true;
                result_from.setText("000");
                startListening.setVisibility(View.GONE);
                heartAnimation.start();
                measuring.setVisibility(View.VISIBLE);
                after_measured.setVisibility(View.GONE);
                after_measured_title.setVisibility(View.GONE);
//                connectionDisposable = device.establishConnection(false)
//                        .flatMapSingle(rxBleConnection -> rxBleConnection.readCharacteristic(convertFromInteger(0x2A37)))
//                        .subscribe(
//                                characteristicValue -> {
//                                    value = byteArrayToHex(characteristicValue);
//                                    String name = new String(characteristicValue);
//                                    System.out.println("Value  "+value);
//                                    System.out.println(name);
//                                },
//                                throwable -> {
//                                    // Handle an error here.
//                                    System.out.println(throwable);
//                                }
//                        );
                disposable = connectionObservable
                        .flatMap(rxBleConnection -> rxBleConnection.setupNotification(convertFromInteger(0x2A37)))
                        .flatMap(notificationObservable -> notificationObservable)
                        .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result_from.setText(value);
                        heartAnimation.stop();
                        heartAnimation.selectDrawable(0);
                        heartAnimation.setVisible(true,true);
                        disposable.dispose();
                        measuring.setVisibility(View.GONE);
                        after_measured.setVisibility(View.VISIBLE);
                        after_measured_title.setVisibility(View.VISIBLE);
                        startListening.setVisibility(View.VISIBLE);
//                        snakeView.setVisibility(View.GONE);
                        wheel = false;
                        System.out.println("Help");
                    }
                }, 10000);
            }

            private void onNotificationSetupFailure(Throwable throwable) {
                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.referralcodedialog);
                dialog.show();
            }

            private void onNotificationReceived(byte[] bytes) {
                final StringBuilder stringBuilder = new StringBuilder(bytes.length);
                for(byte byteChar : bytes){
                    stringBuilder.append(byteChar);
                    System.out.print(byteChar + " ");
                }


//                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
//                        stringBuilder.toString());
                try {
                    System.out.println(new String(bytes,"UTF-8")+"   "+stringBuilder.toString()+"   ");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.print(bytes+"   ");
                value = String.valueOf(bytes[1]);
                System.out.println(value);
//                System.out.print();
                System.out.println("     Heart " + new String(bytes, StandardCharsets.UTF_16LE));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result_from.setText(value);
                    }
                },1000);
            }
        });

        imgBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_heartRate_to_dashBoardFragment);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
        });

        return view;
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }
    private String byteArrayToHex ( byte[] a){
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    public void spinRotate(){
        val = 0;
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//            while (wheel){
//                    if (value_wave <=100){
//                        snakeView.addValue(value_wave++);
//                    }
//                    else{
//                        value_wave = 1f;
//                    }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                }
//            }
//        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (wheel){
//                    snakeView.addValue(value_wave++);
//                    snakeView.addValue(value_wave++);
                    System.out.println("Hi");
                    if (val >= 1.0f){
                        val = 0;
                        System.out.println("inside if");
                    }
                    else{
                        val = (float) (val+0.1f);
                        System.out.println("inside else" + val);
//                        gaugeSeekBar.setProgress(val);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
    private Observable<RxBleConnection> prepareConnectionObservable() {
        return device
                .establishConnection(false);
    }

//    Connection connection;
//
//    {
//        try {
//            Connection connection = DriverManager.getConnection("http://192.168.43.70:8090/infits/heartTodayGraph.php", "username", "password");
//
//        } catch (SQLException e) {
//            throw new RuntimeException ( e );
//        }
//    }
//
//    PreparedStatement statement;
//
//    {
//        try {
//            statement = connection.prepareStatement("SELECT x, y FROM entries");
//        } catch (SQLException e) {
//            throw new RuntimeException ( e );
//        }
//    }
//
//    ResultSet resultSet;
//
//    {
//        try {
//            resultSet = statement.executeQuery();
//        } catch (SQLException e) {
//            throw new RuntimeException ( e );
//        }
//    }



    private ArrayList<Entry> dataValues1()
    {

        ArrayList<Entry> dataVals=new ArrayList<Entry>();


//        while (resultSet.next()) {
//            int x = resultSet.getInt("x");
//            int y = resultSet.getInt("y");
//            Entry entry = new Entry(x, y);
//            entries.add(entry);
//        }
//
        dataVals.add(new Entry(0,-1));
        dataVals.add(new Entry(1,5));
        dataVals.add(new Entry(2,-2));
        dataVals.add(new Entry(3,0));
        dataVals.add(new Entry(4,1));
        dataVals.add(new Entry(5,8));


        return dataVals;
    }
//    private class  MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
//
//        @Override
//        public String getFormattedValue ( float value , AxisBase axis ) {
//            axis.setLabelCount ( 3,true );
//            return value+ "$";
//        }}




//    public class XAxisValueFormatter extends ValueFormatter {
//        @Override
//        public String getAxisLabel(float value, AxisBase axis) {
//            Log.d("ADebugTag", "Value: " + Float.toString(value));   // pass the value instead of 24000
//            int axisValue = (int) value/40;
//            if (axisValue >= 0) {
//                String sh = String.format("%02d:%02d", (axisValue / 3600 * 60 + ((axisValue % 3600) / 60)), (axisValue % 60));
//                return sh;
//            } else {
//                return "";
//            }
//        }
//
//
//    }


}