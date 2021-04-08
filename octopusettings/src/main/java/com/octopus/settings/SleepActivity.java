package com.octopus.settings;

import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SleepActivity extends AppCompatActivity {
    private TextView textView;
    private StringBuffer text = new StringBuffer();
    private TelephonyManager tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim4g);
        textView = findViewById(R.id.ac_sim4g_tv03);

        long uptime = SystemClock.elapsedRealtime()/1000;
        long runTime = SystemClock.uptimeMillis()/1000;
        long sleepTime = uptime - runTime;

        text.append("开机时间：").append(uptime/86400+"天"+uptime%86400/3600+"时"+uptime%3600/60+"分"+uptime%60+"秒").append("\n");
        text.append("运行时间：").append(runTime/86400+"天"+runTime%86400/3600+"时"+runTime%3600/60+"分"+runTime%60+"秒").append("\n");
        text.append("休眠时间：").append(sleepTime/86400+"天"+sleepTime%86400/3600+"时"+sleepTime%3600/60+"分"+sleepTime%60+"秒").append("\n");

        textView.setText(text.toString());
    }

}