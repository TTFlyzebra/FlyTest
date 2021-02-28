package com.octopus.test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.octopus.test.utils.MobileInfoUtil;

public class Sim4GActivity extends AppCompatActivity {
    private TextView textView;
    private StringBuffer text = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim4g);
        textView = findViewById(R.id.ac_sim4g_tv03);

        text.append("...............\n");

        text.append("IMEI:").append(MobileInfoUtil.getIMEI(this)).append("\n");
        text.append("IMSI:").append(MobileInfoUtil.getIMSI(this)).append("\n");

        textView.setText(text.toString());

    }
}