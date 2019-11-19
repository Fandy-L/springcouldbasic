package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author edmund.yu
 */
@Data
public class MessageIdDTO {

    @ApiModelProperty(value = "消息id",required = true)
    @NotBlank
    private String msgId;
}
