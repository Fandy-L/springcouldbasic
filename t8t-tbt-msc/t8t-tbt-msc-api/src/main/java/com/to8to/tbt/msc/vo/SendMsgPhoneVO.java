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
@ApiModel(value = "根据手机号发送短信结果")
public class SendMsgPhoneVO {

    @ApiModelProperty(value = "已发送数量")
    private Integer send;

    @ApiModelProperty(value = "未发送数量")
    @JsonProperty(value = "unsend")
    private Integer unSend;

    @ApiModelProperty(value = "状态码")
    private Integer status;

    @ApiModelProperty(value = "状态码描述")
    private String msg;
}
