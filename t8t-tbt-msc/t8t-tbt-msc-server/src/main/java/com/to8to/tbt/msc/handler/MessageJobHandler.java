package com.to8to.tbt.msc.handler;

import com.to8to.tbt.msc.service.PushMessageService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pajero.quan
 */
@Slf4j
@JobHandler(value = "messageJobHandler")
@Component
public class MessageJobHandler extends IJobHandler {

    @Autowired
    private PushMessageService pushMessageService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("开始清理历史消息!");
        int duration = Integer.valueOf(s);
        pushMessageService.cleanMessages(duration);
        XxlJobLogger.log("历史消息清理完成!");
        return ReturnT.SUCCESS;
    }
}
