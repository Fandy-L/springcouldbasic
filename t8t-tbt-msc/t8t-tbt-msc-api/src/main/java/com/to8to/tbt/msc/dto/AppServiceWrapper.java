package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.dto.CreateAppDTO;
import com.to8to.tbt.msc.entity.dto.UpdateAppDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/23 13:56
 */
public class AppServiceWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("AppServiceWrapper.CreateDTO")
    public static class CreateDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "应用")
        private CreateAppDTO app;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("AppServiceWrapper.EditAppDTO")
    public static class EditAppDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "应用")
        private UpdateAppDTO app;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("AppServiceWrapper.IdDTO")
    public static class IdDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "应用主键id")
        private Integer id;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("AppServiceWrapper.PushAppIdDTO")
    public static class PushAppIdDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "应用id")
        private Integer appId;
    }


}
