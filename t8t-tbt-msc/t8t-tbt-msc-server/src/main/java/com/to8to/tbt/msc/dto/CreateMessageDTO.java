package com.to8to.tbt.msc.dto;

import lombok.Data;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 21:17
 */
@Data
public class CreateMessageDTO {

    /**
     * t8t消息id
     */
    private String t8tMsgId;

    /**
     * 通道消息id
     */
    private Long channelMsgId;

    private Integer pushAppId;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户设备token
     */
    private String deviceToken;

    /**
     * 通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5; 魅族：6
     */
    private Byte channel;

    /**
     * 平台。ios:0; android:1;all:2
     */
    private Byte platform;

    /**
     * 推送范围。全量推送：0；单推：1；群推：2
     */
    private Byte scope;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息文本
     */
    private String content;

    /**
     * 消息状态。待推送：0；成功：1；失败：2
     */
    private Byte status;

    /**
     * 原因。失败时才有
     */
    private String reason;

    /**
     * 定时发送时间
     */
    private Integer startTime;

    /**
     * 定时发送过期时间
     */
    private Integer expireTime;
    /**
     * 定时任务id
     */
    private String scheduleId;

    /**
     * 是否删除
     */
    private Byte deleted;

    /**
     * 消息内容,json
     */
    private byte[] payload;
}
