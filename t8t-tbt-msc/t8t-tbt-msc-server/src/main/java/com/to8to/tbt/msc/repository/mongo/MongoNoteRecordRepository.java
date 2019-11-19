package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoNoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author yason.li
 */
public interface MongoNoteRecordRepository extends MongoRepository<MongoNoteRecord, String> {
}
