package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "发送微信告警消息")
public class SendWeChatAlarmMsgDTO {

    @ApiModelProperty(value = "消息内容", example = "测试告警消息")
    @NotBlank(message = "请输入消息内容")
    private String content;
}
