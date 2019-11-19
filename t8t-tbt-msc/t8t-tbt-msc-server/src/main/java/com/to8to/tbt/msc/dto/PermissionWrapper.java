package com.to8to.tbt.msc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
public class PermissionWrapper {

    /**
     * 查询用户信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetListDTO{
        /**
         * 用户ID
         */
        private List<Integer> uids;

        /**
         * 字段
         */
        private String fields;
    }
}
