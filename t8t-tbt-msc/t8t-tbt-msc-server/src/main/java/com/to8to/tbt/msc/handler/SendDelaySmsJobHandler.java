package com.to8to.tbt.msc.handler;

import com.to8to.tbt.msc.service.SmsMsgService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author juntao.guo
 */
@Component
@JobHandler(value = "sendDelaySmsJobHandler")
public class SendDelaySmsJobHandler extends IJobHandler {

    @Autowired
    private SmsMsgService smsMsgService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            smsMsgService.sendDelayMessage();
        } catch (Exception e) {
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
