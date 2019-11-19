package com.to8to.tbt.msc.entity.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
@Builder
public class MsgResult {

    @ApiModelProperty(value = "结果数")
    private Long result;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "失败")
    private String fail;
}
