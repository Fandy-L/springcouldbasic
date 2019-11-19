package com.to8to.tbt.msc.entity.mysql.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "msgc_timing_app")
public class AppTiming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int tid;

    @Column(name = "appid")
    private int appId;

    /**
     * PUSH版本
     */
    private int version;

    /**
     * 业主ID
     */
    private String uids;

    /**
     * 调用方
     */
    private String sender;

    /**
     * 消息内容
     */
    @Column(name = "msg_content")
    private String msgContent;

    /**
     * 业务数据
     */
    @Column(name = "biz_data")
    private String bizData;

    /**
     * 是否发送0-未发送1-已发送
     */
    @Column(name = "is_send")
    private int isSend;

    /**
     * 添加时间
     */
    @Column(name = "puttime")
    private int putTime;

    /**
     * 发送时间
     */
    @Column(name = "sendtime")
    private int sendTime;

    /**
     * 延时发送日期
     */
    @Column(name = "delaytime")
    private int delayTime;

    /**
     * 发送模式
     */
    @Column(name = "send_mode")
    private int sendMode;
}
