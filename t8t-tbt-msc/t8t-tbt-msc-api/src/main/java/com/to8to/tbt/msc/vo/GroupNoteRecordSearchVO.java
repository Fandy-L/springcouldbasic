package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "短信群发记录搜索结果")
public class GroupNoteRecordSearchVO {
    @ApiModelProperty(value = "结果集")
    public List<GroupNoteRecordWrapper.GroupNoteRecord> records;

    @ApiModelProperty(value = "总记录数")
    @JsonProperty(value = "total_records")
    public Long totalRecords;

    @ApiModelProperty(value = "总页码")
    @JsonProperty(value = "total_pages")
    public Integer totalPages;
}
