// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;
import android.os.Bundle;

// Declare any non-default types here with import statements

interface IOctopuService {

    void upSensorData(in Bundle bundle);

    Bundle getSensorData();

    void upGpsData(in Bundle bundle);

    Bundle getGpsData();

    void upCellData(in Bundle bundle);

    Bundle getCellData();

    void upWifiData(in Bundle bundle);

    Bundle getWifiData();

    void registerListener(OctopuListener octopuListener);

    void unregisterListener(OctopuListener octopuListener);

}
