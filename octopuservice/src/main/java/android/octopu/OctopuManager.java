package android.octopu;

import android.content.Context;
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
    private List<senserListener> mSensorListeners = new ArrayList<>();
    private final Object mListenerLock = new Object();
    private IOctopuService mService;
    private OctopuListener mOctopuListener = new OctopuListener.Stub() {
        @Override
        public void notifySensorChange(int type, float x, float y, float z, long time) throws RemoteException {
            synchronized (mListenerLock) {
                for (senserListener llstener : mSensorListeners) {
                    llstener.notifySensorChange(type, x, y, z, time);
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

    public void upSensorData(int type, float x, float y, float z, long time) {
        try {
            if(mService!=null){
                mService.upSensorData(type, x, y, z, time);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public interface senserListener {
        void notifySensorChange(int type, float x, float y, float z, long time);
    }

    public void addSensorListener(senserListener senserListener) {
        synchronized (mListenerLock) {
            mSensorListeners.add(senserListener);
        }
    }

    public void removeSensorListener(senserListener senserListener) {
        synchronized (mListenerLock) {
            mSensorListeners.remove(senserListener);
        }
    }

}
