package com.to8to.tbt.msc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 21:20
 */
@Data
public class GetMessageListDTO {
    @ApiModelProperty(value = "pushAppId")
    private Integer pushAppId;

    @ApiModelProperty(value = "t8t消息id")
    private String t8tMsgId;

    @ApiModelProperty(value = "通道消息id")
    private Long channelMsgId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "firstId")
    private String firstId;

    @ApiModelProperty(value = "用户设备token")
    private String deviceToken;

    @ApiModelProperty(value = "通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5; 魅族：6")
    private Integer channel;

    @ApiModelProperty(value = "平台。ios:0; android:1;all:2")
    private Integer platform;

    @ApiModelProperty(value = "消息状态。待推送：0；成功：1；失败：2")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Integer fromTime;

    @ApiModelProperty(value = "更新时间")
    private Integer endTime;

    @ApiModelProperty(value = "页号", required = true)
    @NotNull
    @Min(1)
    private Integer page;

    @ApiModelProperty(value = "每页条数", required = true)
    @NotNull
    @Min(1)
    private Integer size;
}
