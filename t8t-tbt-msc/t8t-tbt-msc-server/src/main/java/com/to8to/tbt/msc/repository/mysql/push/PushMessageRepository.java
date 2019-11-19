package com.to8to.tbt.msc.repository.mysql.push;

import com.to8to.tbt.msc.entity.mysql.push.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author pajero.quan
 */
public interface PushMessageRepository extends JpaRepository<Message, Integer>, JpaSpecificationExecutor<Message> {
    void deleteAllByCreateTimeLessThanEqual(int time);
}
