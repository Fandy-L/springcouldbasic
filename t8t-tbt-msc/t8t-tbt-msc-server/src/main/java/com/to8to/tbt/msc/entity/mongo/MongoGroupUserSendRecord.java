package com.to8to.tbt.msc.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "group_user_send_record")
public class MongoGroupUserSendRecord {
    @Id
    @Field(value = "_id")
    public String id;

    /**
     * 短信群发模板ID
     */
    @Field(value = "group_note_id")
    public String groupNoteId;

    /**
     * 用户名
     */
    @Field(value = "user_name")
    public String userName;

    /**
     * 发送总数
     */
    public int count;

    /**
     * 发送时间
     */
    @Field(value = "send_time")
    public int sendTime;
}
