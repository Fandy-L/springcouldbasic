package com.to8to.tbt.msc.service;

/**
 * @author yason.li
 */
public interface ImportDataService {
    /**
     * 同步消息发送记录表
     */
    void syncMsgRecord();

    /**
     * 同步APP消息发送记录表
     */
    void syncAppRecord();
}
