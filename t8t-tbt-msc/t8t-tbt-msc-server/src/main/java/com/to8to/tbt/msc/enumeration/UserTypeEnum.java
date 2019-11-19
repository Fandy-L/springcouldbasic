package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum UserTypeEnum {
    /**
     * 用户类型
     */
    DEFAULT(0), BUSINESS(1);

    private int code;

    UserTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }}
