package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoNoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author juntao.guo
 */
@Component
public interface NoteRecordRepository extends MongoRepository<MongoNoteRecord, String> {

    /**
     * 根据目标对象及进度搜索
     *
     * @param contactList
     * @return
     */
    List<MongoNoteRecord> findAllByTargetContactIn(List<String> contactList);
}
