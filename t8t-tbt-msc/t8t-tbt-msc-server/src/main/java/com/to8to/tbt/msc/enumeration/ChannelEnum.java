package com.to8to.tbt.msc.enumeration;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 20:21
 */
public enum ChannelEnum {
    /**
     * 极光
     */
    JIGUANG((byte) 0),
    /**
     * 苹果
     */
    APPLE((byte) 1),
    /**
     * 华为
     */
    HUAWEI((byte) 2),
    /**
     * 小米
     */
    XIAOMI((byte) 3),
    /**
     * OPPO
     */
    OPPO((byte) 4),
    /**
     * VIVO
     */
    VIVO((byte) 5),
    /**
     * MEIZU
     */
    MEIZU((byte) 6);

    private final byte value;

    ChannelEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static ChannelEnum valueOf(byte value) {
        for (ChannelEnum channelEnum : ChannelEnum.values()) {
            if (channelEnum.getValue() == value) {
                return channelEnum;
            }
        }
        return ChannelEnum.JIGUANG;
    }
}
