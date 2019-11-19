package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author edmund.yu
 */
@Data
public class ListTemplateVO {

    @ApiModelProperty(value = "返回模板结果")
    private List<MsgTemplateVO> msgTemplates;

    @ApiModelProperty(value = "总结果数")
    @JsonProperty(value = "total_records")
    private Long totalRecords;

    @ApiModelProperty(value = "总页数")
    @JsonProperty(value = "total_pages")
    private Long totalPages;
}
