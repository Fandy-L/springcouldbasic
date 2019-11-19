package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_timing_message")
public class TimingMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 短信模板Id
     */
    @Column(name = "tid")
    private int tid;

    /**
     * 电话Id
     */
    @Column(name = "phoneid")
    private int phoneId;

    /**
     * 短信内容
     */
    @Column(name = "msg_content")
    private String msgContent;

    /**
     * 发送状态 0 未发送 1 已发送
     */
    @Column(name = "is_send")
    private int isSend;

    /**
     * 创建时间
     */
    @Column(name = "puttime")
    private int putTime;

    /**
     * 发送时间
     */
    @Column(name = "sendtime")
    private int sendTime;

    /**
     * 延迟时间
     */
    @Column(name = "delaytime")
    private int delayTime;
}
