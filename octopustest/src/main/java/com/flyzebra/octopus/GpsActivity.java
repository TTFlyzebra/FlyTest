package com.flyzebra.octopus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flyzebra.octopus.utils.FlyLog;

import java.util.List;

public class GpsActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener {

    private static final String GPS_LOCATION_NAME = android.location.LocationManager.GPS_PROVIDER;
    private StringBuffer textInfo = new StringBuffer();
    private LocationManager locationManager;
    private boolean isGpsEnabled = false;
    private String locateType;
    private TextView textView;
    public static List<String> list_provider = null;

    @SuppressLint("MissingPermission")
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

        list_provider = locationManager.getProviders(true);

        for(String provider:list_provider){
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
        locationManager.addGpsStatusListener(this );
    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        FlyLog.i("onLocationChanged, location=%s",location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        FlyLog.i("onStatusChanged, provider=%s,status=%d,extras=%s.",provider,status,extras.toString());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        FlyLog.i("onProviderEnabled, provider=%s.",provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        FlyLog.i("onProviderDisabled, provider=%s.",provider);
    }

    @Override
    public void onGpsStatusChanged(int event) {
        FlyLog.i("onGpsStatusChanged, event=%d.",event);
    }
}
