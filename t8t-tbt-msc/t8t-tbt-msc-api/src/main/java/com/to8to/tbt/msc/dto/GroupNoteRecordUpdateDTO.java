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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新群发短信记录")
public class GroupNoteRecordUpdateDTO {
    @JsonProperty(value = "_id")
    private String id;

    @ApiModelProperty(value = "业主ID")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
