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
@ApiModel(value = "旧版消息中心接口响应结果－版式3")
public class ResultStatusResponse<T> {

    @ApiModelProperty(value = "状态码", example = "0")
    private Integer result;

    @ApiModelProperty(value = "结果集", example = "无效的模板！")
    private T status;
}
