package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.SendAllMessageDTO;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.enumeration.AppSendModeEnum;
import com.to8to.tbt.msc.enumeration.MsgSendAutoEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class ComplexMessageServiceTest extends BaseApplication {

    @Autowired
    private ComplexMessageService complexMessageService;

    String tid = "20008";
    String uid = "172174983";
    String ticker = "";
    String sender = "php";
    int appId = 15;
    JSONObject bizParam = JSONObject.parseObject("{\"triggerAccountId\":2161068,\"moduleCode\":\"ugcAnswer\",\"scheme\":\"\",\"schemeUrl\":\"\",\"remark\":\"\",\"subUid\":0,\"triggerUserType\":0,\"type\":13,\"title\":\"回答了你的问题\",\"originContentPic\":\"\",\"url\":\"https://mapp.to8to.com/answer/info/55026\",\"content\":\"euhfncjvhbjj\",\"extraDataParams\":{\"cover\":\"empty\"},\"triggerUid\":1720556,\"originContentDesc\":\"教大家记得记得亟待解决喜欢喜欢很喜欢的话\",\"objectId\":55026}");
    JSONObject keyword = JSONObject.parseObject("{\"用户名称\":\"啊啊啊啊啊啊啊啊\"}");
    int version = 2;
    JSONObject click = JSONObject.parseObject("{\"custom\":{\"cover\":\"empty\"},\"type\":3}");

    @Test
    public void sendAllMessageByApp(){
        SendAllMessageDTO sendAllMessageDTO = SendAllMessageDTO.builder()
                .tid(tid)
                .uid(uid)
                .ticker("")
                .sender(sender)
                .appId(appId)
                .bizParam(bizParam)
                .keyword(keyword)
                .version(version)
                .click(click)
                .isAuto(MsgSendAutoEnum.NO.getCode())
                .sendMode(AppSendModeEnum.SAVE_SEND.ordinal())
                .build();
        ResResult<ResultStatusResponse<String>> resResult = complexMessageService.sendAllMessage(sendAllMessageDTO);
        log.debug("resResult:{}", resResult);
    }
}
