// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;

// Declare any non-default types here with import statements

interface IOctopuService {

    void upSensorData(int type, float x, float y, float z, long time);

    void registerListener(OctopuListener octopuListener);

    void unregisterListener(OctopuListener octopuListener);

}
