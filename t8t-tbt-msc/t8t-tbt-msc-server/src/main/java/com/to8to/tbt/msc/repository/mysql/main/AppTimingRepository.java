package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.AppTiming;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author yason.li
 */
public interface AppTimingRepository extends CrudRepository<AppTiming, Integer> {

    /**
     * 查询未发送消息
     * @param nowTime
     * @return
     */
    @Modifying
    @Query(value = "SELECT a  FROM AppTiming a WHERE a.isSend = 0 AND a.delayTime <= (:nowTime)")
    List<AppTiming> getUnSendMessage(@Param("nowTime") Integer nowTime);
}
