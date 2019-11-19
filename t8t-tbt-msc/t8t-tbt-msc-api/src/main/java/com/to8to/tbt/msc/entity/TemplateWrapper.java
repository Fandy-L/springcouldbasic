package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
public class TemplateWrapper {
    @Builder
    @Data
    @Valid
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "公用模板更新数据")
    public static class Template{
        @ApiModelProperty(value = "模版名称")
        private String title;

        @ApiModelProperty(value = "启用状态 1.启用 2.停用")
        @JsonProperty(value = "is_active")
        private int isActive;

        @ApiModelProperty(value = "触发方式 1.手动 2.自动")
        @JsonProperty(value ="is_auto")
        private int isAuto;

        @ApiModelProperty(value = "接收角色（现在为发送对象)")
        @JsonProperty(value = "target_type")
        private int targetType;

        @ApiModelProperty(value = "发送方式 1.短信，2.pc消息 3.邮件 4.微信")
        @JsonProperty(value = "send_type")
        private int sendType;

        @ApiModelProperty(value = "产品模块")
        @JsonProperty(value = "pm_module")
        private String pmModule;

        @ApiModelProperty(value = "备注")
        @JsonProperty(value = "msg_remark")
        private String msgRemark;

        @ApiModelProperty(value = "修改人id")
        @JsonProperty(value = "modify_id")
        private int modifyId;
    }

    @Builder
    @Data
    @Valid
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "短信及APP模板更新数据")
    public static class MsgTemplate{

        @ApiModelProperty(value = "短信内容")
        @JsonProperty(value = "msg_content")
        private String msgContent;

        @JsonProperty(value = "is_ground")
        @ApiModelProperty(value = "落地情况 1 落地 2非落地")
        private int isGround;

        @JsonProperty(value = "channel_type")
        @ApiModelProperty(value = "通道类型")
        private int channelType;

        @ApiModelProperty(value = "是否需要ip发送量限制:0否,1-是")
        @JsonProperty(value = "need_ip_limit")
        private int needIpLimit;

        @ApiModelProperty(value = "ip发送限制数量")
        @JsonProperty(value = "ip_limit_num")
        private int ipLimitNum;

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
}
