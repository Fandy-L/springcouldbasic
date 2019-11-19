package com.to8to.tbt.msc.service;

/**
 * @author juntao.guo
 */
public interface MongoNoteRecordService {

    /**
     * 添加短信记录
     *
     * @param phone
     * @param status
     * @param content
     * @param channelType
     * @param tid
     * @param errorDescribe
     */
    void create(String phone, int status, String content, int channelType, int tid, String errorDescribe);
}
