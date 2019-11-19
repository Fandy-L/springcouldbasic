package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.SearchTime;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "搜索群发短信模板")
public class GroupNoteSearchDTO {
    public PageInfo pageInfo;

    public SearchTime searchTime;

    @JsonProperty(value = "time_type")
    private Integer timeType;

    private String content;
}
