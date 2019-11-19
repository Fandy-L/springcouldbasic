package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "搜索配置项结果")
public class ConfigureSearchVO {
    private Integer cid;

    @ApiModelProperty(value = "节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块")
    @JsonProperty(value = "config_type")
    private Integer configType;

    @ApiModelProperty(value = "节点描述")
    @JsonProperty(value = "config_describe")
    private String configDescribe;

    @ApiModelProperty(value = "父节点ID")
    @JsonProperty(value = "father_id")
    private Integer fatherId;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(value = "create_time")
    private Integer createTime;

    @ApiModelProperty(value = "创建人ID")
    @JsonProperty(value = "create_id")
    private Integer createId;

    @ApiModelProperty(value = "修改人ID")
    @JsonProperty(value = "modify_id")
    private Integer modifyId;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty(value = "modify_time")
    private Integer modifyTime;

    private String nick;
}
