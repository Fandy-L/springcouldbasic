package com.to8to.tbt.msc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 15:10
 */
@Data
public class UpdateDeviceTokenDTO {
    @ApiModelProperty(name = "设备token",required = true)
    @NotEmpty
    private String deviceToken;

    @ApiModelProperty(name = "应用id",required = true)
    @NotNull
    @Min(1)
    private Integer pushAppId;

    @ApiModelProperty(name = "通道",required = true)
    @NotNull
    @Min(0)
    private Byte channel;

    @ApiModelProperty(name = "用户id")
    @NotNull
    @Min(0)
    private Integer uid;

    @ApiModelProperty(name = "系统平台",required = true)
    @NotNull
    @Min(0)
    private Byte platform;

    @ApiModelProperty(name = "土巴兔设备id")
    private String firstId;

    @ApiModelProperty(name = "版本号")
    private String version;

}
