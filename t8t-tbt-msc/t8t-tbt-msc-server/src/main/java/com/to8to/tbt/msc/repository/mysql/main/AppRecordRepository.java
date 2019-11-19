package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.AppRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * @author juntao.guo
 */
public interface AppRecordRepository extends CrudRepository<AppRecord, Integer> {

    /**
     * 批量更新消息已读
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "update AppRecord set is_read=1 where id in (:ids)")
    void setAppMsgHasReadBatch(@Param("ids") List<Integer> ids);

    /**
     * 批量更新消息已读
     * @param uid
     * @param isRead
     * @param tidList
     */
    @Transactional()
    @Modifying
    @Query(value = "update AppRecord set is_read=:isRead where uid=:uid and tid in (:tidList)")
    void setAppMsgStatusByUidAndTid(@Param("uid") int uid, @Param("isRead") int isRead, @Param("tidList")Set<Integer> tidList);

    /**
     * 根据uid和tidList获取消息数
     * @param uid
     * @param tidList
     * @return
     */
    Integer countAllByUidAndTidIn(@Param("uid") int uid,@Param("tidList")Set<Integer> tidList);

    /**
     * 分页全部APP发送记录
     * @param pageable
     * @return
     */
    Page<AppRecord> findAllBySendStatusIsNotNullOrderById(Pageable pageable);

}
