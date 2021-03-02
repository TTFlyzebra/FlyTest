package com.octopus.test;

import android.annotation.SuppressLint;
import android.octopu.OctopuManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements OctopuManager.SensorListener {
    private TextView textView;
    private StringBuffer text = new StringBuffer();
    private OctopuManager mOctopuManager;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        textView = findViewById(R.id.ac_sensor_tv01);
        mOctopuManager = (OctopuManager) getSystemService("octopu");

        mOctopuManager.addSensorListener(this);
        Bundle bundle = new Bundle();
        bundle.putInt(OctopuManager.SENSOR_TYPE, 1);
        bundle.putFloat(OctopuManager.SENSOR_X, 1.01f);
        bundle.putFloat(OctopuManager.SENSOR_Y, 1.02f);
        bundle.putFloat(OctopuManager.SENSOR_Z, 1.03f);
        bundle.putLong(OctopuManager.SENSOR_TIME, SystemClock.elapsedRealtime());
        mOctopuManager.upSensorData(bundle);
    }


    @Override
    protected void onDestroy() {
        mOctopuManager.removeSensorListener(this);
        super.onDestroy();
    }

    @Override
    public void notifySensorChange(Bundle bundle) {
        int type = bundle.getInt(OctopuManager.SENSOR_TYPE);
        float x = bundle.getFloat(OctopuManager.SENSOR_X);
        float y = bundle.getFloat(OctopuManager.SENSOR_Y);
        float z = bundle.getFloat(OctopuManager.SENSOR_Z);
        float time = bundle.getFloat(OctopuManager.SENSOR_TIME);
        String s = String.format("type=%d, x=%f, y=%f, z=%f, time=%d\n", type, x, y, z, time);
        textView.setText(s);
    }
}