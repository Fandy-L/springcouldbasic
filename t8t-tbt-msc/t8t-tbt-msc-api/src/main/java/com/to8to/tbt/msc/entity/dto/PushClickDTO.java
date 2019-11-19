package com.to8to.tbt.msc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author pajero.quan
 */
@Data
public class PushClickDTO {
    @ApiModelProperty(value = "打开方式,0 - 直接打开APP ， 1 - 打开URL 地址  ， 2 - 打开应用页面地址  ， 3 - 自定义内容，可以为字符串或JSON 格式", example = "1")
    private String type;
    @ApiModelProperty(value = "跳转链接", example = "http://www.to8to.com")
    private String url;
    @ApiModelProperty(value = "应用内页面地址", example = "com.huawei.pushagent")
    private String activity;
    @ApiModelProperty(value = "小米应用内页面地址", example = "com.huawei.pushagent")
    private String activity_uri;
    @ApiModelProperty(value = "自定义字段", example = "{\"josn\":\"string\"}")
    private Map<String,Object> custom;
}
