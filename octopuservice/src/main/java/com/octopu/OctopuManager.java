package com.octopu;

import android.content.Context;
import android.os.RemoteException;

/**
 * ClassName: OctopuManager
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 20-1-8 下午5:44
 */
public class OctopuManager {

    private IOctopuService mService;
    private OctopuListener mOctopuListener = new OctopuListener.Stub() {
        @Override
        public void notifyWifiDevices() throws RemoteException {
        }
    };

    public OctopuManager(Context context, IOctopuService octopuService) {
        mService = octopuService;
    }

}
