package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.ConfigureWrapper;
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
@ApiModel(value = "短信及APP模板搜索结果集")
public class TemplateSearchVO {
    @JsonProperty(value = "create_id")
    private Integer createId;

    private String nick;

    @JsonProperty(value = "father_id")
    private Integer fatherId;

    @JsonProperty(value = "bigtype")
    private ConfigureWrapper.Configure bigType;

    @JsonProperty(value = "smalltype")
    private ConfigureWrapper.Configure smallType;
}
