package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum  SmsChannelEnum {

    /**
     * 通道类型
     */
    PRODUCT(-1);

    private int code;

    SmsChannelEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
