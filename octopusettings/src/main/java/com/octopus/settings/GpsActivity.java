package com.octopus.settings;

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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.octopus.settings.baiduMap.GpsTools;
import com.octopus.settings.utils.DateUtils;
import com.octopus.settings.utils.FlyLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("MissingPermission")
public class GpsActivity extends AppCompatActivity implements GpsStatus.Listener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;

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
            switch (provider) {
                case LocationManager.GPS_PROVIDER:
                    tv_gps.setText("[" + DateUtils.getCurrentDate("HH:mm:ss") + "][" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                case LocationManager.NETWORK_PROVIDER:
                    tv_network.setText("[" + DateUtils.getCurrentDate("HH:mm:ss") + "][" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                case LocationManager.PASSIVE_PROVIDER:
                    tv_passive.setText("[" + DateUtils.getCurrentDate("HH:mm:ss") + "][" + provider + "]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
                default:
                    tv_unknow.setText("[" + DateUtils.getCurrentDate("HH:mm:ss") + "][unknow]\n经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude());
                    break;
            }

            goBaiduLocation(location);
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
        mMapView = findViewById(R.id.ac_baidumap);
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();

        tv_gps = findViewById(R.id.ac_gps_tv_gps);
        tv_passive = findViewById(R.id.ac_gps_tv_passive);
        tv_network = findViewById(R.id.ac_gps_tv_network);
        tv_unknow = findViewById(R.id.ac_gps_tv_unknow);
        tv02 = findViewById(R.id.ac_gps_tv02);
        tv03 = findViewById(R.id.ac_gps_tv03);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onStop() {
        mBaiduMap.setMyLocationEnabled(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (null != mMapView) {
            mMapView.onDestroy();
        }
        if (mBaiduMap != null) {
            mBaiduMap.animateMapStatus(null);
        }
        locationManager.removeGpsStatusListener(this);
        for (LocationListener listener : listeners) {
            locationManager.removeUpdates(listener);
        }
        listeners.clear();
        super.onDestroy();
    }

    public void goBaiduLocation(Location location) {
        if (location != null) {
            double loc[] = GpsTools.WGS84ToGCJ02(location.getLongitude(), location.getLatitude());
            LatLng ll = new LatLng(loc[1], loc[0]);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
            mBaiduMap.animateMapStatus(u);
            MyLocationData myLocationData = new MyLocationData.Builder().accuracy(location.getAccuracy())
                    .direction(location.getBearing())
                    .latitude(loc[1])
                    .longitude(loc[0])
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
        }
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
                //FlyLog.d("Almanac=%b, Ephemeris=%b, Azimuth=%f, Elevation=%f, Snr=%f, Prn=%s",s.hasAlmanac(),s.hasEphemeris(), s.getAzimuth(),s.getElevation(),s.getSnr(),s.getPrn());
            }
            FlyLog.d("卫星总数：" + count1 + "，有效卫星：" + count2 + "。");
            tv02.setText("卫星总数：" + count1 + "，有效卫星：" + count2 + "。");
        }
        tv03.setText(textInfo.toString());
    }
}
