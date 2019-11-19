package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "搜索配置项")
public class ConfigureSearchDTO {
    @JsonProperty(value = "config_type")
    private Integer configType;

    @JsonProperty(value = "father_id")
    @Min(value = 0)
    private Integer fatherId;

    @JsonProperty(value = "father_ids")
    private String fatherIds;
}
