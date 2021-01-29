package com.flyzebra.octopus;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GpsActivity extends AppCompatActivity {

    private static final String GPS_LOCATION_NAME = android.location.LocationManager.GPS_PROVIDER;
    private StringBuffer textInfo = new StringBuffer();
    private LocationManager locationManager;
    private boolean isGpsEnabled = false;
    private String locateType;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        textView = findViewById(R.id.ac_gps_tv01);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //判断是否开启GPS定位功能
        isGpsEnabled = locationManager.isProviderEnabled(GPS_LOCATION_NAME);
        textInfo.append(isGpsEnabled?"GPS定位功能已开启!\n":"GPS定位功能未开启!\n");
        //定位类型：GPS
        locateType = locationManager.GPS_PROVIDER;
        //初始化PermissionHelper
        textView.setText(textInfo.toString());
    }

}
