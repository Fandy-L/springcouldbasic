package com.to8to.tbt.msc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class PcWrapper {

    /**
     * 业务数据
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BizData {
        /**
         * 分类
         */
        private Integer smallType;

        /**
         * 用户类型
         */
        private Integer userType;

        /**
         * 级别
         */
        private Integer level;

        /**
         * 跳转链接
         */
        private String link;

        /**
         * 标题
         */
        private String title;

        /**
         * 上级分类
         */
        private Integer bigType;

        /**
         * 优先组
         */
        private Integer priority;

        /**
         * 节点ID
         */
        private String nodeId;
    }
}
