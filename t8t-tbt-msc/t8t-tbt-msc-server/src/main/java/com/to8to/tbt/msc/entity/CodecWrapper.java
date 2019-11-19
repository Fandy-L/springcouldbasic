package com.to8to.tbt.msc.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class CodecWrapper {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "根据ID批量获取手机号请求项")
    public static class PhoneItem{
        private String eid;

        private Integer type;
    }
}
