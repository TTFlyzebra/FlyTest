package com.flyzebra.octopus;

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

import com.flyzebra.octopus.utils.FlyLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("MissingPermission")
public class GpsActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener {

    private static final String GPS_LOCATION_NAME = android.location.LocationManager.GPS_PROVIDER;
    private StringBuffer textInfo = new StringBuffer();
    private LocationManager locationManager;
    private boolean isGpsEnabled = false;
    private TextView textView;
    public static List<String> list_provider = null;

    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        textView = findViewById(R.id.ac_gps_tv01);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //判断是否开启GPS定位功能
        isGpsEnabled = locationManager.isProviderEnabled(GPS_LOCATION_NAME);
        textInfo.append(isGpsEnabled?"GPS定位功能已开启!\n":"GPS定位功能未开启!\n");
        list_provider = locationManager.getProviders(true);
        for(String provider:list_provider){
            textInfo.append("find provider:"+provider+"\n");
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
        locationManager.addGpsStatusListener(this );
        textView.setText(textInfo.toString());
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
        GpsStatus status = locationManager.getGpsStatus(null);
        switch (event){
            /**
             * Event sent when the GPS system has started.
             */
            case GpsStatus.GPS_EVENT_STARTED :
                textInfo.append("开始卫星定位！\n");
                break;
            /**
             * Event sent when the GPS system has stopped.
             */
            case GpsStatus.GPS_EVENT_STOPPED:
                textInfo.append("停止卫星定位！\n");
                break;
            /**
             * Event sent when the GPS system has received its first fix since starting.
             * Call {@link #getTimeToFirstFix()} to find the time from start to first fix.
             */
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                textInfo.append("第一次定位成功！\n");
                break;
            /**
             * Event sent periodically to report GPS satellite status.
             * Call {@link #getSatellites()} to retrieve the status for each satellite.
             */
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
        }

        if(status==null) {
            textInfo.append("没有捕捉到卫星!\n");
        }else {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;//记录搜索到的实际卫星数
            while (it.hasNext() && count < maxSatellites) {
                GpsSatellite s = it.next();
                numSatelliteList.add(s);//将卫星信息存入队列
                count++;
            }
            textInfo.append("获得卫星总数:" + numSatelliteList.size() + "\n");
        }
        textView.setText(textInfo.toString());
    }
}
