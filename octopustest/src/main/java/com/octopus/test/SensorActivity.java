package com.octopus.test;

import android.annotation.SuppressLint;
import android.octopu.OctopuManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements OctopuManager.SenserListener {
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
        bundle.putInt("TYPE", 1);
        bundle.putFloat("X", 1.01f);
        bundle.putFloat("Y", 1.02f);
        bundle.putFloat("Z", 1.03f);
        bundle.putLong("TIME", SystemClock.elapsedRealtime());
        mOctopuManager.upSensorData(bundle);
    }


    @Override
    protected void onDestroy() {
        mOctopuManager.removeSensorListener(this);
        super.onDestroy();
    }

    @Override
    public void notifySensorChange(Bundle bundle) {
        int type = bundle.getInt("TYPE");
        float x = bundle.getFloat("X");
        float y = bundle.getFloat("X");
        float z = bundle.getFloat("X");
        float time = bundle.getFloat("TIME");
        String s = String.format("type=%d, x=%f, y=%f, z=%f, time=%d\n", type, x, y, z, time);
        textView.setText(s);
    }
}