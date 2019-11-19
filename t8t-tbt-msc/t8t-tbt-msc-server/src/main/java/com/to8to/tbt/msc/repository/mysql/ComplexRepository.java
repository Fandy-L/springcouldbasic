package com.to8to.tbt.msc.repository.mysql;

import java.util.List;
import java.util.Set;

/**
 * @author juntao.guo
 */
public interface ComplexRepository {

    /**
     * 设置APP消息已读
     * @param id
     * @return
     */
    boolean setAppMsgReaded(int id);

    /**
     * 根据节点ID查询模板ID
     * @param nodeIds
     * @return
     */
    Set<Integer> queryTidListByNodeIds(List<Integer> nodeIds);
}
