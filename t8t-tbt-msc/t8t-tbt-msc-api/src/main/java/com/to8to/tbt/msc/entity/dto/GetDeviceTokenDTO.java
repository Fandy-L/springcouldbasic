package com.to8to.tbt.msc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author pajero.quan
 */
@Data
public class GetDeviceTokenDTO {
    @ApiModelProperty(name = "应用id", required = true)
    @NotNull
    @Min(1)
    private Integer appId;

    @ApiModelProperty(name = "通道")
    @NotNull
    @Min(0)
    private Integer channel;

    @ApiModelProperty(name = "系统平台")
    @NotNull
    @Min(0)
    private Integer platform;

    @ApiModelProperty(name = "用户id")
    private Integer uid;

    @ApiModelProperty(name = "土巴兔设备id")
    private String firstId;

}
