package com.example.infits;

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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.polidea.rxandroidble3.RxBleClient;
import com.polidea.rxandroidble3.scan.ScanSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.rxjava3.disposables.Disposable;
import zhan.rippleview.RippleView;

public class DeviceSettings extends AppCompatActivity {
    private SwitchCompat bluetoothSwitch;
    private ImageView scanForDevice;
    private RecyclerView bluetoothList;
    private Disposable scanSubscription;
    private RxBleClient rxBleClient;
    private ArrayList<String> deviceNames = new ArrayList<>();
    private ArrayList<String> deviceAddresses = new ArrayList<>();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Toast.makeText(this, "yes", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "no", Toast.LENGTH_LONG).show();
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothSwitch = findViewById(R.id.bluetoothSwitch);
        scanForDevice = findViewById(R.id.scan_for_device);
        bluetoothList = findViewById(R.id.bluetooth_list);
        rxBleClient = RxBleClient.create(getApplicationContext());
        numberOfDevices = findViewById(R.id.number_of_devices);
        selectedTitle = findViewById(R.id.select_title);
        connectButton = findViewById(R.id.connect_div);
        bluetoothList.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceNames, deviceAddresses, getMacInterface));
        mRipplePulseLayout = findViewById(R.id.layout_ripplepulse);

        if (mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is ON", Toast.LENGTH_LONG).show();
            bluetoothSwitch.setChecked(true);
        }

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    Log.d("DS", "checked");
                    if (ActivityCompat.checkSelfPermission(DeviceSettings.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("DS", "no permission");
                        checkPermission(Manifest.permission.BLUETOOTH_CONNECT, 1);
                        return;
                    }
                    startActivityForResult(enableBtIntent, 1);
                } else {
                    mBluetoothAdapter.disable();
                }
            }
        });

        scanForDevice.setOnClickListener(v -> {
            connectButton.setVisibility(View.GONE);
            numberOfDevices.setVisibility(View.VISIBLE);
            numberOfDevices.setText("Searching for devices...");
            selectedTitle.setText("It is going to take only a few seconds");
            deviceNames.clear();
            deviceAddresses.clear();
            bluetoothList.setAdapter(null);
            scanDiv();
            scanForDevice.setVisibility(View.GONE);
            mRipplePulseLayout.startRippleAnimation();
        });

        getMacInterface = new GetMacInterface() {
            @Override
            public void getMac(String mac) {
                DataFromDatabase.macAddress = mac;
                System.out.println(mac);
            }
        };

        connectButton.setOnClickListener(v -> {
            // Connect button click handling
        });
    }

    private void scanDiv() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scanSubscription != null) {
                    scanSubscription.dispose();
                }
                scanForDevice.setVisibility(View.VISIBLE);
                connectButton.setVisibility(View.VISIBLE);
                int deviceCount = deviceAddresses.size();
                if (deviceCount == 1) {
                    numberOfDevices.setText("Found 1 device");
                    selectedTitle.setText("Select a device and click the Connect button");
                } else if (deviceCount == 0) {
                    numberOfDevices.setText("No devices found");
                    selectedTitle.setText("Click the Scan for Devices button to refresh");
                } else {
                    numberOfDevices.setText(String.format("Found %d devices", deviceCount));
                    selectedTitle.setText("Select a device and click the Connect button");
                }
                bluetoothList.setAdapter(new DeviceListAdapter(getApplicationContext(), deviceNames, deviceAddresses, getMacInterface));
                bluetoothList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mRipplePulseLayout.stopRippleAnimation();
            }
        }, 10000);

        scanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
        ).subscribe(
                scanResult -> {
                    if (ActivityCompat.checkSelfPermission(DeviceSettings.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (scanResult.getBleDevice().getBluetoothDevice().getName() != null) {
                        String name = scanResult.getBleDevice().getBluetoothDevice().getName();
                        String address = scanResult.getBleDevice().getMacAddress();
                        if (!deviceNames.contains(name)) {
                            deviceNames.add(name);
                            deviceAddresses.add(address);
                        }
                    }
                },
                throwable -> {
                    Log.e("Scan Error", throwable.toString());
                }
        );
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(DeviceSettings.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DeviceSettings.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, requestCode);
        } else {
            Toast.makeText(DeviceSettings.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeviceSettings.this, "Bluetooth Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
