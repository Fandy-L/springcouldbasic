package com.to8to.tbt.msc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "兼容老服务的接口返回")
public class OldMsgResponse {
    @ApiModelProperty(value = "状态码")
    private Integer status;

    @ApiModelProperty(value = "状态码描述")
    private String msg;
}