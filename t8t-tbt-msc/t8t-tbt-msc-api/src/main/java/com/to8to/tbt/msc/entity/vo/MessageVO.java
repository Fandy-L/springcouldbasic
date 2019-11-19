package com.to8to.tbt.msc.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("消息")
public class MessageVO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "应用id")
    private Integer pushAppId;

    @ApiModelProperty(value = "t8t消息id")
    private String t8tMsgId;

    @ApiModelProperty(value = "通道消息id")
    private String channelMsgId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户设备token")
    private String deviceToken;

    @ApiModelProperty(value = "通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5; 魅族：6")
    private Byte channel;

    @ApiModelProperty(value = "平台。ios:0; android:1;all:2")
    private Byte platform;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息文本")
    private String content;

    @ApiModelProperty(value = "推送范围。全量推送：0；单推：1；群推：2")
    private Byte scope;

    @ApiModelProperty(value = "消息状态。待推送：0；成功：1；失败：2")
    private Byte status;

    @ApiModelProperty(value = "原因。失败时才有")
    private String reason;

    @ApiModelProperty(value = "消息内容,json")
    private String payload;

    @ApiModelProperty(value = "定时发送时间")
    private Integer startTime;

    @ApiModelProperty(value = "定时发送过期时间")
    private Integer expireTime;

    @ApiModelProperty(value = "定时任务id")
    private String scheduleId;

    @ApiModelProperty(value = "创建时间")
    private Integer createTime;

    @ApiModelProperty(value = "更新时间")
    private Integer updateTime;

}
