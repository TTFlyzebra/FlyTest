// OctopuListener.aidl
package android.octopu;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface OctopuListener {

    oneway void notifySensorChange(in Bundle bundle);

    oneway void notifyGpsChange(in Bundle bundle);

    oneway void notifyCellChange(in Bundle bundle);

    oneway void notifyWifiChange(in Bundle bundle);

    oneway void notifyPhonebookChange(in Bundle bundle);

}
