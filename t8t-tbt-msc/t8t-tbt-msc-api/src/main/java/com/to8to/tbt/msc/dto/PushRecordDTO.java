package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author edmund.yu
 */
@Data
public class PushRecordDTO {

    @ApiModelProperty(value = "自定义消息id",required = true)
    @NotBlank
    private String msgId;

    @ApiModelProperty(value = "厂商，华为-2，小米-3，OPPO-4，VIVO-5，魅族-6",example = "1",required = true)
    @NotNull
    private Integer provider;
}
