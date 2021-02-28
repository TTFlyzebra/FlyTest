// IOctopuService.aidl
package com.octopu;

import com.octopu.OctopuListener;

// Declare any non-default types here with import statements

interface IOctopuService {

    void flyWifiDevices(in List<String> wifiBssids);

}
