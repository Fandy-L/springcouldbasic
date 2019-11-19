package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/**
 * @author yason.li
 */
@Data
@Document(collection = "app_user_msg")
public class MongoAppUserMsg {
    @Id
    @Field(value = "_id")
    private String id;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "yid")
    private Integer yid;

    @Field(value = "read")
    private Boolean read;
}
