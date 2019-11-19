package com.to8to.tbt.msc.repository.mongo.template;

import com.to8to.common.search.PageResult;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNoteRecord;

/**
 * @author juntao.guo
 */
public interface GroupNoteRecordComplexRepository {

    /**
     * 获取群发记录列表
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    PageResult<MongoGroupNoteRecord> searchGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO);
}
