package com.to8to.tbt.msc.entity.mysql.extend;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "to8to_app_ad")
public class AppAd {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue
    private int id;

    /**
     * 应用id
     */
    @Column(name = "appid")
    private int appId;

    /**
     * 平台
     */
    private int platform;

    /**
     * 作用版本
     */
    @Column(name = "enableversion")
    private String enableVersion;

    /**
     * 广告类型
     */
    @Column(name = "adtype")
    private int adType;

    /**
     * banner编号
     */
    @Column(name = "bannerid")
    private int bannerId;

    /**
     * 广告名称
     */
    @Column(name = "adname")
    private String adName;

    /**
     * 640*220广告图
     */
    @Column(name = "smallimg")
    private String smallImg;

    /**
     * 750*260广告图
     */
    @Column(name = "nomalimg")
    private String normalImg;

    /**
     * 1125*390广告图
     */
    @Column(name = "bigimg")
    private String bigImg;

    /**
     *闪屏显示时间
     */
    @Column(name = "showtime")
    private int showTime;

    /**
     * 链接类型
     */
    @Column(name = "linktype")
    private int linkType;

    /**
     * 广告跳转类型
     */
    @Column(name = "jumptype")
    private int jumpType;

    /**
     * 链接地址
     */
    @Column(name = "linkurl")
    private String linkUrl;

    /**
     * 开始时间
     */
    @Column(name = "begintime")
    private int beginTime;

    /**
     * 结束时间
     */
    @Column(name = "endtime")
    private int endTime;

    /**
     * 作用城市
     */
    @Column(name = "usedcity")
    private String usedCity;

    /**
     * 访问量
     */
    @Column(name = "clicknum")
    private int clickNum;

    /**
     * 生效状态，0为失效，1为生效
     */
    private int status;

    /**
     * 目标用户，0全部，1已登记，2未登记
     */
    @Column(name = "usertype")
    private int userType;

    /**
     * 主标题
     */
    @Column(name = "main_title")
    private String mainTitle;

    /**
     * 描述
     */
    private String description;

    /**
     * 安卓安装包
     */
    private String install;
}
