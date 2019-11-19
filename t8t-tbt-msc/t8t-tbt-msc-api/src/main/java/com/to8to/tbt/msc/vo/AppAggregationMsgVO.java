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
@ApiModel(value = "APP聚合消息结果")
public class AppAggregationMsgVO {
    @ApiModelProperty(value = "系统消息")
    private AppMsgWrapper.AggregationMsgItem systemMsg;

    @ApiModelProperty(value = "互动消息")
    private AppMsgWrapper.AggregationMsgItem interactiveMsg;

    @ApiModelProperty(value = "关注消息")
    private AppMsgWrapper.AggregationMsgItem followMsg;

    @ApiModelProperty(value = "装修进度")
    private AppMsgWrapper.AggregationMsgItem progressMsg;
}
