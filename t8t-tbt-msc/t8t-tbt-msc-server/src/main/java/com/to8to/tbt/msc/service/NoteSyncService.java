package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONArray;
import com.to8to.tbt.msc.entity.RabbitMqWrapper;

/**
 * @author juntao.guo
 */
public interface NoteSyncService {
    /**
     * 同步MQ中的短信及APP消息记录至ES
     *
     * @param data
     */
    void execute(JSONArray data);

    /**
     * 同步短信记录
     * @param messageRecord
     * @return
     */
    boolean syncMessageRecord(RabbitMqWrapper.MessageRecord messageRecord);

    /**
     * 同步APP消息记录
     * @param appRecord
     * @return
     */
    boolean syncAppRecord(RabbitMqWrapper.AppRecord appRecord);
}
