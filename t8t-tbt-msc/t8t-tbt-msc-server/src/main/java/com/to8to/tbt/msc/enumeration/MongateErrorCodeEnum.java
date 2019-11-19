package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum MongateErrorCodeEnum {
    /**
     * 账户余额不足
     */
    BALANCE_INSUFFICIENT(-10003);

    private final Integer code;

    MongateErrorCodeEnum(Integer code){
        this.code = code;
    }

    public final boolean equals(String value){
        return value.equals(String.valueOf(this.code));
    }
}
