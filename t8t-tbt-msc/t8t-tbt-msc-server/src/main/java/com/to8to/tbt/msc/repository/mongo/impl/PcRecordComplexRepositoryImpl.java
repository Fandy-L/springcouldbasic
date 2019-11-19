package com.to8to.tbt.msc.repository.mongo.impl;

import com.mongodb.client.result.UpdateResult;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordList;
import com.to8to.tbt.msc.entity.mongo.MongoUserUnAccept;
import com.to8to.tbt.msc.repository.mongo.PcRecordComplexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author edmund.yu
 */
@Repository
public class PcRecordComplexRepositoryImpl implements PcRecordComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MongoPcRecord getMsgHuang(String uid, int num) {
        Query query = new Query();
        query.addCriteria(Criteria.where("target.contact").is(uid));
        query.addCriteria(Criteria.where("is_read").is(0));
        query.addCriteria(Criteria.where("status").is(2));
        query.addCriteria(Criteria.where("bizdata.big_type").ne(8));
        query.with(Sort.by(Sort.Order.desc("send_time")).and(Sort.by(Sort.Order.desc("bizdata.priority"))));
        query.skip(num).limit(1);

        List<MongoPcRecord> mongoPcRecordList;
        mongoPcRecordList = mongoTemplate.find(query, MongoPcRecord.class);
        if (mongoPcRecordList.isEmpty()) {
            return null;
        }
        else {
            return mongoPcRecordList.get(0);
        }
    }

    @Override
    public void delMsg(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, MongoPcRecord.class);
    }

    @Override
    public Map<String, Integer> getUnreadBig(String uid) {
        Map<String, Integer> map = new HashMap(10);
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(Criteria.where("target.contact").is(uid).andOperator(Criteria.where("is_read").is(0))));
        operations.add(Aggregation.group("$bizdata.big_type").count().as("num"));
        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<HashMap> results = mongoTemplate.aggregate(aggregation,"pc_record", HashMap.class);
        if (results == null) {
            return map;
        }
        List<HashMap> objects = results.getMappedResults();
        if (objects.size() == 0){
            return map;
        }
        for (HashMap hashMap:objects) {
            Integer bigType = (Integer) hashMap.get("_id");
            if (bigType != null){
                Integer num = (Integer) hashMap.get("num");
                map.put(bigType.toString(), num);
            }
        }
        return map;
    }

    @Override
    public void setSmallNeed(String uid, List<Integer> smalls) {

        Query query = new Query(Criteria.where("uid").is(uid));
        Update update = new Update().set("smalls",smalls);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, "user_unaccept");
        if (updateResult.getMatchedCount() == 0){
            MongoUserUnAccept mongoUserUnAccept = new MongoUserUnAccept();
            mongoUserUnAccept.setSmalls(smalls);
            mongoUserUnAccept.setUid(uid);
            mongoUserUnAccept.setUpdateTime(System.currentTimeMillis()/1000);
            mongoTemplate.insert(mongoUserUnAccept);
        }
    }

    @Override
    public List<Integer> getUserSmallNeed(String uid) {
        Query query = new Query(Criteria.where("uid").is(uid));
        MongoUserUnAccept mongoUserUnAccept = mongoTemplate.findOne(query, MongoUserUnAccept.class);
        if (mongoUserUnAccept == null) {
            return new ArrayList<>();
        }
        return mongoUserUnAccept.getSmalls();
    }

    @Override
    public MongoPcRecordList searchMsg(ListMsgDTO req, int bigType, int skip, int limit) {

        MongoPcRecordList result = new MongoPcRecordList();
        result.setMongoPcRecords(null);
        result.setTotalRecords(0);

        Query query = new Query();
        if (req.getTargetContact() != null) {
            query.addCriteria(Criteria.where("target.contact").is(req.getTargetContact()));
        }else {
            return result;
        }
        if (req.getToUserType() != null && req.getToUserType() > 0) {
            query.addCriteria(Criteria.where("target.user_type").is(req.getToUserType()));
        }
        if (req.getSendType() != null && req.getSendType() > 0) {
            query.addCriteria(Criteria.where("target.send_type").is(5));
        }
        if (req.getIsRead() != null && req.getIsRead() >= 0) {
            query.addCriteria(Criteria.where("is_read").is(req.getIsRead()));
        }
        query.addCriteria(Criteria.where("status").is(2));
        if (bigType > 0) {
            query.addCriteria(Criteria.where("bizdata.big_type").is(bigType));
        }
        result.setTotalRecords(mongoTemplate.count(query, MongoPcRecord.class));
        query.with(Sort.by(Sort.Order.desc("send_time")));
        query.skip(skip).limit(limit);
        result.setMongoPcRecords(mongoTemplate.find(query, MongoPcRecord.class));
        return result;
    }

    @Override
    public UpdateResult setPcMsgRead(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("is_read", 1);
        return mongoTemplate.updateFirst(query, update, MongoPcRecord.class);
    }

    @Override
    public MongoPcRecord getPcMsgById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, MongoPcRecord.class);
    }
}
