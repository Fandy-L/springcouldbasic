package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@Document(collection = "group_note_record")
public class MongoGroupNoteRecord {
    @Id
    @Field(value = "_id")
    private String id;

    /**
     * 业主ID
     */
    private String name;

    /**
     * 手机号
     */
    public String phone;

    /**
     * 模板ID
     */
    @Field(value = "group_note_id")
    private String groupNoteId;

    /**
     * 发送时间
     */
    @Field(value = "send_time")
    private long sendTime;

    /**
     * 发送状态0-未发送1-已发送
     */
    private int status;

    /**
     * 插入时间
     */
    @Field(value = "insert_time")
    private long insertTime;
}
