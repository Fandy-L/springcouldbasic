package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.dto.GetDeviceTokenDTO;
import com.to8to.tbt.msc.entity.dto.UpdateDeviceTokenDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 16:44
 */
public class DeviceTokenServiceWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("DeviceTokenServiceWrapper.UpdateDTO")
    public static class UpdateDTO {
        @NotNull
        @ApiModelProperty(value = "设备token对象")
        @Valid
        private UpdateDeviceTokenDTO deviceToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("DeviceTokenServiceWrapper.GetDTO")
    public static class GetDTO {
        @NotNull
        @ApiModelProperty(value = "查询参数")
        @Valid
        private GetDeviceTokenDTO query;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("DeviceTokenServiceWrapper.GetTagsDTO")
    public static class GetTagsDTO {
        @NotNull
        @ApiModelProperty(value = "deviceToken")
        private String deviceToken;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "pushAppId")
        private Integer pushAppId;
    }
}
