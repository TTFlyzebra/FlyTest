// OctopuListener.aidl
package android.octopu;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface OctopuListener {

    void notifySensorChange(out Bundle bundle);

    void notifyGpsChange(out Bundle bundle);

    void notifyCellChange(out Bundle bundle);

    void notifyWifiChange(out Bundle bundle);

}
