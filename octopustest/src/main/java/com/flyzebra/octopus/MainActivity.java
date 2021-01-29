package com.flyzebra.octopus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gpstest(View view) {
        startActivity(new Intent(this,GpsActivity.class));
    }

    public void camera1test(View view) {
        startActivity(new Intent(this,Camera1Activity.class));
    }

    public void camera2test(View view) {
        startActivity(new Intent(this,Camera2Activity.class));
    }
}