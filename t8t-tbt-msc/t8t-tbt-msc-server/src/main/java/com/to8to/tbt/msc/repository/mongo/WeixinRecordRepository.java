package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoWeixinRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author juntao.guo
 */
public interface WeixinRecordRepository extends MongoRepository<MongoWeixinRecord, String> {
}
