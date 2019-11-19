package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.SendMsgPhoneDTO;
import com.to8to.tbt.msc.dto.SendSmsBatchDTO;
import com.to8to.tbt.msc.entity.ResponseWrapper;
import com.to8to.tbt.msc.entity.SendMessageWrapper;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.vo.SendMsgPhoneVO;

import java.util.Set;

/**
 * @author juntao.guo
 */
public interface SmsMsgService {

    /**
     * 发送短信模板消息
     *
     * @param sendSmsMessage
     * @return
     */
    StatusResultResponse<String> sendTemplateMsg(SendMessageWrapper.SendSmsMessage sendSmsMessage);

    /**
     * 群发短信
     *
     * @param sendSmsBatchDTO
     * @return
     */
    ResResult<ResultStatusResponse<String>> sendSmsBatch(SendSmsBatchDTO sendSmsBatchDTO);

    /**
     * 根据批量手机号发送短信
     *
     * @param sendMsgPhoneDTO
     * @return
     */
    ResResult sendMsgPhone(SendMsgPhoneDTO sendMsgPhoneDTO);

    /**
     * 根据批量手机号ID发送短信
     *
     * @param sendMsgPhoneDTO
     * @return
     */
    ResResult<SendMsgPhoneVO> sendMsgPhoneId(SendMsgPhoneDTO sendMsgPhoneDTO);

    /**
     * 校验手机号格式
     *
     * @param phones
     * @return
     */
    Set<String> validPhoneFormat(String phones);

    /**
     * 发送短信并保存发送记录 所有的短信发送和记录保存都只走这一个接口
     *
     * @param tid
     * @param phone
     * @param phoneid
     * @param content
     * @param channelType
     * @param saveToMongo
     * @return
     */
    StatusResultResponse<String> sendSmsAndSave(int tid, String phone, String phoneid, String content, int channelType, Boolean saveToMongo);

    /**
     * 发送延时短信
     *
     * @return
     */
    void sendDelayMessage();
}
