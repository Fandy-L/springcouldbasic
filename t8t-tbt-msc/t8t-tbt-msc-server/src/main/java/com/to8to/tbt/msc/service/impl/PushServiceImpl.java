package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.enumeration.ScopeEnum;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.vo.AppVO;
import com.to8to.tbt.msc.manager.HttpClientManager;
import com.to8to.tbt.msc.service.PushAppService;
import com.to8to.tbt.msc.service.AsyncTaskService;
import com.to8to.tbt.msc.service.ChannelPushService;
import com.to8to.tbt.msc.service.PushService;
import com.to8to.tbt.msc.utils.ValidUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import static com.to8to.tbt.msc.common.Constants.*;

/**
 * @author pajero.quan
 */
@Slf4j
@Service
public class PushServiceImpl implements PushService {
    private static final int TASK_COUNT = 2;

    @Value("${push.old.url.send}")
    private String oldPushUrl;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    @Qualifier("jiguangPushService")
    private ChannelPushService jiguangPushService;

    @Autowired
    private HttpClientManager httpClientManager;

    @Autowired
    private PushAppService pushAppService;

    @Override
    public String send(RedirectAppMsgDTO redirectAppMsgDTO) {
        ValidUtils.valid(redirectAppMsgDTO);
        ObjectId objectId = new ObjectId();
        String messageId = objectId.toHexString();
        JSONObject result;

        AppVO appVO = pushAppService.getAppByPushAppId(redirectAppMsgDTO.getAppid().intValue());
        byte scope = redirectAppMsgDTO.getScope().byteValue();
        if (appVO != null && appVO.getEnable()) {
            if (scope == ScopeEnum.ALL.getValue().byteValue()) {
                //全量推送只支持极光通道
                Map<String, Object> to8to = redirectAppMsgDTO.getTo8to();
                if (to8to != null && to8to.containsKey("crm_all_push")) {
                    redirectAppMsgDTO.getMessage().setTitle((String) to8to.get("crm_all_push_title"));
                    redirectAppMsgDTO.setStartTime((String) to8to.get("crm_start_time"));
                    to8to.remove("crm_all_push_title");
                    to8to.remove("crm_start_time");
                }
                result = this.sendToNewServer(redirectAppMsgDTO, messageId);
                log.info("Send message to new server!messageId=" + messageId + ",message=" + redirectAppMsgDTO + ",result=" + result.toJSONString());
            } else if (scope == ScopeEnum.SINGLE.getValue().byteValue() || scope == ScopeEnum.GROUP.getValue().byteValue()) {
                // 接入新版本推送的APP单推，群推和全推需要友盟和极光双推送
                String firstId = redirectAppMsgDTO.getFirstids();
                //土巴兔APP全部走极光
                if (appVO.getPushAppId().equals(APP_ID_T8T)) {
                    result = this.sendToNewServer(redirectAppMsgDTO, messageId);
                    log.info("Send message to new server!messageId=" + messageId + ",message=" + redirectAppMsgDTO + ",result=" + result.toJSONString());
                } else if (!StringUtils.isEmpty(firstId)) {
                    // 仅新版本支持firstId
                    result = this.sendToNewServer(redirectAppMsgDTO, messageId);
                    log.info("Send message to new server!messageId=" + messageId + ",message=" + redirectAppMsgDTO + ",result=" + result.toJSONString());
                } else {
                    result = this.sendToAllServers(redirectAppMsgDTO, messageId);
                    log.info("Send message to all servers!messageId=" + messageId + ",message=" + redirectAppMsgDTO + ",result=" + result.toJSONString());
                }
            } else {
                result = new JSONObject();
                result.put("status", HTTP_SERVER_ERROR_STR);
                result.put("msg", "不支持的推送scope");
                log.info("不支持的推送scope={}", scope);
            }
        } else {
            result = this.sendToOldServer(redirectAppMsgDTO, messageId);
            log.info("Send message to old server!messageId=" + messageId + ",message=" + redirectAppMsgDTO + ",result=" + result.toJSONString());
        }

        return result.toJSONString();
    }

    private JSONObject sendToAllServers(RedirectAppMsgDTO redirectAppMsgDTO, String messageId) {
        JSONObject result = new JSONObject();
        result.put("messageId", messageId);
        final CyclicBarrier cb = new CyclicBarrier(TASK_COUNT + 1);

        final FutureTask<JSONObject> newPushTask = new FutureTask<>(() -> {
            try {
                return sendToNewServer(redirectAppMsgDTO, messageId);
            } finally {
                cb.await();
            }
        });

        final FutureTask<JSONObject> oldPushTask = new FutureTask<>(() -> {
            try {
                return sendToOldServer(redirectAppMsgDTO, messageId);
            } finally {
                cb.await();
            }
        });
        asyncTaskService.submit(newPushTask);
        asyncTaskService.submit(oldPushTask);

        StringBuilder errorMsgBuffer = new StringBuilder();
        errorMsgBuffer.append("极光:");
        try {
            // 打开栅栏
            cb.await();
            JSONObject newPushResult = null;
            try {
                newPushResult = newPushTask.get();
                if (HTTP_SERVER_ERROR_STR.equals(newPushResult.getString("status"))) {
                    errorMsgBuffer.append(newPushResult.getString("msg"));
                }
                errorMsgBuffer.append(";");
            } catch (ExecutionException e) {
                log.warn("newPushTask", e);
                if (e.getCause() != null) {
                    errorMsgBuffer.append(e.getCause().getLocalizedMessage());
                } else {
                    errorMsgBuffer.append(e.getLocalizedMessage());
                }
                errorMsgBuffer.append(";");
            } catch (Exception e) {
                log.warn("newPushTask", e);
                errorMsgBuffer.append(e.getLocalizedMessage());
                errorMsgBuffer.append(";");
            }
            JSONObject oldPushResult = null;
            errorMsgBuffer.append("友盟:");
            try {
                oldPushResult = oldPushTask.get();
                if (oldPushResult != null) {
                    if (oldPushResult.containsKey("status")) {
                        if (oldPushResult.getString("status").equals(HTTP_SERVER_ERROR_STR)) {
                            errorMsgBuffer.append(oldPushResult.getString("msg"));
                        }  // 正常返回

                    } else {
                        errorMsgBuffer.append("umeng return unknow error!");
                    }
                } else {
                    errorMsgBuffer.append("umeng return nothing!");
                }
            } catch (Exception e) {
                log.warn("oldPushTask", e);
                errorMsgBuffer.append(e.getLocalizedMessage());
            }

            /*
              友盟和极光都发送失败了，才算失败。
             */
            boolean newPushSuccess = newPushResult != null && HTTP_RESP_OK_STR.equals(newPushResult.getString("status"));
            boolean oldPushSuccess = oldPushResult != null && HTTP_RESP_OK_STR.equals(oldPushResult.getString("status"));

            if (!newPushSuccess && !oldPushSuccess) {
                result.put("status", HTTP_SERVER_ERROR_STR);
                result.put("msg", errorMsgBuffer.toString());
            } else {
                result.put("status", HTTP_RESP_OK_STR);
                result.put("msg", errorMsgBuffer.toString());
            }
        } catch (Exception e) {
            log.warn("sendToAllServers未知异常", e);
            throw new RPCException("push execution failed!");
        }
        return result;
    }

    private JSONObject sendToNewServer(RedirectAppMsgDTO redirectAppMsgDTO, String messageId) {
        JSONObject result = new JSONObject();
        result.put("messageId", messageId);

        PushDTO pushDTO = new PushDTO();
        pushDTO.setTo8toMsgId(messageId);
        pushDTO.setAppId(redirectAppMsgDTO.getAppid());
        pushDTO.setApnsProduction("true".equals(redirectAppMsgDTO.getProductionMode()));
        byte scope = redirectAppMsgDTO.getScope().byteValue();
        pushDTO.setScope(scope);
        if (scope == ScopeEnum.SINGLE.getValue().intValue()) {
            Integer uid = Integer.valueOf(redirectAppMsgDTO.getUid());
            String firstId = redirectAppMsgDTO.getFirstids();
            if (uid != null && uid > 0) {
                pushDTO.setUidList(Collections.singletonList(uid));
            } else if (!StringUtils.isEmpty(firstId)) {
                pushDTO.setFirstIdList(Collections.singletonList(firstId));
            } else {
                String errMsg = "单推必须指定uid或者firstId";
                log.warn(errMsg);
                result.put("status", HTTP_SERVER_ERROR_STR);
                result.put("msg", errMsg);
                return result;
            }
        } else if (scope == ScopeEnum.GROUP.getValue().intValue()) {
            String uidListStr = redirectAppMsgDTO.getUid();
            String firstIdListStr = redirectAppMsgDTO.getFirstids();
            if (!StringUtils.isEmpty(uidListStr)) {
                String[] uidStrArray = uidListStr.split(",");
                List<Integer> uidList = Arrays.stream(uidStrArray).map(Integer::valueOf).collect(Collectors.toList());
                pushDTO.setUidList(uidList);
            } else if (!StringUtils.isEmpty(firstIdListStr)) {
                String[] firstIdArray = firstIdListStr.split(",");
                pushDTO.setFirstIdList(Arrays.asList(firstIdArray));
            } else {
                String errMsg = "群推必须指定uid或者firstId列表";
                result.put("status", HTTP_SERVER_ERROR_STR);
                result.put("msg", errMsg);
                return result;
            }
        }
        Map<String, String> extras = new HashMap<>();

        Map<String, Object> to8toParams = redirectAppMsgDTO.getTo8to();
        if (to8toParams != null) {
            for (Map.Entry<String, Object> entry : to8toParams.entrySet()) {
                extras.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        Map<String, Object> to8toCustomParams = redirectAppMsgDTO.getTo8toCustom();
        if (to8toCustomParams != null) {
            for (Map.Entry<String, Object> entry : to8toCustomParams.entrySet()) {
                extras.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        MessageDTO message = new MessageDTO();
        message.setTitle(redirectAppMsgDTO.getMessage().getTitle());
        message.setContent(redirectAppMsgDTO.getMessage().getDescription());

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle(redirectAppMsgDTO.getMessage().getTitle());
        notification.setAlert(redirectAppMsgDTO.getMessage().getDescription());
        notification.setBadge(redirectAppMsgDTO.getMessage().getBadge());

        if (redirectAppMsgDTO.getMessage().getClick() != null) {
            Map<String, Object> customObj = redirectAppMsgDTO.getMessage().getClick().getCustom();
            if (customObj != null) {
                for (Map.Entry<String, Object> entry : customObj.entrySet()) {
                    extras.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            message.setExtras(extras);
            notification.setExtras(extras);
        }

        pushDTO.setStartTime(redirectAppMsgDTO.getStartTime());
        pushDTO.setExpireTime(redirectAppMsgDTO.getExpireTime());

        pushDTO.setMessage(message);
        pushDTO.setNotification(notification);

        try {
            PushResultDTO pushResultDTO = jiguangPushService.push(pushDTO);
            result.put("channelMsgId", pushResultDTO.getChannelMsgId());
            result.put("channelScheduleId", pushResultDTO.getChannelScheduleId());
            result.put("status", HTTP_RESP_OK_STR);
        } catch (RPCException e) {
            log.warn("极光push执行异常", e);
            result.put("status", HTTP_SERVER_ERROR_STR);
            result.put("msg", e.getStatus().getMessage());
        } catch (Exception e) {
            log.warn("极光push未知异常", e);
            result.put("status", HTTP_SERVER_ERROR_STR);
            result.put("msg", e.getLocalizedMessage());
        }
        return result;
    }

    private JSONObject sendToOldServer(RedirectAppMsgDTO redirectAppMsgDTO, String messageId) {
        JSONObject result;
        Map<String, String> params = new HashMap<>(11);
        params.put("uid", redirectAppMsgDTO.getUid());
        params.put("scope", redirectAppMsgDTO.getScope().toString());
        params.put("appid", redirectAppMsgDTO.getAppid().toString());
        params.put("type", redirectAppMsgDTO.getType().toString());
        params.put("message", JSON.toJSONString(redirectAppMsgDTO.getMessage()));
        if (redirectAppMsgDTO.getStartTime() != null) {
            params.put("start_time", redirectAppMsgDTO.getStartTime());
        }
        if (redirectAppMsgDTO.getExpireTime() != null) {
            params.put("expire_time", redirectAppMsgDTO.getExpireTime());
        }
        Map<String, Object> to8to = redirectAppMsgDTO.getTo8to();
        if (to8to != null) {
            params.put("to8to", JSON.toJSONString(to8to));
        }

        Map<String, Object> to8toCustom = redirectAppMsgDTO.getTo8toCustom();
        if (to8to != null) {
            params.put("to8to_custom", JSON.toJSONString(to8toCustom));
        }

        if (redirectAppMsgDTO.getSubUid() != null) {
            params.put("uid_sub", redirectAppMsgDTO.getSubUid());
        }

        if (redirectAppMsgDTO.getProductionMode() != null) {
            params.put("production_mode", redirectAppMsgDTO.getProductionMode());
        }

        String resp = httpClientManager.execute(oldPushUrl, params, String.class);
        log.info("umeng push result:" + resp);

        if (resp == null) {
            result = new JSONObject();
            result.put("messageId", messageId);
            result.put("status", HTTP_SERVER_ERROR_STR);
            result.put("msg", "old push server return nothing");
            return result;
        }
        result = JSON.parseObject(resp);
        //bugfix: 这里会返回空指针
        if (result == null) {
            result = new JSONObject();
            result.put("status", HTTP_SERVER_ERROR_STR);
            result.put("msg", "old push server return null");
        }
        result.put("messageId", messageId);
        return result;
    }
}
