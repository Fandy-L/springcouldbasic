package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum  MsgSendAutoEnum {
    /**
     * 手动发送
     */
    YES(1),

    /**
     * 自动发送
     */
    NO(2);

    private final int code;

    MsgSendAutoEnum(int code){
        this.code = code;
    }

    public final int getCode(){
        return code;
    }

    public final boolean compare(int value){
        return code == value;
    }
}
