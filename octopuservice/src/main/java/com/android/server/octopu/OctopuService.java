package com.android.server.octopu;

import android.content.Context;
import android.octopu.FlyLog;
import android.octopu.IOctopuService;
import android.octopu.OctopuListener;
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
    public OctopuService(Context context) {
        mContext = context;
    }

    @Override
    public void setSensorData(int type, float x, float y, float z, long time) throws RemoteException {
        notifySensorChange(type,x,y,z,time);
    }

    @Override
    public void registerListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.register(octopuListener);
    }

    @Override
    public void unregisterListener(OctopuListener octopuListener) throws RemoteException {
        mOctopuListeners.unregister(octopuListener);
    }

    private void notifySensorChange(int type, float x, float y, float z, long time) {
        final int N = mOctopuListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                synchronized (mLock) {
                    mOctopuListeners.getBroadcastItem(i).notifySensorChange(type,  x,  y,  z, time);
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
