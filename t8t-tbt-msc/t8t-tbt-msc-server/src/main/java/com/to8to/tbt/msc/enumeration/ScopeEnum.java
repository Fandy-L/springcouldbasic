package com.to8to.tbt.msc.enumeration;

/**
 * @author pajero.quan
 */

public enum ScopeEnum {
    /**
     * 全量推送
     */
    ALL((byte)0),
    /**
     * 单推
     */
    SINGLE((byte)1),
    /**
     * 群推
     */
    GROUP((byte)2),
    /**
     * 匿名推送
     */
    ANONYMOUS((byte)3);

    private final Byte value;

    ScopeEnum(Byte value){
        this.value = value;
    }

    public Byte getValue(){
        return this.value;
    }
}
