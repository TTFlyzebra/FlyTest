package com.octopus.settings;

import android.annotation.SuppressLint;
import android.octopu.OctopuManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.octopus.settings.utils.FlyLog;

import java.util.ArrayList;

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
        try {
            mOctopuManager = (OctopuManager) getSystemService("octopu");

            mOctopuManager.addSensorListener(this);
            Bundle bundle = new Bundle();
            bundle.putInt(OctopuManager.SENSOR_TYPE, 10);
            bundle.putFloat(OctopuManager.SENSOR_X, 1.01f);
            bundle.putFloat(OctopuManager.SENSOR_Y, 1.02f);
            bundle.putFloat(OctopuManager.SENSOR_Z, 1.03f);
            bundle.getInt(OctopuManager.SENSOR_TYPE);
            bundle.putLong(OctopuManager.SENSOR_TIME, SystemClock.elapsedRealtime());
            mOctopuManager.upSensorData(bundle);
            Bundle ret = mOctopuManager.getSensorData();
            int type = ret.getInt(OctopuManager.SENSOR_TYPE);
            float x = ret.getFloat(OctopuManager.SENSOR_X);
            float y = ret.getFloat(OctopuManager.SENSOR_Y);
            float z = ret.getFloat(OctopuManager.SENSOR_Z);
            long time = ret.getLong(OctopuManager.SENSOR_TIME);
            String s = String.format("Get:\ntype=%d, x=%f, y=%f, z=%f, time=%d\n", type, x, y, z, time);
            text.append(s).append("\n");


            Bundle bundleCell = new Bundle();
            bundleCell.putParcelableArrayList(OctopuManager.CELL_LIST,new ArrayList<>());
            mOctopuManager.upCellData(bundleCell);
            Bundle ret1 = mOctopuManager.getCellData();
            text.append("Cell:\n").append(ret1.toString()).append("\n\n");

            textView.setText(text.toString());
        }catch (Exception e){
            FlyLog.e(e.toString());
        }
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
        long time = bundle.getLong(OctopuManager.SENSOR_TIME);
        String s = String.format("Listener:\ntype=%d, x=%f, y=%f, z=%f, time=%d\n", type, x, y, z, time);
        text.append(s).append("\n");
        textView.setText(text.toString());
    }
}