package com.to8to.tbt.msc.entity.mysql.push;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "push_app")
public class App {
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
     * 应用名称
     */
    private String name;

    /**
     * 应用包名
     */
    @Column(name = "package_name")
    private String packageName;

    /**
     * 是否启用。禁用：0；启用：1
     */
    private Byte enable;

    /**
     * ios最低版本
     */
    @Column(name = "ios_min_version")
    private String iosMinVersion;

    /**
     * Android最低版本
     */
    @Column(name = "android_min_version")
    private String androidMinVersion;

    /**
     * Android app中转页
     */
    @Column(name = "uri_activity")
    private String uriActivity;

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