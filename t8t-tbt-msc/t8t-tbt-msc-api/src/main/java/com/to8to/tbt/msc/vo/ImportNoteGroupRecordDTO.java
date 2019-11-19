package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author juntao.guo
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "导入群发短信记录")
public class ImportNoteGroupRecordDTO {
    @ApiModelProperty(value = "业主ID")
    private String name;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "1[\\d]{10}",message = "手机号码格式不正确")
    private String phone;

    @ApiModelProperty(value = "手机号ID")
    @JsonProperty(value = "phoneid")
    private String phoneId;
}
