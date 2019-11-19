package com.to8to.tbt.msc.vo;

import com.alibaba.fastjson.JSONObject;
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
@ApiModel(value = "API响应结果")
public class T8tTbtApiResultVO {
    @ApiModelProperty(value = "状态码")
    private Integer errorCode;

    @ApiModelProperty(value = "状态码描述")
    private String errorMsg;

    @ApiModelProperty(value = "记录集总数")
    private Integer allRows;

    @ApiModelProperty(value = "结果集")
    private Object data;
}
