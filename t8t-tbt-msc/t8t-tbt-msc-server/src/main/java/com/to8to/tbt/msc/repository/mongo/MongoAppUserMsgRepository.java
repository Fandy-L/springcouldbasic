package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoAppUserMsg;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author yason.li
 */
public interface MongoAppUserMsgRepository extends MongoRepository<MongoAppUserMsg, String> {
    /**
     * 根据用户ID查消息
     * @param userId
     * @return
     */
    List<MongoAppUserMsg> findAllByUserId(String userId);

    /**
     * 根据用户ID和项目ID查询一条消息
     * @param userId
     * @param yid
     * @return
     */
    Optional<MongoAppUserMsg> findFirstByUserIdAndYid(String userId, Integer yid);
}
