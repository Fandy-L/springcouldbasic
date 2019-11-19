package com.to8to.tbt.msc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class SmsWrapper {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TencentSmsTel {
        /**
         * 国家代码
         */
        private String nationcode;

        /**
         * 手机号
         */
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TencentConfig {
        /**
         * 短信前缀
         */
        private String prefix;

        /**
         * 业务类型
         */
        private Integer bizType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MongateAccount {
        /**
         * 账号ID
         */
        private String userId;

        /**
         * 密码
         */
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AliyunConfig {
        /**
         * 短信签名
         */
        private String signName;

        /**
         * 模板名
         */
        private String templateCode;
    }
}
