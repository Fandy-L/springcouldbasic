package com.to8to.tbt.msc.entity;

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
@Builder
@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询起止时间")
public class SearchTime {
    @ApiModelProperty(value = "开始时间", example = "1565158811")
    @JsonProperty(value = "start_time")
    private Long startTime;

    @ApiModelProperty(value = "结束时间", example = "1565158811")
    @JsonProperty(value = "end_time")
    private Long endTime;
}
