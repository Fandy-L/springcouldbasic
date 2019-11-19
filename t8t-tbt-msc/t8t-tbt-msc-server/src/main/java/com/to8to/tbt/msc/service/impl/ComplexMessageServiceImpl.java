package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.constant.MsgSendConstant;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.*;
import com.to8to.tbt.msc.entity.mongo.*;
import com.to8to.tbt.msc.entity.mongo.MongoRecordTarget;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.MsgSendAutoEnum;
import com.to8to.tbt.msc.enumeration.MsgSendTypeEnum;
import com.to8to.tbt.msc.enumeration.SmsChannelEnum;
import com.to8to.tbt.msc.enumeration.UserTypeEnum;
import com.to8to.tbt.msc.repository.mongo.*;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.*;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.MsgRecordVO;
import com.to8to.tbt.msc.vo.SendMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class ComplexMessageServiceImpl implements ComplexMessageService {

    @Autowired
    private NoteRecordRepository noteRecordRepository;

    @Autowired
    private MongoAppRecordRepository mongoAppRecordRepository;

    @Autowired
    private MailRecordRepository mailRecordRepository;

    @Autowired
    private WeixinRecordRepository weixinRecordRepository;

    @Autowired
    private MongoPcRecordRepository mongoPcRecordRepository;

    @Autowired
    private TemplateV2Service templateV2Service;

    @Autowired
    private SmsMsgService smsMsgService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private AppMsgSendService appMsgSendService;

    @Override
    public ListMsgVO<MsgRecordVO> selectMsgByPhone(ListMsgDTO listMsgDTO) {
        ListMsgVO<MsgRecordVO> recordVOListMsgVO = new ListMsgVO<>();
        List<MsgRecordVO> msgRecordVOS = new ArrayList<>();
        recordVOListMsgVO.setMsgRecords(msgRecordVOS);
        recordVOListMsgVO.setTotalRecords(0L);
        recordVOListMsgVO.setTotalPages(0L);
        if (listMsgDTO.getContactList() != null && listMsgDTO.getContactList().size() > 0) {
            if (IntegerUtils.isEqLimitValue(listMsgDTO.getSendType(), MsgSendTypeEnum.SMS.getCode())) {
                List<MongoNoteRecord> mongoNoteRecordList = noteRecordRepository.findAllByTargetContactIn(listMsgDTO.getContactList());
                for (MongoNoteRecord mongoNoteRecord : mongoNoteRecordList) {
                    MsgRecordVO msgRecordVO = DozerUtils.map(mongoNoteRecord, MsgRecordVO.class);
                    RecordTarget recordTarget = DozerUtils.map(mongoNoteRecord.getTarget(), RecordTarget.class);
                    msgRecordVO.setTarget(recordTarget);
                    msgRecordVO.setId(mongoNoteRecord.getId());
                    msgRecordVO.setAttachedId(mongoNoteRecord.getId());
                    msgRecordVOS.add(msgRecordVO);
                }
            }
        }
        return recordVOListMsgVO;
    }

    @Override
    public ResResult sendAllMessage(SendAllMessageDTO sendAllMessageDTO) {
        ResResult<ResultStatusResponse<String>> resResult;
        String tidStr = sendAllMessageDTO.getTid();
        List<Integer> tidList = Arrays.stream(tidStr.split(",")).map(Integer::new).collect(Collectors.toList());
        List<Template> validTemplateList = Lists.newArrayList();
        for (Integer tid : tidList) {
            Template template = templateV2Service.getValidTemplate(tid);
            if (null != template) {
                validTemplateList.add(template);
            }
        }
        if (validTemplateList.isEmpty()) {
            ResultStatusResponse<String> resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_TID_INVALID.getCode())
                    .status(MyExceptionStatus.SEND_ALL_MESSAGE_TID_INVALID.getMessage())
                    .build();
            resResult = ResUtils.fail(resultStatusResponse.getStatus());
            resResult.setData(resultStatusResponse);
            return resResult;
        }

        StringBuilder sb = new StringBuilder("消息发送情况：");
        int succ = 0;
        for (Template template : validTemplateList) {
            StatusResultResponse<String> statusResultResponse;
            if (MsgSendTypeEnum.SMS.equals(template.getSendType())) {
                statusResultResponse = sendSmsMessage(sendAllMessageDTO, template.getTid(), template.getIsAuto(), template.getSendType());
                if (statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode()) {
                    succ++;
                } else {
                    sb.append("短信：" + statusResultResponse.getResult());
                }
            } else if (MsgSendTypeEnum.APP.equals(template.getSendType())) {
                statusResultResponse = sendAppMessage(sendAllMessageDTO, template.getTid(), template.getIsAuto(), template.getSendType());
                if (statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode()) {
                    succ++;
                } else {
                    sb.append("App消息：" + statusResultResponse.getResult());
                }
            }
        }
        if (succ == validTemplateList.size()) {
            ResultStatusResponse resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode())
                    .status(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getMessage())
                    .build();
            resResult = ResUtils.data(resultStatusResponse);
        } else {
            ResultStatusResponse<String> resultStatusResponse = ResultStatusResponse.<String>builder()
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_FAIL.getCode())
                    .status(sb.toString())
                    .build();
            resResult = ResUtils.fail(resultStatusResponse.getStatus());
            resResult.setData(resultStatusResponse);
        }
        return resResult;
    }

    @Override
    public SendMsgVO sendMsg(SendMsgDTO sendMsgDTO) {
        if (IntegerUtils.isEqLimitValue(sendMsgDTO.getChannel())) {
            sendMsgDTO.setChannel(SmsChannelEnum.PRODUCT.getCode());
        }

        int succ = 0;
        int fail = 0;

        Future<Boolean> result = null;
        for (RecordTarget t : sendMsgDTO.getTargets()) {
            boolean flag = false;
            // 发送短信
            if (IntegerUtils.isEqLimitValue(t.getSendType(), MsgSendTypeEnum.SMS.getCode())) {
                SendMsgPhoneDTO sendMsgPhoneDTO = DozerUtils.map(sendMsgDTO, SendMsgPhoneDTO.class);
                sendMsgPhoneDTO.setPhones(t.getContact());
                sendMsgPhoneDTO.setUserType(t.getUserType());
                smsMsgService.sendMsgPhone(sendMsgPhoneDTO);
                succ++;
                continue;
            }
            // 发送邮件
            if (IntegerUtils.isEqLimitValue(t.getSendType(), MsgSendTypeEnum.EMAIL.getCode())) {
                log.warn("正在调用旧的邮件推送接口! req:{}", sendMsgDTO);
                result = asyncTaskService.submit(() -> sendMail(t, sendMsgDTO.getTitle(), sendMsgDTO.getContent(), sendMsgDTO.getPersonal()));
                flag = true;
            }
            // 发送微信
            if (IntegerUtils.isEqLimitValue(t.getSendType(), MsgSendTypeEnum.WECHAT.getCode())) {
                log.warn("正在调用旧的wechat推送接口! req:{}", sendMsgDTO);
                result = asyncTaskService.submit(() -> sendWeixin(t, sendMsgDTO.getContent()));
                flag = true;

            }
            // 发送app消息
            if (IntegerUtils.isEqLimitValue(t.getSendType(), MsgSendTypeEnum.APP.getCode())) {
                log.warn("正在调用旧的app推送接口! req:{}", sendMsgDTO);
                result = null;
                flag = true;
            }
            //发送pc消息
            if (IntegerUtils.isEqLimitValue(t.getSendType(), MsgSendTypeEnum.PC.getCode())) {
                log.warn("正在调用旧的PC消息接口! req:{}", sendMsgDTO);
                result = null;
                flag = true;
            }
            if (flag) {
                succ++;
            } else {
                fail++;
            }
            // 短信记录统一在sendSMSAndSave()接口保存
            Future<Boolean> finalResult = result;
            asyncTaskService.submit(() -> saveMsgRecord(sendMsgDTO, t, finalResult));
        }
        return SendMsgVO.builder()
                .code(200)
                .content("发送成功:" + succ + ";发送失败:" + fail)
                .failNum(fail)
                .succNum(succ)
                .build();
    }

    /**
     * 发送短信
     *
     * @param sendAllMessageDTO
     * @param tid
     * @param isAuto
     * @return
     */
    private StatusResultResponse<String> sendSmsMessage(SendAllMessageDTO sendAllMessageDTO, int tid, int isAuto, int sendType) {
        StatusResultResponse<String> statusResultResponse;
        SendMessageWrapper.SendSmsMessage sendSmsMessage = DozerUtils.map(sendAllMessageDTO, SendMessageWrapper.SendSmsMessage.class);
        sendSmsMessage.setTid(tid);
        if (MsgSendAutoEnum.YES.compare(isAuto) || sendAllMessageDTO.getDelayTime() != null) {
            statusResultResponse = smsMsgService.sendTemplateMsg(sendSmsMessage);
        } else {
            AsyncSendMessage<SendMessageWrapper.SendSmsMessage> asyncSendMessage = AsyncSendMessage.<SendMessageWrapper.SendSmsMessage>builder()
                    .sendType(sendType)
                    .message(sendSmsMessage)
                    .build();
            statusResultResponse = sendAsyncMessage(asyncSendMessage, tid, MsgSendConstant.MQ_ASYNC_MESSAGE_QUEUE);
        }
        return statusResultResponse;
    }

    /**
     * 发送APP消息
     *
     * @param sendAllMessageDTO
     * @param tid
     * @return
     */
    private StatusResultResponse<String> sendAppMessage(SendAllMessageDTO sendAllMessageDTO, int tid, int isAuto, int sendType) {
        StatusResultResponse<String> statusResultResponse;
        TemplateMsgDTO templateMsgDTO = DozerUtils.map(sendAllMessageDTO, TemplateMsgDTO.class);
        templateMsgDTO.setTid(tid);
        if (MsgSendAutoEnum.NO.compare(isAuto)) {
            AsyncSendMessage<TemplateMsgDTO> asyncSendMessage = AsyncSendMessage.<TemplateMsgDTO>builder()
                    .sendType(sendType)
                    .message(templateMsgDTO)
                    .build();
            statusResultResponse = sendAsyncMessage(asyncSendMessage, tid, MsgSendConstant.MQ_ASYNC_APP_NOTE_QUEUE);
        } else {
            statusResultResponse = appMsgSendService.sendAppTemplateMsg(templateMsgDTO);
        }
        return statusResultResponse;
    }

    /**
     * 发送异步消息
     *
     * @param asyncSendMessage
     * @return
     */
    private StatusResultResponse<String> sendAsyncMessage(AsyncSendMessage asyncSendMessage, int tid, String queue) {
        try {
            log.debug(LogUtils.buildTemplate("queue tid asyncSendMessage"), queue, tid, asyncSendMessage);
            rabbitTemplate.convertAndSend(queue, asyncSendMessage);
            return StatusResultResponse.<String>builder()
                    .status(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode())
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getMessage())
                    .build();
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("tid"), tid, e);
            return StatusResultResponse.<String>builder()
                    .status(MyExceptionStatus.SEND_ALL_MESSAGE_FAIL.getCode())
                    .result(MyExceptionStatus.SEND_ALL_MESSAGE_FAIL.getMessage())
                    .build();
        }
    }

    /**
     * 发送邮件
     *
     * @param t
     * @param title
     * @param content
     * @param personal
     * @return
     */
    public boolean sendMail(RecordTarget t, String title, String content, String personal) {
        String toUserAddress = t.getContact();
        if (StringUtils.isEmpty(toUserAddress)) {
            log.warn(LogUtils.buildTemplate("发送邮件时，邮件地址为空,发送内容"), content);
            return false;
        }
        if (MsgCenterConfiguration.MAIL_OPEN) {
            try {
                return SendMailUtils.sendMail(toUserAddress, title, content, personal);
            } catch (Exception e) {
                log.warn(LogUtils.buildTemplate(), e);
            }
        }
        return true;
    }

    /**
     * 发送微信消息
     *
     * @param target
     * @param content
     * @return
     */
    public boolean sendWeixin(RecordTarget target, String content) {
        String openid = target.getContact();
        log.info("sendWeixin target:{}, content:{}", target, content);
        int userType = 3;
        if (target.getUserType() == userType) {
            return SendWechatUtils.sendWeixinEp(openid, content);
        }
        return SendWechatUtils.sendWeixin(openid, content);
    }

    /**
     * 保存消息记录
     *
     * @param sendMsgDTO
     * @param target
     * @param result
     */
    private void saveMsgRecord(SendMsgDTO sendMsgDTO, RecordTarget target, Future<Boolean> result) {
        boolean flag = true;
        try {
            if (result != null) {
                flag = result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.warn("saveMsgRecord Future exception result:{} e:{}", result, e);
            flag = false;
        }
        ComplexMsgRecord complexMsgRecord = DozerUtils.map(sendMsgDTO, ComplexMsgRecord.class);
        complexMsgRecord.setTarget(DozerUtils.map(target, MongoRecordTarget.class));
        complexMsgRecord.setStatus(flag ? 2 : 1);
        complexMsgRecord.setSendTime(TimeUtils.getCurrentTimestamp());
        complexMsgRecord.setIsRead(0);
        try {
            if (IntegerUtils.isEqLimitValue(target.getSendType(), MsgSendTypeEnum.EMAIL.getCode())) {
                MongoMailRecord mongoMailRecord = DozerUtils.map(complexMsgRecord, MongoMailRecord.class);
                mongoMailRecord.setInsertTime(TimeUtils.getCurrentTimestamp());
                mailRecordRepository.save(mongoMailRecord);
            } else if (IntegerUtils.isEqLimitValue(target.getSendType(), MsgSendTypeEnum.WECHAT.getCode())) {
                MongoWeixinRecord mongoWeixinRecord = DozerUtils.map(complexMsgRecord, MongoWeixinRecord.class);
                mongoWeixinRecord.setInsertTime(TimeUtils.getCurrentTimestamp());
                weixinRecordRepository.save(mongoWeixinRecord);
            } else if (IntegerUtils.isEqLimitValue(target.getSendType(), MsgSendTypeEnum.APP.getCode())) {
                MongoAppRecord mongoAppRecord = DozerUtils.map(complexMsgRecord, MongoAppRecord.class);
                mongoAppRecord.setInsertTime(TimeUtils.getCurrentTimestamp());
                mongoAppRecordRepository.save(mongoAppRecord);
            } else if (IntegerUtils.isEqLimitValue(target.getSendType(), MsgSendTypeEnum.PC.getCode())) {
                MongoPcRecordBizData mongoPcRecordBizData = DozerUtils.map(sendMsgDTO.getBizData(), MongoPcRecordBizData.class);
                MongoPcRecord mongoPcRecord = DozerUtils.map(complexMsgRecord, MongoPcRecord.class);
                mongoPcRecord.setBizData(mongoPcRecordBizData);
                mongoPcRecord.setChannel(0);
                mongoPcRecord.setInsertTime(TimeUtils.getCurrentTimestamp());
                mongoPcRecordRepository.save(mongoPcRecord);
            }
        } catch (Exception e) {
            log.warn("saveMsgRecord save fail complexMsgRecord:{} e:{}", complexMsgRecord, e);
        }
    }
}
