package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum AppMsgShowTypeEnum {
    /**
     * 系统消息展示形式1-纯文本2-卡片
     *
     */
    TEXT(1),
    CARD(2);
    private int code;

    AppMsgShowTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }}
