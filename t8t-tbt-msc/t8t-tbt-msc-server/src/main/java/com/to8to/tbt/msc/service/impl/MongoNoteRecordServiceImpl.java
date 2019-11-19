package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.entity.mongo.MongoNoteRecord;
import com.to8to.tbt.msc.entity.mongo.MongoRecordTarget;
import com.to8to.tbt.msc.repository.mongo.MongoNoteRecordRepository;
import com.to8to.tbt.msc.service.MongoNoteRecordService;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class MongoNoteRecordServiceImpl implements MongoNoteRecordService {

    @Autowired
    private MongoNoteRecordRepository mongoNoteRecordRepository;

    @Override
    public void create(String phone, int status, String content, int channelType, int tid, String errorDescribe) {
        MongoNoteRecord msgRecord = new MongoNoteRecord();
        MongoRecordTarget target = new MongoRecordTarget();
        msgRecord.setTarget(target);
        target.setContact(phone);
        // mongoDB 1:失败 2:成功   程序0:失败 1成功
        msgRecord.setStatus(status);
        msgRecord.setContent(content);
        msgRecord.setChannel(channelType);
        msgRecord.setTid(tid);
        msgRecord.setErrorDescribe(StringUtils.defaultString(errorDescribe));
        msgRecord.setSendTime(TimeUtils.getCurrentTimestamp());
        msgRecord.setInsertTime(TimeUtils.getCurrentTimestamp());
        try {
            mongoNoteRecordRepository.save(msgRecord);
            log.debug(LogUtils.buildTemplate("msgRecord"), msgRecord);
        }catch (Exception e){
            log.warn("MongoNoteRecordService create error e:{}", e);
        }
    }
}
