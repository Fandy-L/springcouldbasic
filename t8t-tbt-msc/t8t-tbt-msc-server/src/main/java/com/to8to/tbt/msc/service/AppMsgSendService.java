package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.RedirectAppMsgDTO;
import com.to8to.tbt.msc.dto.TemplateMsgDTO;
import com.to8to.tbt.msc.entity.response.RedirectAppMsgResult;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;

/**
 * @author juntao.guo
 */
public interface AppMsgSendService {

    /**
     * 发送APP模板消息
     *
     * @param params
     * @return
     */
    StatusResultResponse<String> sendAppTemplateMsg(TemplateMsgDTO params);

    /**
     * 发送APP Push消息
     *
     * @param params
     * @return
     */
    RedirectAppMsgResult sendAppMsgV2(RedirectAppMsgDTO params);

    /**
     * 发送延时APP消息
     */
    void sendDelayMessage();
}
