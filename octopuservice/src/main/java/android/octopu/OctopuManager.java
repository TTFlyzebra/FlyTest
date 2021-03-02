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
    private List<SenserListener> mSensorListeners = new ArrayList<>();
    private final Object mListenerLock = new Object();
    private IOctopuService mService;
    private OctopuListener mOctopuListener = new OctopuListener.Stub() {
        @Override
        public void notifySensorChange(Bundle bundle) throws RemoteException {
            synchronized (mListenerLock) {
                for (SenserListener llstener : mSensorListeners) {
                    llstener.notifySensorChange(bundle);
                }
            }
        }
    };

    public OctopuManager(Context context, IOctopuService octopuService) {
        mService = octopuService;
        try {
            mService.registerListener(mOctopuListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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

    void upCellData(Bundle bundle) {
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

    void upWifiData(Bundle bundle) {
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

    public interface SenserListener {
        void notifySensorChange(Bundle bundle);
    }

    public void addSensorListener(SenserListener senserListener) {
        synchronized (mListenerLock) {
            mSensorListeners.add(senserListener);
        }
    }

    public void removeSensorListener(SenserListener senserListener) {
        synchronized (mListenerLock) {
            mSensorListeners.remove(senserListener);
        }
    }

}
