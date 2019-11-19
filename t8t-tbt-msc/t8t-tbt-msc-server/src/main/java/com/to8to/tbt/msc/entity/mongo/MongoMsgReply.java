package com.to8to.tbt.msc.entity.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "msg_reply")
public class MongoMsgReply {
    @Id
    @Field(value = "_id")
    public String id;

    @JsonProperty(value = "replytime")
    private int replyTime;

    private String phone;

    private String reply;
}
