package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author edmund.yu
 */
@Data
public class MongoMsgRecord {

    @Field(value = "_id")
    private String id;

    private String ns;

    /**
     * 目标对象
     */
    private MongoRecordTarget target;

    /**
     * 模板ID
     */
    private int tid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 状态
     */
    private int status;

    /**
     * 发送时间
     */
    @Field(value = "send_time")
    private int sendTime;

    /**
     * 触发人用户ID
     */
    @Field(value = "from_user_id")
    private String fromUserId;

    /**
     * 是否已读
     */
    @Field(value = "is_read")
    private int isRead;

    /**
     * 业务数据
     */
    @Field(value = "bizdata")
    private MongoPcRecordBizData bizData;

    /**
     * 消息标题
     */
    private String title;

    @Field(value = "bizid")
    private int bizId;

    @Field(value = "yjindu")
    private int process;

    private int deal;

    /**
     * 发送通道
     */
    private int channel;

    /**
     * 错误码
     */
    @Field(value = "error_code")
    private int errorCode;

    /**
     * 错误码描述
     */
    @Field(value = "error_describle")
    private String errorDescribe;

    /**
     * 添加时间
     */
    @Field(value = "insert_time")
    private int insertTime;
}
