package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.GroupNoteWrapper;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "搜索群发短信模板结果")
public class GroupNoteSearchVO {
    public List<GroupNoteWrapper.GroupNote> groupNotes;

    @JsonProperty(value = "total_records")
    public Long totalRecords;

    @JsonProperty(value = "total_pages")
    public Long totalPages;
}
