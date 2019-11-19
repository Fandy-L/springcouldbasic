package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "APP消息模板")
public class AppTemplateVO {
    private Integer id;

    @ApiModelProperty(value = "公共模板id")
    private Integer tid;

    @ApiModelProperty(value = "app消息内容")
    @JsonProperty(value = "app_content")
    private String appContent;

    @ApiModelProperty(value = "app类型id")
    @JsonProperty(value = "app_id")
    private Integer appId;

    @ApiModelProperty(value = "是否需要推送:0是，1否")
    @JsonProperty(value = "need_push")
    private Integer needPush;

    @JsonProperty(value = "create_time")
    private Integer createTime;

    @ApiModelProperty(value = "推送方式:1-通知,2-透传")
    @JsonProperty(value = "push_type")
    private Integer pushType;

    @ApiModelProperty(value = "推送集体:1-全部，2-单个")
    @JsonProperty(value = "push_scope")
    private Integer pushScope;
}
