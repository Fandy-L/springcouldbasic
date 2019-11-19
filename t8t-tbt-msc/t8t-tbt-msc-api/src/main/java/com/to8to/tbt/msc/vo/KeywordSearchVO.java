package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@Data
@Valid
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "关键词搜索结果")
public class KeywordSearchVO {
    private Integer kid;

    private String keyword;

    @JsonProperty(value = "create_time")
    private Integer createTime;

    @JsonProperty(value = "create_id")
    private Integer createId;
}
