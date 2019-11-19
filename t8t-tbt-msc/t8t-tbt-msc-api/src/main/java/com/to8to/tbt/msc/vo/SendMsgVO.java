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
@ApiModel(value = "旧版综合消息发送结果集")
public class SendMsgVO {
    @ApiModelProperty(value = "状态码")
    public int code;

    @ApiModelProperty(value = "内容")
    public String content;

    @ApiModelProperty(value = "成功数")
    @JsonProperty(value = "succ_num")
    public int succNum;

    @ApiModelProperty(value = "失败数")
    @JsonProperty(value = "fail_num")
    public int failNum;
}
