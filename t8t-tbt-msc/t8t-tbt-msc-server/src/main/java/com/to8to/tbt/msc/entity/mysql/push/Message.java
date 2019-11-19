package com.to8to.tbt.msc.entity.mysql.push;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author pajero.quan
 */
@Data
@Entity
@Table(name = "push_message")
public class Message {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    /**
     * t8t消息id
     */
    @Column(name = "t8t_msg_id")
    private String t8tMsgId;

    /**
     * 通道消息id
     */
    @Column(name = "channel_msg_id")
    private Long channelMsgId;

    /**
     * appid
     */
    @Column(name = "push_app_id")
    private Integer pushAppId;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户设备token
     */
    @Column(name = "device_token")
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
     * 推送范围。全量推送：0；单推：1；群推：2
     */
    private Byte scope;

    /**
     * 原因。失败时才有
     */
    private String reason;

    /**
     * 定时发送时间
     */
    @Column(name = "start_time")
    private Integer startTime;

    /**
     * 定时发送过期时间
     */
    @Column(name = "expire_time")
    private Integer expireTime;

    /**
     * 定时任务id
     */
    @Column(name = "schedule_id")
    private String scheduleId;

    /**
     * 是否删除
     */
    private Byte deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Integer updateTime;

    /**
     * 消息内容,json
     */
    private byte[] payload;
}