package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoMsgReply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author juntao.guo
 */
@Component
public interface MongoMsgReplyRepository extends MongoRepository<MongoMsgReply, String> {

    /**
     * 根据手机号搜索
     *
     * @param phone
     * @return
     */
    List<MongoMsgReply> findAllByPhone(String phone);
}
