// IOctopuService.aidl
package android.octopu;

import android.octopu.OctopuListener;
import android.os.Bundle;

// Declare any non-default types here with import statements

interface IOctopuService {

    void registerListener(OctopuListener octopuListener);

    void unregisterListener(OctopuListener octopuListener);

    void upSensorData(inout Bundle bundle);

    Bundle getSensorData();

    void upGpsData(inout Bundle bundle);

    Bundle getGpsData();

    void upCellData(inout Bundle bundle);

    Bundle getCellData();

    void upWifiData(inout Bundle bundle);

    Bundle getWifiData();

    void upPhonebookData(inout Bundle bundle);

    Bundle getPhonebookData();

    void setAirplaneModeOn(boolean enabling);

    //OcProcessService
    List<String> getWhiteList();

    boolean addWhiteProcess(String packName);

    boolean delWhiteProcess(String packName);

}
