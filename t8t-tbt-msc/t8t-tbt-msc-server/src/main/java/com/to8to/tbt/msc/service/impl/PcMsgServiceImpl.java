package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.SendMsgDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.PcMsgRequestDTO;
import com.to8to.tbt.msc.entity.*;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordBizData;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordList;
import com.to8to.tbt.msc.repository.mongo.PcRecordComplexRepository;
import com.to8to.tbt.msc.repository.mongo.TemplateRepository;
import com.to8to.tbt.msc.service.AsyncTaskService;
import com.to8to.tbt.msc.service.ComplexMessageService;
import com.to8to.tbt.msc.service.PcMsgService;
import com.to8to.tbt.msc.utils.*;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.PcRecordVO;
import com.to8to.tbt.msc.vo.SendMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author edmund.yu
 */
@Slf4j
@Service
public class PcMsgServiceImpl implements PcMsgService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private PcRecordComplexRepository pcRecordComplexRepository;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private ComplexMessageService complexMessageService;

    @Autowired
    private ValueOperations<String, Map<String, Integer>> valueOperations;

    @Override
    public MsgCenterResponse sendPcMsg(PcMsgRequestDTO pcMsgRequestDTO) {

        ValidUtils.valid(pcMsgRequestDTO);
        List<MongoMsgTemplate> mongoMsgTemplateList;

        mongoMsgTemplateList = templateRepository.getTemplateByNodeAndToUserType(Integer.valueOf(pcMsgRequestDTO.getNodeId()), pcMsgRequestDTO.getToUserType());

        if (mongoMsgTemplateList == null || mongoMsgTemplateList.size() == 0) {
            log.info("can not find template with node:{} and toUserType:{}", Integer.valueOf(pcMsgRequestDTO.getNodeId()), pcMsgRequestDTO.getToUserType());
            throw new RPCException(MyExceptionStatus.TEMPLATE_NOT_FOUND);
        }
        for (MongoMsgTemplate template : mongoMsgTemplateList) {
            String contact = null;

            if (template.getSendType() == null || !(IntegerUtils.isInteger(template.getSendType()))) {
                log.info("template's sendType is wrong with id:{}", template.getId());
                continue;
            }

            int sendType = Integer.parseInt(template.getSendType());
            switch (sendType) {
                case 1:
                    contact = pcMsgRequestDTO.getContactParams().get("phone");
                    break;
                case 2:
                    contact = pcMsgRequestDTO.getContactParams().get("email");
                    break;
                case 3:
                    contact = pcMsgRequestDTO.getContactParams().get("weixin");
                    break;
                case 4:
                    contact = pcMsgRequestDTO.getContactParams().get("app");
                    break;
                case 5:
                    contact = pcMsgRequestDTO.getToUserId();
                    break;
                default:
                    break;
            }
            if (StringUtils.isEmpty(contact)) {
                continue;
            }

            //设置发送目标
            RecordTarget target = new RecordTarget();
            target.setSendType(Integer.parseInt(template.getSendType()));
            target.setContact(contact);
            target.setUserType(pcMsgRequestDTO.getToUserType());
            target.setUserId(pcMsgRequestDTO.getToUserId());
            List<RecordTarget> targetList = new ArrayList<>();
            targetList.add(target);

            //根据参数设置内容
            String content;
            List<String> params = new ArrayList<>();
            String wordIds = template.getWordIds();
            if (StringUtils.isNotEmpty(wordIds)) {
                String[] wordId = wordIds.split(",");
                for (String id : wordId) {
                    if (pcMsgRequestDTO.getParams().get(id) == null){
                        throw new RPCException(MyExceptionStatus.KEYWORD_ID_INVALID);
                    }
                    params.add(pcMsgRequestDTO.getParams().get(id));
                }
                content = MsgCenterUtils.replaceParam(params, template.getContent());
            } else {
                content = template.getContent();
            }

            // 设置预留字段bizdata

            //根据参数设置链接
            String link;
            List<String> urlParams = new ArrayList<>();
            String urlIds = template.getUrlParamIds();
            if (StringUtils.isNotEmpty(urlIds)) {
                String[] urlId = urlIds.split(",");
                for (String id : urlId) {
                    if (pcMsgRequestDTO.getUrlParams().get(id) == null){
                        throw new RPCException(MyExceptionStatus.KEYWORD_ID_INVALID);
                    }
                    urlParams.add(pcMsgRequestDTO.getUrlParams().get(id));
                }
                link = MsgCenterUtils.replaceParam(urlParams, template.getLink());
            } else {
                link = template.getLink();
            }

            //根据参数设置标题
            String title;
            List<String> titleParams = new ArrayList<>();
            String titleIds = template.getTitleParamIds();
            if (StringUtils.isNotEmpty(titleIds)) {
                String[] titleId = titleIds.split(",");
                for (String id : titleId) {
                    if (pcMsgRequestDTO.getTitleParams().get(id) == null){
                        throw new RPCException(MyExceptionStatus.KEYWORD_ID_INVALID);
                    }
                    titleParams.add(pcMsgRequestDTO.getTitleParams().get(id));
                }
                title = MsgCenterUtils.replaceParam(titleParams, template.getTitle());
            } else {
                title = template.getTitle();
            }

            PcWrapper.BizData bizData = new PcWrapper.BizData();
            bizData.setTitle(title);
            bizData.setLink(link);
            bizData.setNodeId(pcMsgRequestDTO.getNodeId());
            bizData.setLevel(IntegerUtils.intValueAsDefault(pcMsgRequestDTO.getLevel()));
            bizData.setBigType(template.getNodeCategory());
            bizData.setSmallType(template.getSmallCategory());
            bizData.setUserType(pcMsgRequestDTO.getToUserType());
            bizData.setPriority(template.getPriority());

            //设置请求消息
            SendMsgRequest request = new SendMsgRequest();
            request.setFromUserId(pcMsgRequestDTO.getFromUserId());
            request.setNs("pc");
            request.setTitle(template.getTitle());
            request.setTargets(targetList);
            request.setContent(content);
            request.setBizData(bizData);

            log.info("发送PC消息：{}", JSONObject.toJSONString(request));

            asyncTaskService.submit(() -> {
                SendMsgDTO sendMsgDTO = DozerUtils.map(request, SendMsgDTO.class);
                SendMsgVO sendMsgVO = complexMessageService.sendMsg(sendMsgDTO);
                if (sendMsgVO.getCode() == 200) {
                    Map<String, Integer> map = valueOperations.get(target.getUserId());
                    if (map != null) {
                        Integer num = map.get(template.getNodeCategory().toString());
                        if (num == null) {
                            updateUnread(template.getNodeCategory().toString());
                        } else {
                            map.put(template.getNodeCategory().toString(), num + 1);
                            valueOperations.set(target.getUserId(), map, 3600, TimeUnit.SECONDS);
                        }
                        log.info("更新大类未读消息数，uid:{},map:{}", target.getUserId(), map);
                    }
                }
            });
        }
        return Response.SUCCESS;
    }

    @Override
    public PcRecordVO getMsgHuang(JSONArray params) {

        String uid = params.getString(0);
        Integer num = params.getInteger(1);

        Preconditions.checkStringNotEmpty(uid);
        if (num == null) {
            num = 0;
        }

        MongoPcRecord mongoPcRecord = pcRecordComplexRepository.getMsgHuang(uid, num);
        Object object = JSONObject.toJSON(mongoPcRecord.getBizData());
        PcRecordVO pcRecordVO = DozerUtils.map(mongoPcRecord, PcRecordVO.class);
        pcRecordVO.setBizDataString(object.toString());
        pcRecordVO.setNickId(pcRecordVO.getId());
        return pcRecordVO;
    }

    @Override
    public MsgCenterResponse delMsg(JSONArray params) {

        String id = params.getString(1);
        Preconditions.checkStringNotEmpty(id);
        pcRecordComplexRepository.delMsg(id);
        return Response.SUCCESS;
    }

    @Override
    public Map<String, Integer> getUnreadBig(String uid) {

        Map<String, Integer> mapResult = new HashMap<>(10);
        Map<String, Integer> map = valueOperations.get(uid);
        if (map != null && map.size() != 0) {
            log.info("从memcached中获取用户未读分类的数量:uid:{}, 对应的详情：{}", uid, map);
            // 把未读消息数小于0置0
            for (String i : map.keySet()) {
                if (map.get(i) < 0) {
                    mapResult.put(i, 0);
                } else {
                    mapResult.put(i, map.get(i));
                }
            }
        } else {
            mapResult = updateUnread(uid);
        }
        return mapResult;
    }

    @Override
    public MsgCenterResponse setSmallNeed(JSONArray params) {

        String uid = params.getString(0);
        List<Integer> smalls = jsonArrayToList(params.getJSONArray(1), Integer.class);
        Preconditions.checkStringNotEmpty(uid);
        Preconditions.checkNotNull(smalls, MyExceptionStatus.PARAMS_CONTAINS_NULL);

        try {
            pcRecordComplexRepository.setSmallNeed(uid, smalls);
        } catch (Exception e) {
            log.warn("PCMsgServiceImpl.setSmallNeed throw exception:");
            return Response.FAIL;
        }
        return Response.SUCCESS;
    }

    @Override
    public List<Integer> getUserSmallNeed(String uid) {

        List<Integer> smalls = pcRecordComplexRepository.getUserSmallNeed(uid);
        return smalls;
    }

    @Override
    public ListMsgVO searchMsgBig(JSONArray params) {

        ListMsgDTO listMsgDTO = JSONObject.toJavaObject(params.getJSONObject(0), ListMsgDTO.class);
        ValidUtils.valid(listMsgDTO);

        Integer bigType = params.getInteger(1);
        ListMsgVO result = ListMsgVO.builder().msgRecords(null)
                .totalPages(0L)
                .totalRecords(0L).build();
        if (bigType == null) {
            bigType = 0;
        }

        if (listMsgDTO.getPageInfo() == null){
            listMsgDTO.setPageInfo(new PageInfo(1, 10));
        }
        PageInfoUtils.CreatDefaultPageInfo(listMsgDTO.getPageInfo());
        int currentPage = listMsgDTO.getPageInfo().getCurrPage();
        int pageSize = listMsgDTO.getPageInfo().getPageSize();
        int skip = pageSize * (currentPage - 1);

        MongoPcRecordList mongoPcRecordList;
        long startTime = System.currentTimeMillis();

        log.info("获取大类消息：req:{}, bigType:{}", JSONObject.toJSONString(listMsgDTO), bigType);
        try {
            mongoPcRecordList = pcRecordComplexRepository.searchMsg(listMsgDTO, bigType, skip, pageSize);
        } catch (Exception e) {
            log.warn("PCMsgServiceImpl.searchMsgBig throw exception:");
            return result;
        }

        Preconditions.checkNotNull(mongoPcRecordList.getMongoPcRecords(), MyExceptionStatus.MESSAGE_CANNOT_FIND);
        long queryTime = System.currentTimeMillis() - startTime;
        long total = mongoPcRecordList.getTotalRecords();
        long queryCountTime = System.currentTimeMillis() - startTime;
        log.info("searchMsgBig run end time:{} queryCountTime:{} bigType:{} count:{}", queryTime, queryCountTime, bigType, total);

        long totalPage = (total % pageSize == 0) ? (total / pageSize) : (total / pageSize + 1);
        List<MsgRecord> msgRecordList = new ArrayList<>();
        for (MongoPcRecord record : mongoPcRecordList.getMongoPcRecords()) {
            MsgRecord msgRecord = DozerUtils.map(record, MsgRecord.class);
            if (record.getBizData() != null) {
                Object object = JSONObject.toJSON(record.getBizData());
                msgRecord.setBizdata(object.toString());
            } else {
                msgRecord.setBizdata("");
            }
            msgRecord.setNickId(msgRecord.getId());
            msgRecordList.add(msgRecord);
        }
        result.setMsgRecords(msgRecordList);
        result.setTotalPages(totalPage);
        result.setTotalRecords(total);
        log.info("搜索大类消息返回值：{}", JSONObject.toJSONString(result));
        return result;
    }

    @Override
    public MsgCenterResponse setMsgRead(String id) {

        asyncTaskService.submit(() -> {
            if (StringUtils.isEmpty(id)) {
                log.info("id为空");
                return;
            }
            MongoPcRecord mongoPcRecord = pcRecordComplexRepository.getPcMsgById(id);
            if (mongoPcRecord == null) {
                log.info("未找到ID:{}对应消息", id);
            } else if (mongoPcRecord.getIsRead() == 0) {
                try {
                    pcRecordComplexRepository.setPcMsgRead(id);
                } catch (Exception e) {
                    log.warn("Mongo更新操作失败");
                }

                Map<String, Integer> map = valueOperations.get(mongoPcRecord.getTarget().getUserId());
                String bigType = new Integer(mongoPcRecord.getBizData().getBigType()).toString();
                if (map == null || map.get(bigType) == null) {
                    updateUnread(mongoPcRecord.getTarget().getUserId());
                    return;
                } else {
                    int num = map.get(bigType);
                    map.put(bigType, num - 1);
                    log.info("更新大类未读消息数，uid:{},map:{}", mongoPcRecord.getTarget().getUserId(), map);
                    long date = 60 * 60;
                    valueOperations.set(mongoPcRecord.getTarget().getUserId(), map, date, TimeUnit.SECONDS);
                }
            } else {
                log.info("消息已经是已读");
                return;
            }
        });
        return Response.SUCCESS;
    }

    private Map<String, Integer> updateUnread(String uid) {
        Map<String, Integer> map = pcRecordComplexRepository.getUnreadBig(uid);
        log.info("更新大类未读消息数，uid:{},map:{}", uid, map);
        long date = 60 * 60;
        valueOperations.set(uid, map, date, TimeUnit.SECONDS);
        return map;
    }

    private <T> List<T> jsonArrayToList(JSONArray array, Class<T> c) {
        List<T> list = new ArrayList<T>();
        if (array == null) {
            return list;
        }
        for (Object o : array) {
            if (o instanceof Integer || o instanceof String) {
                list.add((T) o);
            } else {
                list.add(JSONObject.toJavaObject((JSONObject) o, c));
            }
        }

        return list;
    }
}
