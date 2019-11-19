package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum MsgSendTypeEnum {
    /**
     * 用户类型
     */
    SMS(1), EMAIL(2), WECHAT(3), APP(4), PC(5);

    private int code;

    MsgSendTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public final boolean equals(Integer value){
        return value != null && code == value.intValue();
    }
}
