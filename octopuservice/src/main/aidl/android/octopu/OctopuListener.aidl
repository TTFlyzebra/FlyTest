// OctopuListener.aidl
package android.octopu;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface OctopuListener {

    void notifySensorChange(inout Bundle bundle);

    void notifyGpsChange(inout Bundle bundle);

    void notifyCellChange(inout Bundle bundle);

    void notifyWifiChange(inout Bundle bundle);

}
