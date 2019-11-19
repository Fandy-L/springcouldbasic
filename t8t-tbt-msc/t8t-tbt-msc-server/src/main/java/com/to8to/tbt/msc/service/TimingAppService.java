package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author juntao.guo
 */
public interface TimingAppService {

    /**
     * 添加延时消息记录
     *
     * @param tid
     * @param uids
     * @param sender
     * @param appId
     * @param version
     * @param content
     * @param bizData
     * @param delayTime
     * @param sendMode
     * @return
     */
    boolean create(int tid, String uids, String sender, int appId, int version, String content, JSONObject bizData, int delayTime, int sendMode);
}
