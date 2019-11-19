package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class GroupNoteRecordWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "短信群发记录搜索结果项")
    public static class GroupNoteRecord{
        @JsonProperty(value = "_id")
        private String id;

        @JsonProperty(value = "id")
        private String attachedId;

        @ApiModelProperty(value = "业主ID")
        private String name;

        @ApiModelProperty(value = "手机号")
        private String phone;

        @ApiModelProperty(value = "手机号ID")
        @JsonProperty(value = "phoneid")
        private String phoneId;

        @ApiModelProperty(value = "模板ID")
        @JsonProperty(value = "group_note_id")
        private String groupNoteId;

        @ApiModelProperty(value = "发送时间")
        @JsonProperty(value = "send_time")
        private Long sendTime;

        @ApiModelProperty(value = "发送状态")
        private Integer status;
    }
}
