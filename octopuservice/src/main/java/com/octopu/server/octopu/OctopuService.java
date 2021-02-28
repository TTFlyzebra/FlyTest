package com.octopu.server.octopu;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.octopu.IOctopuService;
import com.octopu.OctopuListener;

import java.util.List;


/**
 * @hide ClassName: OctopuService
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:42
 */
public class OctopuService extends IOctopuService.Stub {
    private static RemoteCallbackList<OctopuListener> mOctopuListeners = new RemoteCallbackList<>();
    private final Object mWifiDevicesLock = new Object();

    public OctopuService(Context context) {
    }


    @Override
    public void flyWifiDevices(List<String> wifiBssids) throws RemoteException {

    }
}
