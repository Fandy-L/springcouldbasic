package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.TemplateMsgDTO;
import com.to8to.tbt.msc.entity.mysql.main.AppTiming;
import com.to8to.tbt.msc.entity.mysql.main.TimingMessage;
import com.to8to.tbt.msc.entity.mysql.push.App;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.enumeration.AppSendModeEnum;
import com.to8to.tbt.msc.enumeration.TimingMessageStatusEnum;
import com.to8to.tbt.msc.repository.mysql.main.AppTimingRepository;
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
public class AppMsgSendServiceTest extends BaseApplication {

    @Autowired
    private AppTimingRepository appTimingRepository;

    @Autowired
    private AppMsgSendService appMsgSendService;

    int tid = 20008;
    String uid = "172174983";
    String ticker = "";
    String sender = "php";
    int appId = 15;
    JSONObject bizParam = JSONObject.parseObject("{\"triggerAccountId\":2161068,\"moduleCode\":\"ugcAnswer\",\"scheme\":\"\",\"schemeUrl\":\"\",\"remark\":\"\",\"subUid\":0,\"triggerUserType\":0,\"type\":13,\"title\":\"回答了你的问题\",\"originContentPic\":\"\",\"url\":\"https://mapp.to8to.com/answer/info/55026\",\"content\":\"euhfncjvhbjj\",\"extraDataParams\":{\"cover\":\"empty\"},\"triggerUid\":1720556,\"originContentDesc\":\"教大家记得记得亟待解决喜欢喜欢很喜欢的话\",\"objectId\":55026}");
    JSONObject keyword = JSONObject.parseObject("{\"用户名称\":\"啊啊啊啊啊啊啊啊\"}");
    int version = 2;
    JSONObject click = JSONObject.parseObject("{\"custom\":{\"cover\":\"empty\"},\"type\":3}");

    @Test
    public void sendAppTemplateMsg(){
        TemplateMsgDTO templateMsgDTO = TemplateMsgDTO.builder()
                .tid(tid)
                .uid(uid)
                .ticker(ticker)
                .sender(sender)
                .appId(appId)
                .bizParam(bizParam)
                .keyword(keyword)
                .version(version)
                .click(click)
                .sendMode(AppSendModeEnum.SAVE_SEND.getSendMode())
                .build();
        StatusResultResponse<String> statusResultResponse = appMsgSendService.sendAppTemplateMsg(templateMsgDTO);
        log.warn("statusResultResponse:{}", statusResultResponse);
        Assert.assertTrue(statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode());
    }

    @Test
    public void sendAppTemplateMsgByDelayTime(){
        int delayTime = TimeUtils.getTimesDayNight();
        TemplateMsgDTO templateMsgDTO = TemplateMsgDTO.builder()
                .tid(tid)
                .uid(uid)
                .ticker(ticker)
                .sender(sender)
                .appId(appId)
                .bizParam(bizParam)
                .keyword(keyword)
                .version(version)
                .click(click)
                .delayTime(delayTime)
                .sendMode(AppSendModeEnum.SAVE_UNSEND.getSendMode())
                .build();
        StatusResultResponse<String> statusResultResponse = appMsgSendService.sendAppTemplateMsg(templateMsgDTO);
        log.warn("statusResultResponse:{}", statusResultResponse);
        Assert.assertTrue(statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode());
    }

    @Test
    public void sendDelayMessage(){
        appMsgSendService.sendDelayMessage();
        int endTime = TimeUtils.getCurrentTimestamp();
        List<AppTiming> unSendList = appTimingRepository.getUnSendMessage(endTime);
        Assert.assertTrue(unSendList.isEmpty());
    }
}
