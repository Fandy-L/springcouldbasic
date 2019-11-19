package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author yason.li
 */
public interface MongoPcRecordRepository extends MongoRepository<MongoPcRecord, String> {

}