package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author edmund.yu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PC消息")
public class PcMsgRequestDTO {

    @ApiModelProperty(value = "节点ID",example = "101")
    @JsonProperty(value = "note_id")
    private String nodeId;

    @ApiModelProperty(value = "发送方ID")
    @JsonProperty(value = "from_userid")
    private String fromUserId;

    @ApiModelProperty(value = "接收方ID")
    @JsonProperty(value = "to_userid")
    private String toUserId;

    @ApiModelProperty(value = "接收方类型")
    @JsonProperty(value = "to_user_type")
    private Integer toUserType;

    @ApiModelProperty(value = "发送方式")
    @JsonProperty(value = "send_type")
    private Integer sendType;

    @ApiModelProperty(value = "内容参数")
    private Map<String,String> params;

    @ApiModelProperty(value = "链接参数")
    @JsonProperty(value = "url_params")
    private Map<String,String> urlParams;

    @ApiModelProperty(value = "标题参数")
    @JsonProperty(value = "title_params")
    private Map<String,String> titleParams;

    @ApiModelProperty(value = "联络参数")
    @JsonProperty(value = "contact_params")
    private Map<String,String> contactParams;

    @ApiModelProperty(value = "额外数据参数")
    @JsonProperty(value = "extra_data_params")
    private Map<String,String> extraDataParams;

    @ApiModelProperty(value = "等级")
    private Integer level;
}
