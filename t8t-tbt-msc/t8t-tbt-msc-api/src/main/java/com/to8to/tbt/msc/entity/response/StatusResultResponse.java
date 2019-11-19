package com.to8to.tbt.msc.entity.response;

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
@ApiModel(value = "旧版消息中心接口响应结果－版式1")
public class StatusResultResponse<T> {

    @ApiModelProperty(value = "状态码", example = "200")
    private Integer status;

    @ApiModelProperty(value = "数据集")
    private T result;
}
