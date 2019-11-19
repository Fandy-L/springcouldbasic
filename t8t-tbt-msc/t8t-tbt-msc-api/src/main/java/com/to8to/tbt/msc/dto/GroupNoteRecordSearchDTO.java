package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.SearchTime;
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
@ApiModel(value = "群发短信记录搜索")
public class GroupNoteRecordSearchDTO {
    @ApiModelProperty(value = "分页信息")
    private PageInfo pageInfo;

    @ApiModelProperty(value = "时间筛选范围")
    private SearchTime searchTime;

    @ApiModelProperty(value = "发送状态")
    @JsonProperty(value = "send_status")
    private Integer sendStatus;

    @ApiModelProperty(value = "发送类型")
    @JsonProperty(value = "search_type")
    private Integer searchType;

    @ApiModelProperty(value = "短信内容")
    private String content;

    @ApiModelProperty(value = "短信模板ID")
    @JsonProperty(value = "group_note_id")
    private String groupNoteId;
}
