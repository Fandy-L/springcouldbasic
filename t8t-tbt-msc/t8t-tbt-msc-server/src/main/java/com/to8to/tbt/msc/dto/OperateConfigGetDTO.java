package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "根据参数名列表获取配置信息")
public class OperateConfigGetDTO {
    @ApiModelProperty(value = "参数名列表")
    private List<String> names;
}
