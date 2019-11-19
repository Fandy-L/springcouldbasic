package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.MessageRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * @author juntao.guo
 */
public interface MessageRecordRepository extends CrudRepository<MessageRecord, Integer> {
    /**
     * 分页全部发送记录
     * @param pageable
     * @return
     */
    Page<MessageRecord> findAllBySendStatusIsNotNullOrderById(Pageable pageable);
}
