package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "短信及APP模板创建")
public class CreateTemplateDTO {

    @ApiModelProperty(value = "发送方式 1.短信，2.pc消息 3.邮件 4.微信")
    @JsonProperty(value = "send_type")
    private Integer sendType;

    @JsonProperty(value = "msg_content")
    private String msgContent;

    @JsonProperty(value = "need_ip_limit")
    private Integer needIpLimit;

    @JsonProperty(value = "ip_limit_num")
    private Integer ipLimitNum;

    @JsonProperty(value = "nodeid")
    private Integer nodeId;

    @ApiModelProperty(value = "模版名称")
    private String title;

    @ApiModelProperty(value = "启用状态 1.启用 2.停用")
    @JsonProperty(value = "is_active")
    private Integer isActive;

    @ApiModelProperty(value = "触发方式 1.手动 2.自动")
    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    @ApiModelProperty(value = "接收角色（现在为发送对象)")
    @JsonProperty(value = "target_type")
    private Integer targetType;

    @ApiModelProperty(value = "产品模块")
    @JsonProperty(value = "pm_module")
    private String pmModule;

    @ApiModelProperty(value = "备注")
    @JsonProperty(value = "msg_remark")
    private String msgRemark;

    @ApiModelProperty(value = "创建人ID")
    @JsonProperty(value = "create_id")
    private Integer createId;

    @ApiModelProperty(value = "落地情况 1 落地 2非落地")
    @JsonProperty(value = "is_ground")
    private Integer isGround;

    @ApiModelProperty(value = "通道类型")
    @JsonProperty(value = "channel_type")
    private Integer channelType;

    @ApiModelProperty(value = "城市ID")
    @JsonProperty(value = "city_ids")
    private String cityIds;

    @ApiModelProperty(value = "app类型id")
    @JsonProperty(value = "app_id")
    private Integer appId;

    @ApiModelProperty(value = "是否需要推送:0是，1否")
    @JsonProperty(value = "need_push")
    private Integer needPush;

    @ApiModelProperty(value = "推送方式:1-通知,2-透传")
    @JsonProperty(value = "push_type")
    private Integer pushType;

    @ApiModelProperty(value = "推送集体:1-全部，2-单个")
    @JsonProperty(value = "push_scope")
    private Integer pushScope;
}
