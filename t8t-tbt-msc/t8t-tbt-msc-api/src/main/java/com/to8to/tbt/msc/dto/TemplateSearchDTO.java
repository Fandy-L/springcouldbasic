package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "短信及APP模板搜索")
public class TemplateSearchDTO {
    @JsonProperty(value = "config_type")
    private Integer configType;

    private Integer cid;

    @JsonProperty(value = "send_type")
    private Integer sendType;

    @JsonProperty(value = "mtid")
    private Integer messageTid;

    @JsonProperty(value = "channel_type")
    private Integer channelType;

    @JsonProperty(value = "is_ground")
    private Integer isGround;

    private Long cityId;

    @JsonProperty(value = "app_id")
    private Integer appId;

    private Integer tid;

    @JsonProperty(value = "pm_module")
    private Integer pmModule;

    @JsonProperty(value = "target_type")
    private Integer targetType;

    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    @JsonProperty(value = "is_active")
    private Integer isActive;

    @JsonProperty(value = "nodeid")
    private Integer nodeId;

    @JsonProperty(value = "stime")
    private Integer startTime;

    @JsonProperty(value = "etime")
    private Integer endTime;

    private PageInfo pageInfo;
}
