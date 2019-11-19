package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.Template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author juntao.guo
 */
public interface TemplateRepository extends CrudRepository<Template, Integer> {

    /**
     * 根据节点ID批量查询
     * @param nodeIds
     * @return
     */
    List<Template> findAllByNodeIdIn(List<Integer> nodeIds);

    /**
     * 根据模板ID查询
     *
     * @param tid
     * @return
     */
    Optional<Template> findByTid(int tid);

    /**
     * 根据节点统计模板数量
     *
     * @param nodeId
     * @return
     */
    int countByNodeId(int nodeId);

    /**
     * 根据节点列表统计模板数量
     *
     * @param nodeIdList
     * @return
     */
    int countByNodeIdIn(List<Integer> nodeIdList);

    /**
     * 根据发送对象ID统计模板数量
     *
     * @param targetType
     * @return
     */
    int countByTargetType(int targetType);

    /**
     * 根据产品模块统计模板数量
     *
     * @param pmModule
     * @return
     */
    int countByPmModule(String pmModule);

    /**
     * 根据节点ID查询并根据创建时间倒序-取最近一条
     *
     * @param nodeId
     * @return
     */
    Optional<Template> findFirstByNodeIdOrderByCreateTimeDesc(int nodeId);

    /**
     * 分页查询模板信息
     *
     * @param pmModule
     * @param sendType
     * @param targetType
     * @param isAuto
     * @param isActive
     * @param nodeId
     * @param nodeIds
     * @param coverNodeIds
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "select t from Template t"
                    + " where (t.pmModule=:pmModule or :pmModule is null)"
                    + " and (t.sendType=:sendType or :sendType is null)"
                    + " and (t.targetType=:targetType or :targetType is null)"
                    + " and (t.isAuto=:isAuto or :isAuto is null)"
                    + " and (t.isActive=:isActive or :isActive is null)"
                    + " and (t.nodeId=:nodeId or :nodeId is null)"
                    + " and (t.nodeId in (:nodeIds) or :coverNodeIds is null)"
                    + " and (t.createTime >= :startTime or :startTime is null)"
                    + " and (t.createTime <= :endTime or :endTime is null)")
    List<Template> search(
                                 @Param("pmModule") String pmModule,
                                 @Param("sendType") Integer sendType,
                                 @Param("targetType") Integer targetType,
                                 @Param("isAuto") Integer isAuto,
                                 @Param("isActive") Integer isActive,
                                 @Param("nodeId") Integer nodeId,
                                 @Param("nodeIds") List<Integer> nodeIds,
                                 @Param("coverNodeIds") Integer coverNodeIds,
                                 @Param("startTime") Integer startTime,
                                 @Param("endTime") Integer endTime);

    /**
     * 根据模板ID批量查询
     *
     * @param tidList
     * @return
     */
    List<Template> findAllByTidInOrderByTidDesc(List<Integer> tidList);
}
