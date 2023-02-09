package com.example.infits;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.scan.ScanSettings;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.rxjava3.disposables.Disposable;
import zhan.rippleview.RippleView;

import static com.example.infits.R.*;

public class DeviceSettings extends AppCompatActivity {
    LinearLayout linear_layout;
   SwitchCompat switchCompat;
    SwitchCompat bluetoothSwitch;
    ImageView scan_for_device;
    RecyclerView bluetooth_list;

    RippleBackground rippleBackground;
    Disposable scanSubscription;
    RxBleClient rxBleClient;
    ArrayList<String> deviceName = new ArrayList<>();
    ArrayList<String> deviceAddress = new ArrayList<>();
  //  RippleView rippleView;      //
   // RipplePulseLayout mRipplePulseLayout;     //changes via suraj

    TextView number_of_devices, selected_title, selected_title2,selected_title3 ;
    Button connect_div,cancel_btn,try_again_btn,back_btn;
    GetMacInterface getMacInterface;


    ImageView imageView,bluetooth_img,line_bottom,cross_bluetooth,circle_blue_img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( layout.activity_device_settings );
        //suraj changes---
        rippleBackground = (RippleBackground) findViewById ( id.content );
        imageView = findViewById ( id.scan_for_device ); /// ---suraj changes
        //SkyfishRippleBackground skyfishRippleBackground = findViewById( id.skyfish_ripple_background);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Toast.makeText ( this , "yes" , Toast.LENGTH_LONG ).show ();
        } else {
            Toast.makeText ( this , "no" , Toast.LENGTH_LONG ).show ();
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter ();
        bluetoothSwitch = findViewById ( id.bluetoothSwitch );
        circle_blue_img=findViewById (id.centerImage); // suraj
        // scan_for_device = findViewById(R.id.scan_for_device);
        rippleBackground = findViewById ( id.content ); //suraj changes
        try_again_btn=findViewById ( id.try_again );  //suraj changes
        back_btn=findViewById ( id.Back_blue );     //suraj changes
        line_bottom = findViewById ( id.line_bottom );
        bluetooth_list = findViewById ( id.bluetooth_list );   //change here suraj
        rxBleClient = RxBleClient.create ( getApplicationContext () );
        number_of_devices = findViewById ( id.number_of_devices );
        selected_title = findViewById ( id.select_title );
        selected_title2 = findViewById ( id.select_title2 ); //suraj change
        connect_div = findViewById ( id.connect_div );
        selected_title3 = findViewById ( id.select_title3 ); //suraj change
        linear_layout = (LinearLayout) findViewById ( id.bluetooth_btn_layout ); //suraj change
        switchCompat = (SwitchCompat) findViewById ( id.bluetoothSwitch );   // suraj change
        bluetooth_img = (ImageView) findViewById ( id.img_bluetooth );
        cancel_btn = findViewById ( id.cancel_btn_bluetooth );   //Suraj Changes here
        bluetooth_list.setAdapter ( new DeviceListAdapter ( getApplicationContext () , deviceName , deviceAddress , getMacInterface ) );
        cross_bluetooth=findViewById ( id.cross_bluetooth_img );
//        rippleView  = findViewById(R.id.ripple);
//        rippleView.startRipple();
        // mRipplePulseLayout = findViewById(R.id.layout_ripplepulse);   //change here suraj
        if (mBluetoothAdapter.isEnabled ()) {
            Toast.makeText ( getApplication () , "Bluetooth is ON" , Toast.LENGTH_LONG ).show ();
            bluetoothSwitch.setChecked ( true );
        }
        bluetoothSwitch.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onCheckedChanged ( CompoundButton buttonView , boolean isChecked ) {
                if (isChecked) {
                    Intent enableBtIntent = new Intent ( BluetoothAdapter.ACTION_REQUEST_ENABLE );
                    Log.d ( "DS" , "checked" );
                    if (ActivityCompat.checkSelfPermission ( DeviceSettings.this , Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED) {
                        Log.d ( "DS" , "no permission" );
                        checkPermission ( Manifest.permission.BLUETOOTH_CONNECT , 1 );
                        return;
                    }
                    startActivityForResult ( enableBtIntent , 1 );
                } else {
                    mBluetoothAdapter.disable ();
                }
            }
        } );
        cancel_btn.setOnClickListener ( view -> {
            try_again_btn.setVisibility ( View.VISIBLE);
            back_btn.setVisibility ( View.VISIBLE );
            cancel_btn.setVisibility ( View.GONE );
            bluetooth_img.setVisibility (View.GONE );
            circle_blue_img.setVisibility (View.GONE );
            cross_bluetooth.setVisibility ( View.VISIBLE );
            selected_title2.setText("Device not detected");
            rippleBackground.stopRippleAnimation ();
                }
        );



            imageView.setOnClickListener ( v -> {
                connect_div.setVisibility ( View.GONE );
               // number_of_devices.setText ( "Searching for devices..." );
                back_btn.setVisibility ( View.GONE );
                try_again_btn.setVisibility ( View.GONE );
                cancel_btn.setVisibility ( View.VISIBLE );
                selected_title.setVisibility ( View.GONE );
                selected_title2.setText ( "Scanning...." );
                selected_title3.setVisibility ( View.GONE );
                linear_layout.setVisibility ( View.GONE );
                switchCompat.setVisibility ( View.GONE );
                selected_title2.setVisibility ( View.VISIBLE );
                bluetooth_img.setVisibility ( View.VISIBLE );
                circle_blue_img.setVisibility ( View.VISIBLE );
                deviceName.removeAll ( deviceName );
                deviceAddress.removeAll ( deviceAddress );
              //  number_of_devices.setVisibility ( View.GONE );
                cross_bluetooth.setVisibility ( View.GONE);
                bluetooth_list.setAdapter ( null );
                scanDiv ();
                imageView.setVisibility ( View.GONE );
//            mRipplePulseLayout.startRippleAnimation();

                rippleBackground.startRippleAnimation ();
            } );


            try_again_btn.setOnClickListener ( view -> {
                cancel_btn.setVisibility ( View.VISIBLE );
                try_again_btn.setVisibility ( View.GONE );
                back_btn.setVisibility ( View.GONE );
                selected_title2.setText("Scanning....");
                bluetooth_img.setVisibility ( View.VISIBLE );
                circle_blue_img.setVisibility ( View.VISIBLE );
                scanDiv ();
                rippleBackground.startRippleAnimation ();
                cross_bluetooth.setVisibility ( View.GONE);

            } );


            back_btn.setOnClickListener ( view -> {
                selected_title3.setVisibility ( View.VISIBLE );
                linear_layout.setVisibility ( View.VISIBLE);
                imageView.setVisibility ( View.VISIBLE );
                line_bottom.setVisibility (View.VISIBLE);
                selected_title.setVisibility ( View.VISIBLE);
                switchCompat.setVisibility ( View.VISIBLE);
                try_again_btn.setVisibility ( View.GONE );
                back_btn.setVisibility ( View.GONE );
                selected_title2.setVisibility ( View.GONE );
                cross_bluetooth.setVisibility ( View.GONE );



            } );

        getMacInterface = new GetMacInterface() {
            @Override
            public void getMac(String mac) {
                DataFromDatabase.macAddress = mac;
                System.out.println(mac);
            }
        };
        connect_div.setOnClickListener(v -> {
//            getMacInterface = new GetMacInterface() {
//                @Override
//                public void getMac(String mac) {
//                    DataFromDatabase.macAddress = mac;
//                    System.out.println(mac);
//                }
//            };
        });
    }



    void scanDiv() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scanSubscription.dispose();
               imageView.setVisibility(View.VISIBLE);
                bluetooth_img.setVisibility(View.GONE);
                selected_title2.setVisibility ( View.VISIBLE );
                cancel_btn.setVisibility (View.GONE );
             //   connect_div.setVisibility(View.VISIBLE);

                line_bottom.setVisibility ( View.GONE );

             //   number_of_devices.setVisibility ( View.VISIBLE);
              ///  selected_title2.setText("Device Not Found");
                if (deviceName.size() == 1) {
                    number_of_devices.setText("Found 1 device");
                    number_of_devices.setVisibility ( View.VISIBLE );
                    selected_title.setText("Select device and click connect button");
                    selected_title.setVisibility ( View.VISIBLE );

                } else if (deviceName.size() == 0) {
//                   rippleBackground.setVisibility (View.VISIBLE );
//                   rippleView.startRipple ();
                    circle_blue_img.setVisibility ( View.GONE );
                    imageView.setVisibility ( View.GONE );
                    selected_title2.setText("Device not detected");    // suraj change here
                    back_btn.setVisibility ( View.VISIBLE );          // suraj change here
                    cross_bluetooth.setVisibility ( View.VISIBLE );  // suraj change here
                    try_again_btn.setVisibility ( View.VISIBLE );   // suraj change here
                    rippleBackground.startRippleAnimation();
                    selected_title.setText("Click search for devices button to refresh");
                } else {
                    number_of_devices.setText(String.format("Found %d device", deviceAddress.size()));
                    selected_title.setText("Select device and click connect button");
                    selected_title.setVisibility ( View.VISIBLE );
                }
                bluetooth_list.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceName, deviceAddress, getMacInterface));
                bluetooth_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rippleBackground.stopRippleAnimation();

            }
        }, 10000);
        scanSubscription = rxBleClient.scanBleDevices(
                        new ScanSettings.Builder()
                                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                                .build()
                )
                .subscribe(
                        scanResult -> {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            if (scanResult.getBleDevice().getBluetoothDevice().getName() != null) {
                                deviceName.add(scanResult.getBleDevice().getBluetoothDevice().getName());
                                deviceAddress.add(scanResult.getBleDevice().getMacAddress());
                                Set<String> s = new HashSet<String>();

                                for (String name : deviceName) {
                                    if (!s.add(name)) {
                                        System.out.println(name + "is duplicated");
                                        deviceName.remove(scanResult.getBleDevice().getBluetoothDevice().getName());
                                        deviceAddress.remove(scanResult.getBleDevice().getMacAddress());
                                    }
                                }
                            }
                                },
                                throwable -> {
                                    // Handle an error here.
                                    System.out.println(throwable);
                                }
                        );
    }



    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(DeviceSettings.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DeviceSettings.this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, requestCode);
        }
        else {
            Toast.makeText(DeviceSettings.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

//package com.example.infits;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SwitchCompat;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
//import com.polidea.rxandroidble3.RxBleClient;
//import com.polidea.rxandroidble3.scan.ScanSettings;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//import io.reactivex.rxjava3.disposables.Disposable;
//import zhan.rippleview.RippleView;
//
//public class DeviceSettings extends AppCompatActivity {
//    SwitchCompat bluetoothSwitch;
//    ImageView scan_for_device;
//    RecyclerView bluetooth_list;
//    Disposable scanSubscription;
//    RxBleClient rxBleClient;
//    ArrayList<String> deviceName = new ArrayList<>();
//    ArrayList<String> deviceAddress = new ArrayList<>();
//    RippleView rippleView;
//    RipplePulseLayout mRipplePulseLayout;
//    TextView number_of_devices, selected_title;
//    Button connect_div;
//    GetMacInterface getMacInterface;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_settings);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
//            Toast.makeText(this, "yes", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "no", Toast.LENGTH_LONG).show();
//        }
//
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothSwitch = findViewById(R.id.bluetoothSwitch);
//        scan_for_device = findViewById(R.id.scan_for_device);
//        bluetooth_list = findViewById(R.id.bluetooth_list);
//        rxBleClient = RxBleClient.create(getApplicationContext());
//        number_of_devices = findViewById(R.id.number_of_devices);
//        selected_title = findViewById(R.id.select_title);
//        connect_div = findViewById(R.id.connect_div);
//        bluetooth_list.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceName, deviceAddress, getMacInterface));
////        rippleView  = findViewById(R.id.ripple);
////        rippleView.startRipple();
//        mRipplePulseLayout = findViewById(R.id.layout_ripplepulse);
//        if (mBluetoothAdapter.isEnabled()) {
//            Toast.makeText(getApplication(), "Bluetooth is ON", Toast.LENGTH_LONG).show();
//            bluetoothSwitch.setChecked(true);
//        }
//        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.S)
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    Log.d("DS", "checked");
//                    if (ActivityCompat.checkSelfPermission(DeviceSettings.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                        Log.d("DS", "no permission");
//                        checkPermission(Manifest.permission.BLUETOOTH_CONNECT, 1);
//                        return;
//                    }
//                    startActivityForResult(enableBtIntent, 1);
//                } else {
//                    mBluetoothAdapter.disable();
//                }
//            }
//        });
//        scan_for_device.setOnClickListener(v -> {
//            connect_div.setVisibility(View.GONE);
//            number_of_devices.setVisibility(View.VISIBLE);
//            number_of_devices.setText("Searching for devices...");
//            selected_title.setText("It is going to take only few seconds");
//            deviceName.removeAll(deviceName);
//            deviceAddress.removeAll(deviceAddress);
//            bluetooth_list.setAdapter(null);
//            scanDiv();
//            scan_for_device.setVisibility(View.GONE);
//            mRipplePulseLayout.startRippleAnimation();
//        });
//        getMacInterface = new GetMacInterface() {
//            @Override
//            public void getMac(String mac) {
//                DataFromDatabase.macAddress = mac;
//                System.out.println(mac);
//            }
//        };
//        connect_div.setOnClickListener(v -> {
////            getMacInterface = new GetMacInterface() {
////                @Override
////                public void getMac(String mac) {
////                    DataFromDatabase.macAddress = mac;
////                    System.out.println(mac);
////                }
////            };
//        });
//    }
//
//    void scanDiv() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scanSubscription.dispose();
//                scan_for_device.setVisibility(View.VISIBLE);
//                connect_div.setVisibility(View.VISIBLE);
//                if (deviceName.size() == 1) {
//                    number_of_devices.setText("Found 1 device");
//                    selected_title.setText("Select device and click connect button");
//                } else if (deviceName.size() == 0) {
//                    number_of_devices.setText("No device Found");
//                    selected_title.setText("Click search for devices button to refresh");
//                } else {
//                    number_of_devices.setText(String.format("Found %d device", deviceAddress.size()));
//                    selected_title.setText("Select device and click connect button");
//                }
//                bluetooth_list.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceName, deviceAddress, getMacInterface));
//                bluetooth_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                mRipplePulseLayout.stopRippleAnimation();
//            }
//        }, 10000);
//        scanSubscription = rxBleClient.scanBleDevices(
//                        new ScanSettings.Builder()
//                                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
//                                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
//                                .build()
//                )
//                .subscribe(
//                        scanResult -> {
//                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return;
//                            }
//                            if (scanResult.getBleDevice().getBluetoothDevice().getName() != null) {
//                                deviceName.add(scanResult.getBleDevice().getBluetoothDevice().getName());
//                                deviceAddress.add(scanResult.getBleDevice().getMacAddress());
//                                Set<String> s = new HashSet<String>();
//
//                                for (String name : deviceName) {
//                                    if (!s.add(name)) {
//                                        System.out.println(name + "is duplicated");
//                                        deviceName.remove(scanResult.getBleDevice().getBluetoothDevice().getName());
//                                        deviceAddress.remove(scanResult.getBleDevice().getMacAddress());
//                                    }
//                                }
//                            }
//                        },
//                        throwable -> {
//                            // Handle an error here.
//                            System.out.println(throwable);
//                        }
//                );
//    }
//
//    public void checkPermission(String permission, int requestCode) {
//        // Checking if permission is not granted
//        if (ContextCompat.checkSelfPermission(DeviceSettings.this, permission) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(DeviceSettings.this, new String[] { Manifest.permission.BLUETOOTH_CONNECT }, requestCode);
//        }
//        else {
//            Toast.makeText(DeviceSettings.this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1) {
//
//            // Checking whether user granted the permission or not.
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Showing the toast message
//                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}