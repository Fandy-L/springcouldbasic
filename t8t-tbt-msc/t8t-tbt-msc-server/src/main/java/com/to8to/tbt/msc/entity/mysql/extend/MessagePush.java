package com.to8to.tbt.msc.entity.mysql.extend;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "to8to_messages_push")
public class MessagePush {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue
    private int id;

    /**
     * 应用程序ID,1为装修管家，15为土巴兔，16为装修图库，29为新房
     */
    @Column(name = "appid")
    private int appId;

    /**
     * 目标用户，1为全部用户，2为批量用户，3为独立用户
     */
    private int type;

    /**
     * 推送时间
     */
    @Column(name = "push_time")
    private int pushTime;

    /**
     * 提示文字
     */
    private String ticker;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * H5页面链接
     */
    private String url;

    /**
     * 图片
     */
    @Column(name = "push_img")
    private String pushImg;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private int createTime;

    /**
     * 应用平台，1为Android，2为iOS
     */
    @Column(name = "appostype")
    private int appOsType;

    /**
     * 分组条件
     */
    @Column(name = "cate_context")
    private String cateContext;

    /**
     * 推送状态，1：未发送，2、完成 3，推送中 4，撤回
     */
    @Column(name = "push_status")
    private int pushStatus;

    /**
     * 推送渠道，1：友盟，2：极光
     */
    @Column(name = "push_channel")
    private int pushChannel;

    /**
     * 信息推送情况统计，成功数：0，失败数：0，总数：0
     */
    @Column(name = "push_sum")
    private String pushSum;

//    /**
//     * 推送类型，1：内容运营、2活动运营、3营销推广、4其他
//     */
//    @Column(name = "push_type")
//    private int pushType;
//
//    /**
//     * 跳转类型，1：跳转2：无跳转
//     */
//    @Column(name = "url_type")
//    private int urlType;
//
//    /**
//     * 图片场景，1:PUSH通知栏展示 2：push通知栏不展示
//     */
//    @Column(name = "picture_scene")
//    private int pictureScene;
}
