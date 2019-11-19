package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author juntao.guo
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "新增群发短信模板")
public class GroupNoteCreateDTO {
    @ApiModelProperty(value = "模板内容")
    public String content;

    @ApiModelProperty(value = "操作人")
    @JsonProperty(value = "rtxname")
    public String rtxName;

    @ApiModelProperty(value = "操作人所属部门")
    public String department;
}
