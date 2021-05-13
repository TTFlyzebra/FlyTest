package com.octopus.settings;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.octopus.settings.baiduMap.SelectMapActivity;
import com.octopus.settings.utils.FlyLog;

public class MainActivity extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };
    private static int REQUEST_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (String s : PERMISSIONS_STORAGE) {
                if (ActivityCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            FlyLog.d("onRequestPermissionsResult");
        }
    }

    public void roottest(View view) {
        startActivity(new Intent(this, RootTestActivity.class));
    }

    public void gpstest(View view) {
        startActivity(new Intent(this, GpsActivity.class));
    }

    public void camera1test(View view) {
        startActivity(new Intent(this, Camera1Activity.class));
    }

    public void camera2test(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        ComponentName cn = new ComponentName("com.flyzebra.player", "com.flyzebra.player.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cn);
        startActivity(intent);
    }

    public void networktest(View view) {
        startActivity(new Intent(this, NetworkActivity.class));
    }

    public void baidumap(View view) {
        startActivity(new Intent(this, SelectMapActivity.class));
    }

    public void sim4gtest(View view) {
        startActivity(new Intent(this, Sim4GActivity.class));
    }

    public void sensortest(View view) {
        startActivity(new Intent(this, SensorActivity.class));
    }

    public void sleepTest(View view) {
        startActivity(new Intent(this, SleepActivity.class));
    }

    public void commandtest(View view) {startActivity(new Intent(this, CommandActivity.class));}

    public void updater(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cn = new ComponentName("com.flyzebra.fota", "com.flyzebra.fota.MainActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cn);
            startActivity(intent);
        }catch (Exception e){
            FlyLog.e(e.toString());
        }
    }
}