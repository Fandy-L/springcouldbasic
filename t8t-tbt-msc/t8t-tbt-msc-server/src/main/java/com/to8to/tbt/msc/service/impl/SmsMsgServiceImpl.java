package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.common.ServiceException;
import com.google.common.collect.Lists;
import com.to8to.sc.compatible.RPCException;
import com.to8to.sc.component.redis.operation.AbstractDefaultOperation;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.configuration.OperateConfigConfiguration;
import com.to8to.tbt.msc.constant.MongateResponseConstant;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.AlarmDTO;
import com.to8to.tbt.msc.dto.SendMsgPhoneDTO;
import com.to8to.tbt.msc.dto.SendSmsBatchDTO;
import com.to8to.tbt.msc.entity.SendMessageWrapper;
import com.to8to.tbt.msc.entity.SmsWrapper;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.mysql.main.TimingMessage;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.MongateErrorCodeEnum;
import com.to8to.tbt.msc.enumeration.SmsOperatorEnum;
import com.to8to.tbt.msc.enumeration.TimingMessageStatusEnum;
import com.to8to.tbt.msc.repository.mysql.main.MessageTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.TimingMessageRepository;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.*;
import com.to8to.tbt.msc.vo.SendMsgPhoneVO;
import com.to8to.tbt.msc.vo.SendTencentSmsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.to8to.tbt.msc.common.MyExceptionStatus.*;


/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class SmsMsgServiceImpl implements SmsMsgService {

    /**
     * 手机号格式校验
     */
    private Pattern phoneValidPattern = Pattern.compile("[\\d]{11}");
    /**
     * 批量发送短信慢日志超时时间
     */
    private static final int sendSmSBatchSlowTime = 20;

    /**
     * 重试次数
     */
    private static final int SEND_RETRY_NUM = 1;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private TemplateV2Service templateV2Service;

    @Autowired
    private AppMsgService appMsgService;

    @Autowired
    private TimingMessageService timingMessageService;

    @Autowired
    private MongoNoteRecordService mongoNoteRecordService;

    @Autowired
    private MessageRecordService messageRecordService;

    @Autowired(required = false)
    public AbstractDefaultOperation<String> abstractDefaultOperation;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private TimingMessageRepository timingMessageRepository;

    @Autowired
    private AlarmService alarmService;

    @Override
    public StatusResultResponse<String> sendTemplateMsg(SendMessageWrapper.SendSmsMessage sendSmsMessage) {

        // 明文手机号的消息保存在mongo里面
        boolean saveToMongo = StringUtils.isNotBlank(sendSmsMessage.getPhone());

        // 校验失败直接返回失败信息
        String message = checkBeforeSend(sendSmsMessage);
        if (StringUtils.isNotEmpty(message)) {
            log.warn(LogUtils.buildTemplate("tid message"), sendSmsMessage.getTid(), message);
            return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_FAIL.getCode(), message);
        }

        // 延时短信
        if (IntegerUtils.isGtLimitValue(sendSmsMessage.getDelayTime())) {
            int tid = sendSmsMessage.getTid();
            int phoneid = sendSmsMessage.getPhoneId();
            String content = sendSmsMessage.getContent();
            int delayTime = sendSmsMessage.getDelayTime();
            boolean result = timingMessageService.addDelayMessage(tid, phoneid, content, delayTime);
            if (result) {
                return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS.getCode(), SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS.getMessage());
            } else {
                return ResponseUtils.buildStatusResultResponse(SEND_ALL_MESSAGE_DELAY_TIME_FAIL.getCode(), SEND_ALL_MESSAGE_DELAY_TIME_FAIL.getMessage());
            }
        }
        asyncTaskService.submit(() -> sendSmsAndSave(sendSmsMessage.getTid(), sendSmsMessage.getPhone(), String.valueOf(sendSmsMessage.getPhoneId()), sendSmsMessage.getContent(), sendSmsMessage.getChannelType(), saveToMongo));
        return ResponseUtils.sendMessageSuccessResponse();
    }

    @Override
    public ResResult<ResultStatusResponse<String>> sendSmsBatch(SendSmsBatchDTO sendSmsBatchDTO) {
        ResResult<ResultStatusResponse<String>> resResult;
        Template template = templateV2Service.getValidTemplate(sendSmsBatchDTO.getTid());
        MessageTemplate messageTemplate = templateV2Service.getValidSmsTemplate(sendSmsBatchDTO.getTid(), sendSmsBatchDTO.getKeyword());
        if (template == null || messageTemplate == null) {
            log.warn(LogUtils.buildTemplate("templateInvalid"), sendSmsBatchDTO);
            ResultStatusResponse<String> resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(SEND_ALL_MESSAGE_FAIL.getCode())
                    .status(SEND_ALL_MESSAGE_TID_INVALID.getMessage())
                    .build();
            resResult = ResUtils.fail(SEND_ALL_MESSAGE_TID_INVALID.getMessage());
            resResult.setData(resultStatusResponse);
            return resResult;
        }
        String phoneIds = sendSmsBatchDTO.getPhoneIds();
        asyncTaskService.submit(() ->
        {
            long startTime = TimeUtils.getCurrentTimestamp();
            List<String> phoneidList = Lists.newArrayList(Arrays.asList(phoneIds.trim().split(",")));
            int size = phoneidList.size();
            // 分批次发送，每次200
            for (int i = 0; i < size; i += OperateConfigConfiguration.SMS_BATCH_SIZE) {
                int j = i + OperateConfigConfiguration.SMS_BATCH_SIZE;
                List<String> tempList = phoneidList.subList(i, (j < size ? j : size));
                Map<String, String> phoneMap = getValidPhoneMap(StringUtils.join(tempList, ","));
                if (null == phoneMap || phoneMap.size() == 0) {
                    continue;
                } else {
                    for (String phoneId : phoneMap.keySet()) {
                        sendSmsAndSave(sendSmsBatchDTO.getTid(), phoneMap.get(phoneId), phoneId, messageTemplate.getMsgContent(), messageTemplate.getChannelType(), false);
                    }
                }
            }
            long costTime = TimeUtils.getCurrentTimestamp() - startTime;
            if (costTime > sendSmSBatchSlowTime) {
                log.warn(LogUtils.buildTemplate("群发短信耗时 共发送短信 平均每条发送耗时"), costTime, size, costTime / size);
            }
        });
        ResultStatusResponse<String> resultStatusResponse = ResultStatusResponse.<String>builder()
                .result(SEND_ALL_MESSAGE_SUCCESS.getCode())
                .status(SEND_ALL_MESSAGE_SUCCESS.getMessage())
                .build();
        return ResUtils.data(resultStatusResponse);
    }

    @Override
    public ResResult sendMsgPhone(SendMsgPhoneDTO obj) {
        log.info(LogUtils.buildTemplate("正在调用旧短信发送接口"), obj);
        if (IntegerUtils.isEqLimitValue(obj.getBlock())) {
            return sendMsgPhoneMain(obj);
        } else {
            asyncTaskService.submit(() -> sendMsgPhoneMain(obj));
            return ResUtils.suc("调用sendMsgPhone发送短信成功!");
        }
    }

    @Override
    public ResResult<SendMsgPhoneVO> sendMsgPhoneId(SendMsgPhoneDTO obj) {
        log.info("正在调用旧短信发送接口[sendMsgPhoneId]：{}", obj);
        String phoneIds = obj.getPhoneIds();
        String[] phoneIdarray = phoneIds.split(",");
        Set<String> phoneIdList = new HashSet<>();
        for (String phoneId : phoneIdarray) {
            phoneIdList.add(phoneId);
        }
        if (phoneIdList.size() == 0) {
            return ResUtils.fail(-1, "电话号码为空!");
        }
        String phones = StringUtils.join(encryptService.getPhoneList(phoneIdList), ",");
        obj.setPhones(phones);
        return sendMsgPhone(obj);
    }

    /**
     * 根据手机号发送短信
     *
     * @param obj
     * @return
     */
    private ResResult<SendMsgPhoneVO> sendMsgPhoneMain(SendMsgPhoneDTO obj) {
        String phones = obj.getPhones();
        String content = obj.getContent();
        int channel = IntegerUtils.intValueAsDefault(obj.getChannel());
        int ywId = IntegerUtils.intValueAsDefault(obj.getYwId());
        int zid = IntegerUtils.intValueAsDefault(obj.getZid());
        int cityid = IntegerUtils.intValueAsDefault(obj.getCityId());
        int ispass = IntegerUtils.intValueAsDefault(obj.getIsPass());
        int ywType = IntegerUtils.intValueAsDefault(obj.getYwType());
        Set<String> phonesList = validPhoneFormat(phones);
        if (null == phonesList || phonesList.isEmpty()) {
            throw new RPCException(MyExceptionStatus.SEND_SMS_PHONE_INVALID);
        }

        int finalChannel = CommonUtils.convertChannel(channel, ywId, ywType, zid, cityid, ispass);

        boolean flag = true;
        for (String phone : phonesList) {
            StatusResultResponse<String> statusResultResponse = sendSmsAndSave(0, phone, null, content, finalChannel, true);
            flag = statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode();
        }

        SendMsgPhoneVO sendMsgPhoneVO;
        if (flag) {
            sendMsgPhoneVO = SendMsgPhoneVO.builder()
                    .send(phonesList.size())
                    .unSend(0)
                    .status(SEND_ALL_MESSAGE_SUCCESS.getCode())
                    .msg(SEND_ALL_MESSAGE_SUCCESS.getMessage())
                    .build();
            return ResUtils.data(sendMsgPhoneVO);
        } else {
            sendMsgPhoneVO = SendMsgPhoneVO.builder()
                    .send(0)
                    .unSend(phonesList.size())
                    .status(SEND_ALL_MESSAGE_FAIL.getCode())
                    .msg(SEND_ALL_MESSAGE_FAIL.getMessage())
                    .build();
            ResResult<SendMsgPhoneVO> resResult = ResUtils.fail(SEND_ALL_MESSAGE_FAIL.getMessage());
            resResult.setData(sendMsgPhoneVO);
            return resResult;
        }
    }

    @Override
    public Set<String> validPhoneFormat(String phones) {
        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(phones)) {
            return set;
        }
        String[] phoneList = phones.split(",");
        for (String phone : phoneList) {
            if (StringUtils.isEmpty(phone)) {
                continue;
            } else if (phone.length() > 11) {
                continue;
            } else {
                try {
                    Long.parseLong(phone);
                    set.add(phone);
                } catch (Exception e) {
                    log.error("validPhoneFormat parseLong exception:{}", e);
                    continue;
                }
            }
        }
        return set;
    }

    @Override
    public StatusResultResponse<String> sendSmsAndSave(int tid, String phone, String phoneid, String content, int channelType, Boolean saveToMongo) {
        int retryTimes = 0;
        StatusResultResponse<String> statusResultResponse;
        // 是否真实发送
        if (MsgCenterConfiguration.SEND_PERMIT == 0) {
            statusResultResponse = ResponseUtils.sendMessageSuccessResponse();
        } else {
            //短信发送限制
            statusResultResponse = checkUpperLimitBySendSms(tid, phone, content);
            if (statusResultResponse != null) {
                log.info(LogUtils.buildTemplate("tid:{} phone:{} content:{} statusResultResponse:{}"), tid, phone, content, statusResultResponse);
                return statusResultResponse;
            }
            // 路由服务供应商
            String tailNumber = String.valueOf(phone.charAt(phone.length() - 1));
            String operator = determineOperator(Integer.parseInt(tailNumber), channelType);
            //营销通道要加 "回复TD"
            if (channelType == MsgCenterConfiguration.MWYX_TMY || channelType == MsgCenterConfiguration.MWYX_TO8TO || channelType == MsgCenterConfiguration.MWYX_DESIGNBOOK) {
                content = content + MsgCenterConfiguration.YXTAG;
            }
            //测试模式加运行环境标识
            if (MsgCenterConfiguration.OPEN_TEST == 1) {
                content = content + "(" + MsgCenterConfiguration.ENV + ")" + " via " + operator;
                phone = MsgCenterConfiguration.TEST_PHONE;
            }
            log.info(LogUtils.buildTemplate("phone tailNumber operator channelType openTest content"), phone, tailNumber, operator, channelType, MsgCenterConfiguration.OPEN_TEST, content);

            //发送短信
            while (retryTimes <= SEND_RETRY_NUM) {
                if (SmsOperatorEnum.TENCENT.equals(operator)) {
                    statusResultResponse = sendTencentSms(phone, content, channelType, retryTimes);
                    if (statusResultResponse == null) {
                        retryTimes++;
                        operator = SmsOperatorEnum.MONGATE.getCode();
                    } else {
                        break;
                    }
                } else if (SmsOperatorEnum.MONGATE.equals(operator)) {
                    statusResultResponse = sendMongateSms(phone, content, channelType, retryTimes);
                    if (statusResultResponse == null) {
                        retryTimes++;
                        operator = SmsOperatorEnum.TENCENT.getCode();
                    } else {
                        break;
                    }
                } else if (SmsOperatorEnum.ALIYUN.equals(operator)) {
                    statusResultResponse = sendAliyunSms(phone, content, channelType, retryTimes);
                    if (statusResultResponse == null) {
                        retryTimes++;
                        operator = SmsOperatorEnum.TENCENT.getCode();
                    } else {
                        break;
                    }
                }
            }
        }

        //保存数据
        if (saveToMongo) {
            int status = 2;
            String errorDescribe = "";
            if (statusResultResponse.getStatus() != MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode()) {
                status = 1;
                errorDescribe = statusResultResponse.getResult();
            }
            mongoNoteRecordService.create(phone, status, content, channelType, tid, errorDescribe);
        } else {
            int status = statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode() ? 1 : 0;
            int errorCode = statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode() ? 0 : statusResultResponse.getStatus();
            messageRecordService.create(tid, Integer.parseInt(phoneid), content, status, errorCode);
        }
        if (retryTimes >= SEND_RETRY_NUM || statusResultResponse == null) {
            statusResultResponse = ResponseUtils.sendMessageFailResponse();
        }
        log.info(LogUtils.buildTemplate("sendPermit tid phone phoneid content channelType saveToMongo response"), MsgCenterConfiguration.SEND_PERMIT, tid, phone, phoneid, content, channelType, saveToMongo, statusResultResponse);
        return statusResultResponse;
    }

    @Override
    public void sendDelayMessage() {
        List<TimingMessage> unSendList = Lists.newArrayList();
        try {
            unSendList = timingMessageRepository.findAllByIsSendAndDelayTimeIsBetween(TimingMessageStatusEnum.UNSENT.ordinal(),
                    TimeUtils.generateNatureWeekStartTime(),
                    TimeUtils.getCurrentTimestamp());
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate(), e);
        }
        if (unSendList.isEmpty()) {
            return;
        }
        for (TimingMessage timingMessage : unSendList) {
            SendMessageWrapper.SendSmsMessage sendSmsMessage = SendMessageWrapper.SendSmsMessage.builder()
                    .tid(timingMessage.getTid())
                    .phoneId(timingMessage.getPhoneId())
                    .content(timingMessage.getMsgContent())
                    .build();
            sendTemplateMsg(sendSmsMessage);
            timingMessage.setIsSend(TimingMessageStatusEnum.HAS_SENT.ordinal());
            timingMessage.setSendTime(TimeUtils.getCurrentTimestamp());
            try {
                timingMessageRepository.save(timingMessage);
            } catch (Exception e) {
                log.warn(LogUtils.buildExceptionTemplate("timingMessage"), timingMessage, e);
            }
        }
        log.info(LogUtils.buildTemplate("size unSendList"), unSendList.size(), unSendList);
    }

    /**
     * 根据批量PhoneId串验证手机号
     *
     * @param phoneIds
     * @return
     */
    private Map<String, String> getValidPhoneMap(String phoneIds) {
        if (StringUtils.isEmpty(phoneIds)) {
            return null;
        }
        Set<String> phoneidSet = Arrays.stream(phoneIds.split(",")).collect(Collectors.toSet());
        Map<String, String> phoneMap = encryptService.getPhoneMap(phoneidSet);
        Iterator<String> it = phoneMap.keySet().iterator();
        while (it.hasNext()) {
            String phoneId = it.next();
            String phone = phoneMap.getOrDefault(phoneId, "");
            Matcher m = phoneValidPattern.matcher(phone);
            if (m.matches()) {
                continue;
            } else {
                log.warn(LogUtils.buildTemplate("phoneId phone"), phoneId, phone);
                it.remove();
            }
        }
        return phoneMap;
    }

    /**
     * 短信发送前检测
     *
     * @param sendSmsMessage
     * @return
     */
    private String checkBeforeSend(SendMessageWrapper.SendSmsMessage sendSmsMessage) {
        // 电话号码检测
        if (StringUtils.isBlank(sendSmsMessage.getPhone()) && IntegerUtils.isGtLimitValue(sendSmsMessage.getPhoneId())) {
            String phone = encryptService.getPlainText(String.valueOf(sendSmsMessage.getPhoneId()));
            sendSmsMessage.setPhone(phone);
        }
        if (StringUtils.isBlank(sendSmsMessage.getPhone())) {
            return "校验失败:电话号码为空! ";
        }
        Matcher matcher = phoneValidPattern.matcher(sendSmsMessage.getPhone());
        if (!matcher.matches()) {
            return "校验失败:电话号码不合法! ";
        }
        // 短信模板检测
        int tid = sendSmsMessage.getTid();
        MessageTemplate messageTemplate = messageTemplateRepository.findByTid(sendSmsMessage.getTid()).orElse(null);
        if (null == messageTemplate) {
            return "校验失败:短信模板为空! ";
        }
        if (messageTemplate.getChannelType() <= 0) {
            return "校验失败:不合法的短信通道! ";
        }
        sendSmsMessage.setChannelType(messageTemplate.getChannelType());
        if (StringUtils.isBlank(messageTemplate.getMsgContent())) {
            return "校验失败:不合法的短信内容！ ";
        }
        sendSmsMessage.setContent(messageTemplate.getMsgContent());

        //关键词替换
        String keywordStartDelimiter = "{";
        String keywordEndDelimiter = "}";
        if (messageTemplate.getMsgContent().contains(keywordStartDelimiter) || messageTemplate.getMsgContent().contains(keywordEndDelimiter)) {
            JSONObject keywords = sendSmsMessage.getKeyword();
            if (null != keywords) {
                try {
                    String content = templateV2Service.replaceKeyword(messageTemplate.getMsgContent(), keywords);
                    sendSmsMessage.setContent(content);
                } catch (NullPointerException exception) {
                    return "替换短信内容关键字失败！";
                } catch (Exception e) {
                    return "替换短信内容关键字失败！";
                }
            }
        }

        //IP限制
        if (IntegerUtils.isEqLimitValue(messageTemplate.getNeedIpLimit(), 1)) {
            String ip = sendSmsMessage.getIp();
            if (StringUtils.isNotBlank(ip)) {
                int count = (int) sendLimit(ip + "tid-" + tid);
                if (count > messageTemplate.getIpLimitNum()) {
                    return "ip 发送次数超限制已被拦截！";
                }
            }
        }
        return null;
    }

    /**
     * 发送限流设置
     *
     * @param key
     * @return
     */
    public long sendLimit(String key) {
        long counter = 0;
        try {
            String md5key = DigestUtils.md5Hex(key);
            int expireTime = TimeUtils.getDayTimePoint(1) - TimeUtils.getCurrentTimestamp();
            counter = abstractDefaultOperation.increment(md5key, 1);
            if (counter > 0) {
                abstractDefaultOperation.expire(md5key, expireTime, TimeUnit.SECONDS);
            }
            log.debug("sendLimit key:{} counter:{}", key, counter);
        } catch (Exception e) {
            log.warn("sendLimit exception e:{}", e);
            e.printStackTrace();
        }
        return counter;
    }

    /**
     * 发送短信-腾讯通道
     *
     * @param phone
     * @param content
     * @param channelType
     * @param retryTimes
     * @return
     */
    private StatusResultResponse<String> sendTencentSms(String phone, String content, int channelType, int retryTimes) {
        SendTencentSmsVO sendTencentSmsVO;
        SmsWrapper.TencentConfig tencentConfig = getTencentSmsConfig(channelType);
        sendTencentSmsVO = SmsUtils.sendTencentSms(phone, tencentConfig.getPrefix() + content, String.valueOf(tencentConfig.getBizType()));
        if (sendTencentSmsVO == null) {
            if (retryTimes == SEND_RETRY_NUM) {
                AlarmDTO alarmDTO = new AlarmDTO();
                alarmDTO.setModule("短信发送[腾讯]");
                alarmDTO.setMessage(String.format("腾讯短信接口进行第%s次重试异常，短信未能成功发送，电话号码%s,内容%s！", retryTimes, phone, tencentConfig.getPrefix() + content));
                alarmService.sendAlarm(alarmDTO);
                log.warn(LogUtils.buildTemplate("sendTencentSmsFail phone content channelType retryTimes"), phone, content, channelType, retryTimes);
            }
            return null;
        } else {
            String failCode = "0";
            if (failCode.equals(sendTencentSmsVO.getResult())) {
                return StatusResultResponse.<String>builder()
                        .status(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode())
                        .result(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getMessage())
                        .build();
            } else {
                log.warn(LogUtils.buildTemplate("sendTencentSmsError phone content channelType retryTimes sendTencentSmsVO"), phone, content, channelType, retryTimes, sendTencentSmsVO);
                return StatusResultResponse.<String>builder()
                        .status(Integer.parseInt(sendTencentSmsVO.getResult()))
                        .result(sendTencentSmsVO.getErrmsg())
                        .build();
            }
        }
    }

    /**
     * 发送短信-梦网通道
     *
     * @param phone
     * @param content
     * @param channelType
     * @param retryTimes
     * @return
     */
    private StatusResultResponse<String> sendMongateSms(String phone, String content, int channelType, int retryTimes) {
        String response = null;
        SmsWrapper.MongateAccount mongateAccount = getMongateSmsAccount(channelType);
        try {
            log.debug(LogUtils.buildTemplate("sendMongateSms:channelType account"), channelType, mongateAccount);
            response = SmsUtils.sendMongateSms(phone, content, mongateAccount.getUserId(), mongateAccount.getPassword());
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("sendMongateSmsException:phone content channelType retryTimes"), phone, content, channelType, retryTimes, e);
        }
        if (StringUtils.isEmpty(response)) {
            if (retryTimes == SEND_RETRY_NUM) {
                AlarmDTO alarmDTO = new AlarmDTO();
                alarmDTO.setModule("短信发送[ 梦网]");
                alarmDTO.setMessage(String.format("梦网短信接口进行第%s次重试异常，短信未能成功发送，电话号码%s,内容%s！", retryTimes, phone, content));
                alarmService.sendAlarm(alarmDTO);
                log.warn(LogUtils.buildTemplate("sendMongateSmsFail:phone content channelType retryTimes"), phone, content, channelType, retryTimes);
            }
        } else {
            if (MongateErrorCodeEnum.BALANCE_INSUFFICIENT.equals(response)) {
                AlarmDTO alarmDTO = new AlarmDTO();
                alarmDTO.setModule("短信发送[ 梦网]");
                alarmDTO.setMessage(String.format("梦网短信费用不足!channel:%s", channelType));
                alarmService.sendAlarm(alarmDTO);
                log.warn(LogUtils.buildTemplate("sendMongateSmsBalanceInsufficient:phone content channelType retryTimes"), phone, content, channelType, retryTimes);
            } else if (MongateResponseConstant.MONGATERESPONSE.keySet().contains(response)) {
                log.warn(LogUtils.buildTemplate("sendMongateSmsError:phone content channelType retryTimes errorCode errorMsg"), phone, content, channelType, retryTimes, response, MongateResponseConstant.MONGATERESPONSE.getString(response));
                return StatusResultResponse.<String>builder()
                        .status(Integer.parseInt(response))
                        .result(MongateResponseConstant.MONGATERESPONSE.getString(response))
                        .build();
            } else {
                return StatusResultResponse.<String>builder()
                        .status(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode())
                        .result(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getMessage())
                        .build();
            }
        }
        return null;
    }

    /**
     * 发送短信-阿里云通道
     *
     * @param phone
     * @param content
     * @param channelType
     * @param retryTimes
     * @return
     */
    private StatusResultResponse<String> sendAliyunSms(String phone, String content, int channelType, int retryTimes) {
        String response = null;
        SmsWrapper.AliyunConfig aliyunConfig = getAliyunSmsConfig(channelType);
        try {
            log.debug(LogUtils.buildTemplate("sendAliyunSms:channelType config"), channelType, aliyunConfig);
            response = SmsUtils.sendAliyunSms(Lists.newArrayList(phone), content, aliyunConfig.getSignName(), aliyunConfig.getTemplateCode());
        } catch (ServiceException se) {
            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setModule("短信发送[ 阿里云]");
            alarmDTO.setMessage(String.format("阿里云短信发送异常！ errorCode:%s,requestId:%s,message:%s", se.getErrorCode(), se.getRequestId(), se.getMessage()));
            alarmService.sendAlarm(alarmDTO);
            log.warn(LogUtils.buildExceptionTemplate("sendAliyunSmsServiceException:phone content channelType"), phone, content, channelType, se);
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("sendAliyunSmsException:phone content channelType"), phone, content, channelType, e);
        }
        if (StringUtils.isNotBlank(response)) {
            return StatusResultResponse.<String>builder()
                    .status(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode())
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getMessage())
                    .build();
        } else {
            log.warn(LogUtils.buildTemplate("sendAliyunSmsError:phone content channelType retryTimes response"), phone, content, channelType, retryTimes, response);
        }
        return null;
    }

    /**
     * 发送短信时-检查是否超出发送限制
     *
     * @param tid
     * @param phone
     * @param content
     * @return
     */
    private StatusResultResponse<String> checkUpperLimitBySendSms(int tid, String phone, String content) {
        log.debug("checkUpperLimitBySendSms PHONE_VALIDATION_REGEX:{} CON_REPEAT_LIMIT:{} ALL_REPEAT_LIMIT:{} SMS_PHONE_TID_REPEAT_SWITCH:{} SMS_WHOLE_TID_REPEAT_SWITCH:{}",
                MsgConstant.PHONE_VALIDATION_REGEX,
                OperateConfigConfiguration.CON_REPEAT_LIMIT,
                OperateConfigConfiguration.ALL_REPEAT_LIMIT,
                OperateConfigConfiguration.SMS_PHONE_TID_REPEAT_SWITCH,
                OperateConfigConfiguration.SMS_WHOLE_TID_REPEAT_SWITCH);
        StatusResultResponse<String> statusResultResponse = StatusResultResponse.<String>builder()
                .status(MyExceptionStatus.OLD_MSG_RESPONSE_FAIL.getCode())
                .build();
        // 电话号码格式校验
        Matcher m = MsgConstant.PHONE_VALIDATION_REGEX.matcher(phone);
        if (!m.matches()) {
            statusResultResponse.setResult(MyExceptionStatus.SEND_MSG_ALL_PHONE_FORMAT_ERROR.getMessage());
            return statusResultResponse;
        }
        // 发送次数限制
        long phoneCounter = sendLimit(phone);
        long contentCounter = sendLimit(phone + content);
        if (contentCounter > OperateConfigConfiguration.CON_REPEAT_LIMIT || phoneCounter > OperateConfigConfiguration.ALL_REPEAT_LIMIT) {
            log.info("checkUpperLimitBySendSms 号码[{}]今日已达发送上限[{}],本次发送是第[{}]次,此条短信：[{}]是今日第[{}]次发送给该号码,超过限定次数[{}]已暂停发送！", EncryptUtils.simpleEncode(phone), OperateConfigConfiguration.ALL_REPEAT_LIMIT, phoneCounter, content, contentCounter, OperateConfigConfiguration.CON_REPEAT_LIMIT);
            statusResultResponse.setResult(MyExceptionStatus.SEND_MSG_ALL_MAX_LIMIT.getMessage());
            return statusResultResponse;
        }
        // 模板id + phone 上限控制
        if (OperateConfigConfiguration.SMS_PHONE_TID_REPEAT_SWITCH && isSmsTidPhoneLimit(tid, phone, content)) {
            statusResultResponse.setResult(MyExceptionStatus.SEND_MSG_ALL_MAX_LIMIT.getMessage());
            return statusResultResponse;
        }
        // 模板 id 总量上限控制
        if (OperateConfigConfiguration.SMS_WHOLE_TID_REPEAT_SWITCH && isSmsTidWholeLimit(tid, phone, content)) {
            statusResultResponse.setResult(MyExceptionStatus.SEND_MSG_ALL_MAX_LIMIT.getMessage());
            return statusResultResponse;
        }
        return null;
    }

    /**
     * 模板id + phone 上限控制
     *
     * @param tid     模板ID
     * @param phone   手机号
     * @param content 发送内容
     * @return 达到上限 true ,否则 false.
     */
    private boolean isSmsTidPhoneLimit(int tid, String phone, String content) {
        long tidPhoneCounter = sendLimit("tid_" + phone + "_" + tid);
        boolean phoneTidCustomsFlag = OperateConfigConfiguration.smsPhoneTidCustoms.get(tid) != null && OperateConfigConfiguration.smsPhoneTidCustoms.get(tid) > 0;
        int tidPhoneThreshold;
        if (OperateConfigConfiguration.SMS_PHONE_TID_STRATEGY == 0) {
            if (phoneTidCustomsFlag) {
                tidPhoneThreshold = OperateConfigConfiguration.smsPhoneTidCustoms.get(tid);
                if (tidPhoneCounter > tidPhoneThreshold) {
                    log.info("isSmsTidPhoneLimit 今日已达'模板[{}] + 手机号[{}]'发送上限次数[{}],本次发送是第[{}]次，模板限制策略'不限量'[{}],超过限定次数该模板已暂停使用！内容：[{}]"
                            , tid, EncryptUtils.simpleEncode(phone), tidPhoneThreshold, tidPhoneCounter, OperateConfigConfiguration.SMS_PHONE_TID_STRATEGY, content);
                    return true;
                }
            }
        } else {
            tidPhoneThreshold = phoneTidCustomsFlag ? OperateConfigConfiguration.smsPhoneTidCustoms.get(tid) : OperateConfigConfiguration.SMS_PHONE_TID_REPEAT_LIMIT;
            if (tidPhoneCounter > tidPhoneThreshold) {
                log.info("isSmsTidPhoneLimit 今日已达'模板[{}] + 手机号[{}]'发送上限次数[{}],本次发送是第[{}]次，模板限制策略'默认值限量'[{}],超过限定次数该模板已暂停使用！内容：[{}]"
                        , tid, EncryptUtils.simpleEncode(phone), tidPhoneThreshold, tidPhoneCounter, OperateConfigConfiguration.SMS_PHONE_TID_STRATEGY, content);
                return true;
            }
        }
        return false;
    }

    /**
     * 模板 id 总量上限控制
     *
     * @param tid     模板ID
     * @param phone   手机号
     * @param content 发送内容
     * @return 达到上限 true ,否则 false.
     */
    private boolean isSmsTidWholeLimit(int tid, String phone, String content) {
        long tidWholeCounter = sendLimit("tid_" + tid);
        boolean wholeTidCustomsFlag = OperateConfigConfiguration.smsWholeTidCustoms.get(tid) != null && OperateConfigConfiguration.smsWholeTidCustoms.get(tid) > 0;
        int tidWholeThreshold;
        if (OperateConfigConfiguration.SMS_WHOLE_TID_STRATEGY == 0) {
            if (wholeTidCustomsFlag) {
                tidWholeThreshold = OperateConfigConfiguration.smsWholeTidCustoms.get(tid);
                if (tidWholeCounter > tidWholeThreshold) {
                    log.info("isSmsTidWholeLimit 今日已达模板[{}]发送总量上限[{}],本次发送是第[{}]次，模板限制策略'不限量'[{}],超过限定次数该模板已暂停使用！手机号[{}],内容：[{}]"
                            , tid, tidWholeThreshold, tidWholeCounter, OperateConfigConfiguration.SMS_WHOLE_TID_STRATEGY, EncryptUtils.simpleEncode(phone), content);
                    return true;
                }
            }
        } else {
            tidWholeThreshold = wholeTidCustomsFlag ? OperateConfigConfiguration.smsWholeTidCustoms.get(tid) : OperateConfigConfiguration.SMS_WHOLE_TID_REPEAT_LIMIT;
            if (tidWholeCounter > tidWholeThreshold) {
                log.info("isSmsTidWholeLimit 今日已达模板[{}]发送总量上限[{}],本次发送是第[{}]次，模板限制'默认值限量'策略[{}],超过限定次数该模板已暂停使用！手机号[{}],内容：[{}]"
                        , tid, tidWholeThreshold, tidWholeCounter, OperateConfigConfiguration.SMS_WHOLE_TID_STRATEGY, EncryptUtils.simpleEncode(phone), content);

                return true;
            }
        }
        return false;
    }

    /**
     * 路由短信通道商
     *
     * @param tailNumber
     * @param channelType
     * @return
     */
    private String determineOperator(int tailNumber, int channelType) {
        log.debug("determineOperator tailNumber:{} operator:{} default:{}", tailNumber, OperateConfigConfiguration.SMS_ROUTE.getJSONObject("operator"), OperateConfigConfiguration.SMS_ROUTE.getString("default"));
        JSONObject operator = OperateConfigConfiguration.SMS_ROUTE.getJSONObject("operator");
        try {
            Iterator it = operator.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String operatorName = (String) entry.getKey();
                JSONObject channelGroup = (JSONObject) entry.getValue();
                JSONArray tailArray = channelGroup.getJSONArray(String.valueOf(channelType));
                if (null != tailArray) {
                    if (tailArray.contains(tailNumber)) {
                        return operatorName;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            log.warn("determineOperator exception smsRoute:{}", OperateConfigConfiguration.SMS_ROUTE);
        }
        return OperateConfigConfiguration.SMS_ROUTE.getString("default");
    }

    /**
     * 腾讯发送通道-根据发送渠道获取短信前缀和业务类型
     *
     * @param channelType
     * @return
     */
    private SmsWrapper.TencentConfig getTencentSmsConfig(int channelType) {
        switch (channelType) {
            case 1:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【图满意】")
                        .bizType(1)
                        .build();
            case 2:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【土巴兔】")
                        .bizType(1)
                        .build();
            case 3:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【图满意】")
                        .bizType(0)
                        .build();
            case 4:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【土巴兔】")
                        .bizType(0)
                        .build();
            case 5:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【设计本】")
                        .bizType(1)
                        .build();
            case 6:
                return SmsWrapper.TencentConfig.builder()
                        .prefix("【设计本】")
                        .bizType(0)
                        .build();
            default:
                return null;
        }
    }

    /**
     * 梦网发送通道-根据发送渠道获取用户名及密码
     *
     * @param channelType
     * @return
     */
    private SmsWrapper.MongateAccount getMongateSmsAccount(int channelType) {
        switch (channelType) {
            case MsgCenterConfiguration.MWYX_TMY:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_TMY_YX_USERID)
                        .password(MsgCenterConfiguration.EMP_TMY_YX_PASSWORD)
                        .build();
            case MsgCenterConfiguration.MWYX_TO8TO:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_TO8TO_YX_USERID)
                        .password(MsgCenterConfiguration.EMP_TO8TO_YX_PASSWORD)
                        .build();
            case MsgCenterConfiguration.MWSC_TMY:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_TMY_SC_USERID)
                        .password(MsgCenterConfiguration.EMP_TMY_SC_PASSWORD)
                        .build();
            case MsgCenterConfiguration.MWSC_TO8TO:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_TO8TO_SC_USERID)
                        .password(MsgCenterConfiguration.EMP_TO8TO_SC_PASSWORD)
                        .build();
            case MsgCenterConfiguration.MWYX_DESIGNBOOK:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_DESIGNBOOK_YX_USERID)
                        .password(MsgCenterConfiguration.EMP_DESIGNBOOK_YX_PASSWORD)
                        .build();
            case MsgCenterConfiguration.MWSC_DESIGNBOOK:
                return SmsWrapper.MongateAccount.builder()
                        .userId(MsgCenterConfiguration.EMP_DESIGNBOOK_SC_USERID)
                        .password(MsgCenterConfiguration.EMP_DESIGNBOOK_SC_PASSWORD)
                        .build();
            default:
                return null;
        }
    }

    /**
     * 阿里云发送通道－根据发送渠道获取阿里云短信签名及模板
     *
     * @param channelType
     * @return
     */
    private SmsWrapper.AliyunConfig getAliyunSmsConfig(int channelType) {
        switch (channelType) {
            case 1:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("图满意")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_MARKETING)
                        .build();
            case 2:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("土巴兔")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_MARKETING)
                        .build();
            case 3:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("图满意")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_INFORM)
                        .build();
            case 4:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("土巴兔")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_INFORM)
                        .build();
            case 5:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("设计本")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_MARKETING)
                        .build();
            case 6:
                return SmsWrapper.AliyunConfig.builder()
                        .signName("设计本")
                        .templateCode(MsgCenterConfiguration.TEMPLATECODE_INFORM)
                        .build();
            default:
                return null;
        }
    }
}
