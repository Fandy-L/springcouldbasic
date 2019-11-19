package com.to8to.tbt.msc.listener;

import com.alibaba.fastjson.JSONArray;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.constant.MsgSendConstant;
import com.to8to.tbt.msc.dto.TemplateMsgDTO;
import com.to8to.tbt.msc.entity.AsyncSendMessage;
import com.to8to.tbt.msc.entity.SendMessageWrapper;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.MsgSendTypeEnum;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class RabbitmqListener {

    @Autowired
    private SmsMsgService smsMsgService;

    @Autowired
    private NoteSyncService noteSyncService;

    @Autowired
    private AppMsgSendService appMsgSendService;

    @RabbitListener(queues = MsgSendConstant.MQ_NOTE_SYNC_QUEUE_TEST)
    public void noteSync(JSONArray data) {
        noteSyncService.execute(data);
    }

    @RabbitListener(queues = MsgSendConstant.MQ_ASYNC_MESSAGE_QUEUE)
    public void asyncMessageSend(AsyncSendMessage<SendMessageWrapper.SendSmsMessage> asyncSendMessage) {
        try {
            StatusResultResponse<String> statusResultResponse = null;
            if (MsgSendTypeEnum.SMS.equals(asyncSendMessage.getSendType())) {
                SendMessageWrapper.SendSmsMessage sendSmsMessage = asyncSendMessage.getMessage();
                statusResultResponse = smsMsgService.sendTemplateMsg(sendSmsMessage);
            }
            log.info(LogUtils.buildTemplate("sendType response message"), asyncSendMessage.getSendType(), statusResultResponse, asyncSendMessage.getMessage());
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("asyncSendMessage"), asyncSendMessage, e);
        }
    }

    @RabbitListener(queues = MsgSendConstant.MQ_ASYNC_APP_NOTE_QUEUE)
    public void asyncAppNoteSend(AsyncSendMessage<TemplateMsgDTO> asyncSendMessage) {
        try {
            StatusResultResponse<String> statusResultResponse = null;
            if (MsgSendTypeEnum.APP.equals(asyncSendMessage.getSendType())) {
                TemplateMsgDTO templateMsgDTO = asyncSendMessage.getMessage();
                statusResultResponse = appMsgSendService.sendAppTemplateMsg(templateMsgDTO);
            }
            log.info(LogUtils.buildTemplate("sendType response message"), asyncSendMessage.getSendType(), statusResultResponse, asyncSendMessage.getMessage());
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("asyncSendMessage"), asyncSendMessage, e);
        }
    }
}
