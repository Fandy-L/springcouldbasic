package com.to8to.tbt.msc.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 13:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("应用")
public class AppVO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "包名")
    private String packageName;

    @ApiModelProperty(value = "应用名")
    private String name;

    @ApiModelProperty(value = "应用id")
    private Integer pushAppId;

    @ApiModelProperty(value = "是否启用")
    private Boolean enable;

    @ApiModelProperty(value = "Android最低版本")
    private String androidMinVersion;

    @ApiModelProperty(value = "ios最低版本")
    private String iosMinVersion;

    @ApiModelProperty(value = "Android中转页uri")
    private String uriActivity;
}
