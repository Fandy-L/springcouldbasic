package com.to8to.tbt.msc.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class UserWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "业主详情")
    public static class Owner{
        @ApiModelProperty(value = "业主ID")
        private Integer uid;

        @ApiModelProperty(value = "用户昵称")
        private String authorName;

        @ApiModelProperty(value = "用户头像")
        private String authorAvatar;

        @ApiModelProperty(value = "认证类型")
        private Integer identificationType;

        @ApiModelProperty(value = "认证状态")
        private Integer identificationStatus;

        @ApiModelProperty(value = "认证时间")
        private Integer identificationTime;

        @ApiModelProperty(value = "认证描述")
        private String identificationDesc;

        @ApiModelProperty(value = "认证头像")
        private String identificationPic;
    }
}
