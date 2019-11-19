package com.to8to.tbt.msc.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 15:16
 */
@Data
public class CreateAppDTO {
    @NotBlank
    private String packageName;
    @NotBlank
    private String name;
    @Min(1)
    private Integer pushAppId;
    @NotNull
    private Boolean enable;
    @NotEmpty
    private String androidMinVersion;
    @NotEmpty
    private String iosMinVersion;
    @NotEmpty
    private String uriActivity;
}
