package com.example.infits;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.scan.ScanSettings;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.Disposable;
import zhan.rippleview.RippleView;

public class  DeviceSettings extends AppCompatActivity
{
    private final ArrayList<String> deviceNames = new ArrayList<>();
    private final ArrayList<String> deviceAddresses = new ArrayList<>();
    String[] permissions12 = {"android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"};
    String[] permissions11 = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    BluetoothAdapter mBluetoothAdapter;
    private ImageView scanForDevice;
    private RecyclerView bluetoothList;
    private Disposable scanSubscription;
    private RxBleClient rxBleClient;
    private RippleView rippleView;
    private RipplePulseLayout mRipplePulseLayout;
    private TextView numberOfDevices;
    private TextView selectedTitle;
    private Button connectButton;
    private GetMacInterface getMacInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        ImageView backBtnDevice = findViewById(R.id.backBtn_DeviceManagement);

        if (Build.VERSION.SDK_INT >= 31) {
            requestPermissions(permissions12, 12);
        } else {
            requestPermissions(permissions11, 11);
            statusCheck();
        }


        if (Build.VERSION.SDK_INT >= 31) {
            BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        SwitchCompat bluetoothSwitch = findViewById(R.id.bluetoothSwitch);
        scanForDevice = findViewById(R.id.scan_for_device);
        bluetoothList = findViewById(R.id.bluetooth_list);
        rxBleClient = RxBleClient.create(getApplicationContext());
        numberOfDevices = findViewById(R.id.number_of_devices);
        selectedTitle = findViewById(R.id.select_title);
        connectButton = findViewById(R.id.connect_div);
        bluetoothList.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceNames, deviceAddresses, getMacInterface));
        mRipplePulseLayout = findViewById(R.id.layout_ripplepulse);

        if (mBluetoothAdapter.isEnabled()) {
            bluetoothSwitch.setChecked(true);
        }

        bluetoothSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(DeviceSettings.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("DeviceSettingsCheck", "no permission");
                        requestPermissions(permissions12, 12);
                    } else {
                        Log.d("DeviceSettingsCheck", "startActivityForResult call 12");
                        startActivityForResult(enableBtIntent, 12);
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(DeviceSettings.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        statusCheck();
                        Log.d("DeviceSettingsCheck", "no permission");
                    } else {
                        Log.d("DeviceSettingsCheck", "startActivityForResult 11 enable intent call");
                        startActivityForResult(enableBtIntent, 11);
                    }

                }

            } else {
                mBluetoothAdapter.disable();
            }
        });

        scanForDevice.setOnClickListener(v -> {
            connectButton.setVisibility(View.GONE);
            numberOfDevices.setVisibility(View.VISIBLE);
            numberOfDevices.setText(R.string.searching_for_devices);
            selectedTitle.setText(R.string.it_is_going_to_take_only_a_few_seconds);
            deviceNames.clear();
            deviceAddresses.clear();
            bluetoothList.setAdapter(null);
            scanDiv();
            scanForDevice.setVisibility(View.GONE);
            mRipplePulseLayout.startRippleAnimation();
        });

        getMacInterface = mac -> {
            DataFromDatabase.macAddress = mac;
            System.out.println(mac);
        };

        connectButton.setOnClickListener(v -> {
            // Connect button click handling
        });
//        backBtnDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    private void scanDiv() {
        new Handler().postDelayed(() -> {
            Log.d("DeviceSettingsCheck", "scanDivCall");
            if (scanSubscription != null) {
                Log.d("DeviceSettingsCheck", "scanSubscription.dispose() call");
                scanSubscription.dispose();
            }
            scanForDevice.setVisibility(View.VISIBLE);
            connectButton.setVisibility(View.VISIBLE);
            int deviceCount = deviceAddresses.size();
            if (deviceCount == 1) {
                numberOfDevices.setText(R.string.found_1_device);
                selectedTitle.setText(R.string.select_a_device_and_click_the_connect_button);
            } else if (deviceCount == 0) {
                numberOfDevices.setText(R.string.no_devices_found);
                selectedTitle.setText(R.string.click_the_scan_for_devices_button_to_refresh);
            } else {
                numberOfDevices.setText(String.format(getString(R.string.found_d_devices), deviceCount));
                selectedTitle.setText(R.string.select_a_device_and_click_the_connect_button);
            }
            bluetoothList.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceNames, deviceAddresses, getMacInterface));
            bluetoothList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRipplePulseLayout.stopRippleAnimation();
        }, 10000);

        scanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
        ).subscribe(
                scanResult -> {
                    if (Build.VERSION.SDK_INT >= 31) {

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
                            String name = scanResult.getBleDevice().getBluetoothDevice().getName();
                            String address = scanResult.getBleDevice().getMacAddress();
                            Log.e("DeviceSettingsCheck", "12 else call" + name);
                            if (!deviceNames.contains(name)) {
                                deviceNames.add(name);
                                deviceAddresses.add(address);
                            }
                        } else {
                            Log.e("DeviceSettingsCheck", scanResult.getBleDevice().toString());
                        }

                    } else {
                        if (scanResult.getBleDevice().getBluetoothDevice().getName() != null) {
                            String name = scanResult.getBleDevice().getBluetoothDevice().getName();
                            String address = scanResult.getBleDevice().getMacAddress();
                            Log.e("DeviceSettingsCheck", "Scan Result for 11 " + name);
                            Log.e("DeviceSettingsCheck", scanResult.getBleDevice().toString());
                            if (!deviceNames.contains(name)) {
                                deviceNames.add(name);
                                deviceAddresses.add(address);
                            }
                        } else {
                            Log.e("DeviceSettingsCheck", scanResult.getBleDevice().toString());
                        }
                    }


                },
                throwable -> Log.e("DeviceSettingsCheck", throwable.toString())
        );
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DeviceSettings.this, "Location 11 Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeviceSettings.this, "Location 11 Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DeviceSettings.this, "Bluetooth 12 Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeviceSettings.this, "Bluetooth 12 Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
