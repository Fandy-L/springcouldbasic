package com.to8to.tbt.msc.dto;

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
@ApiModel(value = "验证码校验并生成凭证")
public class VerificationCodeCheckDTO {
    @ApiModelProperty(value = "平台", example = "t8t-tbt-msc")
    private String platform;

    @ApiModelProperty(value = "验证码生成类型说明：短信-1、邮件-2、微信-3、APP消息-4、PC消息-5、其他-0", example = "1")
    private Integer generateType;

    @ApiModelProperty(value = "验证码功能类型说明：注册-1、登录-2、绑定-3、解绑-4、修改密码-5、其他 -0", example = "2")
    private Integer codeType;

    @ApiModelProperty(value = "唯一标识对象如：\"15926214373\"、\"diaoge@corp.to8to.com\"", example = "15824909673")
    private String uniqueObject;

    @ApiModelProperty(value = "验证码", example = "167099")
    private Integer code;

    @ApiModelProperty(value = "验证码验证通过凭证失效时间", example = "300")
    private Integer certificateExpiresTime;
}
