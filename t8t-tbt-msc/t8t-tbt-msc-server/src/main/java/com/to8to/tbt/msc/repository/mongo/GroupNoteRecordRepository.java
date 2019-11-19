package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoGroupNoteRecord;
import com.to8to.tbt.msc.entity.mongo.MongoNoteRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author juntao.guo
 */
@Component
public interface GroupNoteRecordRepository extends MongoRepository<MongoGroupNoteRecord, String> {

    /**
     * 根据模板ID批量查询
     *
     * @param groupNoteId
     * @return
     */
    List<MongoGroupNoteRecord> findAllByGroupNoteId(String groupNoteId);

    /**
     * 根据群发短信模板的ID和发送状态查询
     *
     * @param groupNoteId
     * @param status
     * @return
     */
    List<MongoGroupNoteRecord> findAllByGroupNoteIdAndStatus(String groupNoteId, int status);

    /**
     * 根据主键ID批量查询
     *
     * @param ids
     * @return
     */
    List<MongoGroupNoteRecord> findAllByIdIn(List<String> ids);
}
