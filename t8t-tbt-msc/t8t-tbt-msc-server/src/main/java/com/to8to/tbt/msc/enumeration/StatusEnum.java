package com.to8to.tbt.msc.enumeration;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 22:16
 */
public enum StatusEnum {
    /**
     * 等待中
     */
    PENDING((byte)0),
    /**
     * 成功
     */
    SUCCESS((byte)1),
    /**
     * 失败
     */
    FAIL((byte)2);

    private final Byte value;

    StatusEnum(Byte value){
        this.value = value;
    }

    public Byte getValue(){
        return this.value;
    }
}
