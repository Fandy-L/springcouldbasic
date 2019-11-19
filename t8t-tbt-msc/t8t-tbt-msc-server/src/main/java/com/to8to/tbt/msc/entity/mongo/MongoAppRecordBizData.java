package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author yason.li
 */
@Data
public class MongoAppRecordBizData {
    @Field(value = "link")
    private String link;

    @Field(value = "node_id")
    private Integer nodeId;

    @Field(value = "yid")
    private Integer yid;
}
