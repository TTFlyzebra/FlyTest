package com.flyzebra.octopus.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

public class GpsTools {


    public final static String MSPIT = "GXPXS";
    public static List<String> list_provider = null;

    public static LocationListener StartListenerGps(final LocationManager locationManager){
        // Find Location point!
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		criteria.setAltitudeRequired(false);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_HIGH);
//		provider = locationManager.getBestProvider(criteria, true);
//		FlyLog.i("<GpsTools>"+provider);
//		locationManager.setTestProviderEnabled("gps", true);
        list_provider = locationManager.getProviders(true);
        LocationListener llistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                FlyLog.i("<GpsTools>onLocationChanged");
                if (location != null) {
                    FlyLog.i("<GpsTools>Current altitude = " + location.getAltitude());
                    FlyLog.i("<GpsTools>Current latitude = " + location.getLatitude());
                }
                locationManager.removeUpdates(this);
//				locationManager.setTestProviderEnabled(provider, false);
            }
            @Override
            public void onProviderDisabled(String provider) {
                FlyLog.i("<GpsTools>onProviderDisabled");
            }
            @Override
            public void onProviderEnabled(String provider) {
                FlyLog.i("<GpsTools>onProviderEnabled");
            }
            @Override
            public void onStatusChanged(String provider, int status,Bundle extras) {
                FlyLog.i("<GpsTools>onStatusChanged");
            }
        };
        for(String provider:list_provider){
            locationManager.requestLocationUpdates(provider, 1000, 1, llistener);
        }
        return llistener;
    }

    public static String GetMyGps(Context context) {
        StringBuffer mGps = new StringBuffer();
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (list_provider != null) {
            for (String provider : list_provider) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    mGps = mGps.append(location.getLongitude()).append(",")
                            .append(location.getLatitude()).append(",")
                            .append(provider).append(MSPIT);
                } else {
                    mGps = mGps.append("NoGetLocation!").append(provider).append(MSPIT);
                }
            }
        }
        return mGps.toString();
    }

    public static void StopListenerGps(LocationManager locationManager,
                                       LocationListener llistener) {
        locationManager.removeUpdates(llistener);

    }
}