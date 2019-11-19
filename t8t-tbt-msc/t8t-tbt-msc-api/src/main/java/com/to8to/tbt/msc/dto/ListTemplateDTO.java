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
import org.hibernate.validator.constraints.Range;

/**
 * @author edmund.yu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询消息列表参数")
public class ListTemplateDTO {

    @ApiModelProperty(value = "_id",example = "548570e0dc59bf4b677c9b90")
    @JsonProperty(value = "template_id")
    private String id;

    @ApiModelProperty(value = "短信内容")
    private String content;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "发送节点")
    @JsonProperty(value = "msg_node")
    private String msgNode;

    @ApiModelProperty(value = "小分类",example = "101")
    @JsonProperty(value = "small_category")
    private Integer smallCategory;

    @ApiModelProperty(value = "发送对象",example = "1")
    @JsonProperty(value = "to_user_type")
    private String toUserType;

    @ApiModelProperty(value = "发送方式",example = "1")
    @JsonProperty("send_type")
    private String sendType;

    @ApiModelProperty(value = "落地情况")
    @JsonProperty(value = "isground")
    @Range(min = 0,max = 3)
    private Integer isGround;

    @ApiModelProperty(value = "页面信息")
    private PageInfo pageInfo;

    @ApiModelProperty(value = "查询起止时间")
    private SearchTime searchTime;

}
