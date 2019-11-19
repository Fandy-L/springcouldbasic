package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.Configure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author juntao.guo
 */
public interface ConfigureRepository extends CrudRepository<Configure, Integer> {

    /**
     * 根据父节点ID批量查询
     *
     * @param fatherIds
     * @return
     */
    List<Configure> findAllByFatherIdIn(List<Integer> fatherIds);

    /**
     * 根据节点类型及父节点查询
     *
     * @param configType
     * @param fatherId
     * @return
     */
    List<Configure> findAllByConfigTypeAndFatherId(int configType, int fatherId);

    /**
     * 根据主键ID批量查询
     *
     * @param cidList
     * @return
     */
    List<Configure> findAllByCidIn(Set<Integer> cidList);

    /**
     * 根据节点描述查询单个配置项
     *
     * @param configDescribe
     * @return
     */
    Optional<Configure> findByConfigDescribe(String configDescribe);

    /**
     * 根据节点描述查询
     *
     * @param configDescribe
     * @return
     */
    List<Configure> findAllByConfigDescribe(String configDescribe);

    /**
     * 根据cid查询
     *
     * @param cid
     * @return
     */
    Configure findByCid(int cid);

    /**
     * 根据节点类型及节点ID查询查询
     *
     * @param configType
     * @param cid
     * @return
     */
    Optional<Configure> findByConfigTypeAndCid(int configType, int cid);

    /**
     * 根据节点类型及父节点列表组查询
     *
     * @param configType
     * @param fatherIds
     * @return
     */
    List<Configure> findAllByConfigTypeAndFatherIdIn(int configType, List<Integer> fatherIds);

    /**
     * 根据TYPE查询并根据主键倒序-取结果集中的第一条
     * @param configType
     * @return
     */
    Optional<Configure> findFirstByConfigTypeOrderByCidDesc(int configType);

    /**
     * 根据TYPE查询并根据主键倒序
     * @param configType
     * @return
     */
    List<Configure> findAllByConfigTypeOrderByCidDesc(int configType);
}
