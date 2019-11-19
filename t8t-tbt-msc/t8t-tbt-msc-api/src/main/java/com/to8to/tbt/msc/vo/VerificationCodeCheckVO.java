package com.to8to.tbt.msc.vo;

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
@ApiModel(value = "验证码校验结果")
public class VerificationCodeCheckVO {
    @ApiModelProperty(value = "凭证")
    private String certificate;

    @ApiModelProperty(value = "状态码")
    private Integer status;
}
