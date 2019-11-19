package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoMailRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author juntao.guo
 */
public interface MailRecordRepository extends MongoRepository<MongoMailRecord, String> {
}
