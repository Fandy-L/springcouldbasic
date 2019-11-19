package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.configuration.OperateConfigConfiguration;
import com.to8to.tbt.msc.entity.SendMessageWrapper;
import com.to8to.tbt.msc.entity.mysql.main.TimingMessage;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.TimingMessageStatusEnum;
import com.to8to.tbt.msc.repository.mysql.main.TimingMessageRepository;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class SmsMsgServiceTest extends BaseApplication {

    @Autowired
    private SmsMsgService smsMsgService;

    @Autowired
    private TimingMessageRepository timingMessageRepository;

    @Test
    public void sendTemplateMsgDelayTime(){
        int tid = 664;
        int phoneId = 7141328;
        int delayTime = 86400;
        String ip = "127.0.0.1";
        JSONObject keyword = JSONObject.parseObject("{\"content\":\"测试\",\"vcode\":\"60000\"}");
        int sourceCount = timingMessageRepository.countByTidAndPhoneId(tid, phoneId);
        SendMessageWrapper.SendSmsMessage sendSmsMessage = SendMessageWrapper.SendSmsMessage.builder()
                .tid(tid)
                .phoneId(phoneId)
                .delayTime(delayTime)
                .keyword(keyword)
                .ip(ip)
                .build();
        int failNum = 0;
        for (int i = 0;i < 20;i++){
            StatusResultResponse<String> statusResultResponse = smsMsgService.sendTemplateMsg(sendSmsMessage);
            log.debug("result:{}", statusResultResponse);
            if (statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode()){
                int targetCount = timingMessageRepository.countByTidAndPhoneId(tid, phoneId);
                Assert.assertTrue(statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS.getCode() && (sourceCount + 1 + i - failNum) == targetCount);
            }else {
                failNum++;
                break;
            }
        }
    }

    @Test
    public void sendSmsAndSaveByTencent(){
        sendSmsAndSave();
    }

    @Test
    public void sendSmsAndSaveByAliyun(){
        JSONObject operator = JSONObject.parseObject("{\"aliyunSMS\":{\"1\":[0,1,2,3,4,5,6,7,8,9],\"2\":[0,1,2,3,4,5,6,7,8,9],\"3\":[0,1,2,3,4,5,6,7,8,9],\"4\":[0,1,2,3,4,5,6,7,8,9],\"5\":[0,1,2,3,4,5,6,7,8,9],\"6\":[0,1,2,3,4,5,6,7,8,9]}}");;
        OperateConfigConfiguration.SMS_ROUTE.put("operator", operator);
        sendSmsAndSave();
    }

    @Test
    public void sendSmsAndSaveByMongate(){
        JSONObject operator = JSONObject.parseObject("{\"mongateSMS\":{\"1\":[0,1,2,3,4,5,6,7,8,9],\"2\":[0,1,2,3,4,5,6,7,8,9],\"3\":[0,1,2,3,4,5,6,7,8,9],\"4\":[0,1,2,3,4,5,6,7,8,9],\"5\":[0,1,2,3,4,5,6,7,8,9],\"6\":[0,1,2,3,4,5,6,7,8,9]}}");;
        OperateConfigConfiguration.SMS_ROUTE.put("operator", operator);
        sendSmsAndSave();
    }

    @Test
    public void sendSmsAndSave(){
        MsgCenterConfiguration.SEND_PERMIT = 1;
        int tid = 664;
        String phone = "18682133031";
        String phoneId = "7141328";
        String content = "您正在进行测试，验证码为:"+ TimeUtils.getCurrentTimestamp() +"，请在1分钟内输入验证码。";
        int channelType = 4;
        StatusResultResponse<String> statusResultResponse = smsMsgService.sendSmsAndSave(tid, phone, phoneId, content, channelType, false);
        log.debug("statusResultResponse:{}", statusResultResponse);
        Assert.assertTrue(statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode());
    }

    @Test
    public void sendDelayMessage(){
        smsMsgService.sendDelayMessage();
        int startTime = TimeUtils.generateNatureWeekStartTime();
        int endTime = TimeUtils.getCurrentTimestamp();
        List<TimingMessage> unSendList = timingMessageRepository.findAllByIsSendAndDelayTimeIsBetween(TimingMessageStatusEnum.UNSENT.ordinal(), startTime, endTime);
        Assert.assertTrue(unSendList.isEmpty());
    }
}
