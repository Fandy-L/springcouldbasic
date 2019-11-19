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
@ApiModel(value = "修改配置项")
public class ConfigureUpdateDTO {

    @ApiModelProperty(value = "节点ID")
    @NotNull(message = "更新节点ID不合法！")
    @Min(value = 1, message = "更新节点ID不合法！")
    private Integer cid;

    @ApiModelProperty(value = "节点描述")
    @JsonProperty(value = "config_describe")
    @NotBlank(message = "请填写节点描述")
    private String configDescribe;

    @ApiModelProperty(value = "父节点ID")
    @JsonProperty(value = "father_id")
    @NotNull(message = "请选择父节点")
    @Min(value = 0)
    private Integer fatherId;
}
