package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
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
public interface AppTemplateRepository extends CrudRepository<AppTemplate, Integer> {
    /**
     * 根据模板ID查询
     *
     * @param tid
     * @return
     */
    Optional<AppTemplate> findByTid(Integer tid);

    /**
     * 分页查询短信模板信息
     *
     * @param appId
     * @param tid
     * @param tidList
     * @param coverTidList
     * @param pageable
     * @return
     */
    @Query(
            countQuery = "select count(t.id) from AppTemplate t"
                    + " where (t.appId=:appId or :appId is null)"
                    + " and (t.tid=:tid or :tid is null)"
                    + " and (t.tid in (:tidList) or :coverTidList is null)",
            value = "select t from AppTemplate t"
                    + " where (t.appId=:appId or :appId is null)"
                    + " and (t.tid=:tid or :tid is null)"
                    + " and (t.tid in (:tidList) or :coverTidList is null)")
    Page<AppTemplate> search(@Param("appId") Integer appId,
                                 @Param("tid") Integer tid,
                                 @Param("tidList") List<Integer> tidList,
                                 @Param("coverTidList") Integer coverTidList,
                                 Pageable pageable);
}
