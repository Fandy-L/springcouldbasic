package com.to8to.tbt.msc.service;


import com.to8to.tbt.msc.dto.PushDTO;
import com.to8to.tbt.msc.dto.PushResultDTO;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 19:46
 */
public interface ChannelPushService {
    /**
     * 推送消息
     * @param pushDto
     * @return
     */
    PushResultDTO push(PushDTO pushDto);
}
