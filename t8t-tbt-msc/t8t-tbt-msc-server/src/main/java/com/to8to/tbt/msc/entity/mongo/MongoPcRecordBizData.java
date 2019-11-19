package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
public class MongoPcRecordBizData {
    @Field(value = "small_type")
    private int smallType;

    @Field(value = "user_type")
    private int userType;

    private int level;

    private String link;

    private String title;

    @Field(value = "big_type")
    private int bigType;

    private int priority;

    @Field(value = "node_id")
    private String nodeId;
}
