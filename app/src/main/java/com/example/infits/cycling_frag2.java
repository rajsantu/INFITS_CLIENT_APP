package com.example.infits;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


public class cycling_frag2 extends Fragment implements LocationListener {

    long time;

    float totalDistance,weight,calorie_burn,temp_calorie=0;
    private double distance;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Location lastLocation;
    TextView distance_show,Calorie_meter,time_meter;
    Button btn_pause,btn_start;
    TextView running_txt, cont_running_txt,dunit;

    public cycling_frag2 () {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static cycling_frag2 newInstance ( String param1 , String param2 ) {
        cycling_frag2 fragment = new cycling_frag2 ();

        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view=inflater.inflate ( R.layout.fragment_cycling_frag2 , container , false );
        // Inflate the layout for this fragment
        distance_show=view.findViewById(R.id.textView70);
        btn_pause=view.findViewById ( R.id.imageView86 );
        btn_start=view.findViewById ( R.id.imageView105 );
        running_txt=view.findViewById ( R.id.textView63 );
        cont_running_txt=view.findViewById ( R.id.textView89 );
        dunit=view.findViewById(R.id.textView83);
        Calorie_meter=view.findViewById(R.id.textView72);
        time_meter=view.findViewById(R.id.textView73);
        time=System.currentTimeMillis();
        Calorie_meter.setText("0");
        time_meter.setText("0");

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        startLocationUpdates();

        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(requireContext());
        weight= data.getFloat("weight",60);

        btn_pause.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.VISIBLE );
                btn_pause.setVisibility ( View.GONE );
                running_txt.setVisibility ( View.GONE );
                cont_running_txt.setVisibility ( View.VISIBLE );
                stopLocationUpdates();
                time=0;
                temp_calorie+=calorie_burn;
            }
        } );

        btn_start.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                btn_start.setVisibility ( View.GONE );
                btn_pause.setVisibility ( View.VISIBLE );
                running_txt.setVisibility ( View.VISIBLE);
                cont_running_txt.setVisibility ( View.GONE);
                startLocationUpdates();
                time =  System.currentTimeMillis();
            }
        } );

        return view;

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        float accuracy = location.getAccuracy();

        // Only process the location if the accuracy is less than 10 meters
        if (accuracy < 10) {
            // Calculate the distance between the current and last location
            if (lastLocation != null) {
                float distance = location.distanceTo(lastLocation);
                totalDistance += distance;
            }
            // Update the last location
            lastLocation = location;
            distance_show.setText(String.format("%.2f",totalDistance));

            if(totalDistance/1000>=1) dunit.setText("KM");
            Calorieburn(location.getSpeed(),weight );

        }
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(this);
        Log.d("Location Stopped","");
        lastLocation=null;
    }

    public void Calorieburn(float speed,float weight)
    {
        speed =(float) (speed*3.6);
        float MET=0;
        long elapsed_time;
        elapsed_time=(System.currentTimeMillis()-time)/1000;

        if(speed==0){}
        else {


            MET = (float) ((0.175 * speed) + 3.5);

            calorie_burn = MET * weight * (elapsed_time)/3600;
        }

        Log.d("calorie_burn",String.valueOf(calorie_burn));
        Log.d("MET_burn",String.valueOf(MET));
        Log.d("speed",String.valueOf(speed));
        Log.d("time",Long.toString(elapsed_time));


        //  if(elapsed_time)

        time_meter.setText(String.format("%.1f",(float)elapsed_time));






        Calorie_meter.setText(String.format("%.1f", (calorie_burn+temp_calorie) ));



    }




}