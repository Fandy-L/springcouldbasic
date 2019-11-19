package com.to8to.tbt.msc.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class SendMessageWrapper {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendSmsMessage {
        /**
         * 模板ID
         */
        private Integer tid;

        private Integer isNew;

        /**
         * 发送类型
         */
        private Integer sendType;

        /**
         * 发送延迟时间
         */
        private Integer delayTime;

        /**
         * 是否即时发送1即时发送 2非即时发送
         */
        private Integer isAuto;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 手机号ID
         */
        private Integer phoneId;

        /**
         * 发送通道
         */
        private Integer channelType;

        /**
         * 短信内容
         */
        private String content;

        /**
         * 关键词
         */
        private JSONObject keyword;

        /**
         * IP地址
         */
        private String ip;
    }
}
