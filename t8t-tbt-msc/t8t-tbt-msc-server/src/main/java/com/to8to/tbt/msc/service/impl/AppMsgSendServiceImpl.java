package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.to8to.common.executors.SafeRunnable;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.Constants;
import com.to8to.tbt.msc.dto.RedirectAppMsgDTO;
import com.to8to.tbt.msc.dto.TemplateMsgDTO;
import com.to8to.tbt.msc.entity.response.RedirectAppMsgResult;
import com.to8to.tbt.msc.entity.dto.PushClickDTO;
import com.to8to.tbt.msc.entity.dto.PushMessageDTO;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import com.to8to.tbt.msc.entity.mysql.main.AppTiming;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.AppApiVersionEnum;
import com.to8to.tbt.msc.enumeration.AppSendModeEnum;
import com.to8to.tbt.msc.enumeration.MsgSendStatusEnum;
import com.to8to.tbt.msc.repository.mysql.main.AppTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppTimingRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.to8to.tbt.msc.common.MyExceptionStatus.*;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class AppMsgSendServiceImpl implements AppMsgSendService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Autowired
    private AppTimingRepository appTimingRepository;

    @Autowired
    private PushService pushService;

    @Autowired
    private TemplateV2Service templateV2Service;

    @Autowired
    private AppRecordService appRecordService;

    @Autowired
    private TimingAppService timingAppService;

    @Autowired
    private AsyncTaskService asyncTaskService;


    @Override
    public StatusResultResponse<String> sendAppTemplateMsg(TemplateMsgDTO params) {
        String message = handlerBeforeSend(params);
        if (StringUtils.isNotBlank(message)) {
            log.warn(LogUtils.buildTemplate("params message"), params, message);
            return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_FAIL.getCode(), message);
        }
        //过滤表情符号
        params = filterEmoji(params);

        //延时消息
        if (IntegerUtils.isGtLimitValue(params.getDelayTime())) {
            JSONObject bizData = new JSONObject();
            JSONObject bizParam = Optional.ofNullable(params.getBizParam()).orElse(new JSONObject());
            String ticker = params.getTicker();
            JSONObject click = params.getClick();
            bizData.put("biz_param", bizParam);
            if (StringUtils.isNotEmpty(ticker)) {
                bizData.put("ticker", ticker);
            }
            if (click != null) {
                bizData.put("click", click);
            }
            boolean status = timingAppService.create(params.getTid(), params.getUid(), params.getSender(), params.getAppId(), params.getVersion(), params.getContent(), bizData, params.getDelayTime(), params.getSendMode());
            if (!AppSendModeEnum.SAVE_UNSEND.compare(params.getSendMode())) {
                if (status) {
                    return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS.getCode(), SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS.getMessage());
                } else {
                    return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_DELAY_TIME_FAIL.getCode(), SEND_ALL_MESSAGE_DELAY_TIME_FAIL.getMessage());
                }
            }
        }
        sendAppMsgV2(params);
        return ResponseUtils.sendMessageSuccessResponse();
    }

    @Override
    public RedirectAppMsgResult sendAppMsgV2(RedirectAppMsgDTO params) {
        try {
            ValidUtils.valid(params);
        }catch (RPCException e){
            return RedirectAppMsgResult.builder().code(-1).msg(e.getLocalizedMessage()).build();
        }
        try {
            String resResultStr = pushService.send(params);
            log.info("[redirectAppMsg]重定向app消息ֵ{}", resResultStr);
            return RedirectAppMsgResult.builder().code(1).msg("推送app消息成功").result(resResultStr).build();
        } catch (Exception e) {
            log.warn("[redirectAppMsg]重定向app消息异常{}", e);
        }
        return RedirectAppMsgResult.builder().code(-9).msg("app推送失败").build();
    }

    @Override
    public void sendDelayMessage() {
        List<AppTiming> unSendList = appTimingRepository.getUnSendMessage(TimeUtils.getCurrentTimestamp());
        if (unSendList.isEmpty()) {
            return;
        }
        for (AppTiming appTiming : unSendList) {
            JSONObject bizData = JSONObject.parseObject(appTiming.getBizData());
            JSONObject bizParam = bizData.getJSONObject("biz_param");
            String ticker = bizData.getString("ticker");
            JSONObject click = bizData.getJSONObject("click");
            int sendMode = appTiming.getSendMode();
            sendMode = sendMode == AppSendModeEnum.SAVE_UNSEND.getSendMode() ?
                    AppSendModeEnum.UNSAVE_SEND.getSendMode() : sendMode;

            TemplateMsgDTO templateMsgDTO = TemplateMsgDTO.builder()
                    .tid(appTiming.getTid())
                    .appId(appTiming.getAppId())
                    .uid(appTiming.getUids())
                    .content(appTiming.getMsgContent())
                    .sender(appTiming.getSender())
                    .version(appTiming.getVersion())
                    .sendMode(sendMode)
                    .bizParam(bizParam)
                    .ticker(ticker)
                    .click(click)
                    .build();

            StatusResultResponse<String> statusResultResponse = sendAppTemplateMsg(templateMsgDTO);
            if (statusResultResponse.getStatus() == SEND_ALL_MESSAGE_SUCCESS.getCode()) {
                appTiming.setIsSend(1);
                appTiming.setSendTime(TimeUtils.getCurrentTimestamp());
                try {
                    appTimingRepository.save(appTiming);
                } catch (Exception e) {
                    log.warn(LogUtils.buildExceptionTemplate("appTiming"), appTiming, e);
                }
            } else {
                log.warn(LogUtils.buildExceptionTemplate("appTiming statusResultResponse"), appTiming, statusResultResponse);
            }
        }
        log.info(LogUtils.buildTemplate("size list"), unSendList.size(), unSendList);
    }

    /**
     * 过滤掉内容中的表情符号
     *
     * @param params
     * @return
     */
    private TemplateMsgDTO filterEmoji(TemplateMsgDTO params) {
        JSONObject filter = JSONObject.parseObject(JSON.toJSONString(params));
        if (filter != null && filter.size() > 0) {
            try {
                filter = JSONObject.parseObject(CommonUtils.filterEmoji(filter.toJSONString()));
            } catch (Exception e) {
                log.warn("filterEmoji 表情符号过滤失败:{}", filter.toJSONString());
            }
        }
        return filter.toJavaObject(TemplateMsgDTO.class);
    }

    /**
     * APP消息发送前置检查
     *
     * @param params
     * @return
     */
    private String handlerBeforeSend(TemplateMsgDTO params) {
        AppTemplate appMsgTemplate = appTemplateRepository.findByTid(params.getTid()).orElse(null);
        // 模板正确性检查
        if (appMsgTemplate == null || appMsgTemplate.getAppId() <= 0 || StringUtil.isEmpty(appMsgTemplate.getAppContent())) {
            return "模板错误，请检查模板内容和AppID是否正确！";
        }
        // UID参数检查
        long noNumericCount = Arrays.stream(params.getUid().split(",")).filter(uid -> !StringUtils.isNumeric(uid)).count();
        if (noNumericCount > 0) {
            return "无效的参数，uid 必须为整数！";
        }

        // V2.0 版本参数校验
        if (IntegerUtils.isEqLimitValue(params.getVersion(), AppApiVersionEnum.SECOND_VERSION.getVersion())) {
            if (IntegerUtils.isEqLimitValue(appMsgTemplate.getPushType()) || IntegerUtils.isEqLimitValue(appMsgTemplate.getPushScope())) {
                return "【V2】无效的参数，模板推送类型、推送范围不正确！";
            }
            int singlePushScope = 2;
            if (IntegerUtils.isEqLimitValue(appMsgTemplate.getPushScope(), singlePushScope)) {
                String subUids = Optional.ofNullable(params.getBizParam()).orElse(new JSONObject()).getString("subUid");
                if (StringUtil.isEmpty(subUids)) {
                    return "无效的参数，subUid 为 null！";
                } else {
                    noNumericCount = Arrays.stream(subUids.split(",")).filter(uid -> !StringUtils.isNumeric(uid)).count();
                    if (noNumericCount > 0) {
                        return "无效的参数，subUid 必须为整数！";
                    }
                }
            }
        }
        // 关键字校验
        String content = StringUtils.isNotBlank(params.getContent()) ? params.getContent() : appMsgTemplate.getAppContent();
        if (content.contains("{") || content.contains("}")) {
            JSONObject keywords = params.getKeyword();
            if (null != keywords) {
                try {
                    content = templateV2Service.replaceKeyword(content, keywords);
                } catch (NullPointerException exception) {
                    log.warn("handlerBeforeSend 替换App消息内容关键字失败(null)! 内容:{},关键字参数:{},", appMsgTemplate.getAppContent(), keywords.toJSONString());
                    return "无效的消息内容,替换关键字失败！";
                } catch (Exception e) {
                    log.error("handlerBeforeSend 替换App消息内容关键字失败! 内容:{},关键字参数:{},错误:{}", appMsgTemplate.getAppContent(), keywords.toJSONString(), e);
                    return "无效的消息内容,替换关键字失败！";
                }
            }
        }
        params.setContent(content);
        return null;
    }

    /**
     * 发送APP消息-版本2
     *
     * @param params
     * @return
     */
    private boolean sendAppMsgV2(TemplateMsgDTO params) {
        int tid = params.getTid();
        int appId = params.getAppId();
        int sendMode = IntegerUtils.intValueAsDefault(params.getSendMode());
        String uids = params.getUid();
        String finalContent = params.getContent();
        JSONObject bizParam = Optional.ofNullable(params.getBizParam()).orElse(new JSONObject());
        AppTemplate appMsgTemplate = appTemplateRepository.findByTid(tid).orElse(null);
        int pushScope = appMsgTemplate.getPushScope() - 1;
        int pushType = appMsgTemplate.getPushType() - 1;
        int needPush = appMsgTemplate.getNeedPush();


        Template msgcTemplate = templateRepository.findById(tid).orElse(null);
        String title = msgcTemplate.getTitle();

        // 给主账号发送
        if (pushScope == 0) {
            List<Integer> uidList = Arrays.stream(uids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            // 不需要推送
            if (needPush == 1) {
                uidList.forEach(uid -> appRecordService.create(tid, uid, params.getSender(), finalContent, MsgSendStatusEnum.SUCCESS.ordinal(), bizParam.toJSONString()));
            } else {
                asyncTaskService.submit(new SafeRunnable(()-> uidList.forEach(uid -> asyncAppMsgPushV2(tid, uid, 0, pushType, pushScope, appId, title, finalContent, params.getSender(), params.getTicker(), params.getClick(), bizParam, sendMode))));
            }
        }
        // 给主账号下的子账号发送
        else if (pushScope == 1) {
            String uid = uids.split(",")[0];
            int uidInt = Integer.parseInt(uid);
            String subUids = bizParam.getString("subUid");
            List<Integer> subUidList = Arrays.stream(subUids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            if (needPush == 1 || AppSendModeEnum.SAVE_UNSEND.compare(sendMode)) {
                subUidList.forEach(subUid -> {
                    bizParam.put("subUid", subUid);
                    appRecordService.create(tid, uidInt, params.getSender(), finalContent, MsgSendStatusEnum.SUCCESS.ordinal(), bizParam.toJSONString());
                });
            } else {
                asyncTaskService.submit(new SafeRunnable(() -> subUidList.forEach(subUid -> {
                    bizParam.put("subUid", subUid);
                    asyncAppMsgPushV2(tid, uidInt, subUid, pushType, pushScope, appId, title, finalContent, params.getSender(), params.getTicker(), params.getClick(), bizParam, sendMode);
                })));
            }
        }
        return true;
    }

    /**
     * 异步发送APP消息
     *
     * @param tid
     * @param uid
     * @param subUid
     * @param pushType
     * @param pushScope
     * @param appId
     * @param title
     * @param content
     * @param sender
     * @param ticker
     * @param click
     * @param bizParam
     * @param sendMode
     */
    private void asyncAppMsgPushV2(int tid, int uid, int subUid, int pushType, int pushScope,
                                   int appId, String title, String content, String sender,
                                   String ticker, JSONObject click, JSONObject bizParam, int sendMode) {
        int status = MsgSendStatusEnum.SUCCESS.ordinal();
        RedirectAppMsgDTO redirectAppMsgDTO = null;
        String pushResult = null;
        if (AppSendModeEnum.SAVE_SEND.compare(sendMode) || AppSendModeEnum.UNSAVE_SEND.compare(sendMode)) {
            PushClickDTO clickDTO = null;
            if (click != null) {
                clickDTO = click.toJavaObject(PushClickDTO.class);
            }
            PushMessageDTO messageDTO = new PushMessageDTO();
            messageDTO.setTitle(title);
            messageDTO.setDescription(content);
            messageDTO.setTicker(StringUtils.isNotBlank(ticker) ? ticker : content);
            messageDTO.setClick(clickDTO);
            redirectAppMsgDTO = RedirectAppMsgDTO.builder()
                    .type(pushType)
                    .scope(pushScope)
                    .uid(String.valueOf(uid))
                    .subUid(String.valueOf(subUid))
                    .appid(Integer.valueOf(appId))
                    .message(messageDTO)
                    .to8to(bizParam)
                    .build();
            pushResult = sendAppMsgV2(redirectAppMsgDTO).getResult();
            if (pushResult != null) {
                try {
                    JSONObject response = JSONObject.parseObject(pushResult);
                    int pushStatus = response.getIntValue("status");
                    if (pushStatus != Constants.HTTP_RESP_OK) {
                        status = MsgSendStatusEnum.FAIL.ordinal();
                    }
                } catch (Exception e) {
                    log.warn(LogUtils.buildExceptionTemplate("pushResult"), pushResult, e);
                }
            } else {
                status = MsgSendStatusEnum.FAIL.ordinal();
            }
            if (AppSendModeEnum.SAVE_SEND.compare(sendMode)) {
                appRecordService.create(tid, uid, sender, content, status, bizParam.toJSONString());
            }
        }
        log.info(LogUtils.buildTemplate("sendMode redirectAppMsgDTO pushResult"), sendMode, redirectAppMsgDTO, pushResult);
    }
}
