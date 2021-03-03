package com.octopus.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.octopus.test.utils.MobileInfoUtil;

import java.util.List;

@SuppressLint("MissingPermission")
public class Sim4GActivity extends AppCompatActivity {
    private TextView textView;
    private StringBuffer text = new StringBuffer();
    private TelephonyManager tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim4g);
        textView = findViewById(R.id.ac_sim4g_tv03);

        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        text.append("IMEI:").append(MobileInfoUtil.getIMEI(this)).append("\n");
        text.append("IMSI:").append(MobileInfoUtil.getIMSI(this)).append("\n");

        CellLocation cel = tel.getCellLocation();
        int nPhoneType = tel.getPhoneType();
        if (nPhoneType == 2 && cel instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
            text.append("GsmCell:").append(gsmCellLocation.toString()).append("\n");
            text.append("Cid:").append(gsmCellLocation.getCid()).append("\n");
            text.append("Lac:").append(gsmCellLocation.getLac()).append("\n");
            text.append("Psc:").append(gsmCellLocation.getPsc()).append("\n");
        }else if (nPhoneType == 2 && cel instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
            text.append("CdmaCell:").append(cdmaCellLocation.toString()).append("\n");
            text.append("NetworkId:").append(cdmaCellLocation.getNetworkId()).append("\n");
            text.append("SystemId:").append(cdmaCellLocation.getSystemId()).append("\n");
            text.append("BaseStationId:").append(cdmaCellLocation.getBaseStationId()).append("\n");
            text.append("BaseStationLatitude:").append(cdmaCellLocation.getBaseStationLatitude()).append("\n");
            text.append("BaseStationLongitude:").append(cdmaCellLocation.getBaseStationLongitude()).append("\n");
        }else {
            text.append("No find CellLocation!").append("\n");
        }
        //获取所有基站信息
        List<CellInfo> cellInfos = tel.getAllCellInfo();
        if(cellInfos!=null){
            text.append("cell number:").append(cellInfos.size()).append("\n");
        }
        text.append("getAllCellInfo:").append(""+cellInfos).append("\n");

        textView.setText(text.toString());
    }
}