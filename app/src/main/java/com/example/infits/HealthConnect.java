package com.example.infits;

import static com.google.android.gms.common.util.CollectionUtils.setOf;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.PermissionController;
import androidx.health.connect.client.permission.HealthPermission;
import androidx.health.connect.client.records.HeartRateRecord;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Set;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class HealthConnect extends AppCompatActivity {

    HealthConnectClient healthConnectClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_connect);

        healthConnect();
    }

    private void healthConnect() {
        if (HealthConnectClient.isProviderAvailable(this)) {
            // Health Connect is available and installed.
            healthConnectClient = HealthConnectClient.getOrCreate(this);
            checkPermissionsAndRun();
        } else {
            Toast.makeText(this, "Health Connect App not installed", Toast.LENGTH_LONG).show();
        }
    }

    private void readHeartRate(HealthConnectClient healthConnectClient) {
//        LocalDateTime end = LocalDateTime.now();
//        LocalDateTime start = end.minusYears(1);
//        KClass<HeartRateRecord> HeartClass = kotlin.jvm.JvmClassMappingKt.getKotlinClass(HeartRateRecord.class);

        new Thread(()-> new HealthConnectKt().readStepsByTimeRange(healthConnectClient, new Continuation<Object>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                System.out.println("resumeWith()");
            }
        })).start();
    }

    Set<HealthPermission> PERMISSIONS = setOf(
            HealthPermission.createReadPermission(kotlin.jvm.JvmClassMappingKt.getKotlinClass(HeartRateRecord.class))
    );

    ActivityResultContract<Set<HealthPermission>, Set<HealthPermission>> requestPermissionActivityContract =
            PermissionController.createRequestPermissionResultContract();

    ActivityResultLauncher<Set<HealthPermission>> requestPermissions = registerForActivityResult(
            requestPermissionActivityContract,
            result -> {
                if (result.containsAll(PERMISSIONS)) {
                    Toast.makeText(this, "permissions granted", Toast.LENGTH_LONG).show();
                    readHeartRate(healthConnectClient);
                } else {
                    Toast.makeText(this, "permissions denied", Toast.LENGTH_LONG).show();
                }
            }
    );

    private void checkPermissionsAndRun() {
        requestPermissions.launch(PERMISSIONS);

//        @SuppressWarnings("unchecked")
////        Set<HealthPermission> granted = (Set<HealthPermission>) healthConnectClient.getPermissionController().getGrantedPermissions(PERMISSIONS, null);
//        Object granted = healthConnectClient.getPermissionController().getGrantedPermissions(PERMISSIONS, new Continuation<Set<HealthPermission>>() {
//            @NonNull
//            @Override
//            public CoroutineContext getContext() {
//                return EmptyCoroutineContext.INSTANCE;
//            }
//
//            @Override
//            public void resumeWith(@NonNull Object o) {
//                System.out.println("resumeWith()");
//            }
//        });
//        System.out.println("granted " + granted.getClass());
//        if(granted instanceof Set) {
//            System.out.println("instanceOf");
//            assert granted != null;
//            if (((Set<HealthPermission>) granted).containsAll(PERMISSIONS)) {
//                // Permissions already granted, proceed with inserting or reading data.
//                readHeartRate(healthConnectClient);
//            } else {
//                requestPermissions.launch(PERMISSIONS);
//            }
//        }
    }
}