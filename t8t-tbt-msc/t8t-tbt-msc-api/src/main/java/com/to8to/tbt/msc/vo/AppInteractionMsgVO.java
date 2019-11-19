package com.to8to.tbt.msc.vo;

import com.to8to.tbt.msc.entity.AppMsgWrapper;
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
@ApiModel(value = "APP互动消息列表结果集")
public class AppInteractionMsgVO {
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "认证类型", example = "0")
    private Integer identificationType;

    @ApiModelProperty(value = "认证状态", example = "0")
    private Integer identificationStatus;

    @ApiModelProperty(value = "认证时间", example = "0")
    private Integer identificationTime;

    @ApiModelProperty(value = "认证描述")
    private String identificationDesc;

    @ApiModelProperty(value = "认证图标")
    private String identificationPic;

    @ApiModelProperty(value = "消息ID", example = "1")
    private Integer msgId;

    @ApiModelProperty(value = "消息标题", example = "赞了您的日记")
    private String msgAction;

    @ApiModelProperty(value = "消息描述", example = "赞了您的日记")
    private String msgDesc;

    @ApiModelProperty(value = "发送时间")
    private Integer msgTime;

    @ApiModelProperty(value = "是否已读", example = "true")
    private Boolean isRead;

    @ApiModelProperty(value = "内容类型", example = "diary")
    private String contentType;

    @ApiModelProperty(value = "原始内容描述")
    private String originContentDesc;

    @ApiModelProperty(value = "原始图片URL")
    private String originContentPic;

    @ApiModelProperty(value = "消息跳转参数")
    private AppMsgWrapper.MsgJumpParams msgJumpParams;

    @ApiModelProperty(value = "评论内容详情")
    private AppMsgWrapper.MsgContent msgContent;

    @ApiModelProperty(value = "跳转协议")
    private String schemeUrl;
}
