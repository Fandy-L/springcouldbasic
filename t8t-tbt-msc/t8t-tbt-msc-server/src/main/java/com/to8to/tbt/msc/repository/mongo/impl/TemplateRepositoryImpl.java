package com.to8to.tbt.msc.repository.mongo.impl;

import com.mongodb.client.result.UpdateResult;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoTemplateList;
import com.to8to.tbt.msc.repository.mongo.TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author edmund.yu
 */
@Slf4j
@Repository
public class TemplateRepositoryImpl implements TemplateRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addTemplate(MongoMsgTemplate mongoMsgTemplate) {
        mongoTemplate.insert(mongoMsgTemplate);
    }

    @Override
    public UpdateResult updateTemplate(MongoMsgTemplate mongoMsgTemplate) {
        Query query = new Query(Criteria.where("_id").is(mongoMsgTemplate.getId()));
        Update update = new Update().set("title", mongoMsgTemplate.getTitle())
                .set("content", mongoMsgTemplate.getContent())
                .set("link", mongoMsgTemplate.getLink())
                .set("to_user_type", mongoMsgTemplate.getToUserType())
                .set("send_type", mongoMsgTemplate.getSendType())
                .set("update_time", System.currentTimeMillis()/1000)
                .set("url_param_ids", mongoMsgTemplate.getUrlParamIds())
                .set("title_param_ids", mongoMsgTemplate.getTitleParamIds())
                .set("word__ids", mongoMsgTemplate.getWordIds());
        if (mongoMsgTemplate.getMsgNode() != null){
            update.set("msg_node", mongoMsgTemplate.getMsgNode());
        }
        if (mongoMsgTemplate.getNodeCategory() != null) {
            update.set("node_category", mongoMsgTemplate.getNodeCategory());
        }
        if (mongoMsgTemplate.getIsGround() != null) {
            update.set("isground", mongoMsgTemplate.getIsGround());
        }
        if (mongoMsgTemplate.getStatus() != null) {
            update.set("status", mongoMsgTemplate.getStatus());
        }
        if (mongoMsgTemplate.getSmallCategory() != null) {
            update.set("small_category", mongoMsgTemplate.getSmallCategory());
        }
        if (mongoMsgTemplate.getPriority() != null) {
            update.set("priority", mongoMsgTemplate.getPriority());
        }
        return mongoTemplate.updateFirst(query, update, MongoMsgTemplate.class);
    }

    @Override
    public void deleteTemplateById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,MongoMsgTemplate.class);
    }

    @Override
    public List<MongoMsgTemplate> listTemplateByNote(Integer nodeId) {
        if (nodeId == null){
            nodeId = 0;
        }
        log.info("获取模板,node_id:{}",nodeId);
        List<MongoMsgTemplate> mongoMsgTemplateList;
        Query query = new Query(Criteria.where("small_category").is(nodeId)).with(Sort.by(Sort.Order.desc("title")));
        mongoMsgTemplateList = mongoTemplate.find(query, MongoMsgTemplate.class);
        return mongoMsgTemplateList;
    }

    @Override
    public MongoTemplateList listTemplate(MongoMsgTemplate mongoMsgTemplate, SearchTime searchTime, int skip, int limit) {

        Query query = new Query();
        MongoTemplateList result = new MongoTemplateList();
        if (mongoMsgTemplate != null) {
            if (mongoMsgTemplate.getTitle() != null) {
                Pattern pattern = Pattern.compile("^.*" + mongoMsgTemplate.getTitle() + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(Criteria.where("title").regex(pattern));
            }

            if (mongoMsgTemplate.getMsgNode() != null) {
                Pattern pattern1 = Pattern.compile("^.*" + mongoMsgTemplate.getMsgNode() + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(Criteria.where("msg_node").regex(pattern1));
            }

            if (mongoMsgTemplate.getContent() != null) {
                Pattern pattern2 = Pattern.compile("^.*" + mongoMsgTemplate.getContent() + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(Criteria.where("content").regex(pattern2));
            }

            if (mongoMsgTemplate.getSmallCategory() != null) {
                query.addCriteria(Criteria.where("small_category").is(mongoMsgTemplate.getSmallCategory()));
            }
            if (mongoMsgTemplate.getToUserType() != null) {
                query.addCriteria(Criteria.where("to_user_type").is(mongoMsgTemplate.getToUserType()));
            }
            if (mongoMsgTemplate.getSendType() != null) {
                query.addCriteria(Criteria.where("send_type").is(mongoMsgTemplate.getSendType()));
            }
            if (StringUtils.isNotEmpty(mongoMsgTemplate.getId())) {
                query.addCriteria(Criteria.where("_id").is(mongoMsgTemplate.getId()));
            }
            if (mongoMsgTemplate.getIsGround() != null) {
                query.addCriteria(Criteria.where("isground").is(mongoMsgTemplate.getIsGround()));
            }
        }
        if (searchTime != null) {
            if (searchTime.getStartTime() == null){
                searchTime.setStartTime(0L);
            }
            if (searchTime.getEndTime() == null){
                searchTime.setEndTime(Long.MAX_VALUE);
            }
            query.addCriteria(Criteria.where("create_time").gte(searchTime.getStartTime()).lte(searchTime.getEndTime()));
        }
        result.setTotalRecord(mongoTemplate.count(query, MongoMsgTemplate.class));
        query.with(Sort.by(Sort.Order.desc("create_time")));
        query.skip(skip).limit(limit);
        result.setMongoMsgTemplates(mongoTemplate.find(query, MongoMsgTemplate.class));
        return result;
    }

    @Override
    public MongoMsgTemplate getTemplateById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, MongoMsgTemplate.class);
    }

    @Override
    public List<MongoMsgTemplate> getTemplateByNodeAndToUserType(Integer nodeId, Integer toUserType) {
        if (nodeId == null || toUserType == null) {
            return null;
        }
        Query query = new Query(Criteria.where("small_category").is(nodeId));
        query.addCriteria(Criteria.where("to_user_type").is(toUserType.toString()));
        return mongoTemplate.find(query, MongoMsgTemplate.class);
    }

}
