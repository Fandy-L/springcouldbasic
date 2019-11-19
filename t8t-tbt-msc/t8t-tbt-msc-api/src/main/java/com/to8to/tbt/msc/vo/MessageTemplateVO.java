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
@ApiModel(value = "短信模板")
public class MessageTemplateVO {
    @ApiModelProperty(value = "主键ID", example = "1")
    @JsonProperty(value = "mtid")
    private Integer id;

    @ApiModelProperty(value = "所属公共模板id")
    private Integer tid;

    @ApiModelProperty(value = "短信内容")
    @JsonProperty(value = "msg_content")
    private String msgContent;

    @ApiModelProperty(value = "落地情况 1 落地 2非落地")
    @JsonProperty(value = "is_ground")
    private Integer isGround;

    @ApiModelProperty(name = "通道类型")
    @JsonProperty(value = "channel_type")
    private Integer channelType;

    @ApiModelProperty(value = "是否需要ip发送量限制:0否,1-是")
    @JsonProperty(value = "need_ip_limit")
    private Integer needIpLimit;

    @ApiModelProperty(value = "ip发送限制数量")
    @JsonProperty(value = "ip_limit_num")
    private Integer ipLimitNum;

    @ApiModelProperty(value = "城市ID")
    @JsonProperty(value = "city_ids")
    private String cityIds;
}
