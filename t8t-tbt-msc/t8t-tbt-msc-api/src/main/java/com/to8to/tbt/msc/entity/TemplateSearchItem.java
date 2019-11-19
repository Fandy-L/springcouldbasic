package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateSearchItem {
    /**
     * 模板ID
     */
    private Integer tid;

    /**
     * 节点ID
     */
    @JsonProperty(value = "nodeid")
    private Integer nodeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 启用状态 1.启用 2.停用
     */
    @JsonProperty(value = "is_active")
    private Integer isActive;

    /**
     * 触发方式 1.手动 2.自动
     */
    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    /**
     * 目标用户类型
     */
    @JsonProperty(value = "target_type")
    private Integer targetType;

    /**
     * 发送方式
     */
    @JsonProperty(value = "send_type")
    private Integer sendType;

    /**
     * 备注
     */
    @JsonProperty(value = "msg_remark")
    private String msgRemark;

    /**
     * 产品模块
     */
    @JsonProperty(value = "pm_module")
    private Integer pmModule;

    /**
     * 创建时间
     */
    @JsonProperty(value = "create_time")
    private Integer createTime;

    /**
     * 创建人ID
     */
    @JsonProperty(value = "create_id")
    private Integer createId;

    /**
     * APP模板内容
     */
    @JsonProperty(value = "app_content")
    private String appContent;

    /**
     * 应用ID
     */
    @JsonProperty(value = "app_id")
    private Integer appId;

    /**
     * 节点父ID
     */
    @JsonProperty(value = "father_id")
    private Integer fatherId;

    /**
     * 节点描述
     */
    @JsonProperty(value = "config_describe")
    private String configDescribe;

    /**
     * 创建人昵称
     */
    private String nick;

    /**
     * 短信模板内容
     */
    @JsonProperty(value = "msg_content")
    private String msgContent;

    /**
     * 短信模板是否落地
     */
    @JsonProperty(value = "is_ground")
    private Integer isGround;

    /**
     * 发送通道
     */
    @JsonProperty(value = "channel_type")
    private Integer channelType;

    /**
     * 城市ID
     */
    @JsonProperty(value = "city_ids")
    private String cityIds;

    @JsonProperty(value = "bigtype")
    private ConfigureWrapper.Configure bigType;

    @JsonProperty(value = "smalltype")
    private ConfigureWrapper.Configure smallType;
}
