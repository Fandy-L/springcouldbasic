package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class WechatMessageWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendDTO {
        /**
         * 微信用户唯一标识
         */
        private String openid;

        /**
         * 消息内容
         */
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendVO {

        private Integer id;

        @JsonProperty(value = "_id")
        private Integer subId;

        @JsonProperty(value = "msgid")
        private Integer msgId;

        /**
         * 状态码
         */
        private Integer code;

        /**
         * 状态码描述
         */
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendEpDTO {

        /**
         * 用户名
         */
        private String username;

        /**
         * 消息内容
         */
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendEpVO {
        /**
         * 状态码
         */
        private Integer code;

        /**
         * 状态码描述
         */
        private String msg;
    }
}
