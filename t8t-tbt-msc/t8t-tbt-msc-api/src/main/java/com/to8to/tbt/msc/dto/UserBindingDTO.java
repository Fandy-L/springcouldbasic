package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yason.li
 */
@Data
@ApiModel(value = "添加用户设备绑定-根据用户信息")
public class UserBindingDTO {
    @ApiModelProperty(value = "APP应用ID", example = "15")
    @Min(1)
    @NotNull
    private Integer appId;

    @ApiModelProperty(value = "手机在每个应用中的唯一 token", example = "190e35f7e024a6963d0")
    @JsonProperty(value = "deviceid")
    @NotBlank
    private String deviceId;

    @ApiModelProperty(value = "第三方推送平台： 0-友盟 ，1-小米， 2-华为 ，3-魅族，4-极光，5-OPPO, 6-VIVO，7-APPLE", example = "4")
    @JsonProperty(value = "platform")
    @NotNull
    @Range(min = 0, max = 7)
    private Integer platform;

    @ApiModelProperty(value = "用户UID(装修公司ID) ，(退出用户 Uid 传0， 删除设备号;)", example = "11177666")
    @JsonProperty(value = "uid")
    @NotNull
    @Min(0)
    private Integer uid;

    @ApiModelProperty(value = "子用 户UID(登录用户id)", example = "0")
    @JsonProperty(value = "subUid")
    private Integer uidSub;

    @ApiModelProperty(value = "手机类型 ：默认 0-安卓 ， 1-IOS", example = "0")
    @JsonProperty(value = "type")
    @NotNull
    @Range(min = 0,max = 1)
    private Integer type;

    @ApiModelProperty(value = "App版本号", example = "7.9.1")
    private String appVersion;

    @ApiModelProperty(value = "土巴兔终端设备id(天眼生成)", example = "0302cbc8933c2359b4d6651c93040ecb")
    private String firstId;
}
