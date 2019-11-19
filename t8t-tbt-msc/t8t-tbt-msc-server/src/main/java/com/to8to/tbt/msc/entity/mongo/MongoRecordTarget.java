package com.to8to.tbt.msc.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoRecordTarget {
    @Field(value = "user_id")
    public String userId;

    public String contact;

    @Field(value = "user_type")
    public int userType;

    @Field(value = "send_type")
    public int sendType;
}
