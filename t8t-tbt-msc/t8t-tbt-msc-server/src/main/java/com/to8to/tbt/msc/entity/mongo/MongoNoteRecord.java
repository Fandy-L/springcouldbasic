package com.to8to.tbt.msc.entity.mongo;

import com.to8to.tbt.msc.constant.MsgConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@Document(collection = MsgConstant.COLLECTION_NOTE_RECORD)
public class MongoNoteRecord {
    @Id
    @Field(value = "_id")
    public String id;

    public int tid;

    public String ns;

    public MongoRecordTarget target;

    public String content;

    public int status;

    @Field(value = "send_time")
    public long sendTime;

    @Field(value = "from_user_id")
    public String fromUserId;

    @Field(value = "is_read")
    public int isRead;

    @Field(value = "bizdata")
    public String bizData;

    public String title;

    @Field(value = "bizid")
    public int bizId;

    @Field(value = "yjindu")
    public int process;

    public int deal;

    public int channel;

    @Field(value = "error_code")
    public int errorCode;

    @Field(value = "error_describle")
    public String errorDescribe;

    @Field(value = "insert_time")
    private int insertTime;
}
