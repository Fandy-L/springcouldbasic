package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@Document(collection = "group_note")
public class MongoGroupNote {
    @Id
    @Field(value = "_id")
    private String id;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 成功数
     */
    @Field(value = "succ_num")
    private int succNum;

    /**
     * 失败数
     */
    @Field(value = "fail_num")
    private int failNum;

    /**
     * 创建时间
     */
    @Field(value = "create_time")
    private long createTime;

    /**
     * 发送时间
     */
    @Field(value = "send_time")
    private long sendTime;

    /**
     * 操作人
     */
    @Field(value = "rtxname")
    private String rtxName;

    /**
     * 所属部门
     */
    private String department;

    /**
     * 插入时间
     */
    @Field(value = "insert_time")
    private long insertTime;
}
