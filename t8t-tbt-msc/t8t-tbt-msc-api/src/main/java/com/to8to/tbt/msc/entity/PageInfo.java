package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "分页信息")
public class PageInfo {
    @ApiModelProperty(value = "当前页", example = "1")
    @Min(value = 1)
    @JsonProperty(value = "curr_page")
    private Integer currPage;

    @ApiModelProperty(value = "每页加载的条数", example = "10")
    @Min(value = 1)
    @JsonProperty(value = "page_size")
    private Integer pageSize;
}
