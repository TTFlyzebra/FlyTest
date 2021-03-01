// OctopuListener.aidl
package android.octopu;

// Declare any non-default types here with import statements

interface OctopuListener {

    void notifySensorChange(int type, float x, float y, float z, long time);
}
