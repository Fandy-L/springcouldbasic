package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum AppMsgTypeEnum {
    /**
     * APP消息类型
     */
    NOTICE(14), SYSTEM(15);

    private int code;

    AppMsgTypeEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }}
