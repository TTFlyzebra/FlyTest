package android.octopu;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: OctopuManager
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:44
 */
public class OctopuManager {

    public static final String SENSOR_TYPE = "TYPE";
    public static final String SENSOR_X = "X";
    public static final String SENSOR_Y = "Y";
    public static final String SENSOR_Z = "Z";
    public static final String SENSOR_TIME = "TIME";

    private IOctopuService mService;
    private OctopuListener mOctopuListener = new OctopuListener.Stub() {
        @Override
        public void notifySensorChange(Bundle bundle) throws RemoteException {
            synchronized (mSensorLock) {
                for (SensorListener llstener : mSensorListeners) {
                    llstener.notifySensorChange(bundle);
                }
            }
        }

        @Override
        public void notifyGpsChange(Bundle bundle) throws RemoteException {
            synchronized (mGpsLock) {
                for (GpsListener llstener : mGpsListeners) {
                    llstener.notifyGpsChange(bundle);
                }
            }
        }

        @Override
        public void notifyCellChange(Bundle bundle) throws RemoteException {
            synchronized (mCellLock) {
                for (CellListener llstener : mCellListeners) {
                    llstener.notifyCellChange(bundle);
                }
            }
        }

        @Override
        public void notifyWifiChange(Bundle bundle) throws RemoteException {
            synchronized (mWifiLock) {
                for (WifiListener llstener : mWifiListeners) {
                    llstener.notifyWifiChange(bundle);
                }
            }
        }
    };

    public OctopuManager(Context context, IOctopuService octopuService) {
        mService = octopuService;
    }

    public void upSensorData(Bundle bundle) {
        try {
            if(mService!=null){
                mService.upSensorData(bundle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public Bundle getSensorData(){
        try {
            if(mService!=null){
                return mService.getSensorData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void upGpsData(Bundle bundle) {
        try {
            if(mService!=null){
                mService.upGpsData(bundle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Bundle getGpsData(){
        try {
            if(mService!=null){
                return mService.getGpsData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void upCellData(Bundle bundle) {
        try {
            if(mService!=null){
                mService.upCellData(bundle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Bundle getCellData(){
        try {
            if(mService!=null){
                return mService.getCellData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void upWifiData(Bundle bundle) {
        try {
            if(mService!=null){
                mService.upWifiData(bundle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Bundle getWifiData(){
        try {
            if(mService!=null){
                return mService.getWifiData();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<SensorListener> mSensorListeners = new ArrayList<>();
    private final Object mSensorLock = new Object();
    public interface SensorListener {
        void notifySensorChange(Bundle bundle);
    }

    public void addSensorListener(SensorListener sensorListener) {
        registerLisenter();
        synchronized (mSensorLock) {
            mSensorListeners.add(sensorListener);
        }
    }

    public void removeSensorListener(SensorListener sensorListener) {
        synchronized (mSensorLock) {
            mSensorListeners.remove(sensorListener);
        }
        registerLisenter();
    }

    private List<GpsListener> mGpsListeners = new ArrayList<>();
    private final Object mGpsLock = new Object();
    public interface GpsListener {
        void notifyGpsChange(Bundle bundle);
    }

    public void addSensorListener(GpsListener gpsListener) {
        registerLisenter();
        synchronized (mGpsLock) {
            mGpsListeners.add(gpsListener);
        }
    }

    public void removeSensorListener(GpsListener gpsListener) {
        synchronized (mGpsLock) {
            mGpsListeners.remove(gpsListener);
        }
        unregisterListener();
    }

    private List<CellListener> mCellListeners = new ArrayList<>();
    private final Object mCellLock = new Object();
    public interface CellListener {
        void notifyCellChange(Bundle bundle);
    }

    public void addSensorListener(CellListener cellListener) {
        registerLisenter();
        synchronized (mCellLock) {
            mCellListeners.add(cellListener);
        }
    }

    public void removeSensorListener(CellListener cellListener) {
        synchronized (mCellLock) {
            mCellListeners.remove(cellListener);
        }
        unregisterListener();
    }

    private List<WifiListener> mWifiListeners = new ArrayList<>();
    private final Object mWifiLock = new Object();
    public interface WifiListener {
        void notifyWifiChange(Bundle bundle);
    }

    public void addSensorListener(WifiListener wifiListener) {
        registerLisenter();
        synchronized (mWifiLock) {
            mWifiListeners.add(wifiListener);
        }
    }

    public void removeSensorListener(WifiListener wifiListener) {
        synchronized (mWifiLock) {
            mWifiListeners.remove(wifiListener);
        }
        unregisterListener();
    }

    private boolean isRegister = false;
    private void registerLisenter(){
        if(mService!=null && !isRegister){
            try {
                mService.registerListener(mOctopuListener);
                isRegister = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void unregisterListener(){
        if(mSensorListeners.isEmpty()&&
                mGpsListeners.isEmpty()&&
                mCellListeners.isEmpty()&&
                mWifiListeners.isEmpty()){
            if(mService!=null){
                try {
                    mService.unregisterListener(mOctopuListener);
                    isRegister = false;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
