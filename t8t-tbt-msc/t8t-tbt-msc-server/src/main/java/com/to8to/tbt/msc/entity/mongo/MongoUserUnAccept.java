package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author edmund.yu
 */
@Data
@Document(collection = "user_unaccept")
public class MongoUserUnAccept {
    @Id
    @Field("_id")
    private String id;

    @Field("uid")
    private String uid;

    @Field("update_time")
    private long updateTime;

    @Field("smalls")
    private List<Integer> smalls;
}
