package com.to8to.tbt.msc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
public class RabbitMqWrapper {

    /**
     * ES同步原始数据包装类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteSyncData<T> {

        /**
         * 数据库名
         */
        private String database;

        /**
         * 表名
         */
        private String table;

        /**
         * 操作
         */
        private String action;

        /**
         * 结果集
         */
        private List<T> list;

        /**
         * 模板ID
         */
        private Integer tid;

        /**
         * 错误码
         */
        @JSONField(name = "error_code")
        private Integer errorCode;

        /**
         * 业务参数
         */
        @JSONField(name = "biz_param")
        private String bizParam;
    }

    /**
     * APP消息数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppRecord {
        /**
         * 主键ID
         */
        private Integer id;

        /**
         * 模板ID
         */
        private Integer tid;

        /**
         * 业主ID
         */
        private Integer uid;

        /**
         * 发送方标识
         */
        private String sender;

        /**
         * app消息内容
         */
        @JSONField(name = "app_content")
        private String appContent;

        /**
         * 业务字段，可选
         */
        @JSONField(name = "biz_param")
        private String bizParam;

        /**
         * 发送状态：0-缺省值，未发送；1-发送成功；2-发送失败
         */
        @JSONField(name = "send_status")
        private Integer sendStatus;

        /**
         * 读取状态：0-缺省值，未读；1-已读
         */
        @JSONField(name = "is_read")
        private Integer isRead;

        /**
         * 发送时间
         */
        @JSONField(name = "send_time")
        private Integer sendTime;

        /**
         * 入库时间
         */
        @JSONField(name = "create_time")
        private Integer createTime;
    }

    /**
     * 短信消息数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRecord {
        /**
         * 主键ID
         */
        private Integer id;

        /**
         * 模板ID
         */
        private Integer tid;

        /**
         * 电话ID
         */
        @JSONField(name = "phoneid")
        private Integer phoneId;

        /**
         * 短信内容
         */
        @JSONField(name = "msg_content")
        private String msgContent;

        /**
         * 短信发送状态 0 发送失败 1发送成功
         */
        @JSONField(name = "send_status")
        private Integer sendStatus;

        /**
         * 发送时间
         */
        @JSONField(name = "send_time")
        private Integer sendTime;

        /**
         * 短信发送异常状态码
         */
        @JSONField(name = "error_code")
        private Integer errorCode;
    }


}
