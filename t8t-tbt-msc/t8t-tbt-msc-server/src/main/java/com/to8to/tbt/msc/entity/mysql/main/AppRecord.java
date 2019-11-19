package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_app_record")
public class AppRecord {
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
     * 业主ID
     */
    private int uid;

    /**
     * 发送方标识
     */
    private String sender;

    /**
     * app消息内容
     */
    @Column(name = "app_content")
    private String appContent;

    /**
     * 业务字段，可选
     */
    @Column(name = "biz_param")
    private String bizParam;

    /**
     * 发送状态：0-缺省值，未发送；1-发送成功；2-发送失败
     */
    @Column(name = "send_status")
    private int sendStatus;

    /**
     * 读取状态：0-缺省值，未读；1-已读
     */
    @Column(name = "is_read")
    private int isRead = 0;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private int sendTime;

    /**
     * 入库时间
     */
    @Column(name = "create_time")
    private int createTime;
}
