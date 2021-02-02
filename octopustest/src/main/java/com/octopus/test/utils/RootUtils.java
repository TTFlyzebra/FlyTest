package com.octopus.test.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RootUtils
{

    public static final String[] rootFilePaths = {
            "/system/xbin/daemonsu",
            "/system/etc/init.d/99SuperSUDaemon",
            "/data/data/com.topjohnwu.magisk",
            "/sdcard/MagiskManager"
    };

    public static final String[] exeFilePaths = {
            "/sbin/su",
            "/system/bin/su",
            "/system/sbin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/data/local/su",
            "/su/bin/su"
    };

    public static boolean checkRootM1()
    {
        File file = new File("/system/app/Superuser.apk");
        boolean find1 = file.exists();

        //File file1 = new File("/system/bin/sh");
        //boolean find2 = file1.exists();

        if( find1
        //        || find2
        )
            return true;
        else
            return false;
    }


    public static boolean checkRootM2()
    {
        ArrayList<String> paths = new ArrayList<String>(Arrays.asList(rootFilePaths));

        for (String path:paths){
            File filePath = new File(path);
            if (filePath.exists()){
                return true;
            }
        }

        return false;
    }

    public static boolean checkRootM3()
    {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")||rootTestKeyDetection()) {
            return true;
        }
        return false;
    }

    public static boolean checkRootM4()
    {
        ArrayList<String> paths = new ArrayList<String>(Arrays.asList(exeFilePaths));
        for (String path:paths){
            File filePath = new File(path);
            if (filePath.exists() && isExecutable(path)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkRootM5()
    {
        String filePath = "/data/SafeEdit.txt";
        try {
            File file1 = new File(filePath);
            if (!file1.exists()) {
                file1.createNewFile();
            }

            if (file1.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkRootM6()
    {
        File file1 = new File("/root");
        File[] files = file1.listFiles();
        if (files == null){
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkRootM7(){
        String binPath = "/system/bin/busybox";
        String xBinPath = "/system/xbin/busybox";
        String shPath = "/system/sbin/busybox";
        if (new File(binPath).exists()) {
            return true;
        }

        if (new File(xBinPath).exists()) {
            return true;
        }

        if (new File(shPath).exists()) {
            return true;
        }

        return false;
    }

    public static boolean checkRootM8(){
        String[] props = {"ro.debuggable","ro.secure","ro.adb.secure"};
        String[] lines = propsReader("getprop");
        if (lines == null){
            return false;
        }
        for (String line:lines){
            if (line.indexOf(props[0]) != -1){
                if (line.indexOf("1") != -1)
                    return true;
            }
            if (line.indexOf(props[1]) != -1){
                if (line.indexOf("0") != -1)
                    return true;
            }
            if (line.indexOf(props[2]) != -1){
                if (line.indexOf("0") != -1)
                    return true;
            }
        }
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        BufferedReader in = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();

            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            p.destroy();
        }

        return false;
    }

    private static boolean rootTestKeyDetection(){
        String[] lines = propsReader("getprop");
        if (lines == null){
            return false;
        }
        for (String line:lines){
            if (line.indexOf("build.fingerprint") != -1){
                if (line.indexOf("test-keys") != -1){
                    return true;
                }
            }
        }
        return false;
    }

    private static String[] propsReader(String cmd) {
        InputStream inputstream = null;
        try {
            inputstream = Runtime.getRuntime().exec(cmd).getInputStream();
            if (inputstream == null) return null;
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (inputstream != null)
                    inputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
