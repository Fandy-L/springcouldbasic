package com.to8to.tbt.msc.service;

/**
 * @author juntao.guo
 */
public interface MessageRecordService {

    /**
     * 保存短信记录
     *
     * @param tid
     * @param phoneId
     * @param msgContent
     * @param status
     * @param errorCode
     */
    void create(int tid, int phoneId, String msgContent, int status, int errorCode);
}
