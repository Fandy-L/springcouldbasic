package com.to8to.tbt.msc.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import com.google.gson.JsonObject;
import com.to8to.common.util.DateUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.*;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.vo.AppVO;
import com.to8to.tbt.msc.entity.vo.ChannelVO;
import com.to8to.tbt.msc.enumeration.ChannelEnum;
import com.to8to.tbt.msc.enumeration.PlatformEnum;
import com.to8to.tbt.msc.enumeration.ScopeEnum;
import com.to8to.tbt.msc.enumeration.StatusEnum;
import com.to8to.tbt.msc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cn.jpush.api.push.model.notification.Notification.newBuilder;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 19:51
 */
@Slf4j
@Service("jiguangPushService")
public class JiguangChannelPushServiceImpl implements ChannelPushService {

    private final static int JIGUANG_RESPONSE_SUCCESS = 200;
    private final static String TEST_APP_KEY = "fcf274551cbdf249751d385e";
    private final static String TEST_MASTER_SECRET = "6ba5b514e8bc08b9e0299146";

    @Autowired
    private PushAppService pushAppService;

    @Autowired
    private PushChannelService pushChannelService;

    @Autowired
    @Qualifier("jiguangDeviceTokenService")
    private ChannelDeviceTokenService channelDeviceTokenService;

    @Autowired
    private PushMessageService pushMessageService;

    @Value("${push.tag.user.all}")
    private String allUserTag;

    @Value("${push.tag.user.anonymous}")
    private String anonymousUserTag;

    @Value("${push.apns_production.enable}")
    private Boolean isApnsProduction;

    @Value("${push.platform}")
    private Integer pushPlatform;

    @Value("${push.production:false}")
    private Boolean isProduction;

    @Autowired
    private AlarmService alarmService;

    private ConcurrentMap<String, JPushClient> jPushClientMap = new ConcurrentHashMap<>();

    @Override
    public PushResultDTO push(PushDTO pushDto) {
        PushResultDTO pushResultDTO = new PushResultDTO();
        AppVO appVO = pushAppService.getAppByPushAppId(pushDto.getAppId());
        ChannelVO channelVO = pushChannelService.getChannel(pushDto.getAppId(), ChannelEnum.JIGUANG);
        JPushClient jpushClient;
        if (this.useProductionAccount(appVO.getPushAppId(), pushDto.getScope())) {
            jpushClient = this.getPushClient(channelVO.getAppKey(), channelVO.getMasterSecret());
        } else {
            jpushClient = this.getPushClient(TEST_APP_KEY, TEST_MASTER_SECRET);
        }

        //构建Audience
        List<String> tokenList = new ArrayList<>();
        Audience audience = this.buildAudience(pushDto, appVO.getPushAppId(), channelVO.getChannel(), tokenList);

        //构建Notification
        Notification notification = this.buildNotification(pushDto.getNotification(), appVO.getUriActivity());

        //构建Options
        Options options = this.buildOptions();

        //构建Platform
        Platform platform = this.buildPlatform(pushPlatform);

        PushPayload.Builder payloadBuilder = new PushPayload.Builder();
        PushPayload payload = payloadBuilder
                .setPlatform(platform)
                .setAudience(audience)
//                .setMessage(message) //有message只会走极光通道
                .setNotification(notification)
                .setOptions(options)
                .build();
        log.info("极光推送payload={}", payload.toString());

        //配置CreateMessageDTO
        CreateMessageDTO messageDTO = new CreateMessageDTO();
        messageDTO.setPushAppId(pushDto.getAppId());
        messageDTO.setT8tMsgId(pushDto.getTo8toMsgId());
        if (pushDto.getScope().byteValue() == ScopeEnum.SINGLE.getValue()) {
            if (pushDto.getUidList() != null && !pushDto.getUidList().isEmpty()) {
                messageDTO.setUid(pushDto.getUidList().get(0));
                messageDTO.setDeviceToken(tokenList.get(0));
            }
        }  //do nothing

        messageDTO.setChannel(channelVO.getChannel());
        messageDTO.setPlatform(pushPlatform.byteValue());
        messageDTO.setTitle(pushDto.getNotification().getTitle());
        messageDTO.setContent(pushDto.getNotification().getAlert());
        messageDTO.setPayload(payload.toString().getBytes(Charsets.UTF_8));
        messageDTO.setScope(pushDto.getScope());

        int startTime = 0;
        if (pushDto.getStartTime() != null) {
            startTime = (int) (DateUtils.parse(pushDto.getStartTime()).getTime() / 1000);
            messageDTO.setStartTime(startTime);
        }
        int expireTime;
        if (pushDto.getExpireTime() != null) {
            expireTime = (int) (DateUtils.parse(pushDto.getStartTime()).getTime() / 1000);
            messageDTO.setExpireTime(expireTime);
        }

        try {
            if (startTime == 0) {
                //及时推送
                PushResult result = jpushClient.sendPush(payload);
                pushResultDTO.setChannelMsgId(String.valueOf(result.msg_id));
                log.info("极光推送结果:{}", result);
                messageDTO.setChannelMsgId(result.msg_id);
                if (result.statusCode != JIGUANG_RESPONSE_SUCCESS) {
                    messageDTO.setStatus(StatusEnum.SUCCESS.getValue());
                    messageDTO.setReason(result.toString());
                } else {
                    messageDTO.setStatus(StatusEnum.FAIL.getValue());
                    messageDTO.setReason(result.toString());
                    throw new RPCException(MyExceptionStatus.CHANNEL_BIZ_ERROR, result.error.getMessage());
                }
            } else {
                //定时推送
                ScheduleResult result = jpushClient.createSingleSchedule(pushDto.getTo8toMsgId(), pushDto.getStartTime(), payload);
                log.info("极光定时推送结果:{}:", result);
                if (result.isResultOK()) {
                    messageDTO.setScheduleId(result.getSchedule_id());
                    pushResultDTO.setChannelScheduleId(result.getSchedule_id());
                    messageDTO.setStatus(StatusEnum.SUCCESS.getValue());
                    messageDTO.setReason(result.toString());
                } else {
                    messageDTO.setStatus(StatusEnum.FAIL.getValue());
                    messageDTO.setReason(result.toString());
                    throw new RPCException(MyExceptionStatus.CHANNEL_BIZ_ERROR, result.toString());
                }
            }

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("极光消息推送连接异常", e);
            messageDTO.setStatus(StatusEnum.FAIL.getValue());
            messageDTO.setReason(e.toString());
            alarmService.sendAlarm(new AlarmDTO("消息推送", "极光消息推送连接异常"));
            throw new RPCException(MyExceptionStatus.CHANNEL_CONNECTION_ERROR, e.getLocalizedMessage());
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("极光消息推送API请求异常!status=" + e.getStatus() + ",code=" + e.getErrorCode() + ",message=" + e.getErrorMessage());
            messageDTO.setStatus(StatusEnum.FAIL.getValue());
            messageDTO.setReason(e.getStatus() + ":" + e.getErrorCode() + ":" + e.getErrorMessage());
            alarmService.sendAlarm(new AlarmDTO("消息推送", "极光消息推送请求异常"));
            throw new RPCException(MyExceptionStatus.CHANNEL_REQUEST_ERROR, e.getErrorMessage());
        } catch (Exception e) {
            log.error("极光消息推送未知异常", e);
            messageDTO.setStatus(StatusEnum.FAIL.getValue());
            messageDTO.setReason(e.toString());
            alarmService.sendAlarm(new AlarmDTO("消息推送", "极光消息推送未知异常"));
            throw new RPCException(MyExceptionStatus.CHANNEL_UNKNOWN_ERROR, e.getLocalizedMessage());
        } finally {
            pushMessageService.createMessage(messageDTO);
        }
        return pushResultDTO;
    }

    private boolean useProductionAccount(Integer pushAppId, Byte scope) {
        return (pushAppId == Constants.APP_ID_T8T && (isProduction || !scope.equals(ScopeEnum.ALL.getValue())));
    }

    private Options buildOptions() {
        Options options = Options.sendno();
        options.setApnsProduction(isApnsProduction);
        return options;
    }

    private Platform buildPlatform(Integer pushPlatform) {
        Platform platform;
        if (pushPlatform == PlatformEnum.ANDROID.getValue()) {
            platform = Platform.android();
        } else if (pushPlatform == PlatformEnum.IOS.getValue()) {
            platform = Platform.ios();
        } else {
            platform = Platform.android_ios();
        }
        return platform;
    }

    private Audience buildAudience(PushDTO pushDto, Integer pushAppId, Byte channel, List<String> tokenList) {
        Audience audience = null;
        if (pushDto.getScope().equals(ScopeEnum.SINGLE.getValue())) {
            List<Integer> uidList = pushDto.getUidList();
            List<String> firstIdList = pushDto.getFirstIdList();
            String token = null;
            int uid = 0;
            String firstId = null;
            if (uidList != null && !uidList.isEmpty()) {
                uid = uidList.get(0);
                token = channelDeviceTokenService.getTokenByUid(pushAppId, channel, uid);
            } else if (firstIdList != null && !firstIdList.isEmpty()) {
                firstId = firstIdList.get(0);
                token = channelDeviceTokenService.getTokenByFirstId(pushAppId, channel, firstId);
            } else {
                log.warn("uid和firstId都为空");
            }
            if (token == null) {
                String message = "用户不存在,uid=" + uid + ",firstId=" + firstId;
                throw new RPCException(MyExceptionStatus.USER_NOT_EXIST_IN_CHANNEL, message);
            }
            tokenList.add(token);
            audience = Audience.registrationId(tokenList);
        } else if (pushDto.getScope().equals(ScopeEnum.GROUP.getValue())) {
            List<String> targetTokenList = null;
            if (pushDto.getUidList() != null) {
                targetTokenList = channelDeviceTokenService.getTokenListByUidList(pushAppId, channel, pushDto.getUidList());
            } else if (pushDto.getFirstIdList() != null) {
                targetTokenList = channelDeviceTokenService.getTokenListByFirstIdList(pushAppId, channel, pushDto.getFirstIdList());
            } else {
                log.warn("群推用户ID列表和firstId列表都为空！！！");
            }
            if (targetTokenList == null || targetTokenList.isEmpty()) {
                String message = "用户不存在,uid=" + pushDto.getUidList() + ",firstId=" + pushDto.getFirstIdList();
                throw new RPCException(MyExceptionStatus.USER_NOT_EXIST_IN_CHANNEL, message);
            }
            audience = Audience.registrationId(targetTokenList);
        } else if (pushDto.getScope().equals(ScopeEnum.ALL.getValue())) {
            audience = Audience.all();
        }
        return audience;
    }

    private Notification buildNotification(NotificationDTO notification, String uriActivity) {
        Integer badge = notification.getBadge();
        JsonObject iosAlert = new JsonObject();
        iosAlert.addProperty("title", notification.getTitle());
        iosAlert.addProperty("body", notification.getAlert());
        return newBuilder()
                .addPlatformNotification(AndroidNotification.newBuilder()
                        .setAlert(notification.getAlert())
                        .setTitle(notification.getTitle())
                        //.setIntent(intent)
                        .setUriActivity(uriActivity)
                        .setUriAction(uriActivity)
                        .addExtras(notification.getExtras())
                        .build())
                .addPlatformNotification(IosNotification.newBuilder()
                        .setAlert(iosAlert)
                        .setBadge(badge != null ? badge : 0)
                        .addExtras(notification.getExtras())
                        .setMutableContent(true)
                        .build()
                )
                .build();
    }

    private JPushClient getPushClient(String appKey, String mastetSecrect) {
        JPushClient jpushClient = jPushClientMap.get(appKey);
        if (jpushClient == null) {
            jpushClient = new JPushClient(mastetSecrect, appKey, null, ClientConfig.getInstance());
            jPushClientMap.put(appKey, jpushClient);
        }
        return jpushClient;
    }
}
