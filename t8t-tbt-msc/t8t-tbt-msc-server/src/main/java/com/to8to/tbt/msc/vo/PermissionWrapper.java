package com.to8to.tbt.msc.vo;

import lombok.Data;

/**
 * @author juntao.guo
 */
public class PermissionWrapper {

    /**
     * 用户列表查询结果集子项
     */
    @Data
    public static class CrmUserVO{
        /**
         * 昵称
         */
        private String nick;

        /**
         * 用户ID
         */
        private Integer uid;
    }
}
