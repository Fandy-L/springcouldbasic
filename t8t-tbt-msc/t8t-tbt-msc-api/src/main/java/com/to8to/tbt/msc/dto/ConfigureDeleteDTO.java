package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "删除配置项")
public class ConfigureDeleteDTO {

    @ApiModelProperty(value = "节点ID")
    @NotNull(message = "节点删除失败，节点ID不合法！")
    @Min(value = 1, message = "节点删除失败，节点ID不合法！")
    private Integer cid;

    @JsonProperty(value = "config_type")
    private Integer configType;
}
