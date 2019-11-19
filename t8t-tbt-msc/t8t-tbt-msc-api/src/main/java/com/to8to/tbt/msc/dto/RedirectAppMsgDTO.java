package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.dto.PushMessageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "重定向app消息")
public class RedirectAppMsgDTO {
    @ApiModelProperty(value = "APP应用ID", example = "15")
    @JsonProperty(value = "appid")
    @NotNull
    @Min(1)
    private Integer appid;

    @ApiModelProperty(value = "推送集体：0-全部（安装APP的所有用户），1-单个（单一台手机和UID 关联），2-多用户（英文字符逗号区分）, 3-全部匿名用户（对应的uid填0）", example = "1")
    @JsonProperty(value = "scope")
    @NotNull
    @Min(0)
    private Integer scope;

    @ApiModelProperty(value = "推送方式：0-通知 ，1-透传", example = "0")
    @JsonProperty(value = "type")
    @NotNull
    @Min(0)
    private Integer type;

    @ApiModelProperty(value = "用户UID ( 或 scope = 2时, 可以使用 uid 串,多用户推送 最多 500 人 )", example = "11177666")
    @JsonProperty(value = "uid")
    private String uid;

    @ApiModelProperty(value = "子用户UID  ( 或 scope = 2时, 可以使用 uid 串 )", example = "0")
    @JsonProperty(value = "uid_sub")
    private String subUid;

    @ApiModelProperty(value = "设备firstids ( 或 scope = 2时, 可以使用firstid 串,多设备推送 最多 500 人 )", example = "0302cbc8933c2359b4d6651c93040ecb")
    @JsonProperty(value = "firstids")
    private String firstids;

    @ApiModelProperty(value = "device_tokens 推送（现只支持多列推送 scope =2 ，最多推送500个，如传device_tokens，不需要绑定uid，只做推送功能）", example = "0302cbc8933c2359b4d6651c93040ecb")
    @JsonProperty(value = "device_tokens")
    private String deviceTokens;

    @ApiModelProperty(value = "第三方推送平台： 0-友盟 ，1-小米， 2-华为 ，3-魅族，4-极光，5-OPPO, 6-VIVO，7-APPLE", example = "4")
    @JsonProperty(value = "platform")
    private Integer platform;

    @ApiModelProperty(value = "推送信息的 标题 和 描述;", example = "{\"badge\":196,\"ticker\":\"派单系统通知\",\"description\":\"您收到一个新的派单项目，请及时回访跟进！\",\"title\":\"收到新的派单项目App推送\"}")
    @JsonProperty(value = "message")
    @Valid
    @NotNull
    private PushMessageDTO message;

    @ApiModelProperty(value = "自定义字段 主要为了传输其它信息，将整个json插入extra中，key=to8o", example = "{\"jumpUrl\":\"tubusiness://route?type=1001&content={\\\"orderCode\\\":\\\"JCPD0000063603\\\",\\\"noticeId\\\":610252}\"}")
    @JsonProperty(value = "to8to")
    private Map<String,Object> to8to;

    @ApiModelProperty(value = "自定义字段 主要为了传输其它信息，提取字段插入extra中", example = "{}")
    @JsonProperty(value = "to8to_custom")
    private Map<String,Object> to8toCustom;

    @ApiModelProperty(value = "定时发送时间，格式yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "start_time")
    private String startTime;

    @ApiModelProperty(value = "过期时间，其值不可小于发送时间，格式yyyy-MM-dd HH:mm:ss，友盟默认3天")
    @JsonProperty(value = "expire_time")
    private String expireTime;

    @ApiModelProperty(value = "IOS 分环境 测试（ false ）和 正式（默认 true）[测试：开发安装的APP][正式：平台下载安装的APP]")
    @JsonProperty(value = "production_mode")
    private String productionMode;
}
