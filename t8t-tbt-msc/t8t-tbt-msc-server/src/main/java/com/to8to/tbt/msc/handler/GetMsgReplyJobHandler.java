package com.to8to.tbt.msc.handler;

import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.service.impl.MessageCenterServiceImpl;
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
@JobHandler(value = "getMsgReplyJobHandler")
public class GetMsgReplyJobHandler extends IJobHandler {
    @Autowired
    private MessageCenterServiceImpl messageCenterService;

    @Override
    public ReturnT<String> execute(String argStr) throws Exception {

        try {
            XxlJobLogger.log("MSC get msg reply task has started!");
            log.info("自动获取短信上行记录任务：启动！");
            messageCenterService.insertMsgReply(MsgCenterConfiguration.EMP_TMY_YX_USERID, MsgCenterConfiguration.EMP_TMY_YX_PASSWORD);
            messageCenterService.insertMsgReply(MsgCenterConfiguration.EMP_TO8TO_YX_USERID, MsgCenterConfiguration.EMP_TO8TO_YX_PASSWORD);
            messageCenterService.insertMsgReply(MsgCenterConfiguration.EMP_TMY_SC_USERID, MsgCenterConfiguration.EMP_TMY_SC_PASSWORD);
            messageCenterService.insertMsgReply(MsgCenterConfiguration.EMP_TO8TO_SC_USERID, MsgCenterConfiguration.EMP_TO8TO_SC_PASSWORD);
            messageCenterService.saveAliyunUpLinkRecord();

        } catch (Exception e) {
            XxlJobLogger.log("MSC get msg reply task error!", e);
            log.warn("自动获取短信上行记录任务异常：{}", e);
            return FAIL;
        }
        return SUCCESS;
    }
}
