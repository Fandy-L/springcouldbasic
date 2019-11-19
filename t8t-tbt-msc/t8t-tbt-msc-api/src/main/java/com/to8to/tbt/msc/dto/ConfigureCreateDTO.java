package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "新增配置项")
public class ConfigureCreateDTO {
    @ApiModelProperty(value = "节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块")
    @JsonProperty(value = "config_type")
    @NotNull(message = "请选择节点类型")
    @Range(min = 1,max = 5)
    private Integer configType;

    @ApiModelProperty(value = "节点描述")
    @JsonProperty(value = "config_describe")
    @NotBlank(message = "请填写节点描述")
    private String configDescribe;

    @ApiModelProperty(value = "父节点ID")
    @JsonProperty(value = "father_id")
    @NotNull(message = "请选择父节点")
    @Min(value = 0)
    private Integer fatherId;

    @ApiModelProperty(value = "创建人ID")
    @JsonProperty(value = "create_id")
    @NotNull(message = "请填写创建人")
    @Min(value = 0)
    private Integer createId;
}
