package com.to8to.tbt.msc.repository.mysql.extend;

import com.to8to.tbt.msc.entity.mysql.extend.MessagePush;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface MessagePushRepository extends CrudRepository<MessagePush, Integer> {

    /**
     * 查询群推消息
     * @param startTime
     * @param appOsType
     * @param currentTime
     * @param msgIds
     * @return
     */
    @Query(value = "select a from MessagePush a where a.pushTime>=:startTime and ((a.type=1 and a.appOsType=:appOsType and a.pushStatus=0 and a.pushTime<:currentTime) or (a.type=1 and a.pushStatus=2 and a.pushTime<:currentTime) or (a.type in (2, 3) and a.pushStatus=2 and a.id in (:msgIds))) order by a.pushTime desc")
    List<MessagePush> queryPushMsg(@Param("startTime") int startTime, @Param("appOsType") int appOsType, @Param("currentTime") int currentTime, @Param("msgIds") List<Integer> msgIds);
}
