package com.to8to.tbt.msc.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pajero.quan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("设备token")
public class DeviceTokenVO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "应用id")
    private Integer pushAppId;
    @ApiModelProperty(value = "用户id")
    private Integer uid;
    @ApiModelProperty(value = "设备token")
    private String deviceToken;
    @ApiModelProperty(value = "通道")
    private Byte channel;
    @ApiModelProperty(value = "系统平台")
    private Byte platform;
    @ApiModelProperty(value = "firstId")
    private String firstId;
    @ApiModelProperty(value = "版本号")
    private String version;
    @ApiModelProperty(value = "是否删除")
    private Byte deleted;
    @ApiModelProperty(value = "创建时间")
    private Integer createTime;
    @ApiModelProperty(value = "更新时间")
    private Integer updateTime;
}
