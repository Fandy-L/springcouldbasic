package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author edmund.yu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息模板参数")
public class MsgTemplateDTO {

    @ApiModelProperty(value = "_id",example = "548570e0dc59bf4b677c9b90")
    @JsonProperty(value = "_id")
    private String id;

    @ApiModelProperty(value = "发送节点")
    @JsonProperty(value = "msg_node")
    private String msgNode;

    @ApiModelProperty(value = "节点分类",example = "2")
    @JsonProperty(value = "node_category")
    private Integer nodeCategory;

    @ApiModelProperty(value = "标题",required = true)
    @NotNull
    private String title;

    @ApiModelProperty(value = "短信内容",required = true)
    @NotNull
    private String content;

    @ApiModelProperty(value = "链接",example = "",required = true)
    @NotNull
    private String link;

    @ApiModelProperty(value = "发送对象",example = "1",required = true)
    @JsonProperty(value = "to_user_type")
    @NotNull
    private String toUserType;

    @ApiModelProperty(value = "发送方式",example = "1",required = true)
    @JsonProperty(value = "send_type")
    @NotNull
    private String sendType;

    @ApiModelProperty(value = "落地情况",example = "1")
    @JsonProperty(value = "isground")
    private Integer isGround;

    @ApiModelProperty(value = "状态",example = "1")
    private Integer status;

    @ApiModelProperty(value = "优先级",example = "1")
    private Integer priority;

    @ApiModelProperty(value = "小分类",example = "101")
    @JsonProperty(value = "small_category")
    private Integer smallCategory;

}