package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("查询短信及APP消息记录")
public class SearchMessageRecordDTO {
    @ApiModelProperty(value = "消息类型1-短信4-APP", example = "1")
    @Range(min = 1, max = 4, message = "不支持的消息类型1-短信4-APP")
    @JsonProperty(value = "send_type")
    private Integer sendType;

    @ApiModelProperty(value = "分页信息")
    private PageInfo pageInfo;

    @ApiModelProperty(value = "查询字段")
    private String field;

    @ApiModelProperty(value = "配置类型", example = "0")
    @JsonProperty(value = "config_type")
    private Integer configType;

    @ApiModelProperty(value = "分类ID", example = "0")
    private Integer cid;

    @ApiModelProperty(value = "手机号", example = "15627861270")
    private String phone;

    @ApiModelProperty(value = "手机号ID", example = "7138052")
    @JsonProperty(value = "phoneid")
    private Integer phoneId;

    @ApiModelProperty(value = "发送状态0-发送失败1-发送成功")
    @JsonProperty(value = "send_status")
    private Integer sendStatus;

    @ApiModelProperty(value = "渠道类型")
    @JsonProperty(value = "channel_type")
    private Integer channelType;

    @ApiModelProperty(value = "模板ID")
    private Integer tid;

    @ApiModelProperty(value = "模板ID批量")
    private List<Integer> tids;

    @ApiModelProperty(value = "开始时间")
    @JsonProperty(value = "start_time")
    private Integer startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonProperty(value = "end_time")
    private Integer endTime;

    @ApiModelProperty(value = "产品模块")
    @JsonProperty(value = "pm_module")
    private Integer pmModule;

    @ApiModelProperty(value = "发送对象", example = "1")
    @JsonProperty(value = "target_type")
    private Integer targetType;

    @ApiModelProperty(value = "是否自动发送", example = "1")
    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    @ApiModelProperty(value = "是否激活", example = "1")
    @JsonProperty(value = "is_active")
    private Integer isActive;

    @ApiModelProperty(value = "节点ID", example = "10")
    @JsonProperty(value = "nodeid")
    private Integer nodeId;

    @ApiModelProperty(value = "节点类型", example = "10")
    @JsonProperty(value = "node_id")
    private Integer nodeType;

    @ApiModelProperty(value = "节点类型批量", example = "[10]")
    @JsonProperty(value = "node_ids")
    private List<Integer> nodeIds;

    @ApiModelProperty(value = "应用ID", example = "5")
    @JsonProperty(value = "app_id")
    private Integer appId;

    @ApiModelProperty(value = "应用ID批量", example = "[5]")
    @JsonProperty(value = "app_ids")
    private List<Integer> appIds;

    @ApiModelProperty(value = "业主ID", example = "10000")
    private Integer uid;

    @ApiModelProperty(value = "业主ID批量", example = "[10000]")
    private List<Integer> uids;

    @ApiModelProperty(value = "是否已读")
    @JsonProperty(value = "is_read")
    private Integer isRead;

    @ApiModelProperty(value = "项目ID")
    private Integer yid;

    @ApiModelProperty(value = "项目ID批量", example = "[10]")
    private List<Integer> yids;

    @ApiModelProperty(value = "开始时间")
    private Integer stime;

    @ApiModelProperty(value = "结束时间")
    private Integer etime;
}
