package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.entity.mysql.main.TimingMessage;
import com.to8to.tbt.msc.repository.mysql.main.TimingMessageRepository;
import com.to8to.tbt.msc.service.TimingMessageService;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class TimingMessageServiceImpl implements TimingMessageService {

    @Autowired
    private TimingMessageRepository timingMessageRepository;

    @Override
    public boolean addDelayMessage(int tid, int phoneId, String content, int delayTime) {
        TimingMessage timingMessage = new TimingMessage();
        timingMessage.setTid(tid);
        timingMessage.setPhoneId(phoneId);
        timingMessage.setMsgContent(content);
        timingMessage.setIsSend(0);
        timingMessage.setPutTime(TimeUtils.getCurrentTimestamp());
        timingMessage.setSendTime(0);
        timingMessage.setDelayTime(delayTime);
        try {
            timingMessage = timingMessageRepository.save(timingMessage);
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("timingMessage"), timingMessage, e);
            return false;
        }
        return timingMessage.getId() > 0;
    }
}
