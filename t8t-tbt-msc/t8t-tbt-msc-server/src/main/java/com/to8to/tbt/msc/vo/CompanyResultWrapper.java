package com.to8to.tbt.msc.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class CompanyResultWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "装企信息")
    public static class Business{
        private Integer id;

        private String shortName;

        private String name;

        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "装企信息")
    public static class DecMember{
        private Integer decId;

        private Integer accountId;
    }
}
