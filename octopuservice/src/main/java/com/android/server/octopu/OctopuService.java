package com.android.server.octopu;

import android.content.Context;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuListener;
import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;


/**
 * @hide ClassName: OctopuService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:42
 */
public class OctopuService extends IOctopuService.Stub {
    private Context mContext;
    private static RemoteCallbackList<OctopuListener> mOctopuListeners = new RemoteCallbackList<>();
    private final Object mLock = new Object();

    private Bundle sensorBundle;
    private Bundle gpsBundle;
    private Bundle cellBundle;
    private Bundle wifiBundle;


    public OctopuService(Context context) {
        mContext = context;
    }

    @Override
    public void upSensorData(Bundle bundle) throws RemoteException {
        sensorBundle = bundle;
        notifySensorChange(sensorBundle);
    }

    @Override
    public Bundle getSensorData() throws RemoteException {
        return sensorBundle;
    }

    @Override
    public void upGpsData(Bundle bundle) throws RemoteException {
        gpsBundle = bundle;
    }

    @Override
    public Bundle getGpsData() throws RemoteException {
        return gpsBundle;
    }

    @Override
    public void upCellData(Bundle bundle) throws RemoteException {
        cellBundle = bundle;
    }

    @Override
    public Bundle getCellData() throws RemoteException {
        return cellBundle;
    }

    @Override
    public void upWifiData(Bundle bundle) throws RemoteException {
        wifiBundle = bundle;
    }

    @Override
    public Bundle getWifiData() throws RemoteException {
        return wifiBundle;
    }

    @Override
    public void registerListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.register(octopuListener);
    }

    @Override
    public void unregisterListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.unregister(octopuListener);
    }

    private void notifySensorChange(Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifySensorChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }


}
