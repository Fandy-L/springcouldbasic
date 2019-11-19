package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum MsgModuleCodeEnum {
    /**
     * 模块标识
     */
    OTHER("other"), DIARY("diary"), RELATIONSHIP("relationship"), DIARY_CHANNEL("diaryChannel"), SCORE("SCORE"), DIARY_DETAIL_VIEW("diaryDetailView");

    private String code;

    MsgModuleCodeEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }}
