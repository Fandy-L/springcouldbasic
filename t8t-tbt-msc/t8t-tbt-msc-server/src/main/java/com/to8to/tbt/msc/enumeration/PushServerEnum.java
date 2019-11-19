package com.to8to.tbt.msc.enumeration;

public enum PushServerEnum {
    /**
     * 旧版推送（友盟）
     */
    OLD((byte)0),
    /**
     * 新版推送(极光)
     */
    NEW((byte)1),
    /**
     * 全部
     */
    BOTH((byte)1);

    private final Byte value;

    PushServerEnum(Byte value){
        this.value = value;
    }

    public Byte getValue(){
        return this.value;
    }
}
