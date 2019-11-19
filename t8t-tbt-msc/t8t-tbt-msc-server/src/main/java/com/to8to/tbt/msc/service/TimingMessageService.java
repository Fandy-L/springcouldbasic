package com.to8to.tbt.msc.service;

/**
 * @author juntao.guo
 */
public interface TimingMessageService {

    /**
     * 添加延时短信消息
     *
     * @param tid
     * @param phoneId
     * @param content
     * @param delayTime
     * @return
     */
    boolean addDelayMessage(int tid, int phoneId, String content, int delayTime);
}
