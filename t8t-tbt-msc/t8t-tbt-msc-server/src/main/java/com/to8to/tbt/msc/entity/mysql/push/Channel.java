package com.to8to.tbt.msc.entity.mysql.push;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author pajero.quan
 */
@Data
@Entity
@Table(name = "push_channel")
public class Channel {
    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    /**
     * 应用ID
     */
    @Column(name = "push_app_id")
    private Integer pushAppId;

    /**
     * 通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5;魅族：6
     */
    private Byte channel;

    /**
     * AppId
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * AppKey
     */
    @Column(name = "app_key")
    private String appKey;

    /**
     * AppSecret
     */
    @Column(name = "app_secret")
    private String appSecret;

    /**
     * MasterSecret
     */
    @Column(name = "master_secret")
    private String masterSecret;

    /**
     * 禁用：0；启用：1
     */
    private Byte enable;

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
}