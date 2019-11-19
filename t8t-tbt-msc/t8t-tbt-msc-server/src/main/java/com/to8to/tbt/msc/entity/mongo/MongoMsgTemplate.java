package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author edmund.yu
 */
@Data
@Document(collection = "msg_template")
public class MongoMsgTemplate {
    @Id
    @Field(value = "_id")
    private String id;

    @Field(value = "msg_node")
    private String msgNode;

    @Field(value = "node_category")
    private Integer nodeCategory;

    @Field(value = "title")
    private String title;

    @Field(value = "content")
    private String content;

    @Field(value = "link")
    private String link;

    @Field(value = "to_user_type")
    private String toUserType;

    @Field(value = "send_type")
    private String sendType;

    @Field(value = "isground")
    private Integer isGround;

    @Field(value = "create_time")
    private long createTime;

    @Field(value = "word__ids")
    private String wordIds;

    @Field(value = "status")
    private Integer status;

    @Field(value = "small_category")
    private Integer smallCategory;

    @Field(value = "priority")
    private Integer priority;

    @Field(value = "url_param_ids")
    private String urlParamIds;

    @Field(value = "title_param_ids")
    private String titleParamIds;

}
