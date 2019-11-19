package com.to8to.tbt.msc.vo;

import com.to8to.tbt.msc.entity.AppMsgWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统消息结果项")
public class AppSystemMsgVO {
    @ApiModelProperty(value = "展示类型2-群发消息")
    private Integer showType;

    @ApiModelProperty(value = "消息ID")
    private String msgId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "封面图")
    private String imageUrl;

    @ApiModelProperty(value = "是否分组消息")
    private Boolean isGroupMsg;

    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    @ApiModelProperty(value = "时间戳")
    private Integer time;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否需要跳转")
    private Boolean jumpFlag;

    @ApiModelProperty(value = "跳转参数")
    private AppMsgWrapper.MsgJumpParams msgJumpParams;

    @ApiModelProperty(value = "跳转协议")
    private String schemeUrl;
}
