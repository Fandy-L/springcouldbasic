package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

/**
 * @author edmund.yu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "配置项参数")
public class ConfigureDTO {

    @ApiModelProperty(value = "cid")
    private Integer cid;

    @ApiModelProperty(value = "节点类型")
    @Range(min = 1,max = 5)
    private Integer configType;

    @ApiModelProperty(value = "节点描述")
    private String configDescribe;

    @ApiModelProperty(value = "父节点ID")
    private Integer fatherId;

    @ApiModelProperty(value = "创建时间")
    private Integer creatTime;

    @ApiModelProperty(value = "创建人ID")
    private Integer creatId;

    @ApiModelProperty(value = "修改人ID")
    private Integer modifyId;

    @ApiModelProperty(value = "修改时间")
    private Integer modifyTime;
}
