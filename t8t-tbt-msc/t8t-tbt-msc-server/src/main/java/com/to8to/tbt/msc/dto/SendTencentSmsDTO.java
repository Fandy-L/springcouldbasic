package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.SmsWrapper;
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
@ApiModel(value = "发送腾讯短信的请求体")
public class SendTencentSmsDTO {

    @ApiModelProperty(value = "手机号")
    private SmsWrapper.TencentSmsTel tel;

    @ApiModelProperty(value = "业务类型")
    private String type;

    @ApiModelProperty(value = "短信内容")
    private String msg;

    @ApiModelProperty(value = "签名")
    private String sig;

    @ApiModelProperty(value = "扩展数据")
    private String extend;

    @ApiModelProperty(value = "扩展数据")
    private String ext;
}
