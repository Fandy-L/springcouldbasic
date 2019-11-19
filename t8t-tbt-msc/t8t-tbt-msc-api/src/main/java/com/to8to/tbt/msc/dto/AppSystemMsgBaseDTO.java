package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author juntao.guo
 */
@Data
public class AppSystemMsgBaseDTO extends AppBaseDTO{
    @ApiModelProperty(value = "应用ID 15-土巴兔", example = "15")
    @JsonProperty(value = "appid")
    private Integer appId;

    @ApiModelProperty(value = "设备标识", example = "fa3ee6d983a72d101b575ccd19ba5344")
    @JsonProperty(value = "first_id")
    private String firstId;

    @ApiModelProperty(value = "设备类型 1-android 2-ios", example = "1")
    @JsonProperty(value = "appostype")
    private Integer appOsType;

    @ApiModelProperty(value = "城市ID", example = "1130")
    private Integer cityId;

    @ApiModelProperty(value = "版本号", example = "7.7.1")
    @JsonProperty(value = "appversion")
    private String appVersion;

    @ApiModelProperty(value = "业主ID", example = "172176021")
    private Integer uid;
}
