package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoAppRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author yason.li
 */
public interface MongoAppRecordRepository extends MongoRepository<MongoAppRecord, String> {
}
