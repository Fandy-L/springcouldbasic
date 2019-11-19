package com.to8to.tbt.msc.dto;

import com.alibaba.fastjson.JSONObject;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发送短信及APP消息")
public class SendAllMessageDTO {
    @ApiModelProperty(value = "模板ID", example = "10950")
    private String tid;

    @ApiModelProperty(value = "消息延迟发送的时间")
    @JsonProperty(value = "delaytime")
    private Integer delayTime;

    @ApiModelProperty(value = "1即时发送 2非即时发送")
    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    @ApiModelProperty(value = "手机号", example = "18664594496")
    private String phone;

    @JsonProperty(value = "phoneid")
    private Integer phoneId;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "关键词", example = "{\"验证码\":\"209798\"}")
    private JSONObject keyword;

    @ApiModelProperty(value = "客户端IP", example = "127.0.0.1")
    private String ip;

    @ApiModelProperty(value = "业主ID列表")
    private String uid;

    @ApiModelProperty(value = "子用户")
    private Integer subUid;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "自定义字段")
    @JsonProperty(value = "biz_param")
    private JSONObject bizParam;

    @ApiModelProperty(value = "应用ID")
    private Integer appId;

    @ApiModelProperty(value = "发送模式")
    @JsonProperty(value = "send_mode")
    private Integer sendMode;

    @ApiModelProperty(value = "业务调用户")
    private String sender;

    @ApiModelProperty(value = "提示文字")
    private String ticker;

    @ApiModelProperty(value = "消息点击跳转的动作")
    private JSONObject click;
}
