package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author edmund.yu
 */
@Data
@Document(collection = "keyword")
public class MongoKeyword {

    @Id
    @Field(value = "_id")
    private String id;

    @Field(value = "keyword")
    private String keyword;


    @Field(value = "database")
    private String database;

    @Field(value = "table")
    private String table;

    @Field(value = "column")
    private String column;

    @Field(value = "select_column")
    private String selectColumn;

    @Field(value = "create_time")
    private long createTime;

    @Field(value = "insert_time")
    private long insertTime;
}

