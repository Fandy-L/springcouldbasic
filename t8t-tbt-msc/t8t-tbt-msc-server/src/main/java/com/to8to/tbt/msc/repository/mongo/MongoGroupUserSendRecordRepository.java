package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoGroupUserSendRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface MongoGroupUserSendRecordRepository extends MongoRepository<MongoGroupUserSendRecord, String> {

    /**
     * 根据用户名及时间区间查询
     *
     * @param userName
     * @param startTime
     * @param endTime
     * @return
     */
    List<MongoGroupUserSendRecord> findAllByUserNameAndSendTimeBetween(String userName, int startTime, int endTime);
}
