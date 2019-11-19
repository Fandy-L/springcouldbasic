package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.model.Message;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.MsgRecord;
import com.to8to.tbt.msc.entity.RecordTarget;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.PageResult;
import com.to8to.tbt.msc.entity.mongo.*;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.MsgResult;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.repository.mongo.*;
import com.to8to.tbt.msc.repository.mongo.template.MsgRecordComplexRepository;
import com.to8to.tbt.msc.repository.mysql.ComplexRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.service.MessageCenterService;
import com.to8to.tbt.msc.utils.*;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class MessageCenterServiceImpl implements MessageCenterService {

    @Autowired
    private EsMsgRecordRepository esMsgRecordRepository;

    @Autowired
    private AppRecordRepository appRecordRepository;

    @Autowired
    private MongoAppRecordRepository mongoAppRecordRepository;

    @Autowired
    private MongoPcRecordRepository mongoPcRecordRepository;

    @Autowired
    private MongoNoteRecordRepository mongoNoteRecordRepository;

    @Autowired
    private MongoAppUserMsgRepository mongoAppUserMsgRepository;

    @Autowired
    private MongoMsgReplyRepository mongoMsgReplyRepository;

    @Autowired
    private ComplexRepository complexRepository;

    @Autowired
    private MsgRecordComplexRepository msgRecordComplexRepository;

    @Override
    public SearchMessageRecordVO searchMessageRecord(SearchMessageRecordDTO params) {
        SearchMessageRecordVO searchMessageRecordVO;
        params.setPageInfo(buildPageInfo(params.getPageInfo()));
        params.setSendType(IntegerUtils.intValueAsDefault(params.getSendType(),MsgConstant.SENDTYPE_SMS));
        if (params.getSendType() == MsgConstant.SENDTYPE_SMS) {
            try {
                searchMessageRecordVO = esMsgRecordRepository.querySmsRecord(params);
            } catch (Exception e) {
                log.warn("MessageCenterServiceImpl.searchMessageRecord querySmsRecord exception params:{} e:{}", params, e);
                throw new RPCException(MyExceptionStatus.NETWORK_ERROR);
            }
        } else if (params.getSendType() == MsgConstant.SENDTYPE_APP) {
            try {
                searchMessageRecordVO = esMsgRecordRepository.queryAppRecord(params);
            } catch (Exception e) {
                log.warn("MessageCenterServiceImpl.searchMessageRecord queryAppRecord exception params:{} e:{}", params, e);
                throw new RPCException(MyExceptionStatus.NETWORK_ERROR);
            }
        } else {
            log.warn("searchMessageRecord params error params:{}", params);
            throw new RPCException(MyExceptionStatus.SEND_TYPE_INVALID);
        }
        return searchMessageRecordVO;
    }

    @Override
    public ListMsgVO listMsg(ListMsgDTO params) {
        ListMsgVO listMsgVO = null;
        params.setPageInfo(buildPageInfo(params.getPageInfo()));
        if (params.getSendType() == MsgConstant.SENDTYPE_APP) {
            try {
                listMsgVO = queryAppMsgRecord(params);
            } catch (Exception e) {
                log.warn("MessageCenterServiceImpl.listMsg queryAppRecord exception params:{} e:{}", params, e);
            }
        } else {
            try {
                PageResult pageResult = msgRecordComplexRepository.query(params);
                listMsgVO = formatMongoMsgRecord(params.getPageInfo().getPageSize(), pageResult);
            } catch (Exception e) {
                log.warn("MessageCenterServiceImpl.listMsg complexQuery exception params:{} e:{}", params, e);
            }
        }
        if (listMsgVO == null) {
            listMsgVO = ListMsgVO.builder()
                    .msgRecords(new ArrayList<>())
                    .totalPages(0L)
                    .totalRecords(0L)
                    .build();
        }
        return listMsgVO;
    }

    @Override
    public MsgCenterResponse setAppMsgRead(int id) {
        try {
            boolean status = complexRepository.setAppMsgReaded(id);
            if (!status) {
                setMsgReaded(String.valueOf(id), MsgConstant.APP_RECORD_SEND_TYPE);
                log.warn("MessageCenterServiceImpl.setAppMsgRead error id:{}", id);
                return Response.FAIL;
            }
        } catch (Exception e) {
            log.warn("MessageCenterServiceImpl.setAppMsgRead error id:{} e:{}", id, e);
            return Response.FAIL;
        }
        return Response.SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setAppMsgHasReadBatch(List<Integer> msgIds) {
        try {
            if (msgIds.size() > 0) {
                appRecordRepository.setAppMsgHasReadBatch(msgIds);
            }
        } catch (Exception e) {
            log.warn("setAppMsgHasReadBatch error msgIds:{} e:{}", msgIds, e);
            throw new RPCException(MyExceptionStatus.NETWORK_ERROR);
        }
    }

    @Override
    public MsgCenterResponse setAppMsgHasReadBatch(String msgIdStr) {
        if (StringUtils.isNotBlank(msgIdStr)) {
            try {
                List<Integer> msgIds = Arrays.stream(StringUtils.split(msgIdStr, ",")).map(Integer::parseInt).collect(Collectors.toList());
                this.setAppMsgHasReadBatch(msgIds);
                return Response.SUCCESS;
            } catch (Exception e) {
                log.warn("setAppMsgHasReadBatch exception msgIdStr:{} e:{}", msgIdStr, e);
            }
        }
        return Response.FAIL;
    }

    @Override
    public MsgResult setAppMsgStatusByUidAndNodeId(SetAppMsgStatusNodeDTO setAppMsgStatusNodeDTO) {
        try {
            ValidUtils.valid(setAppMsgStatusNodeDTO);
        }catch (Exception e){
            return MsgResult.builder().result(-1L).fail("参数不合法！").build();
        }
        List<Integer> nodeIds = setAppMsgStatusNodeDTO.getNodeIds();
        int uid = setAppMsgStatusNodeDTO.getUid();
        int isRead = setAppMsgStatusNodeDTO.getIsRead();
        return setAppMsgStatusByUidAndNodeId(uid, nodeIds, isRead);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MsgResult setAppMsgStatusByUidAndNodeId(Integer uid, List<Integer> nodeIds, Integer isRead) {
        Set<Integer> tidList = complexRepository.queryTidListByNodeIds(nodeIds);
        if (tidList == null || tidList.size() == 0) {
            log.warn("setAppMsgStatusByUidAndNodeId nodeIds invalid uid:{} nodeIds:{} isRead:{}", uid, nodeIds, isRead);
            return MsgResult.builder().result(-1L).fail("无效 node_ids ,无法匹配！").build();
        }
        try {
            appRecordRepository.setAppMsgStatusByUidAndTid(uid, isRead, new HashSet<>(tidList));
            Integer count = appRecordRepository.countAllByUidAndTidIn(uid,new HashSet<>(tidList));
            log.debug("setAppMsgStatusByUidAndNodeId uid:{} nodeIds:{} isRead:{} tidList:{}", uid, nodeIds, isRead, tidList);
            return MsgResult.builder()
                    .status(200)
                    .result(count.longValue()).build();
        } catch (Exception e) {
            log.warn("setAppMsgStatusByUidAndNodeId exception uid:{} nodeIds:{} isRead:{} e:{}", uid, nodeIds, isRead, e);
        }
        return MsgResult.builder().result(-1L).fail("设置出错！").build();
    }

    @Override
    public MsgCenterResponse setAppMsgReaderByTidAndUid(List<Integer> tids, Integer uid) {
        try {
            Set<Integer> tidList = new HashSet<>(tids);
            appRecordRepository.setAppMsgStatusByUidAndTid(uid, 1, tidList);
        } catch (Exception e) {
            log.warn("setAppMsgStatusByUidAndTid异常！{}", e);
            return Response.FAIL;
        }
        return Response.SUCCESS;
    }

    @Override
    public Map<Integer, Boolean> getUserMsgState(String userId) {
        Map<Integer, Boolean> retMap = new HashMap<>();
        List<MongoAppUserMsg> userMsgList = mongoAppUserMsgRepository.findAllByUserId(userId);
        for (MongoAppUserMsg userMsgObj : userMsgList) {
            retMap.put(userMsgObj.getYid(), userMsgObj.getRead());
        }
        return retMap;
    }

    @Override
    public MsgCenterResponse setUserMsgRead(JSONArray params, Boolean readFlg) {
        String userId = params.getString(0);
        int yid = params.getIntValue(1);
        try {
            MongoAppUserMsg mongoAppUserMsg = mongoAppUserMsgRepository.findFirstByUserIdAndYid(userId, yid).orElse(null);
            if (mongoAppUserMsg != null) {
                mongoAppUserMsg.setRead(readFlg);
                mongoAppUserMsgRepository.save(mongoAppUserMsg);
            }
        } catch (Exception e) {
            log.warn("setUserMsgReaded异常！{}", e);
            return Response.FAIL;
        }
        return Response.SUCCESS;
    }

    @Override
    public MsgResult setAppMsgStatusByUidAndTid(SetAppMsgStatusByTidDTO setAppMsgStatusByTidDTO) {
        try {
            ValidUtils.valid(setAppMsgStatusByTidDTO);
        }catch (RPCException e){
            return MsgResult.builder().result(-1L).fail(e.getLocalizedMessage()).build();
        }
        int uid = setAppMsgStatusByTidDTO.getUid();
        int isRead = setAppMsgStatusByTidDTO.getIsRead();
        List<Integer> tidList = setAppMsgStatusByTidDTO.getTids();
        try {
            appRecordRepository.setAppMsgStatusByUidAndTid(uid, isRead, new HashSet<>(tidList));
            Integer count = appRecordRepository.countAllByUidAndTidIn(uid, new HashSet<>(tidList));
            return MsgResult.builder()
                    .status(200)
                    .result(count.longValue()).build();
        } catch (Exception e) {
            log.warn("setAppMsgStatusByUidAndTid异常{}", e);
        }
        return MsgResult.builder().result(0L).status(200).build();
    }

    @Override
    public MsgResult getAppMsgRecordCountByUidAndTid(List<Integer> uidList, List<Integer> tidList, Integer isRead) {
        long count = esMsgRecordRepository.getAppMsgCount(uidList, tidList, isRead);
        return MsgResult.builder()
                .result(count)
                .status(200).build();
    }

    @Override
    public MsgResult getAppMsgRecordCountByUidAndNodeId(List<Integer> uidList, List<Integer> nodeList, Integer isRead) {
        long count = esMsgRecordRepository.getAppMsgCountByNodeList(uidList, nodeList, isRead);
        return MsgResult.builder()
                .result(count)
                .status(200).build();
    }

    public void insertMsgReply(String userid, String password) {
        List<MongoMsgReply> mongateUpLinkList = SmsUtils.getMongateUplinkRecord(userid, password);
        if (mongateUpLinkList.size() > 0) {
            log.info("获取上行记录:[{}]", JSONObject.toJSONString(mongateUpLinkList));
            for (MongoMsgReply mr : mongateUpLinkList) {
                mongoMsgReplyRepository.save(mr);
            }
        }
    }

    public void saveAliyunUpLinkRecord() {
        List<Message> messageList = SmsUtils.getAliyunSmsReply();
        for (Message message : messageList) {
            try {
                String messageBody = message.getMessageBodyAsRawString();
                String phone = StringUtils.substringBetween(messageBody, "sender=86", "&content");
                String replyContent = StringUtils.substringBetween(messageBody, "&content=", "&receive_time");
                String replyTime = StringUtils.substringBetween(messageBody, "&receive_time=", "&extend_code");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(replyTime);
                int unixTime = (int) (d.getTime() / 1000);
                MongoMsgReply msgReply = new MongoMsgReply();
                msgReply.setPhone(phone);
                msgReply.setReply(replyContent);
                msgReply.setReplyTime(unixTime);
                mongoMsgReplyRepository.save(msgReply);
            } catch (ParseException e) {
                log.error("[saveAliyunUpLinkRecord] error:{}", e);
            }
        }
    }

    /**
     * 生成分页请求参数
     *
     * @param pageInfo
     * @return
     */
    private PageInfo buildPageInfo(PageInfo pageInfo) {
        if (!(pageInfo instanceof PageInfo)) {
            pageInfo = PageInfo.builder()
                    .currPage(1)
                    .pageSize(MsgConstant.PAGE_SIZE)
                    .build();
        }
        return pageInfo;
    }

    /**
     * 格式化综合消息列表
     *
     * @param pageResult
     * @return
     */
    private ListMsgVO formatMongoMsgRecord(int pageSize, PageResult pageResult) {
        ListMsgVO<MsgRecord> listMsgVO = null;
        List<MongoMsgRecord> mongoPcRecords = pageResult.getData();
        if (mongoPcRecords != null && mongoPcRecords.size() > 0) {
            List<MsgRecord> msgRecords = new ArrayList<>(mongoPcRecords.size());
            long totalPage = pageResult.getTotal() % pageSize == 0 ? pageResult.getTotal() / pageSize : pageResult.getTotal() / pageSize + 1;
            for (MongoMsgRecord mongoMsgRecord : mongoPcRecords) {
                String bizData;
                if (mongoMsgRecord.getBizData() != null) {
                    bizData = JSONObject.toJSONString(mongoMsgRecord.getBizData());
                } else {
                    bizData = "";
                }
                MsgRecord msgRecord = DozerUtils.map(mongoMsgRecord, MsgRecord.class);
                msgRecord.setBizdata(bizData);
                msgRecord.setNickId(mongoMsgRecord.getId());
                msgRecord.setYjindu(mongoMsgRecord.getProcess());
                msgRecord.setBizid(mongoMsgRecord.getBizId());
                msgRecords.add(msgRecord);
            }
            listMsgVO = new ListMsgVO<>();
            listMsgVO.setMsgRecords(msgRecords);
            listMsgVO.setTotalRecords(pageResult.getTotal());
            listMsgVO.setTotalPages(totalPage);
        }
        return listMsgVO;
    }

    public void setMsgReaded(String id, Integer sendType) {
        switch (sendType) {
            case MsgConstant.PC_RECORD_SEND_TYPE:
                MongoPcRecord mongoPcRecord = mongoPcRecordRepository.findById(id).orElse(new MongoPcRecord());
                String pId = mongoPcRecord.getId() == null ? MongoUtils.getObjectId() : mongoPcRecord.getId();
                mongoPcRecord.setId(pId);
                mongoPcRecord.setIsRead(1);
                mongoPcRecordRepository.save(mongoPcRecord);
                break;
            case MsgConstant.APP_RECORD_SEND_TYPE:
                MongoAppRecord mongoAppRecord = mongoAppRecordRepository.findById(id).orElse(new MongoAppRecord());
                String aId = mongoAppRecord.getId() == null ? MongoUtils.getObjectId() : mongoAppRecord.getId();
                mongoAppRecord.setId(aId);
                mongoAppRecord.setIsRead(1);
                mongoAppRecordRepository.save(mongoAppRecord);
                break;
            default:
                MongoNoteRecord mongoNoteRecord = mongoNoteRecordRepository.findById(id).orElse(new MongoNoteRecord());
                String nId = mongoNoteRecord.getId() == null ? MongoUtils.getObjectId() : mongoNoteRecord.getId();
                mongoNoteRecord.setId(nId);
                mongoNoteRecord.setIsRead(1);
                mongoNoteRecordRepository.save(mongoNoteRecord);
                break;
        }
    }

    /**
     * 查询APP消息列表
     *
     * @param params
     * @return
     */
    private ListMsgVO queryAppMsgRecord(ListMsgDTO params) {
        ListMsgVO<MsgRecord> listMsgVO = null;
        int uid = 0;
        if (StringUtils.isNotBlank(params.getTargetContact())) {
            uid = Integer.parseInt(params.getTargetContact());
        }
        SearchMessageRecordDTO searchMessageRecordDTO = SearchMessageRecordDTO.builder()
                .pageInfo(params.getPageInfo())
                .uid(uid)
                .isRead(params.getIsRead())
                .yid(params.getYid())
                .build();
        SearchMessageRecordVO searchMessageRecordVO = esMsgRecordRepository.queryAppRecord(searchMessageRecordDTO);
        if (searchMessageRecordVO.getTotal() > 0) {
            List<MsgRecord> msgRecords = new ArrayList<>(searchMessageRecordVO.getResult().size());
            long totalPage;
            if (searchMessageRecordVO.getTotal() % params.getPageInfo().getPageSize() == 0) {
                totalPage = searchMessageRecordVO.getTotal() / params.getPageInfo().getPageSize();
            } else {
                totalPage = searchMessageRecordVO.getTotal() / params.getPageInfo().getPageSize() + 1;
            }
            for (JSONObject obj : searchMessageRecordVO.getResult()) {
                RecordTarget recordTarget = RecordTarget.builder()
                        .userId(obj.getString("uid"))
                        .contact(obj.getString("uid"))
                        .userType(obj.getIntValue("target_type"))
                        .sendType(4)
                        .build();
                JSONObject bizParam = new JSONObject();
                try {
                    bizParam = JSONObject.parseObject(obj.getString("biz_param"));
                } catch (Exception e) {
                    log.warn("queryAppMsgRecord parseObject exception obj:{} e:{}", obj, e);
                }
                bizParam.put("yid", obj.getIntValue("yid"));
                MsgRecord msgRecord = MsgRecord.builder()
                        .target(recordTarget)
                        .id(obj.getString("id"))
                        .nickId(obj.getString("id"))
                        .tid(obj.getIntValue("tid"))
                        .title(obj.getString("title"))
                        .sendTime(obj.getIntValue("send_time"))
                        .content(obj.getString("app_content"))
                        .isRead(obj.getIntValue("is_read"))
                        .bizdata(bizParam.toJSONString())
                        .errorDescribe(StringUtils.defaultString(obj.getString("error_describle")))
                        .build();
                msgRecords.add(msgRecord);
            }
            listMsgVO = new ListMsgVO<>();
            listMsgVO.setMsgRecords(msgRecords);
            listMsgVO.setTotalPages(totalPage);
            listMsgVO.setTotalRecords(searchMessageRecordVO.getTotal());
        }
        return listMsgVO;
    }
}
