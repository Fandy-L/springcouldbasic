package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_app_template")
public class AppTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 公共模板id
     */
    private int tid;

    /**
     * app消息内容
     */
    @Column(name = "app_content")
    private String appContent;

    /**
     * app类型id
     */
    @Column(name = "app_id")
    private int appId;

    /**
     * 是否需要推送:0是，1否
     */
    @Column(name = "need_push")
    private int needPush;

    @Column(name = "create_time")
    private int createTime;

    /**
     * 推送方式:1-通知,2-透传
     */
    @Column(name = "push_type")
    private int pushType;

    /**
     * 推送集体:1-全部，2-单个
     */
    @Column(name = "push_scope")
    private int pushScope;
}
