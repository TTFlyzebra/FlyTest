// IOctopuService.aidl
package android.octopu;

// Declare any non-default types here with import statements

interface IOcProcessService {

    List<String> getWhiteList();

    boolean addWhiteProcess(String packName);

    boolean delWhiteProcess(String packName);

    void notifyProcessStatu(String packName, int statu);
}
