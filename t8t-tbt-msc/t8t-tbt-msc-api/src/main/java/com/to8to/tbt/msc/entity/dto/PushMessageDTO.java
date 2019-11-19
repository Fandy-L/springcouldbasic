package com.to8to.tbt.msc.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author pajero.quan
 */
@Data
public class PushMessageDTO {
    @ApiModelProperty(value = "标题", required = true, example = "住下来啦!这就是我的家")
    @NotBlank
    private String title;
    @ApiModelProperty(value = "描述", required = true, example = "住下来啦!这就是我的家")
    @NotBlank
    private String description;
    @ApiModelProperty(value = "提示文字，同描述", required = true, example = "住下来啦!这就是我的家")
    @NotBlank
    private String ticker;
    @ApiModelProperty(value = "是否开启MIUI和EMUI的厂商通道推送", example = "false")
    private String mipush;
    @ApiModelProperty(value = "跳转的页面路径,mipush为true生效", example = "com.umeng,message.example.MipushTestActivity")
    @JsonProperty(value = "mi_activity")
    private String mi_activity;
    @ApiModelProperty(value = "IOS角标数字", example = "1")
    private Integer badge;
    @ApiModelProperty(value = "点击事件行为")
    @Valid
    private PushClickDTO click;
}
