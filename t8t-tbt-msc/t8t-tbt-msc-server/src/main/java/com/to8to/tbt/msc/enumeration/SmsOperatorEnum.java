package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum SmsOperatorEnum {
    /**
     * 腾讯/梦网/阿里云
     */
    TENCENT("tencentSMS"), MONGATE("mongateSMS"), ALIYUN("aliyunSMS");

    private final String code;

    SmsOperatorEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public boolean equals(String value){
        return value.equals(this.code);
    }
}
