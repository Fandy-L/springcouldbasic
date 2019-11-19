package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_message_template")
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mtid")
    private int id;

    /**
     * 所属公共模板id
     */
    private int tid;

    /**
     * 短信内容
     */
    @Column(name = "msg_content")
    private String msgContent;

    /**
     * 落地情况 1 落地 2非落地
     */
    @Column(name = "is_ground")
    private int isGround;

    /**
     * 通道类型
     */
    @Column(name = "channel_type")
    private int channelType;

    /**
     * 是否需要ip发送量限制:0否,1-是
     */
    @Column(name = "need_ip_limit")
    private int needIpLimit;

    /**
     * ip发送限制数量
     */
    @Column(name = "ip_limit_num")
    private int ipLimitNum;

    /**
     * 城市ID
     */
    @Column(name = "city_ids")
    private String cityIds;
}
