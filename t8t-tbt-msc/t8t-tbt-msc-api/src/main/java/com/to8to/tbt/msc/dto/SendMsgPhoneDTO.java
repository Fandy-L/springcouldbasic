package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "根据手机号发送短信")
public class SendMsgPhoneDTO {
    @NotEmpty(message = "电话号码不可为空")
    @Length(min = 1, message = "电话号码不可为空")
    @ApiModelProperty(value = "手机号串")
    private String phones;

    @NotEmpty(message = "内容不可为空")
    @Length(min = 1, message="内容不可为空")
    @ApiModelProperty(value = "短信内容")
    private String content;

    @ApiModelProperty(value = "发送渠道")
    private Integer channel;

    @ApiModelProperty(value = "城市ID")
    @JsonProperty(value = "cityid")
    private Integer cityId;

    @ApiModelProperty(value = "业务ID")
    private Integer ywId;

    @ApiModelProperty(value = "用户类型")
    @JsonProperty(value = "user_type")
    private Integer userType;

    @ApiModelProperty(value = "是否阻塞模式0-是1-否")
    private Integer block;

    @ApiModelProperty(value = "手机号ID串")
    private String phoneIds;

    private Integer zid;

    @JsonProperty(value = "ispass")
    private Integer isPass;

    private Integer ywType;
}
