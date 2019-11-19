package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum  AppApiVersionEnum {
    /**
     * 版本号
     */
    FIRST_VERSION(1, "第一版推送接口"), SECOND_VERSION(2, "第二版推送接口");

    private int version;

    private String name;

    AppApiVersionEnum(int version, String name) {
        this.version = version;
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
