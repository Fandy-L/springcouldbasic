package com.to8to.tbt.msc.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发送APP模板消息参数")
public class TemplateMsgDTO {

    @ApiModelProperty(value = "模板ID",required = true)
    @JsonProperty(value = "tid")
    @NotNull
    @Range(min = 1, message = "模板ID必须大于0")
    private Integer tid;

    @ApiModelProperty(value = "用户ID",required = true)
    @JsonProperty(value = "uid")
    @NotBlank
    private String uid;

    @ApiModelProperty(value = "子用户ID")
    @JsonProperty(value = "sub_uid")
    private Integer subUid;

    @ApiModelProperty(value = "版本",required = true)
    @JsonProperty(value = "version")
    @NotNull
    private Integer version;

    @ApiModelProperty(value = "自定义字段",required = true)
    @JsonProperty(value = "biz_param")
    private JSONObject bizParam;

    @ApiModelProperty(value = "消息内容",required = true)
    @JsonProperty(value = "content")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "关键词",required = true)
    @JsonProperty(value = "keyword")
    @NotNull
    private JSONObject keyword;

    @ApiModelProperty(value = "延时时间",required = true)
    @JsonProperty(value = "delaytime")
    private Integer delayTime;

    @ApiModelProperty(value = "appId",required = true)
    @JsonProperty(value = "appId")
    private Integer appId;

    @ApiModelProperty(value = "发送模式",required = true)
    @JsonProperty(value = "send_mode")
    private Integer sendMode;

    @ApiModelProperty(value = "发送者",required = true)
    @JsonProperty(value = "sender")
    private String sender;

    @ApiModelProperty(value = "提示文字",required = true)
    @JsonProperty(value = "ticker")
    private String ticker;

    @ApiModelProperty(value = "点击该消息后的动作",required = true)
    @JsonProperty(value = "click")
    private JSONObject click;

}
