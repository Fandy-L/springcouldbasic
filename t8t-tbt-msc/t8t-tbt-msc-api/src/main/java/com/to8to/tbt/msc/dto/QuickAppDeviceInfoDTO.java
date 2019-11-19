package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author edmund.yu
 */
@Data
public class QuickAppDeviceInfoDTO {

    @ApiModelProperty(value = "设备唯一标识",required = true)
    @NotBlank(message = "设备唯一标识deviceId不能为空！")
    private String deviceId;

    @ApiModelProperty(value = "注册id",example = "AAWWHI94sgUR2RU5_P1ZptUiwLq7W8XWJO2LxaAPuXw4_HOJFXnBlN-q5_3bwlxVW_SHeDPx_s5bWW-9DjtWZsvcm9CwXe1FHJg0u-D2pcQPcb3sTxDTJeiwEb9WBPl_9w",required = true)
    @NotBlank(message = "注册id不能为空！")
    private String regId;

    @ApiModelProperty(value = "厂商，华为-2，小米-3，OPPO-4，VIVO-5，魅族-6",example = "1",required = true)
    @NotNull(message = "厂商标识不能为空！")
    private Integer provider;

    @ApiModelProperty(value = "应用版本",required = true)
    @NotNull(message = "应用版本号不能为空！")
    private String version;
}
