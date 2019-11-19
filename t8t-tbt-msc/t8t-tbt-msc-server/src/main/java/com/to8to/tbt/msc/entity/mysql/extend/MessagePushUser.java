package com.to8to.tbt.msc.entity.mysql.extend;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "to8to_messages_push_user")
public class MessagePushUser {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "first_id")
    private String firstId;

    @Column(name = "msg_id")
    private String msgId;

    @Column(name = "push_status")
    private int pushStatus;

    @Column(name = "update_time")
    private int updateTime;

    @Column(name = "create_time")
    private int createTime;
}
