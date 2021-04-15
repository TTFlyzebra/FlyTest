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
    private Bundle phonebookBundle;
    private Bundle webcamBundle;

    public OctopuService(Context context) {
        mContext = context;
    }

    @Override
    public void registerListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.register(octopuListener);
    }

    @Override
    public void unregisterListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.unregister(octopuListener);
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
        notifyGpsChange(gpsBundle);
    }

    @Override
    public Bundle getGpsData() throws RemoteException {
        return gpsBundle;
    }

    @Override
    public void upCellData(Bundle bundle) throws RemoteException {
        cellBundle = bundle;
        notifyCellChange(cellBundle);
    }

    @Override
    public Bundle getCellData() throws RemoteException {
        return cellBundle;
    }

    @Override
    public void upWifiData(Bundle bundle) throws RemoteException {
        wifiBundle = bundle;
        notifyWifiChange(wifiBundle);
    }

    @Override
    public Bundle getWifiData() throws RemoteException {
        return wifiBundle;
    }

    @Override
    public void upPhonebookData(Bundle bundle)  throws RemoteException {
        phonebookBundle = bundle;
        notifyPhonebookChange(phonebookBundle);
    }

    @Override
    public Bundle getPhonebookData() throws RemoteException {
        return phonebookBundle;
    }

    @Override
    public void upWebcamData(Bundle bundle) throws RemoteException {
        webcamBundle = bundle;
        notifyWebcamChange(webcamBundle);
    }

    @Override
    public Bundle getWebcamData() throws RemoteException {
        return webcamBundle;
    }

    private void notifySensorChange(final Bundle bundle) {
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

    private void notifyGpsChange(final Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifyGpsChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private void notifyCellChange(final Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifyCellChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private void notifyWifiChange(final Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifyWifiChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private void notifyPhonebookChange(final Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifyPhonebookChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    private void notifyWebcamChange(final Bundle bundle) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifyWebcamChange(bundle);
                }
            } catch (RemoteException e) {
                FlyLog.e(e.toString());
            } catch (Exception e) {
                FlyLog.e(e.toString());
            }
        }
        mOctopuListeners.finishBroadcast();
    }

    @Override
    public void setAirplaneModeOn(boolean enabling) {

    }
}
