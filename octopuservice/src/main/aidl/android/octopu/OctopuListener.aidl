// OctopuListener.aidl
package android.octopu;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface OctopuListener {

    void notifySensorChange(out Bundle bundle);

}
