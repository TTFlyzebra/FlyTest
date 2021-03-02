package com.octopus.test.utils; /**
 * FileName: MobileInfoUtil
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 2021/2/28 19:37
 * Description:
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

/**
 * 获取手机信息工具类
 *
 * @author HLQ
 * @createtime 2016-12-7下午2:06:03
 * @remarks
 */
public class MobileInfoUtil {

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机IMSI
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMSI号
            String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void getCellInfo(Context context) {
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        CellLocation cel = tel.getCellLocation();
        int nPhoneType = tel.getPhoneType();
        if (nPhoneType == 2 && cel instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
            int nGSMCID = gsmCellLocation.getCid();
            if (nGSMCID > 0) {
                if (nGSMCID != 65535) {
                }
            }
        }else if (nPhoneType == 2 && cel instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cel;
        }
    }

}