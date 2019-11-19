package com.to8to.tbt.msc.enumeration;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 15:45
 */
public enum BooleanEnum {
    /**
     * TRUE
     */
    TRUE((byte) 1),
    /**
     * FALSE
     */
    FALSE((byte) 0);

    private Byte value;

    BooleanEnum(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }
}
