package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum MsgReadStatusEnum {
    /**
     * 消息读取状态
     */
    UNREAD(0), HAS_READ(1), INVALID(2);

    private int code;

    MsgReadStatusEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }}
