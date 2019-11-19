package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.to8to.common.http.DefaultWebClient;
import com.to8to.common.search.PageResult;
import com.to8to.sc.compatible.RPCException;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.constant.AppMsgConstant;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.*;
import com.to8to.tbt.msc.entity.es.EsAppRecord;
import com.to8to.tbt.msc.entity.mysql.extend.AppAd;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePush;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePushUser;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.repository.mysql.extend.AppAdRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushUserRepository;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.enumeration.*;
import com.to8to.tbt.msc.vo.wechat.WeChatResMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class AppMsgServiceImpl implements AppMsgService {

    @Value(value = "${app.msg.tid.follow}")
    private int followMsgTid;

    @Value(value = "${app.msg.tid.comment.refuse}")
    private int commentRefuseMsgTid;

    @Value(value = "${app.msg.node.interaction}")
    private String interactionNodeIds;

    @Value(value = "${app.msg.node.system}")
    private String systemNodeIds;

    @Value(value = "${app.msg.process.filter.type}")
    private String processMsgFilterType;

    @Value(value = "${app.msg.node.set.read}")
    private String msgSetHasReadNode;

    @Value("${app.msg.wechat.alarm.url}")
    private String wechatAlarmUrl;

    private Pattern picUrlPattern = Pattern.compile("^http|https+");

    private HashMap<Integer, String> diaryPushMsg = new HashMap<>();

    @Autowired
    private MessageCenterService messageCenterService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessagePushUserRepository messagePushUserRepository;

    @Autowired
    private MessagePushRepository messagePushRepository;

    @Autowired
    private AppAdRepository appAdRepository;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private PushService pushService;

    private volatile DefaultWebClient webClient;

    @PostConstruct
    public void init() {
        diaryPushMsg.put(301, "评论了您的日记");
        diaryPushMsg.put(302, "回复了您的评论");
        diaryPushMsg.put(303, "赞了您的日记");
        diaryPushMsg.put(304, "收藏了您的日记");
    }

    @Override
    public PageResult<AppInteractionMsgVO> queryFollowList(Integer uid, AppMsgListDTO params) {
        uid = extractRpcUid(uid, params.getUid());
        params = formatAppMsgListDTO(params);
        if (IntegerUtils.isEqLimitValue(uid)) {
            log.debug("AppMsgServiceImpl.queryFollowList uid:{} page:{} pageSize:{}", uid, params.getPage(), params.getPageSize());
            return ResponseUtils.buildPageResult();
        }
        return executeQueryFollowList(uid, MsgReadStatusEnum.INVALID.getCode(), params.getPage(), params.getPageSize(), Boolean.TRUE);
    }

    @Override
    public PageResult<AppInteractionMsgVO> queryInteractionList(Integer uid, AppMsgListDTO params) {
        uid = extractRpcUid(uid, params.getUid());
        params = formatAppMsgListDTO(params);
        if (IntegerUtils.isEqLimitValue(uid)) {
            log.debug("queryInteractionList invalid user uid:{} page:{} pageSize:{}", uid, params.getPage(), params.getPageSize());
            return ResponseUtils.buildPageResult();
        }
        return executeQueryInteractionList(uid, MsgReadStatusEnum.INVALID.getCode(), params.getPage(), params.getPageSize(), Boolean.TRUE);
    }

    @Override
    public PageResult<AppSystemMsgVO> aggregationSystemList(Integer uid, AppSystemListDTO appSystemListDTO) {
        uid = extractRpcUid(uid, appSystemListDTO.getUid());
        AppPubArgs appPubArgs = generateAppPubArgs(appSystemListDTO);
        return queryAggregationSystemList(uid, appPubArgs.getAppId(), appPubArgs.getFirstId(), appPubArgs.getAppOsType(), appPubArgs.getCityId(), appPubArgs.getAppVersion(), 0);
    }

    @Override
    public AppAggregationMsgVO aggregationMsg(Integer uid, AppMsgMainDTO appMsgMainDTO) {
        uid = extractRpcUid(uid, appMsgMainDTO.getUid());
        AppPubArgs appPubArgs = generateAppPubArgs(appMsgMainDTO);
        appMsgMainDTO.setSysMsgReadCount(IntegerUtils.intValueAsDefault(appMsgMainDTO.getSysMsgReadCount()));
        AppAggregationMsgVO appAggregationMsgVO = new AppAggregationMsgVO();
        if (IntegerUtils.isGtLimitValue(uid)) {
            appAggregationMsgVO.setInteractiveMsg(countInteractionMsg(uid));
            appAggregationMsgVO.setFollowMsg(countFollowMsg(uid));
            appAggregationMsgVO.setProgressMsg(countProcessMsg(uid));
        } else {
            AppMsgWrapper.AggregationMsgItem aggregationMsgItem = generateAggregationMsgItem(0, null, "");
            appAggregationMsgVO.setInteractiveMsg(aggregationMsgItem);
            appAggregationMsgVO.setFollowMsg(aggregationMsgItem);
            appAggregationMsgVO.setProgressMsg(aggregationMsgItem);
        }
        appAggregationMsgVO.setSystemMsg(countSystemMsg(uid, appPubArgs.getAppId(), appPubArgs.getFirstId(), appPubArgs.getAppOsType(), appPubArgs.getCityId(), appPubArgs.getAppVersion(), appMsgMainDTO.getSysMsgReadCount()));
        return appAggregationMsgVO;
    }

    @Override
    public ListMsgVO queryProcessList(Integer uid, AppMsgListDTO appMsgListDTO) {
        uid = extractRpcUid(uid, appMsgListDTO.getUid());
        appMsgListDTO = formatAppMsgListDTO(appMsgListDTO);
        if (IntegerUtils.isEqLimitValue(uid)) {
            log.debug("queryProcessList invalid user uid:{} page:{} pageSize:{}", uid, appMsgListDTO.getPage(), appMsgListDTO.getPageSize());
            return buildAppProcessMsgResult();
        }
        return executeQueryProcessList(uid, MsgReadStatusEnum.INVALID.getCode(), appMsgListDTO.getPage(), appMsgListDTO.getPageSize(), Boolean.FALSE);
    }

    @Override
    public List<MsgSetHasReadVO> msgSetHasRead(Integer uid, AppReadClearDTO appReadClearDTO) {
        uid = extractRpcUid(uid, appReadClearDTO.getUid());
        appReadClearDTO.setMsgId(IntegerUtils.intValueAsDefault(appReadClearDTO.getMsgId()));
        appReadClearDTO.setReadAll(IntegerUtils.intValueAsDefault(appReadClearDTO.getReadAll()));
        AppPubArgs appPubArgs = generateAppPubArgs(appReadClearDTO);
        List<MsgSetHasReadVO> msgSetHasReadVOS;
        if (appReadClearDTO.getReadAll() > 0) {
            msgSetHasReadVOS = queryGroupSendMsg(appPubArgs.getFirstId(), appPubArgs.getAppOsType(), appPubArgs.getAppVersion(), appPubArgs.getCityId(), appPubArgs.getAppId());
        } else {
            msgSetHasReadVOS = new ArrayList<>();
        }
        if (IntegerUtils.isGtLimitValue(uid)) {
            if (appReadClearDTO.getReadAll() > 0) {
                try {
                    if (StringUtils.isNotBlank(msgSetHasReadNode)) {
                        List<Integer> msgSetHasReadNodes = JSONArray.parseArray(msgSetHasReadNode).toJavaList(Integer.class);
                        messageCenterService.setAppMsgStatusByUidAndNodeId(uid, msgSetHasReadNodes, MsgReadStatusEnum.HAS_READ.getCode());
                    } else {
                        log.error("msgSetHasRead msgSetHasReadNode is empty");
                    }
                } catch (Exception e) {
                    log.warn("msgSetHasRead.setAppMsgStatusByUidAndNodeId exception uid:{} msgSetHasReadNode:{} e:{}", uid, msgSetHasReadNode, e);
                    throw new RPCException(MyExceptionStatus.NETWORK_ERROR);
                }
                ListMsgVO<AppProcessMsgVO> appProcessMsgVOPageResult = executeQueryProcessList(uid, MsgReadStatusEnum.UNREAD.getCode(), 1, AppMsgConstant.PAGE_MAX_SIZE, Boolean.FALSE);
                try {
                    if (appProcessMsgVOPageResult.getMsgRecords().size() > 0) {
                        List<Integer> processMsgIds = appProcessMsgVOPageResult.getMsgRecords().stream().map(appProcessMsgVO -> appProcessMsgVO.getId()).collect(Collectors.toList());
                        messageCenterService.setAppMsgHasReadBatch(processMsgIds);
                        log.debug("msgSetHasRead.setAppMsgReadBatch uid:{} processMsgIds:{} appProcessMsgVOS:{} ", uid, processMsgIds, appProcessMsgVOPageResult.getMsgRecords());
                    }
                } catch (Exception e) {
                    log.warn("msgSetHasRead.setAppMsgReadBatch exception uid:{} appProcessMsgVOPageResult:{} e:{}", uid, appProcessMsgVOPageResult, e);
                    throw new RPCException(MyExceptionStatus.NETWORK_ERROR);
                }
            } else if (appReadClearDTO.getMsgId() > 0) {
                messageCenterService.setAppMsgRead(appReadClearDTO.getMsgId());
            } else {
                log.warn("msgSetHasRead params error uid:{} msgId:{}", uid, appReadClearDTO.getMsgId());
            }
        }
        return msgSetHasReadVOS;
    }

    @Override
    public ResResult<ResultStatusResponse<String>> sendWeChatAlarmMsg(SendWeChatAlarmMsgDTO sendWeChatAlarmMsgDTO) {
        ResultStatusResponse<String> resultStatusResponse;
        WeChatResMsgVO weChatResMsgVO = sendWeChatAlarmMsg(sendWeChatAlarmMsgDTO.getContent());
        if (weChatResMsgVO != null && weChatResMsgVO.getSuccess()){
            resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(MyExceptionStatus.SEND_WECHAT_ALARM_SUCCESS.getCode())
                    .status(MyExceptionStatus.SEND_WECHAT_ALARM_SUCCESS.getMessage())
                    .build();
            return ResUtils.data(resultStatusResponse);
        }else {
            resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(MyExceptionStatus.SEND_WECHAT_ALARM_FAIL.getCode())
                    .status(MyExceptionStatus.SEND_WECHAT_ALARM_FAIL.getMessage())
                    .build();
            ResResult<ResultStatusResponse<String>> resResult = ResUtils.fail(MyExceptionStatus.SEND_WECHAT_ALARM_FAIL.getCode(), MyExceptionStatus.SEND_WECHAT_ALARM_FAIL.getMessage());
            resResult.setData(resultStatusResponse);
            return resResult;
        }
    }

    @Override
    public WeChatResMsgVO sendWeChatAlarmMsg(String content) {
        try {
            String httpUrl = wechatAlarmUrl + "&content=" + URLEncoder.encode(content.trim(), "UTF-8");
            if (webClient == null) {
                webClient = new DefaultWebClient();
            }
            return webClient.execute(httpUrl, WeChatResMsgVO.class);
        } catch (Exception e) {
            log.warn("sendWeChatAlarmMsg fail content:{} e:{}", content, e);
            return null;
        }
    }

    /**
     * 提取Header中的UID
     *
     * @param rpcUid
     * @param paramsUid
     * @return
     */
    private Integer extractRpcUid(Integer rpcUid, Integer paramsUid) {
        if (rpcUid == null) {
            return paramsUid == null ? 0 : paramsUid;
        } else {
            return rpcUid;
        }
    }

    /**
     * 格式化消息入参
     *
     * @param params
     * @return
     */
    private AppMsgListDTO formatAppMsgListDTO(AppMsgListDTO params) {
        params.setPage(IntegerUtils.intValueAsDefault(params.getPage(), 1));
        if (IntegerUtils.isGtLimitValue(params.getPerPage())) {
            params.setPageSize(params.getPerPage());
        } else {
            params.setPageSize(IntegerUtils.intValueAsDefault(params.getPageSize(), AppMsgConstant.PAGE_SIZE));
        }
        return params;
    }

    /**
     * 格式化APP公参
     *
     * @param appPubArgs
     * @return
     */
    private AppPubArgs formatAppPubArgs(AppPubArgs appPubArgs) {
        appPubArgs.setUid(IntegerUtils.intValueAsDefault(appPubArgs.getUid()));
        appPubArgs.setAppId(IntegerUtils.intValueAsDefault(appPubArgs.getAppId()));
        appPubArgs.setFirstId(StringUtils.defaultString(appPubArgs.getFirstId()));
        appPubArgs.setAppOsType(IntegerUtils.intValueAsDefault(appPubArgs.getAppOsType()));
        appPubArgs.setCityId(IntegerUtils.intValueAsDefault(appPubArgs.getCityId()));
        appPubArgs.setAppVersion(StringUtils.defaultString(appPubArgs.getAppVersion()));
        return appPubArgs;
    }

    /**
     * 转换APP公参为对象
     *
     * @param args
     * @return
     */
    private AppPubArgs transAppPubArgs(String args) {
        AppPubArgs appPubArgs = new AppPubArgs();
        try {
            if (StringUtils.isNotBlank(args)) {
                appPubArgs = JSONObject.toJavaObject(JSONObject.parseObject(args), AppPubArgs.class);
            } else {
                log.warn("transAppPubArgs args empty");
            }
        } catch (Exception e) {
            log.warn("transAppPubArgs fail e:{}", e);
        }
        return appPubArgs;
    }

    /**
     * 生成APP公参
     *
     * @param appSystemMsgBaseDTO
     * @return
     */
    private AppPubArgs generateAppPubArgs(AppSystemMsgBaseDTO appSystemMsgBaseDTO) {
        AppPubArgs appPubArgs;
        if (appSystemMsgBaseDTO.getPubArgs() == null) {
            appPubArgs = AppPubArgs.builder()
                    .uid(appSystemMsgBaseDTO.getUid())
                    .appId(appSystemMsgBaseDTO.getAppId())
                    .firstId(appSystemMsgBaseDTO.getFirstId())
                    .appOsType(appSystemMsgBaseDTO.getAppOsType())
                    .cityId(appSystemMsgBaseDTO.getCityId())
                    .appVersion(appSystemMsgBaseDTO.getAppVersion())
                    .build();
        } else {
            appPubArgs = transAppPubArgs(appSystemMsgBaseDTO.getPubArgs());
        }
        return formatAppPubArgs(appPubArgs);
    }

    /**
     * 查询群发消息-批量设置已读
     *
     * @param firstId
     * @param appOsType
     * @param appVersion
     * @param cityId
     * @param appId
     * @return
     */
    private List<MsgSetHasReadVO> queryGroupSendMsg(String firstId, int appOsType, String appVersion, int cityId, int appId) {
        List<MsgSetHasReadVO> msgSetHasReadVOS = new ArrayList<>();
        List<AppSystemMsgVO> appSystemMsgVOS = queryGroupMessage(appId, firstId, appOsType, cityId, appVersion, generateMsgQueryStartTime(AppMsgConstant.PUSH_MESSAGE_QUERY_START_NUM));
        if (appSystemMsgVOS.size() > 0) {
            for (AppSystemMsgVO appSystemMsgVO : appSystemMsgVOS) {
                MsgSetHasReadVO msgSetHasReadVO = MsgSetHasReadVO.builder()
                        .msgId(appSystemMsgVO.getMsgId())
                        .msgTime(IntegerUtils.intValueAsDefault(appSystemMsgVO.getTime()))
                        .build();
                msgSetHasReadVOS.add(msgSetHasReadVO);
            }
        }
        return msgSetHasReadVOS;
    }

    /**
     * 生成装修进度列表的查询结果
     *
     * @return
     */
    private ListMsgVO buildAppProcessMsgResult() {
        ListMsgVO listMsgVO = ListMsgVO.builder()
                .totalRecords(0L)
                .totalPages(0L)
                .msgRecords(new ArrayList<>())
                .build();
        return listMsgVO;
    }

    /**
     * 执行装修进度查询
     *
     * @param uid
     * @param page
     * @param pageSize
     * @return
     */
    public ListMsgVO executeQueryProcessList(int uid, int isRead, int page, int pageSize, boolean isCountUnReadNum) {
        int unReadNum = 0;
        List<Integer> filterTypes;
        if (StringUtils.isNotBlank(processMsgFilterType)) {
            try {
                filterTypes = Arrays.stream(processMsgFilterType.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("executeQueryProcessList processMsgFilterType parse exception processMsgFilterType:{}", processMsgFilterType);
                return buildAppProcessMsgResult();
            }
        } else {
            log.error("executeQueryProcessList params error processMsgFilterType is empty");
            return buildAppProcessMsgResult();
        }
        try {
            ListMsgVO<MsgRecord> listMsgVO = messageCenterService.listMsg(generateQueryProcessListParams(uid, isRead, page, pageSize));
            List<AppProcessMsgVO> appProcessMsgVOS = new ArrayList<>();
            List<MsgRecord> filterMsgRecords = new ArrayList<>();
            if (listMsgVO.getMsgRecords() != null) {
                for (MsgRecord msgRecord : listMsgVO.getMsgRecords()) {
                    AppProcessMsgVO appProcessMsgVO = formatProcessMsg(msgRecord, filterTypes);
                    if (appProcessMsgVO instanceof AppProcessMsgVO) {
                        appProcessMsgVOS.add(appProcessMsgVO);
                        if (isCountUnReadNum && !IntegerUtils.isGtLimitValue(appProcessMsgVO.getIsRead())) {
                            unReadNum++;
                        }
                    } else {
                        filterMsgRecords.add(msgRecord);
                    }
                }
            }
            if (filterMsgRecords.size() > 0) {
                log.debug("executeQueryProcessList filterResult uid:{} filterMsgRecords:{}", uid, filterMsgRecords);
            }
            log.debug("executeQueryProcessList uid:{} page:{} pageSize:{} filterTypes:{} listMsgVO:{} appProcessMsgVOS:{}", uid, page, pageSize, filterTypes, listMsgVO, appProcessMsgVOS);
            ListMsgVO<AppProcessMsgVO> appProcessMsgVOListMsgVO = new ListMsgVO<>();
            appProcessMsgVOListMsgVO.setMsgRecords(appProcessMsgVOS);
            if (isCountUnReadNum) {
                appProcessMsgVOListMsgVO.setTotalPages(1L);
                appProcessMsgVOListMsgVO.setTotalRecords((long) unReadNum);
            } else {
                appProcessMsgVOListMsgVO.setTotalPages(listMsgVO.getTotalPages());
                appProcessMsgVOListMsgVO.setTotalRecords(listMsgVO.getTotalRecords());
            }
            return appProcessMsgVOListMsgVO;
        } catch (Exception e) {
            log.warn("executeQueryProcessList exception uid:{} e:{}", uid, e);
            return buildAppProcessMsgResult();
        }
    }

    /**
     * 格式化装修进度消息
     *
     * @param msgRecord
     * @param filterTypes
     * @return
     */
    private AppProcessMsgVO formatProcessMsg(MsgRecord msgRecord, List<Integer> filterTypes) {
        JSONObject bizData = new JSONObject();
        AppMsgWrapper.BizData msgBizData = new AppMsgWrapper.BizData();
        if (StringUtils.isNotBlank(msgRecord.getBizdata())) {
            try {
                bizData = JSONObject.parseObject(msgRecord.getBizdata());
                msgBizData = bizData.toJavaObject(AppMsgWrapper.BizData.class);
            } catch (Exception e) {
                log.warn("formatProcessMsg exception msgRecord:{} bizData:{} e:{}", msgRecord, msgRecord.getBizdata(), e);
            }
        }
        if (!filterTypes.contains(IntegerUtils.intValueAsDefault(msgBizData.getType()))) {
            return AppProcessMsgVO.builder()
                    .id(IntegerUtils.intValueAsDefault(msgRecord.getId()))
                    .title(StringUtils.defaultString(msgRecord.getTitle()))
                    .content(StringEscapeUtils.unescapeHtml(StringUtils.defaultString(msgRecord.getContent())))
                    .sendTime(msgRecord.getSendTime())
                    .isRead(msgRecord.getIsRead())
                    .bizData(bizData)
                    .build();
        }
        return null;
    }

    /**
     * 生成查询装修进度消息的参数
     *
     * @param uid
     * @param page
     * @param pageSize
     * @return
     */
    private ListMsgDTO generateQueryProcessListParams(int uid, int isRead, int page, int pageSize) {
        ListMsgDTO listMsgDTO = new ListMsgDTO();
        listMsgDTO.setTargetContact(String.valueOf(uid));
        listMsgDTO.setSendType(MsgSendTypeEnum.APP.getCode());
        if (isRead != MsgReadStatusEnum.INVALID.getCode()) {
            listMsgDTO.setIsRead(isRead);
        }
        PageInfo pageInfo = PageInfo.builder()
                .currPage(page)
                .pageSize(pageSize)
                .build();
        listMsgDTO.setPageInfo(pageInfo);
        return listMsgDTO;
    }

    /**
     * 执行互动消息列表查询
     *
     * @param uid
     * @param isRead
     * @param page
     * @param pageSize
     * @return
     */
    private PageResult<AppInteractionMsgVO> executeQueryInteractionList(int uid, int isRead, int page, int pageSize, boolean isReplenishUserInfo) {
        if (StringUtils.isAllBlank(interactionNodeIds)) {
            log.warn("executeQueryInteractionList params error interactionNodeIds:{} uid:{}", interactionNodeIds, uid);
            return ResponseUtils.buildPageResult();
        }
        SearchMessageRecordDTO searchMessageRecordDTO = generateInteractionMsgQueryParams(isRead);
        if (searchMessageRecordDTO == null) {
            return ResponseUtils.buildPageResult();
        }
        searchMessageRecordDTO = generateMsgQueryBaseParams(
                searchMessageRecordDTO,
                uid,
                page,
                pageSize
        );
        log.debug("executeQueryInteractionList searchParams searchMessageRecordDTO:{}", searchMessageRecordDTO);
        PageResult<AppInteractionMsgVO> resultVO;
        try {
            resultVO = queryInteractionMsg(searchMessageRecordDTO, isReplenishUserInfo);
        } catch (Exception e) {
            log.warn("executeQueryInteractionList exception searchMessageRecordDTO:{} e:{}", searchMessageRecordDTO, e);
            return ResponseUtils.buildPageResult();
        }
        log.debug("executeQueryInteractionList uid:{} searchMessageRecordDTO:{} resultVO:{}", uid, searchMessageRecordDTO, resultVO);
        return resultVO;
    }

    /**
     * 执行关注消息查询
     *
     * @param uid
     * @param isRead
     * @param page
     * @param pageSize
     * @return
     */
    public PageResult<AppInteractionMsgVO> executeQueryFollowList(int uid, int isRead, int page, int pageSize, boolean isReplenishUserInfo) {
        if (followMsgTid == 0) {
            log.error("executeQueryFollowList params error followMsgTid:{} uid:{}", followMsgTid, uid);
            return ResponseUtils.buildPageResult();
        }
        SearchMessageRecordDTO searchMessageRecordDTO = generateMsgQueryBaseParams(
                generateFollowMsgQueryParams(isRead),
                uid,
                page,
                pageSize
        );
        PageResult<AppInteractionMsgVO> resultVO;
        try {
            resultVO = queryInteractionMsg(searchMessageRecordDTO, isReplenishUserInfo);
        } catch (Exception e) {
            log.warn("executeQueryFollowList exception searchMessageRecordDTO:{} e:{}", searchMessageRecordDTO, e);
            resultVO = ResponseUtils.buildPageResult();
        }
        log.debug("executeQueryFollowList uid:{} searchMessageRecordDTO:{} resultVO:{}", uid, searchMessageRecordDTO, resultVO);
        return resultVO;
    }

    /**
     * 统计系统消息
     *
     * @param uid
     * @param appId
     * @param firstId
     * @param appOsType
     * @param cityId
     * @param appVersion
     * @param sysMsgReadCount
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem countSystemMsg(int uid, int appId, String firstId, int appOsType, int cityId, String appVersion, int sysMsgReadCount) {
        int groupMessageNum;
        int systemMessageNum = 0;
        AppSystemMsgVO appSystemMsgVO = new AppSystemMsgVO();
        List<AppSystemMsgVO> systemMsgVOS = new ArrayList<>();
        List<AppSystemMsgVO> appSystemMsgVOS = queryGroupMessage(appId, firstId, appOsType, cityId, appVersion, generateMsgQueryStartTime(AppMsgConstant.PUSH_MESSAGE_QUERY_START_NUM));
        groupMessageNum = appSystemMsgVOS.size();
        if (uid > 0) {
            try {
                systemMsgVOS = querySystemList(new ArrayList<>(), uid, MsgReadStatusEnum.INVALID.getCode());
            } catch (Exception e) {
                log.warn("queryAggregationSystemList querySystemList exception uid:{} e:{}", uid, e);
            }
        }
        if (systemMsgVOS.size() > 0) {
            for (AppSystemMsgVO item : systemMsgVOS) {
                appSystemMsgVOS.add(item);
                if (item.getIsRead().equals(Boolean.FALSE)) {
                    systemMessageNum++;
                }
            }
        }
        if (appSystemMsgVOS.size() > 0) {
            appSystemMsgVOS.sort((AppSystemMsgVO o1, AppSystemMsgVO o2) -> o2.getTime() - o1.getTime());
            appSystemMsgVO = appSystemMsgVOS.get(0);
        }
        int count = groupMessageNum > sysMsgReadCount ? groupMessageNum - sysMsgReadCount : 0;
        String message = StringUtils.defaultString(appSystemMsgVO.getTitle());
        if (StringUtils.isAllBlank(message)) {
            message = StringUtils.defaultString(appSystemMsgVO.getContent());
            if (message.length() > 0) {
                message = StringUtils.substring(message, 0, 20);
            }
        }
        return generateAggregationMsgItem(count + systemMessageNum, appSystemMsgVO.getTime(), message);
    }

    /**
     * 统计互动消息
     *
     * @param uid
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem countInteractionMsg(int uid) {
        PageResult<AppInteractionMsgVO> unReadMsgResult = executeQueryInteractionList(uid, MsgReadStatusEnum.UNREAD.getCode(), 1, 1, Boolean.FALSE);
        PageResult<AppInteractionMsgVO> allMsgResult = executeQueryInteractionList(uid, MsgReadStatusEnum.INVALID.getCode(), 1, 1, Boolean.FALSE);
        return generateCountInteractionMsgResult(unReadMsgResult, allMsgResult);
    }

    /**
     * 统计关注消息
     *
     * @param uid
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem countFollowMsg(int uid) {
        PageResult<AppInteractionMsgVO> unReadMsgResult = executeQueryFollowList(uid, MsgReadStatusEnum.UNREAD.getCode(), 1, 1, Boolean.FALSE);
        PageResult<AppInteractionMsgVO> allMsgResult = executeQueryFollowList(uid, MsgReadStatusEnum.INVALID.getCode(), 1, 1, Boolean.FALSE);
        return generateCountInteractionMsgResult(unReadMsgResult, allMsgResult);
    }

    /**
     * 统计装修进度消息
     *
     * @param uid
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem countProcessMsg(int uid) {
        ListMsgVO<AppProcessMsgVO> appProcessMsgVOPageResult = executeQueryProcessList(uid, MsgReadStatusEnum.INVALID.getCode(), 1, AppMsgConstant.PAGE_MAX_SIZE, Boolean.TRUE);
        Long count = new Long(appProcessMsgVOPageResult.getTotalRecords());
        AppProcessMsgVO appProcessMsgVO = new AppProcessMsgVO();
        if (appProcessMsgVOPageResult.getMsgRecords().size() > 0) {
            appProcessMsgVO = appProcessMsgVOPageResult.getMsgRecords().get(0);
        }
        return AppMsgWrapper.AggregationMsgItem.builder()
                .count(count.intValue())
                .time(TimeUtils.timestampToDate(IntegerUtils.intValuefromLong(appProcessMsgVO.getSendTime())))
                .message(StringUtils.defaultString(appProcessMsgVO.getTitle(), StringUtils.defaultString(appProcessMsgVO.getContent())))
                .build();
    }

    /**
     * 生成统计互动消息的结果
     *
     * @param unReadMsgResult
     * @param allMsgResult
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem generateCountInteractionMsgResult(PageResult<AppInteractionMsgVO> unReadMsgResult, PageResult<AppInteractionMsgVO> allMsgResult) {
        AppInteractionMsgVO appInteractionMsgVO = new AppInteractionMsgVO();
        if (allMsgResult.getRows().size() > 0) {
            appInteractionMsgVO = allMsgResult.getRows().get(0);
        }
        return generateAggregationMsgItem(IntegerUtils.intValuefromLong(unReadMsgResult.getTotal()), appInteractionMsgVO.getMsgTime(), appInteractionMsgVO.getMsgAction());
    }

    /**
     * 生成消息聚合页强果项
     *
     * @param count
     * @param timestamp
     * @param title
     * @return
     */
    private AppMsgWrapper.AggregationMsgItem generateAggregationMsgItem(int count, Integer timestamp, String title) {
        return AppMsgWrapper.AggregationMsgItem.builder()
                .count(count)
                .time(TimeUtils.timestampToDate(IntegerUtils.intValueAsDefault(timestamp)))
                .message(StringUtils.defaultString(title))
                .build();
    }

    /**
     * 查询聚合系统消息列表
     *
     * @param uid
     * @param appId
     * @param firstId
     * @param appOsType
     * @param cityId
     * @param appVersion
     * @return
     */
    private PageResult<AppSystemMsgVO> queryAggregationSystemList(int uid, int appId, String firstId, int appOsType, int cityId, String appVersion, int pushMsgStartTime) {
        List<AppSystemMsgVO> appSystemMsgVOS = queryGroupMessage(appId, firstId, appOsType, cityId, appVersion, pushMsgStartTime);
        return mergeSystemList(uid, MsgReadStatusEnum.INVALID.getCode(), appSystemMsgVOS);
    }

    /**
     * 合并系统消息列表
     *
     * @param uid
     * @param appSystemMsgVOS
     * @return
     */
    private PageResult<AppSystemMsgVO> mergeSystemList(int uid, int isRead, List<AppSystemMsgVO> appSystemMsgVOS) {
        if (uid > 0) {
            try {
                appSystemMsgVOS = querySystemList(appSystemMsgVOS, uid, isRead);
            } catch (Exception e) {
                log.warn("queryAggregationSystemList querySystemList exception uid:{} e:{}", uid, e);
            }
        }
        appSystemMsgVOS.sort((AppSystemMsgVO o1, AppSystemMsgVO o2) -> o2.getTime() - o1.getTime());
        log.debug("mergeSystemList uid:{} total:{} appSystemMsgVOS:{}", uid, appSystemMsgVOS.size(), appSystemMsgVOS);
        return ResponseUtils.buildPageResult(appSystemMsgVOS.size(), appSystemMsgVOS);
    }

    /**
     * 查询群组消息
     *
     * @param appId
     * @param firstId
     * @param appOsType
     * @param cityId
     * @param appVersion
     * @param pushMsgStartTime
     * @return
     */
    private List<AppSystemMsgVO> queryGroupMessage(int appId, String firstId, int appOsType, int cityId, String appVersion, int pushMsgStartTime) {
        List<AppSystemMsgVO> appSystemMsgVOS = new ArrayList<>();
        List<Integer> msgIds = queryPushUserMsgIds(firstId);
        if (msgIds.isEmpty()) {
            msgIds.add(0);
        }
        if (pushMsgStartTime <= 0) {
            pushMsgStartTime = generateMsgQueryStartTime();
        }
        appSystemMsgVOS = queryPushMessage(appSystemMsgVOS, appOsType, msgIds, pushMsgStartTime);
        appSystemMsgVOS = queryAppAds(appSystemMsgVOS, appId, appOsType, cityId, appVersion, pushMsgStartTime);
        log.debug("queryGroupMessage appId:{} firstId:{} appOsType:{} cityId:{} appVersion:{} pushMsgStartTime:{} total:{} appSystemMsgVOS:{}", appId, firstId, appOsType, cityId, appVersion, pushMsgStartTime, appSystemMsgVOS.size(), appSystemMsgVOS);
        return appSystemMsgVOS;
    }

    /**
     * 查询系统消息列表
     *
     * @param appSystemMsgVOS
     * @param uid
     * @return
     */
    private List<AppSystemMsgVO> querySystemList(List<AppSystemMsgVO> appSystemMsgVOS, int uid, int isRead) {
        SearchMessageRecordDTO searchMessageRecordDTO = generateSystemMsgQueryParams(isRead);
        if (searchMessageRecordDTO == null) {
            return new ArrayList<>();
        }
        searchMessageRecordDTO = generateMsgQueryBaseParams(
                searchMessageRecordDTO,
                uid,
                AppMsgConstant.PAGE_NUMBER,
                AppMsgConstant.PAGE_MAX_SIZE);
        log.debug("querySystemList searchParams searchMessageRecordDTO:{}", searchMessageRecordDTO);
        SearchMessageRecordVO searchMessageRecordVO = messageCenterService.searchMessageRecord(searchMessageRecordDTO);
        if (searchMessageRecordVO.getResult() == null) {
            return appSystemMsgVOS;
        }
        for (JSONObject item : searchMessageRecordVO.getResult()) {
            EsAppRecord esAppRecord = jsonToAppRecord(item);
            if (esAppRecord == null) {
                continue;
            }
            AppMsgWrapper.BizParams bizParams = esAppRecord.getBizParams();
            if (bizParams == null) {
                continue;
            }
            AppMsgWrapper.MsgJumpParams msgJumpParams = generateMsgJumpParams(
                    bizParams.getModuleCode(),
                    StringUtils.defaultString(bizParams.getUrl()),
                    IntegerUtils.intValueAsDefault(bizParams.getObjectId()),
                    ObjectUtils.defaultIfNull(bizParams.getExtraDataParams(), generateExtraDataParams())
            );
            AppSystemMsgVO appSystemMsgVO = AppSystemMsgVO.builder()
                    .showType(AppMsgShowTypeEnum.TEXT.getCode())
                    .msgId(String.valueOf(IntegerUtils.intValueAsDefault(esAppRecord.getId())))
                    .title(StringUtils.defaultString(bizParams.getTitle()))
                    .content(StringUtils.defaultString(esAppRecord.getAppContent()))
                    .imageUrl("")
                    .isGroupMsg(Boolean.FALSE)
                    .isRead(IntegerUtils.intValueAsDefault(esAppRecord.getIsRead()) > 0 ? Boolean.TRUE : Boolean.FALSE)
                    .time(IntegerUtils.intValuefromLong(esAppRecord.getSendTime()))
                    .icon("")
                    .jumpFlag(Boolean.FALSE)
                    .schemeUrl(StringUtils.defaultString(bizParams.getSchemeUrl()))
                    .msgJumpParams(msgJumpParams)
                    .build();
            if (IntegerUtils.intValueAsDefault(bizParams.getType()) == AppMsgTypeEnum.NOTICE.getCode()) {
                appSystemMsgVO.setIcon(AppMsgConstant.ICON_MSG_NOTICE);
            }
            boolean jumpFlag = ((IntegerUtils.intValueAsDefault(bizParams.getType()) == AppMsgTypeEnum.SYSTEM.getCode())
                    || (bizParams.getModuleCode().equals(MsgModuleCodeEnum.DIARY.getCode())
                    || bizParams.getModuleCode().equals(MsgModuleCodeEnum.DIARY_CHANNEL.getCode())
                    || bizParams.getModuleCode().equals(MsgModuleCodeEnum.SCORE.getCode())
                    || bizParams.getModuleCode().equals(MsgModuleCodeEnum.DIARY_DETAIL_VIEW.getCode())))
                    && IntegerUtils.intValueAsDefault(esAppRecord.getTid()) != commentRefuseMsgTid;
            if (jumpFlag || StringUtils.isNotEmpty(appSystemMsgVO.getSchemeUrl())) {
                appSystemMsgVO.setJumpFlag(Boolean.TRUE);
            }
            appSystemMsgVOS.add(appSystemMsgVO);
        }
        log.debug("querySystemList uid:{} searchMessageRecordVO:{} appSystemMsgVOS:{}", uid, searchMessageRecordVO, appSystemMsgVOS);
        return appSystemMsgVOS;
    }

    /**
     * JSON转AppRecord对象
     *
     * @param data
     * @return
     */
    private EsAppRecord jsonToAppRecord(JSONObject data) {
        EsAppRecord esAppRecord = null;
        try {
            esAppRecord = data.toJavaObject(EsAppRecord.class);
            AppMsgWrapper.BizParams bizParams = null;
            if (StringUtils.isNotBlank(esAppRecord.getBizParam())) {
                try {
                    bizParams = JSONObject.parseObject(esAppRecord.getBizParam()).toJavaObject(AppMsgWrapper.BizParams.class);
                    if (bizParams != null) {
                        bizParams.setModuleCode(StringUtils.defaultString(bizParams.getModuleCode(), MsgModuleCodeEnum.OTHER.getCode()));
                    }
                } catch (Exception e) {
                    log.warn("jsonToAppRecord json parseObject exception data:{} e:{}", data, e);
                }
            }
            bizParams.setModuleCode(StringUtils.defaultString(bizParams.getModuleCode(), MsgModuleCodeEnum.OTHER.getCode()));
            esAppRecord.setBizParams(bizParams);
        } catch (Exception e) {
            log.warn("jsonToAppRecord exception data:{} e:{}", data, e);
        }
        return esAppRecord;
    }

    /**
     * 根据FirstId查询群发消息ID
     *
     * @param firstId
     * @return
     */
    private List<Integer> queryPushUserMsgIds(String firstId) {
        List<Integer> msgIds = new ArrayList<>();
        MessagePushUser messagePushUser = null;
        if (StringUtils.isNotBlank(firstId)) {
            try {
                Optional<MessagePushUser> messagePushUserOptional = messagePushUserRepository.findByFirstIdOrderById(firstId);
                if (messagePushUserOptional != null && messagePushUserOptional.isPresent()) {
                    messagePushUser = messagePushUserOptional.get();
                }
                if (messagePushUser != null && StringUtils.isNotBlank(messagePushUser.getMsgId())) {
                    msgIds = Arrays.stream(messagePushUser.getMsgId().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.warn("getPushUserMsgIds exception firstId:{} messagePushUser:{} msgIds:{} e:{}", firstId, messagePushUser, msgIds, e);
            }
            log.debug("getPushUserMsgIds firstId:{} messagePushUser:{} msgIds:{}", firstId, messagePushUser, msgIds);
        } else {
            log.info("getPushUserMsgIds params invalid");
        }
        return msgIds;
    }

    /**
     * 查询群发消息
     *
     * @param appSystemMsgVOS
     * @param appOsType
     * @param msgIds
     * @return
     */
    private List<AppSystemMsgVO> queryPushMessage(List<AppSystemMsgVO> appSystemMsgVOS, int appOsType, List<Integer> msgIds, int startTime) {
        try {
            List<MessagePush> messagePushes = messagePushRepository.queryPushMsg(startTime, appOsType, TimeUtils.getCurrentTimestamp(), msgIds);
            for (MessagePush messagePush : messagePushes) {
                AppSystemMsgVO appSystemMsgVO = AppSystemMsgVO.builder()
                        .showType(StringUtils.isNoneBlank(messagePush.getPushImg()) ? AppMsgShowTypeEnum.CARD.getCode() : AppMsgShowTypeEnum.TEXT.getCode())
                        .msgId("msg" + messagePush.getId())
                        .title(StringUtils.defaultString(messagePush.getTitle()))
                        .content(StringUtils.defaultString(messagePush.getContent()))
                        .imageUrl(formatPicUrl(StringUtils.defaultString(messagePush.getPushImg()), AppMsgConstant.HOST_PIC))
                        .isGroupMsg(Boolean.TRUE)
                        .isRead(Boolean.FALSE)
                        .time(messagePush.getPushTime())
                        .icon("")
                        .jumpFlag(StringUtils.isBlank(messagePush.getUrl()) ? Boolean.FALSE : Boolean.TRUE)
                        .schemeUrl(StringUtils.defaultString(messagePush.getUrl()))
                        .msgJumpParams(generateMsgJumpParams(MsgModuleCodeEnum.OTHER.getCode(), messagePush.getUrl()))
                        .build();
                appSystemMsgVOS.add(appSystemMsgVO);
            }
            log.debug("queryPushMessage appOsType:{} msgIds:{} startTime:{} messagePushes:{} appSystemMsgVOS;{}", appOsType, msgIds, startTime, messagePushes, appSystemMsgVOS);
        } catch (Exception e) {
            log.warn("queryPushMessage exception appOsType:{} msgIds:{} e:{}", appOsType, msgIds, e);
        }
        return appSystemMsgVOS;
    }

    /**
     * 查询活动消息
     *
     * @param appSystemMsgVOS
     * @param appId
     * @param appOsType
     * @param cityId
     * @param appVersion
     * @return
     */
    private List<AppSystemMsgVO> queryAppAds(List<AppSystemMsgVO> appSystemMsgVOS, int appId, int appOsType, int cityId, String appVersion, int startTime) {
        try {
            List<String> linkUrls = new ArrayList<>();
            List<AppAd> appAds = appAdRepository.queryAppAds(startTime, TimeUtils.getCurrentTimestamp(), appId, appOsType, cityId, appVersion);
            for (AppAd appAd : appAds) {
                if (StringUtils.isEmpty(appAd.getLinkUrl()) || linkUrls.contains(appAd.getLinkUrl())) {
                    continue;
                }
                linkUrls.add(appAd.getLinkUrl());
                AppSystemMsgVO appSystemMsgVO = AppSystemMsgVO.builder()
                        .showType(AppMsgShowTypeEnum.CARD.getCode())
                        .msgId("ad" + appAd.getId())
                        .title(StringUtils.defaultString(appAd.getAdName()))
                        .content("")
                        .imageUrl(formatPicUrl(StringUtils.defaultString(appAd.getNormalImg()), AppMsgConstant.HOST_PIC_HOTAREA))
                        .isGroupMsg(Boolean.TRUE)
                        .isRead(Boolean.FALSE)
                        .time(appAd.getBeginTime())
                        .icon("")
                        .jumpFlag(Boolean.TRUE)
                        .schemeUrl("")
                        .msgJumpParams(generateMsgJumpParams(MsgModuleCodeEnum.OTHER.getCode(), appAd.getLinkUrl()))
                        .build();
                appSystemMsgVOS.add(appSystemMsgVO);
            }
            log.debug("queryAppAds appId:{} appOsType:{} cityId:{} appVersion;{} appAds:{} appSystemMsgVOS:{}", appId, appOsType, cityId, appVersion, appAds, appSystemMsgVOS);
        } catch (Exception e) {
            log.warn("queryAppAds exception appId:{} appOsType:{} cityId:{} appVersion;{} e:{}", appId, appOsType, cityId, appVersion, e);
        }
        return appSystemMsgVOS;
    }

    /**
     * 格式化图片地址
     *
     * @param picurl
     * @param host
     * @return
     */
    private String formatPicUrl(String picurl, String host) {
        if (StringUtils.isAllBlank(picurl)) {
            return picurl;
        }
        try {
            Matcher matcher = picUrlPattern.matcher(picurl);
            boolean state = matcher.find();
            if (!state) {
                picurl = host + picurl;
            }
        } catch (Exception e) {
            log.warn("formatPicUrl exception picurl:{} host:{} e:{}", picurl, host, e);
            picurl = host + picurl;
        }
        return picurl;
    }

    /**
     * 生成消息跳转数据
     *
     * @param moduleCode
     * @param url
     * @return
     */
    private AppMsgWrapper.MsgJumpParams generateMsgJumpParams(String moduleCode, String url) {
        return AppMsgWrapper.MsgJumpParams.builder()
                .moduleCode(StringUtils.defaultString(moduleCode, MsgModuleCodeEnum.OTHER.getCode()))
                .objectId(0)
                .url(StringUtils.defaultString(url, ""))
                .extraDataParams(generateExtraDataParams())
                .build();
    }

    /**
     * 生成默认的跳转扩展参数
     *
     * @return
     */
    private JSONObject generateExtraDataParams() {
        JSONObject extraDataParams = new JSONObject();
        extraDataParams.put("cover", "empty");
        return extraDataParams;
    }

    /**
     * 生成消息跳转数据
     *
     * @param moduleCode
     * @param url
     * @return
     */
    private AppMsgWrapper.MsgJumpParams generateMsgJumpParams(String moduleCode, String url, int objectId, JSONObject extraDataParams) {
        return AppMsgWrapper.MsgJumpParams.builder()
                .moduleCode(StringUtils.defaultString(moduleCode, MsgModuleCodeEnum.OTHER.getCode()))
                .objectId(objectId)
                .url(StringUtils.defaultString(url, ""))
                .extraDataParams(ObjectUtils.defaultIfNull(extraDataParams, generateExtraDataParams()))
                .build();
    }

    /**
     * 查询互动消息
     *
     * @param searchMessageRecordDTO
     * @return
     */
    private PageResult<AppInteractionMsgVO> queryInteractionMsg(SearchMessageRecordDTO searchMessageRecordDTO, boolean isReplenishUserInfo) {
        SearchMessageRecordVO searchMessageRecordVO = messageCenterService.searchMessageRecord(searchMessageRecordDTO);
        if (!(searchMessageRecordVO instanceof SearchMessageRecordVO)) {
            log.warn("queryInteractionMsg searchMessageRecord result format error searchMessageRecordDTO:{} searchMessageRecordVO:{}", searchMessageRecordDTO, searchMessageRecordVO);
            return ResponseUtils.buildPageResult();
        }
        List<AppInteractionMsgVO> appInteractionMsgVOS = new ArrayList<>();
        List<Integer> uidList = new ArrayList<>();
        List<Integer> accountList = new ArrayList<>();
        List<EsAppRecord> esAppRecords = new ArrayList<>();
        for (JSONObject item : searchMessageRecordVO.getResult()) {
            EsAppRecord esAppRecord = jsonToAppRecord(item);
            if (esAppRecord == null || esAppRecord.getBizParams() == null) {
                continue;
            }
            AppMsgWrapper.BizParams bizParams = esAppRecord.getBizParams();
            esAppRecords.add(esAppRecord);
            if (isReplenishUserInfo) {
                if (IntegerUtils.intValueAsDefault(bizParams.getTriggerUserType()) == UserTypeEnum.BUSINESS.getCode()) {
                    if (IntegerUtils.isGtLimitValue(bizParams.getTriggerAccountId())) {
                        accountList.add(bizParams.getTriggerAccountId());
                    }
                } else {
                    if (IntegerUtils.isGtLimitValue(bizParams.getTriggerUid())) {
                        uidList.add(bizParams.getTriggerUid());
                    }
                }
            }
        }
        Map<Integer, UserWrapper.Owner> ownerMap = null;
        Map<Integer, CompanyResultWrapper.Business> businessMap = null;
        try {
            if (uidList.size() > 0) {
                ownerMap = accountService.batchQueryOwner(uidList);
            }
            if (accountList.size() > 0) {
                businessMap = externalService.decInfoQueryList(accountList);
            }
        } catch (Exception e) {
            log.warn("AppMsgServiceImpl.queryInteractionMsg batchQueryOwner exception uidList:{} e:{}", uidList, e);
        }
        for (EsAppRecord esAppRecord : esAppRecords) {
            AppInteractionMsgVO appInteractionMsgVO = formatInteractionMsg(esAppRecord, esAppRecord.getBizParams());
            appInteractionMsgVOS.add(replenishUserInfo(appInteractionMsgVO, esAppRecord.getBizParams(), ownerMap, businessMap));
        }
        log.debug("queryInteractionMsg accountList:{} businessMap:{} uidList:{} ownerMap;{} esAppRecords:{} searchMessageRecordVO:{} appInteractionMsgVOS:{}", accountList, businessMap, uidList, ownerMap, esAppRecords, searchMessageRecordVO, appInteractionMsgVOS);
        return ResponseUtils.buildPageResult(IntegerUtils.intValuefromLong(searchMessageRecordVO.getTotal()), appInteractionMsgVOS);
    }

    /**
     * 格式化互动消息
     *
     * @param esAppRecord
     * @return
     */
    private AppInteractionMsgVO formatInteractionMsg(EsAppRecord esAppRecord, AppMsgWrapper.BizParams bizParams) {
        AppInteractionMsgVO appInteractionMsgVO = new AppInteractionMsgVO();
        appInteractionMsgVO.setMsgId(IntegerUtils.intValueAsDefault(esAppRecord.getId()));
        if (bizParams.getModuleCode().equals(MsgModuleCodeEnum.DIARY.getCode()) && IntegerUtils.isGtLimitValue(bizParams.getNoteId())) {
            appInteractionMsgVO.setMsgAction(diaryPushMsg.get(bizParams.getNoteId().intValue()));
        } else if (StringUtils.isNotBlank(bizParams.getTitle())) {
            appInteractionMsgVO.setMsgAction(bizParams.getTitle());
        } else {
            appInteractionMsgVO.setMsgAction(StringUtils.defaultString(esAppRecord.getAppContent()));
        }
        appInteractionMsgVO.setMsgDesc(StringUtils.defaultString(bizParams.getContent()));
        appInteractionMsgVO.setMsgTime(IntegerUtils.intValuefromLong(esAppRecord.getSendTime()));
        appInteractionMsgVO.setIsRead(IntegerUtils.isGtLimitValue(esAppRecord.getIsRead()));
        appInteractionMsgVO.setContentType(bizParams.getModuleCode());
        appInteractionMsgVO.setOriginContentDesc(StringUtils.defaultString(bizParams.getOriginContentDesc()));
        if (StringUtils.isNotBlank(bizParams.getOriginContentPic())) {
            appInteractionMsgVO.setOriginContentPic(StringUtils.replace(bizParams.getOriginContentPic(), "!750.webp", ""));
        } else {
            appInteractionMsgVO.setOriginContentPic(StringUtils.defaultString(bizParams.getOriginContentPic()));
        }
        AppMsgWrapper.MsgJumpParams msgJumpParams = new AppMsgWrapper.MsgJumpParams();
        msgJumpParams.setModuleCode(bizParams.getModuleCode());
        if (bizParams.getModuleCode().equals(MsgModuleCodeEnum.RELATIONSHIP.getCode())) {
            if (bizParams.getTriggerUserType().intValue() == UserTypeEnum.BUSINESS.getCode()) {
                msgJumpParams.setObjectId(bizParams.getTriggerAccountId());
            } else {
                msgJumpParams.setObjectId(bizParams.getTriggerUid());
            }
        } else {
            msgJumpParams.setObjectId(IntegerUtils.intValueAsDefault(bizParams.getObjectId()));
        }
        msgJumpParams.setUrl(StringUtils.defaultString(bizParams.getUrl()));
        msgJumpParams.setExtraDataParams(ObjectUtils.defaultIfNull(bizParams.getExtraDataParams(), generateExtraDataParams()));
        appInteractionMsgVO.setMsgJumpParams(msgJumpParams);
        AppMsgWrapper.Comment comment = new AppMsgWrapper.Comment();
        if (StringUtils.isNotBlank(bizParams.getRemark())) {
            comment.setOrigin("我的评论：" + bizParams.getRemark());
        } else {
            comment.setOrigin("");
        }
        appInteractionMsgVO.setMsgContent(AppMsgWrapper.MsgContent.builder()
                .comment(comment)
                .build());
        appInteractionMsgVO.setSchemeUrl(StringUtils.defaultString(bizParams.getSchemeUrl()));
        return appInteractionMsgVO;
    }

    /**
     * 补充用户信息
     *
     * @param appInteractionMsgVO
     * @param bizParams
     * @return
     */
    private AppInteractionMsgVO replenishUserInfo(AppInteractionMsgVO appInteractionMsgVO, AppMsgWrapper.BizParams bizParams, Map<Integer, UserWrapper.Owner> ownerMap, Map<Integer, CompanyResultWrapper.Business> businessMap) {
        if (IntegerUtils.intValueAsDefault(bizParams.getTriggerUserType()) == UserTypeEnum.BUSINESS.getCode()) {
            CompanyResultWrapper.Business business;
            if (businessMap == null) {
                business = new CompanyResultWrapper.Business();
            } else {
                business = businessMap.getOrDefault(IntegerUtils.intValueAsDefault(bizParams.getTriggerAccountId()), new CompanyResultWrapper.Business());
            }
            appInteractionMsgVO.setUserAvatar(StringUtils.defaultString(business.getAvatar(), AppMsgConstant.OWNER_AVATAR_DEFAULT));
            appInteractionMsgVO.setNickName(StringUtils.defaultString(business.getShortName(), StringUtils.defaultString(business.getName())));
            appInteractionMsgVO.setIdentificationStatus(0);
            appInteractionMsgVO.setIdentificationType(0);
            appInteractionMsgVO.setIdentificationTime(0);
            appInteractionMsgVO.setIdentificationDesc("");
            appInteractionMsgVO.setIdentificationPic("");
        } else {
            UserWrapper.Owner owner;
            if (ownerMap == null) {
                owner = buildDefaultOwner();
            } else {
                owner = ownerMap.getOrDefault(IntegerUtils.intValueAsDefault(bizParams.getTriggerUid()), buildDefaultOwner());
            }
            appInteractionMsgVO.setNickName(owner.getAuthorName());
            appInteractionMsgVO.setUserAvatar(owner.getAuthorAvatar());
            appInteractionMsgVO.setIdentificationStatus(owner.getIdentificationStatus());
            appInteractionMsgVO.setIdentificationType(owner.getIdentificationType());
            appInteractionMsgVO.setIdentificationTime(owner.getIdentificationTime());
            appInteractionMsgVO.setIdentificationDesc(owner.getIdentificationDesc());
            appInteractionMsgVO.setIdentificationPic(owner.getIdentificationPic());
        }
        return appInteractionMsgVO;
    }

    /**
     * 生成默认业主对象
     *
     * @return
     */
    private UserWrapper.Owner buildDefaultOwner() {
        return UserWrapper.Owner.builder()
                .authorName("")
                .authorAvatar(AppMsgConstant.OWNER_AVATAR_DEFAULT)
                .identificationType(0)
                .identificationStatus(0)
                .identificationTime(0)
                .identificationPic("")
                .identificationDesc("")
                .build();
    }

    /**
     * 生成关注消息查询的参数
     *
     * @return
     */
    private SearchMessageRecordDTO generateFollowMsgQueryParams(int isRead) {
        SearchMessageRecordDTO searchMessageRecordDTO = new SearchMessageRecordDTO();
        searchMessageRecordDTO.setTid(followMsgTid);
        if (isRead != MsgReadStatusEnum.INVALID.getCode()) {
            searchMessageRecordDTO.setIsRead(isRead);
        }
        return searchMessageRecordDTO;
    }

    /**
     * 生成关注消息查询的参数
     *
     * @return
     */
    private SearchMessageRecordDTO generateInteractionMsgQueryParams(int isRead) {
        SearchMessageRecordDTO searchMessageRecordDTO = new SearchMessageRecordDTO();
        try {
            List<Integer> nodeIds = JSONArray.parseArray(interactionNodeIds).toJavaList(Integer.class);
            searchMessageRecordDTO.setNodeIds(nodeIds);
            if (isRead != MsgReadStatusEnum.INVALID.getCode()) {
                searchMessageRecordDTO.setIsRead(isRead);
            }
        } catch (Exception e) {
            log.warn("generateInteractionMsgQueryParams exception interactionNodeIds:{} e:{}", interactionNodeIds, e);
            return null;
        }
        return searchMessageRecordDTO;
    }

    /**
     * 生成系统消息查询的参数
     *
     * @return
     */
    private SearchMessageRecordDTO generateSystemMsgQueryParams(int isRead) {
        if (StringUtils.isNotBlank(systemNodeIds)) {
            try {
                List<Integer> nodeIds = JSONArray.parseArray(systemNodeIds).toJavaList(Integer.class);
                log.debug("generateSystemMsgQueryParams systemNodeIds:{} nodeIds:{}", systemNodeIds, nodeIds);
                SearchMessageRecordDTO searchMessageRecordDTO = new SearchMessageRecordDTO();
                searchMessageRecordDTO.setNodeIds(nodeIds);
                if (isRead != MsgReadStatusEnum.INVALID.getCode()) {
                    searchMessageRecordDTO.setIsRead(isRead);
                }
                return searchMessageRecordDTO;
            } catch (Exception e) {
                log.warn("generateSystemMsgQueryParams exception systemNodeIds:{} e:{}", systemNodeIds, e);
            }
        } else {
            log.warn("generateSystemMsgQueryParams systemNodeIds is empty");
        }
        return null;
    }

    /**
     * 生成消息查询的基础参数
     *
     * @param uid
     * @param page
     * @param pageSize
     * @return
     */
    private SearchMessageRecordDTO generateMsgQueryBaseParams(SearchMessageRecordDTO searchMessageRecordDTO, int uid, int page, int pageSize) {
        PageInfo pageInfo = PageInfo.builder()
                .currPage(page)
                .pageSize(pageSize)
                .build();
        searchMessageRecordDTO.setUid(uid);
        searchMessageRecordDTO.setSendType(MsgSendTypeEnum.APP.getCode());
        searchMessageRecordDTO.setStime(generateMsgQueryStartTime());
        searchMessageRecordDTO.setEtime(TimeUtils.getCurrentTimestamp());
        searchMessageRecordDTO.setPageInfo(pageInfo);
        return searchMessageRecordDTO;
    }

    /**
     * 生成消息查询的开始时间
     *
     * @return
     */
    private int generateMsgQueryStartTime() {
        int currentTimestamp = TimeUtils.getCurrentTimestamp();
        return currentTimestamp - (3600 * 24 * 30);
    }

    /**
     * 生成消息查询的开始时间
     *
     * @return
     */
    private int generateMsgQueryStartTime(int num) {
        int currentTimestamp = TimeUtils.getCurrentTimestamp();
        return currentTimestamp - (3600 * 24 * num);
    }
}
