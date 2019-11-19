package com.to8to.tbt.msc.handler;

import com.to8to.tbt.msc.service.AppMsgSendService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yason.li
 */
@Slf4j
@Component
@JobHandler(value = "sendAppMessageJobHandler")
public class SendAppMessageJobHandler extends IJobHandler {

    @Autowired
    private AppMsgSendService appMsgSendService;

    @Override
    public ReturnT<String> execute(String argStr) {

        try {
            appMsgSendService.sendDelayMessage();
        } catch (Exception e) {
            XxlJobLogger.log("MSC send app message task error!", e);
            XxlJobLogger.log(e);
            return FAIL;
        }
        return SUCCESS;
    }

}
