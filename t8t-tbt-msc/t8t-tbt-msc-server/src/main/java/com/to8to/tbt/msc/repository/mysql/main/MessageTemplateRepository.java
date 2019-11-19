package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author juntao.guo
 */
public interface MessageTemplateRepository extends CrudRepository<MessageTemplate, Integer> {

    /**
     * 根据模板ID查询
     *
     * @param tid
     * @return
     */
    Optional<MessageTemplate> findByTid(Integer tid);


    /**
     * 分页查询短信模板信息
     *
     * @param id
     * @param channelType
     * @param isGround
     * @param isLikeGround
     * @param cityIds
     * @param tid
     * @param tidList
     * @param coverTidList
     * @param pageable
     * @return
     */
    @Query(
            countQuery = "select count(t.id) from MessageTemplate t"
                    + " where (t.id=:id or :id is null)"
                    + " and (t.channelType=:channelType or :channelType is null)"
                    + " and (t.isGround=:isGround or :isGround is null)"
                    + " and ((t.cityIds like '%:cityIds%' or :cityIds is null) or t.isGround=3 or (t.isGround=:isLikeGround or :isLikeGround is null))"
                    + " and (t.tid=:tid or :tid is null)"
                    + " and (t.tid in (:tidList) or :coverTidList is null)",
            value = "select t from MessageTemplate t"
                    + " where (t.id=:id or :id is null)"
                    + " and (t.channelType=:channelType or :channelType is null)"
                    + " and (t.isGround=:isGround or :isGround is null)"
                    + " and ((t.cityIds like '%:cityIds%' or :cityIds is null) or t.isGround=3 or (t.isGround=:isLikeGround or :isLikeGround is null))"
                    + " and (t.tid=:tid or :tid is null)"
                    + " and (t.tid in (:tidList) or :coverTidList is null)")
    Page<MessageTemplate> search(@Param("id") Integer id,
                                 @Param("channelType") Integer channelType,
                                 @Param("isGround") Integer isGround,
                                 @Param("isLikeGround") Integer isLikeGround,
                                 @Param("cityIds") String cityIds,
                                 @Param("tid") Integer tid,
                                 @Param("tidList") List<Integer> tidList,
                                 @Param("coverTidList") Integer coverTidList,
                                 Pageable pageable);
}
