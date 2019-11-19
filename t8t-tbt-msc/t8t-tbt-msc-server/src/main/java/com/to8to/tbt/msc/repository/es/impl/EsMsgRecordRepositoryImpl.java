package com.to8to.tbt.msc.repository.es.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.enumeration.MsgConfigTypeEnum;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.service.ConfigureService;
import com.to8to.tbt.msc.service.ExternalService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.vo.MsgcConfigureVO;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.*;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class EsMsgRecordRepositoryImpl implements EsMsgRecordRepository {

    @Value(value = "${es.index}")
    public String index;

    @Value(value = "${es.sms_record_type}")
    public String smsRecordType;

    @Value(value = "${es.app_record_type}")
    public String appRecordType;

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private ExternalService externalService;

    private static RangeFilterBuilder getTimeRangeFilter(String fullFieldName, long start, long end) {
        RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter(fullFieldName);
        rangeFilter.from(start);
        if (end != 0) {
            rangeFilter.to(end);
        }
        return rangeFilter;
    }

    @Override
    public SearchMessageRecordVO querySmsRecord(SearchMessageRecordDTO params) {
        String[] fieldsInclude = {"id", "large_type", "large_type_des", "small_type", "small_type_des", "node_type", "node_type_des", "target_type", "pm_module", "tid", "title", "is_active", "is_auto", "send_type", "msg_content", "is_ground", "channel_type", "phoneid", "send_status", "send_time", "error_code", "error_describle"};
        return generateRecordRequestBuilder(params, fieldsInclude, smsRecordType);
    }

    @Override
    public SearchMessageRecordVO queryAppRecord(SearchMessageRecordDTO params) {
        String[] fields = {"id", "large_type", "large_type_des", "small_type", "small_type_des", "node_type", "node_type_des", "target_type", "pm_module", "tid", "title", "is_active", "is_auto", "send_type", "app_content", "app_id", "yid", "send_status", "send_time", "is_read", "uid", "biz_param", "sender"};
        if (StringUtils.isNotBlank(params.getField())) {
            fields = params.getField().split(",");
        }
        return generateRecordRequestBuilder(params, fields, appRecordType);
    }

    @Override
    public Long getAppMsgCount(List<Integer> uidList, List<Integer> tidList, Integer isRead) {
        Long count = 0L;
        try {
            SearchMessageRecordDTO params = SearchMessageRecordDTO.builder()
                    .uids(uidList)
                    .tids(tidList)
                    .isRead(isRead)
                    .build();
            String[] fields = {"id"};
            count = generateRecordRequestBuilder(params, fields, appRecordType).getTotal();
        } catch (Exception e) {
            log.error("[getAppMsgCount] count:{}, error:{}", count, e);
        }
        return count;
    }

    @Override
    public Long getAppMsgCountByNodeList(List<Integer> uidList, List<Integer> nodeList, Integer isRead) {
        Long count = 0L;
        try {
            SearchMessageRecordDTO params = SearchMessageRecordDTO.builder()
                    .uids(uidList)
                    .nodeIds(nodeList)
                    .isRead(isRead)
                    .build();
            String[] fields = {"id"};
            count = generateRecordRequestBuilder(params, fields, appRecordType).getTotal();
        } catch (Exception e) {
            log.error("[getAppMsgCount] count:{}, error:{}", count, e);
        }
        return count;
    }

    @Override
    public Map<String, Long> countTemplateSend(SearchMessageRecordDTO searchMessageRecordDTO) {
        return countRecordSend(searchMessageRecordDTO, smsRecordType, "tid");
    }

    @Override
    public Map<String, Long> countChannelType(SearchMessageRecordDTO searchMessageRecordDTO) {
        return countRecordSend(searchMessageRecordDTO, smsRecordType, "channel_type");
    }

    @Override
    public Map<String, Long> countAppMessageRecord(SearchMessageRecordDTO searchMessageRecordDTO) {
        return countRecordSend(searchMessageRecordDTO, appRecordType, "tid");
    }

    @Override
    public List<NoteReplyVO> getNoteRecord(Integer phoneId) {
        List<NoteReplyVO> noteReplyVOList = new ArrayList<>();
        try {
            BoolFilterBuilder boolFilter = boolFilter();
            boolFilter.must(termFilter("phoneid", phoneId));
            String[] fieldsInclude = {"node_type_des", "msg_content", "send_time"};
            SearchResponse resp = transportClient.prepareSearch(index).setTypes(smsRecordType).setFetchSource(fieldsInclude, null).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilter))
                    .setFrom(0)
                    .setSize(1000)
                    .get();
            SearchHit[] hits = resp.getHits().getHits();
            for (SearchHit hit : hits) {
                try {
                    NoteReplyVO noteReplyVO = JSONObject.toJavaObject(new JSONObject(hit.getSource()), NoteReplyVO.class);
                    noteReplyVO.setType(1);
                    noteReplyVOList.add(noteReplyVO);
                } catch (Exception e) {
                    log.warn("getNoteRecord toJavaObject fail item:{} e:{}", hit.getSource(), e);
                }
            }
        } catch (Exception e) {
            log.warn("getNoteRecord es search error:{}", e);
        }
        return noteReplyVOList;
    }

    @Override
    public void saveRecord(JSONObject data) {
        try {
            String id = data.getString("id");
            boolean isExists = transportClient.prepareGet(index, smsRecordType, id).get().isExists();
            if (!isExists) {
                transportClient.prepareIndex(index, smsRecordType, id).setSource(data).get();
            } else {
                transportClient.prepareUpdate(index, smsRecordType, id).setDoc(data.toJSONString()).get();
            }
        }catch (Exception e){
            log.warn(LogUtils.buildExceptionTemplate("data"), data, e);
        }
    }

    /**
     * 根据指定字段聚合统计短信的发送量
     *
     * @param searchMessageRecordDTO
     * @param field
     * @return
     */
    private Map<String, Long> countRecordSend(SearchMessageRecordDTO searchMessageRecordDTO, String type, String field){
        Map<String, Long> countMap = Maps.newHashMap();
        try {
            SearchRequestBuilder searchReqBuilder = transportClient.prepareSearch(index).setTypes(type).setSearchType(SearchType.COUNT)
                    .setQuery(getBoolFilterBuilder(searchMessageRecordDTO)).addAggregation(AggregationBuilders.terms(field).field(field));
            SearchResponse searchResponse = searchReqBuilder.get();
            if (searchResponse != null) {
                Terms terms = searchResponse.getAggregations().get(field);
                for (Terms.Bucket entry : terms.getBuckets()) {
                    countMap.put(entry.getKey(), entry.getDocCount());
                }
            }
        } catch (Exception e) {
            log.warn("countSmsRecordSend error type:{} field:{} params:{} e:{}", type, field, searchMessageRecordDTO, e);
        }
        return countMap;
    }

    /**
     * 生成查询消息记录的请求
     *
     * @param params
     * @param fields
     * @return
     */
    private SearchMessageRecordVO generateRecordRequestBuilder(SearchMessageRecordDTO params, String[] fields, String typeName) {
        List<JSONObject> data = new ArrayList<>();
        SearchMessageRecordVO searchMessageRecordVO = SearchMessageRecordVO.builder()
                .result(data)
                .total(0L)
                .build();
        PageInfo pageInfo = params.getPageInfo();
        int start = 0;
        int size = 10;
        if (pageInfo != null){
            start = (pageInfo.getCurrPage() - 1) * pageInfo.getPageSize();
            size = pageInfo.getPageSize();
        }
        QueryBuilder queryBuilder = getBoolFilterBuilder(params);
        SearchRequestBuilder builder = transportClient.prepareSearch(index)
                .setFetchSource(fields, null)
                .setQuery(queryBuilder)
                .setTypes(typeName)
                .addSort("send_time", SortOrder.DESC)
                .setFrom(start)
                .setSize(size);
        SearchResponse response = builder.get();
        if (response != null) {
            SearchHit[] hists = response.getHits().getHits();
            for (SearchHit hit : hists) {
                JSONObject item = new JSONObject(hit.getSource());
                data.add(item);
            }
            searchMessageRecordVO.setTotal(response.getHits().getTotalHits());
        }
        log.debug("MsgRecordRepositoryImpl.generateRecordRequestBuilder index:{} typeName:{} params:{} builder:{} searchMessageRecordVO:{}", index, typeName, params, builder, searchMessageRecordVO);
        return searchMessageRecordVO;
    }

    /**
     * 生成短信记录查询条件
     *
     * @param searchMessageRecordDTO
     * @return
     */
    private QueryBuilder getBoolFilterBuilder(SearchMessageRecordDTO searchMessageRecordDTO) {
        BoolFilterBuilder boolFilter = FilterBuilders.boolFilter();
        if (searchMessageRecordDTO.getStime() == null) {
            searchMessageRecordDTO.setStime(searchMessageRecordDTO.getStartTime());
        }
        if (searchMessageRecordDTO.getEtime() == null) {
            searchMessageRecordDTO.setEtime(searchMessageRecordDTO.getEndTime());
        }
        boolFilter = generateFilterBuilderByConfigType(boolFilter, searchMessageRecordDTO.getConfigType(), searchMessageRecordDTO.getCid());
        boolFilter = generateFilterBuilderByPhone(boolFilter, searchMessageRecordDTO.getPhone(), searchMessageRecordDTO.getPhoneId());
        boolFilter = generateFilterBuilderBySendStatus(boolFilter, searchMessageRecordDTO.getSendStatus());
        boolFilter = generateFilterBuilderByChannelType(boolFilter, searchMessageRecordDTO.getChannelType());
        boolFilter = generateFilterBuilderByTid(boolFilter, searchMessageRecordDTO.getTid());
        boolFilter = generateFilterBuilderByTidList(boolFilter, searchMessageRecordDTO.getTids());
        boolFilter = generateFilterBuilderByPmModule(boolFilter, searchMessageRecordDTO.getPmModule());
        boolFilter = generateFilterBuilderBySendType(boolFilter, searchMessageRecordDTO.getSendType());
        boolFilter = generateFilterBuilderByTargetType(boolFilter, searchMessageRecordDTO.getTargetType());
        boolFilter = generateFilterBuilderByIsAuto(boolFilter, searchMessageRecordDTO.getIsAuto());
        boolFilter = generateFilterBuilderByIsActive(boolFilter, searchMessageRecordDTO.getIsActive());
        boolFilter = generateFilterBuilderByNodeId(boolFilter, searchMessageRecordDTO.getNodeId());
        boolFilter = generateFilterBuilderByNodeType(boolFilter, searchMessageRecordDTO.getNodeType());
        boolFilter = generateFilterBuilderByNodeIds(boolFilter, searchMessageRecordDTO.getNodeIds());
        boolFilter = generateFilterBuilderByAppId(boolFilter, searchMessageRecordDTO.getAppId());
        boolFilter = generateFilterBuilderByAppIds(boolFilter, searchMessageRecordDTO.getAppIds());
        boolFilter = generateFilterBuilderByUid(boolFilter, searchMessageRecordDTO.getUid());
        boolFilter = generateFilterBuilderByUidList(boolFilter, searchMessageRecordDTO.getUids());
        boolFilter = generateFilterBuilderByIsRead(boolFilter, searchMessageRecordDTO.getIsRead());
        boolFilter = generateFilterBuilderByYid(boolFilter, searchMessageRecordDTO.getYid());
        boolFilter = generateFilterBuilderByYidList(boolFilter, searchMessageRecordDTO.getYids());
        boolFilter = generateFilterBuilderBySendTime(boolFilter, searchMessageRecordDTO.getStime(), searchMessageRecordDTO.getEtime());
        if (FilterBuilders.boolFilter().toString().equals(boolFilter.toString())) {
            return QueryBuilders.matchAllQuery();
        } else if (boolFilter instanceof BoolFilterBuilder) {
            boolFilter.cache(true);
        }
        return QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilter);
    }

    /**
     * 根据业务类型生成查询条件
     *
     * @param boolFilterBuilder
     * @param configType
     * @param cid
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByConfigType(BoolFilterBuilder boolFilterBuilder, Integer configType, Integer cid) {
        if (IntegerUtils.isGtLimitValue(configType) && IntegerUtils.isGtLimitValue(cid)) {
            if (configType.intValue() == MsgConfigTypeEnum.BUSINESS_TYPE.getCode()) {
                List<MsgcConfigureVO> msgcConfigureVOList = configureService.searchConfiguration(MsgConstant.CONFIG_TYPE_BUSINESS_ITEM, cid, "");
                if (!msgcConfigureVOList.isEmpty()) {
                    List<Integer> cidList = msgcConfigureVOList.stream().map(MsgcConfigureVO::getCid).collect(Collectors.toList());
                    boolFilterBuilder.must(termsFilter("small_type", cidList));
                }
            } else if (configType.intValue() == MsgConfigTypeEnum.BUSINESS_ITEM.getCode()) {
                boolFilterBuilder.must(termFilter("small_type", cid));
            } else if (configType.intValue() == MsgConfigTypeEnum.SEND_NODE.getCode()) {
                boolFilterBuilder.must(termFilter("node_type", cid));
            }
        }
        return boolFilterBuilder;
    }

    /**
     * 根据手机号生成查询条件
     *
     * @param boolFilterBuilder
     * @param phone
     * @param phoneId
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByPhone(BoolFilterBuilder boolFilterBuilder, String phone, Integer phoneId) {
        if (StringUtils.isNotBlank(phone)) {
            phoneId = externalService.getIdByPhone(phone);
        }
        if (IntegerUtils.isGtLimitValue(phoneId)) {
            boolFilterBuilder.must(termFilter("phoneid", phoneId));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据发送状态生成查询条件
     *
     * @param boolFilterBuilder
     * @param sendStatus
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderBySendStatus(BoolFilterBuilder boolFilterBuilder, Integer sendStatus) {
        if (IntegerUtils.isMinValue(sendStatus)) {
            boolFilterBuilder.must(termFilter("send_status", sendStatus));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据发送渠道生成查询条件
     *
     * @param boolFilterBuilder
     * @param channelType
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByChannelType(BoolFilterBuilder boolFilterBuilder, Integer channelType) {
        if (IntegerUtils.isGtLimitValue(channelType)) {
            boolFilterBuilder.must(termFilter("channel_type", channelType));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据模板ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param tid
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByTid(BoolFilterBuilder boolFilterBuilder, Integer tid) {
        if (IntegerUtils.isGtLimitValue(tid)) {
            boolFilterBuilder.must(termFilter("tid", tid));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批量模板ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param tidList
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByTidList(BoolFilterBuilder boolFilterBuilder, List<Integer> tidList) {
        if (tidList != null && !tidList.isEmpty()) {
            boolFilterBuilder.must(termsFilter("tid", tidList));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据产品模块生成查询条件
     *
     * @param boolFilterBuilder
     * @param pmModule
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByPmModule(BoolFilterBuilder boolFilterBuilder, Integer pmModule) {
        if (IntegerUtils.isGtLimitValue(pmModule)) {
            boolFilterBuilder.must(termFilter("pm_module", pmModule));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据发送类型生成查询条件
     *
     * @param boolFilterBuilder
     * @param sendType
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderBySendType(BoolFilterBuilder boolFilterBuilder, Integer sendType) {
        if (IntegerUtils.isGtLimitValue(sendType)) {
            boolFilterBuilder.must(termFilter("send_type", sendType));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据发送目标生成查询条件
     *
     * @param boolFilterBuilder
     * @param targetType
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByTargetType(BoolFilterBuilder boolFilterBuilder, Integer targetType) {
        if (IntegerUtils.isGtLimitValue(targetType)) {
            boolFilterBuilder.must(termFilter("target_type", targetType));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据是否手动触发短信发送生成查询条件
     *
     * @param boolFilterBuilder
     * @param isAuto
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByIsAuto(BoolFilterBuilder boolFilterBuilder, Integer isAuto) {
        if (IntegerUtils.isMinValue(isAuto)) {
            boolFilterBuilder.must(termFilter("is_auto", isAuto));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据模板是否启用生成查询条件
     *
     * @param boolFilterBuilder
     * @param isActive
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByIsActive(BoolFilterBuilder boolFilterBuilder, Integer isActive) {
        if (IntegerUtils.isMinValue(isActive)) {
            boolFilterBuilder.must(termFilter("is_active", isActive));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据节点ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param nodeId
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByNodeId(BoolFilterBuilder boolFilterBuilder, Integer nodeId) {
        if (IntegerUtils.isGtLimitValue(nodeId)) {
            boolFilterBuilder.must(termFilter("nodeid", nodeId));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据节点类型生成查询条件
     *
     * @param boolFilterBuilder
     * @param nodeType
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByNodeType(BoolFilterBuilder boolFilterBuilder, Integer nodeType) {
        if (IntegerUtils.isGtLimitValue(nodeType)) {
            boolFilterBuilder.must(termFilter("node_type", nodeType));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批量节点ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param nodeIds
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByNodeIds(BoolFilterBuilder boolFilterBuilder, List<Integer> nodeIds) {
        if (nodeIds != null && !nodeIds.isEmpty()) {
            boolFilterBuilder.must(termsFilter("node_type", nodeIds));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据应用ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param appId
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByAppId(BoolFilterBuilder boolFilterBuilder, Integer appId) {
        if (IntegerUtils.isGtLimitValue(appId)) {
            boolFilterBuilder.must(termFilter("app_id", appId));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批量应用ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param appIds
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByAppIds(BoolFilterBuilder boolFilterBuilder, List<Integer> appIds) {
        if (appIds != null && !appIds.isEmpty()) {
            boolFilterBuilder.must(termsFilter("app_id", appIds));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据作者ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param uid
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByUid(BoolFilterBuilder boolFilterBuilder, Integer uid) {
        if (IntegerUtils.isGtLimitValue(uid)) {
            boolFilterBuilder.must(termFilter("uid", uid));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批量作者ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param uidList
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByUidList(BoolFilterBuilder boolFilterBuilder, List<Integer> uidList) {
        if (uidList != null && !uidList.isEmpty()) {
            boolFilterBuilder.must(termsFilter("uid", uidList));
        }
        return boolFilterBuilder;
    }


    /**
     * 根据消息是否已读生成查询条件
     *
     * @param boolFilterBuilder
     * @param isRead
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByIsRead(BoolFilterBuilder boolFilterBuilder, Integer isRead) {
        if (IntegerUtils.isMinValue(isRead)) {
            boolFilterBuilder.must(termFilter("is_read", isRead));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据项目ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param yid
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByYid(BoolFilterBuilder boolFilterBuilder, Integer yid) {
        if (IntegerUtils.isGtLimitValue(yid)) {
            boolFilterBuilder.must(termFilter("yid", yid));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批量项目ID生成查询条件
     *
     * @param boolFilterBuilder
     * @param yidList
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderByYidList(BoolFilterBuilder boolFilterBuilder, List<Integer> yidList) {
        if (yidList != null && !yidList.isEmpty()) {
            boolFilterBuilder.must(termsFilter("yid", yidList));
        }
        return boolFilterBuilder;
    }

    /**
     * 根据批发送时间生成查询条件
     *
     * @param boolFilterBuilder
     * @param startTime
     * @param endTime
     * @return
     */
    private BoolFilterBuilder generateFilterBuilderBySendTime(BoolFilterBuilder boolFilterBuilder, Integer startTime, Integer endTime) {
        if (IntegerUtils.isGtLimitValue(startTime)) {
            boolFilterBuilder.must(new RangeFilterBuilder("send_time").gt(startTime));
        }
        if (IntegerUtils.isGtLimitValue(endTime)) {
            boolFilterBuilder.must(new RangeFilterBuilder("send_time").lt(endTime));
        }
        return boolFilterBuilder;
    }
}
