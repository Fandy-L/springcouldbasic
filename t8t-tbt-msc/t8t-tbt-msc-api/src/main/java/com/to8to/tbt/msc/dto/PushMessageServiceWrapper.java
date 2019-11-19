package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.dto.GetMessageListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 14:57
 */
public class PushMessageServiceWrapper {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("MessageServiceWrapper.GetListDTO")
    public static class GetListDTO {
        @Valid
        @NotNull
        @ApiModelProperty(value = "查询参数")
        private GetMessageListDTO query;
    }
}
