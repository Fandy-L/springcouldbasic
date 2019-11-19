package com.to8to.tbt.msc.entity;

import com.to8to.tbt.msc.entity.mongo.MongoPcRecordBizData;
import com.to8to.tbt.msc.entity.mongo.MongoRecordTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexMsgRecord {

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
    private int sendTime;

    /**
     * 触发人用户ID
     */
    private String fromUserId;

    /**
     * 是否已读
     */
    private int isRead;

    /**
     * 业务数据
     */
    private MongoPcRecordBizData bizData;

    /**
     * 消息标题
     */
    private String title;

    private int bizId;

    private int process;

    private int deal;

    /**
     * 发送通道
     */
    private int channel;

    /**
     * 错误码
     */
    private int errorCode;

    /**
     * 错误码描述
     */
    private String errorDescribe;

    /**
     * 添加时间
     */
    private int insertTime;
}
