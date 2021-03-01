// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;

// Declare any non-default types here with import statements

interface IOctopuService {

    void setSensorData(int type, float x, float y, float z, long time);

    void registerListener(OctopuListener octopuListener);

    void unregisterListener(OctopuListener octopuListener);

}
