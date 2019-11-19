package com.to8to.tbt.msc.repository.mongo.impl;

import com.google.common.collect.Lists;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.MsgRecordWrapper;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.PageResult;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.entity.mongo.MongoMsgRecord;
import com.to8to.tbt.msc.entity.mongo.MongoNoteRecord;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.repository.mongo.template.MsgRecordComplexRepository;
import com.to8to.tbt.msc.utils.IntegerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class MsgRecordComplexRepositoryImpl implements MsgRecordComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<Integer, String> tableId2tableName = new HashMap<Integer, String>();

    @PostConstruct
    public void init()
    {
        tableId2tableName.put(1, MsgConstant.COLLECTION_NOTE_RECORD);
        tableId2tableName.put(2, MsgConstant.COLLECTION_MAIL_RECORD);
        tableId2tableName.put(3, MsgConstant.COLLECTION_WEIXIN_RECORD);
        tableId2tableName.put(4, MsgConstant.COLLECTION_APP_RECORD);
        tableId2tableName.put(5, MsgConstant.COLLECITON_MSG_RECORD_PC);
    }

    @Override
    public PageResult query(ListMsgDTO params) {
        Criteria criteria = generateQueryCriteria(params);
        PageInfo pageInfo = params.getPageInfo();
        int skip = (pageInfo.getCurrPage() - 1) * pageInfo.getPageSize();
        Query query = Query.query(criteria)
                .with(Sort.by(Sort.Direction.DESC, "insert_time"))
                .skip(skip)
                .limit(pageInfo.getPageSize());
        String collectionName = tableId2tableName.get(params.getSendType());
        List<MongoMsgRecord> mongoPcRecords = mongoTemplate.find(query, MongoMsgRecord.class, collectionName);
        long total = mongoTemplate.count(Query.query(criteria), MongoMsgRecord.class,collectionName);
        log.debug("MsgRecordMongoRepositoryImpl.query total:{} skip:{} limit:{} query:{} params:{} mongoPcRecords:{}", total, skip, pageInfo.getPageSize(), query, params, mongoPcRecords);
        return PageResult.builder()
                .data(mongoPcRecords)
                .total(total)
                .build();
    }

    @Override
    public Map<Integer, Integer> countChannel(SearchMessageRecordDTO searchMessageRecordDTO) {
        Map<Integer, Integer> countChannelMap = Maps.newHashMap();
        List<AggregationOperation> aggregationOperationList = Lists.newArrayList();
        if (IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getStime()) || IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getEtime())) {
            Criteria criteria = new Criteria();
            if (IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getStime()) && IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getEtime())) {
                criteria.and("send_time").gte(searchMessageRecordDTO.getStime()).lte(searchMessageRecordDTO.getEtime());
            }else if (IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getStime())){
                criteria.and("send_time").gte(searchMessageRecordDTO.getStime());
            }else if (IntegerUtils.isGtLimitValue(searchMessageRecordDTO.getEtime())){
                criteria.and("send_time").lte(searchMessageRecordDTO.getEtime());
            }
            aggregationOperationList.add(Aggregation.match(criteria));
        }
        aggregationOperationList.add(Aggregation.group("channel").count().as("count"));
        try {
            Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList);
            AggregationResults<MsgRecordWrapper.NoteRecordCountChannel> noteRecordCountChannelAggregationResults = mongoTemplate.aggregate(aggregation, MsgConstant.COLLECTION_NOTE_RECORD, MsgRecordWrapper.NoteRecordCountChannel.class);
            for (MsgRecordWrapper.NoteRecordCountChannel noteRecordCountChannel : noteRecordCountChannelAggregationResults.getMappedResults()) {
                if (IntegerUtils.isMinValue(noteRecordCountChannel.getId())){
                    countChannelMap.put(noteRecordCountChannel.getId(), noteRecordCountChannel.getCount());
                }
            }
            log.debug("countChannel countChannelMap:{} noteRecordCountChannelList:{}", countChannelMap, noteRecordCountChannelAggregationResults.getMappedResults());
        } catch (Exception e) {
            log.warn("countChannel error e:{}", e);
        }
        return countChannelMap;
    }

    /**
     * 生成综合查询的条件
     *
     * @param params
     * @return
     */
    private Criteria generateQueryCriteria(ListMsgDTO params) {
        SearchTime searchTime = params.getSearchTime();
        Criteria criteria = new Criteria();
        if (searchTime != null) {
            criteria = criteria.and("insert_time").gte(searchTime.getStartTime());
            criteria = criteria.and("insert_time").lte(searchTime.getEndTime());
        }
        if (StringUtils.isNotBlank(params.getTitle())) {
            Pattern pattern = Pattern.compile("^.*" + params.getTitle() + ".*$",
                    Pattern.CASE_INSENSITIVE);
            criteria = criteria.and("title").regex(pattern);
        }
        if (StringUtils.isNotBlank(params.getContent())) {
            Pattern pattern = Pattern.compile("^.*" + params.getContent() + ".*$",
                    Pattern.MULTILINE);
            criteria = criteria.and("content").regex(pattern);
        }
        if (IntegerUtils.isMinValue(params.getIsRead())) {
            criteria = criteria.and("is_read").is(params.getIsRead());
        }
        if (IntegerUtils.isGtLimitValue(params.getYid())) {
            criteria = criteria.and("bizdata.yid").is(params.getYid());
        }
        if (StringUtils.isNotBlank(params.getUserId())) {
            criteria = criteria.and("target.user_id").is(params.getUserId());
        }
        // 发送目标
        if (StringUtils.isNotEmpty(params.getTargetContact())) {
            criteria = criteria.and("target.contact").is(params.getTargetContact());
        }

        // 发送的对象类型
        if (IntegerUtils.isGtLimitValue(params.getToUserType())) {
            criteria.andOperator(Criteria.where("target.user_type").is(params.getToUserType()));
        }

        // 节点
        if (IntegerUtils.isGtLimitValue(params.getNoteId())) {
            criteria.andOperator(Criteria.where("bizdata.node_id").is(params.getNoteId()).orOperator(
                    Criteria.where("bizdata.node_id").is(String.valueOf(params.getNoteId()))
            ));
        }

        // 发送状态
        if (IntegerUtils.isGtLimitValue(params.getSendStatus())) {
            criteria.andOperator(Criteria.where("status").is(params.getSendStatus()));
        }

        // 模板id
        if (IntegerUtils.isGtLimitValue(params.getTid())) {
            criteria.andOperator(Criteria.where("tid").is(params.getTid()));
        }
        return criteria;
    }
}