package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
public class MsgTemplateVO {

    @ApiModelProperty(value = "_id")
    @JsonProperty(value = "_id")
    private String id;

    @ApiModelProperty(value = "id")
    @JsonProperty(value = "id")
    private String nickId;

    @ApiModelProperty(value = "发送节点")
    @JsonProperty(value = "msg_node")
    private String msgNode;

    @ApiModelProperty(value = "节点分类")
    @JsonProperty(value = "node_category")
    private Integer nodeCategory;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "链接")
    private String link;

    @ApiModelProperty(value = "发送对象")
    @JsonProperty(value = "to_user_type")
    private String toUserType;

    @ApiModelProperty(value = "发送方式")
    @JsonProperty(value = "send_type")
    private String sendType;

    @ApiModelProperty(value = "落地情况")
    @JsonProperty(value = "isground")
    private Integer isGround;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(value = "create_time")
    private Long createTime;

    @ApiModelProperty(value = "内容关键字ID")
    @JsonProperty(value = "word__ids")
    private String wordIds;

    @ApiModelProperty(value = "是否启用")
    private Integer status;

    @ApiModelProperty(value = "小分类")
    @JsonProperty(value = "small_category")
    private Integer smallCategory;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "链接关键字ID")
    @JsonProperty(value = "url_param_ids")
    private String urlParamIds;

    @ApiModelProperty(value = "标题关键字ID")
    @JsonProperty(value = "title_param_ids")
    private String titleParamIds;

}
