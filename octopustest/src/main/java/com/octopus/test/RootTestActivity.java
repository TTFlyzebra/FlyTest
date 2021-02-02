package com.octopus.test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.octopus.test.utils.RootUtils;

public class RootTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);

        StringBuffer sb = new StringBuffer();
        sb.append("M1--").append(RootUtils.checkRootM1()).append("\n");
        sb.append("M2--").append(RootUtils.checkRootM2()).append("\n");
        sb.append("M3--").append(RootUtils.checkRootM3()).append("\n");
        sb.append("M4--").append(RootUtils.checkRootM4()).append("\n");
        sb.append("M5--").append(RootUtils.checkRootM5()).append("\n");
        sb.append("M6--").append(RootUtils.checkRootM6()).append("\n");
        sb.append("M7--").append(RootUtils.checkRootM7()).append("\n");
        sb.append("M8--").append(RootUtils.checkRootM8()).append("\n");

        textView.setText(sb.toString());
    }
}
