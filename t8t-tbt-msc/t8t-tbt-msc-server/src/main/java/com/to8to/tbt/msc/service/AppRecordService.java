package com.to8to.tbt.msc.service;

/**
 * @author juntao.guo
 */
public interface AppRecordService {

    /**
     * 添加APP消息发送记录
     *
     * @param tid
     * @param uid
     * @param sender
     * @param content
     * @param sendStatus
     * @param bizParam
     * @return
     */
    boolean create(int tid, int uid, String sender, String content, int sendStatus, String bizParam);
}
