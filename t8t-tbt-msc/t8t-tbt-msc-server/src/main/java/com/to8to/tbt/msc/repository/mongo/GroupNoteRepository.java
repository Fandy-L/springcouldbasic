package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoGroupNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * @author juntao.guo
 */
@Component
public interface GroupNoteRepository extends MongoRepository<MongoGroupNote, String> {


}
