package com.octopus.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.octopus.test.utils.FlyLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("MissingPermission")
public class GpsActivity extends AppCompatActivity implements GpsStatus.Listener {

    private StringBuffer textInfo = new StringBuffer();
    private LocationManager locationManager;
    private boolean isGpsEnabled = false;
    private TextView tv_gps, tv_passive, tv_network, tv_unknow, tv02, tv03;
    public static List<String> list_provider = null;

    private List<LocationListener> listeners = new ArrayList<>();

    private class MyLocationListener implements LocationListener {

        private String provider;

        public MyLocationListener(String provider) {
            this.provider = provider;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            FlyLog.i("onLocationChanged, location=%s", location.toString());
            FlyLog.i("[" + provider + "]经度：" + location.getLongitude() + "，纬度：" + location.getLatitude());
            switch (provider){
                case LocationManager.GPS_PROVIDER:
                    tv_gps.setText("[" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                case LocationManager.NETWORK_PROVIDER:
                    tv_network.setText("[" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                case LocationManager.PASSIVE_PROVIDER:
                    tv_passive.setText("[" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                default:
                    tv_unknow.setText("[unknow]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            FlyLog.i("onStatusChanged, provider=%s,status=%d,extras=%s.", provider, status, extras.toString());
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            FlyLog.i("onProviderEnabled, provider=%s.", provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            FlyLog.i("onProviderDisabled, provider=%s.", provider);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        tv_gps = findViewById(R.id.ac_gps_tv_gps);
        tv_passive = findViewById(R.id.ac_gps_tv_passive);
        tv_network = findViewById(R.id.ac_gps_tv_network);
        tv_unknow = findViewById(R.id.ac_gps_tv_unknow);
        tv02 = findViewById(R.id.ac_gps_tv02);
        tv03 = findViewById(R.id.ac_gps_tv03);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, "delete_aiding_data", null);
        //判断是否开启GPS定位功能
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        textInfo.append(isGpsEnabled ? "GPS定位功能已开启!\n" : "GPS定位功能未开启!\n");
        list_provider = locationManager.getProviders(true);
        for (String provider : list_provider) {
            textInfo.append("find provider:" + provider + ".\n");
            FlyLog.i("find provider:" + provider + ".");
            LocationListener listener = new MyLocationListener(provider);
            listeners.add(listener);
            locationManager.requestLocationUpdates(provider, 1000, 0, listener);
        }
        locationManager.addGpsStatusListener(this);
        tv03.setText(textInfo.toString());
    }


    @Override
    protected void onDestroy() {
        for (LocationListener listener : listeners) {
            locationManager.removeUpdates(listener);
        }
        super.onDestroy();

    }

    @Override
    public void onGpsStatusChanged(int event) {
        FlyLog.i("onGpsStatusChanged, event=%d.", event);
        //textInfo.append("onGpsStatusChanged!["+event+"]\n");
        GpsStatus status = locationManager.getGpsStatus(null);
        String temp = "";
        switch (event) {
            /**
             * Event sent when the GPS system has started.
             */
            case GpsStatus.GPS_EVENT_STARTED:
                temp = "开始卫星定位！";
                FlyLog.d(temp);
                textInfo.append(temp + "\n");
                break;
            /**
             * Event sent when the GPS system has stopped.
             */
            case GpsStatus.GPS_EVENT_STOPPED:
                temp = "停止卫星定位！";
                FlyLog.d(temp);
                textInfo.append(temp + "\n");
                break;
            /**
             * Event sent when the GPS system has received its first fix since starting.
             * Call {@link #getTimeToFirstFix()} to find the time from start to first fix.
             */
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                temp = "第一次定位成功！";
                FlyLog.d(temp);
                textInfo.append(temp + "\n");
                break;
            /**
             * Event sent periodically to report GPS satellite status.
             * Call {@link #getSatellites()} to retrieve the status for each satellite.
             */
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
        }

        if (status == null) {
            temp = "没有捕捉到卫星!";
            FlyLog.d(temp);
            textInfo.append(temp + "\n");
        } else {
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            int count1 = 0;//记录搜索到的实际卫星数
            int count2 = 0;//有效卫星数
            for (GpsSatellite s : status.getSatellites()) {
                count1++;
                if (s.usedInFix()) {
                    count2++;
                }
            }
            FlyLog.d("卫星总数：" + count1 + "，有效卫星：" + count2 + "。");
            tv02.setText("卫星总数：" + count1 + "\n有效卫星：" + count2 + "。");
        }
        tv03.setText(textInfo.toString());
    }
}
