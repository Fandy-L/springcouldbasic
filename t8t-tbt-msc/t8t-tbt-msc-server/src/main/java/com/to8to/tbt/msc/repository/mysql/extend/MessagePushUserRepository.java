package com.to8to.tbt.msc.repository.mysql.extend;

import com.to8to.tbt.msc.entity.mysql.extend.MessagePushUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author juntao.guo
 */
public interface MessagePushUserRepository extends CrudRepository<MessagePushUser, Integer> {
    /**
     * 根据firstId查询
     * @param firstId
     * @return
     */
    Optional<MessagePushUser> findByFirstIdOrderById(String firstId);
}
