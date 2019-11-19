package com.to8to.tbt.msc.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author juntao.guo
 */
@Valid
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "群发短信")
public class SendSmsBatchDTO {
    @ApiModelProperty(value = "模板ID")
    private Integer tid;

    @ApiModelProperty(value = "模板关键词")
    private JSONObject keyword;

    @NotEmpty(message = "电话id为空！")
    @Length(min = 1, message = "电话id为空！")
    @ApiModelProperty(value = "电话号码ID串")
    @JsonProperty(value = "phoneids")
    private String phoneIds;
}
