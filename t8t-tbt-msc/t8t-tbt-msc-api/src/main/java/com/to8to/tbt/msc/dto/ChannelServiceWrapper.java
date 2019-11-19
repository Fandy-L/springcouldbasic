package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.dto.CreateChannelDTO;
import com.to8to.tbt.msc.entity.dto.UpdateChannelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 14:29
 */
public class ChannelServiceWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("ChannelServiceWrapper.CreateDTO")
    public static class CreateDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "渠道")
        private CreateChannelDTO channel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("ChannelServiceWrapper.EditDTO")
    public static class EditDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "渠道")
        private UpdateChannelDTO channel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("ChannelServiceWrapper.IdDTO")
    public static class IdDTO {
        @NotNull
        @Min(1)
        @ApiModelProperty(value = "id")
        private Integer id;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel("ChannelServiceWrapper.AppIdDTO")
    public static class AppIdDTO {
        @NotNull
        @Min(1)
        @ApiModelProperty(value = "appId")
        private Integer appId;
    }
}
