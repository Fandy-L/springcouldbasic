package com.to8to.tbt.msc.repository.mongo;

import com.mongodb.client.result.UpdateResult;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoTemplateList;

import java.util.List;

/**
 * @author edmund.yu
 */

public interface TemplateRepository {
    /**
     * 增加
     *
     * @param mongoMsgTemplate
     */

    void addTemplate(MongoMsgTemplate mongoMsgTemplate);

    /**
     * 更新
     *
     * @param mongoMsgTemplate
     * @return
     */
    UpdateResult updateTemplate(MongoMsgTemplate mongoMsgTemplate);

    /**
     * 根据ID删除
     * @param id
     */
    void deleteTemplateById(String id);

    /**
     * 根据节点ID获取关键字
     * @param nodeId
     * @return
     */
    List<MongoMsgTemplate> listTemplateByNote(Integer nodeId);

    /**
     * 获取列表
     * @param mongoMsgTemplate
     * @param searchTime
     * @param skip
     * @param limit
     * @return
     */
    MongoTemplateList listTemplate(MongoMsgTemplate mongoMsgTemplate, SearchTime searchTime, int skip, int limit);

    /**
     * 根据Id获取模板
     * @param id
     * @return
     */
    MongoMsgTemplate getTemplateById(String id);

    /**
     * 根据node_id和to_user_type获取模板
     *
     * @param nodeId
     * @param toUserType
     * @return
     */
    List<MongoMsgTemplate> getTemplateByNodeAndToUserType(Integer nodeId, Integer toUserType);
}
