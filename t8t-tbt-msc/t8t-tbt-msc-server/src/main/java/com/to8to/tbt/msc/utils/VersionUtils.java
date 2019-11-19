package com.to8to.tbt.msc.utils;


/**
 * @author pajero.quan
 */
public class VersionUtils {
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");

        for (int i = 0; i < Math.min(versionArray1.length, versionArray2.length); i++) {
            Integer value1 = Integer.valueOf(versionArray1[i]);
            Integer value2 = Integer.valueOf(versionArray2[i]);
            if (value1 > value2) {
                return 1;
            } else if (value1 < value2) {
                return -1;
            }  // do nothing

        }
        return 0;
    }
}
