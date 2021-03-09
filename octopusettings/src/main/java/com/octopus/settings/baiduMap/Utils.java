package com.octopus.settings.baiduMap;

import android.os.Bundle;
import android.os.SystemClock;

import com.baidu.mapapi.model.LatLng;

public class Utils {
    public static boolean isLatlngEqual(LatLng latLng0, LatLng latLng1) {
        if (latLng0.latitude == latLng1.latitude
                && latLng0.longitude == latLng1.longitude) {
            return true;
        }

        return false;
    }

    public static Bundle getGpsInfo(double lon, double lat) {
        Bundle bundle = new Bundle();

        bundle.putBoolean("isGpsSimulate", true);
        bundle.putFloat("accuracy", (float) 1.0f);
        bundle.putDouble("altitude", 10.0d);
        bundle.putFloat("bearing", 5.0f);
        bundle.putFloat("speed", 2.0f);
        bundle.putDouble("longitude", lon);
        bundle.putDouble("latitude", lat);
        bundle.putLong("elapsedRealtimeNanos", SystemClock.elapsedRealtimeNanos());
        bundle.putLong("time", System.currentTimeMillis());

        return bundle;
    }
}
