package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_message_record")
public class MessageRecord {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 模板ID
     */
    private int tid;

    /**
     * 电话ID
     */
    @Column(name = "phoneid")
    private int phoneId;

    /**
     * 短信内容
     */
    @Column(name = "msg_content")
    private String msgContent;

    /**
     * 短信发送状态 0 发送失败 1发送成功
     */
    @Column(name = "send_status")
    private int sendStatus;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private int sendTime;

    /**
     * 短信发送异常状态码
     */
    @Column(name = "error_code")
    private int errorCode;
}
