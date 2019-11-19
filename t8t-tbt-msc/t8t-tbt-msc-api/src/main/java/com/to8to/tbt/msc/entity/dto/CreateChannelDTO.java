package com.to8to.tbt.msc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 15:13
 */
@Data
public class CreateChannelDTO {
    @ApiModelProperty(value = "推送应用id",required = true)
    @NotNull
    @Min(1)
    private Integer pushAppId;

    @ApiModelProperty(value = "通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5;魅族：6",required = true)
    @NotNull
    @Min(0)
    private Byte channel;

    @ApiModelProperty(value = "通道AppId")
    private String appId;

    @ApiModelProperty(value = "通道AppKey")
    private String appKey;

    @ApiModelProperty(value = "通道AppSecret")
    private String appSecret;

    @ApiModelProperty(value = "通道masterSecret")
    private String masterSecret;

    @ApiModelProperty(value = "是否启用")
    @NotNull
    private Boolean enable;
}
