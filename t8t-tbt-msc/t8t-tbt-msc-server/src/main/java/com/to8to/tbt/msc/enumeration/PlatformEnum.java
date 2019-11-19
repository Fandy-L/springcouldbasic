package com.to8to.tbt.msc.enumeration;

/**
 * @author pajero.quan
 */

public enum PlatformEnum {
    /**
     * ANDROID
     */
    ANDROID((byte) 0),
    /**
     * IOS
     */
    IOS((byte) 1),
    /**
     * 全部
     */
    ALL((byte) 2);

    private final byte value;

    PlatformEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static PlatformEnum valueOf(byte value) {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.getValue() == value) {
                return platformEnum;
            }
        }
        return PlatformEnum.ANDROID;
    }
}
