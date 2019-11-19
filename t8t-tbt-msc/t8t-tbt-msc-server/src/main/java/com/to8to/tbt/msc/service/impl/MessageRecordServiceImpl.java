package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.entity.mysql.main.MessageRecord;
import com.to8to.tbt.msc.repository.mysql.main.MessageRecordRepository;
import com.to8to.tbt.msc.service.MessageRecordService;
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
public class MessageRecordServiceImpl implements MessageRecordService {

    @Autowired
    private MessageRecordRepository messageRecordRepository;

    @Override
    public void create(int tid, int phoneId, String msgContent, int status, int errorCode) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setTid(tid);
        messageRecord.setPhoneId(phoneId);
        messageRecord.setMsgContent(msgContent);
        messageRecord.setSendStatus(status);
        messageRecord.setSendTime(TimeUtils.getCurrentTimestamp());
        messageRecord.setErrorCode(errorCode);
        try {
            messageRecordRepository.save(messageRecord);
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("messageRecord"), messageRecord, e);
        }
    }
}
