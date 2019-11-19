package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@ApiModel(value = "根据参数名列表获取配置信息结果")
public class OperateConfigGetVO {

    @ApiModelProperty(value = "短信通道")
    @JsonProperty(value = "sms_route")
    private String smsRoute;

    @ApiModelProperty(value = "短信重复次数限制")
    @JsonProperty(value = "msgc_repeat_limit")
    private String msgRepeatLimit;

    @ApiModelProperty(value = "短信模板发送次数限制")
    @JsonProperty(value = "msgc_repeat_tid_limit")
    private String msgRepeatTidLimit;

    @ApiModelProperty(value = "群组短信统计限制")
    @JsonProperty(value = "groupSendNote_count_limit")
    private String groupSendNoteCountLimit;

    @ApiModelProperty(value = "群组短信统计限制白名单用户")
    @JsonProperty(value = "groupSendNote_nolimit_user")
    private String groupSendNoteNoLimitUser;

    @ApiModelProperty(value = "群发短信的上限")
    @JsonProperty(value = "sms_batch_size")
    private Integer smsBatchSize;
}
