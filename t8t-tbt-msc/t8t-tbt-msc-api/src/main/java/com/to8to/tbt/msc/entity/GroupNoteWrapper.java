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
public class GroupNoteWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "搜索群发短信模板结果项")
    public static class GroupNote{
        @JsonProperty(value = "_id")
        private String id;

        @JsonProperty(value = "id")
        private String attachedId;

        @ApiModelProperty(value = "内容")
        private String content;

        @ApiModelProperty(value = "成功数")
        @JsonProperty(value = "succ_num")
        private Integer succNum;

        @ApiModelProperty(value = "失败数")
        @JsonProperty(value = "fail_num")
        private Integer failNum;

        @ApiModelProperty(value = "创建时间")
        @JsonProperty(value = "create_time")
        private Long createTime;

        @ApiModelProperty(value = "发送时间")
        @JsonProperty(value = "send_time")
        private Long sendTime;

        @ApiModelProperty(value = "添加人")
        @JsonProperty(value = "rtxname")
        private String rtxName;

        @ApiModelProperty(value = "添加人所属部门")
        private String department;
    }
}
