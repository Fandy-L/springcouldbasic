package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.entity.mongo.MongoGroupUserSendRecord;
import com.to8to.tbt.msc.repository.mongo.MongoGroupUserSendRecordRepository;
import com.to8to.tbt.msc.service.GroupUserSendRecordService;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class GroupUserSendRecordServiceImpl implements GroupUserSendRecordService {

    @Autowired
    private MongoGroupUserSendRecordRepository mongoGroupUserSendRecordRepository;

    @Override
    public void insertGroupUserSendRecord(String groupNoteId, String userName, int succNum) {
        MongoGroupUserSendRecord mongoGroupUserSendRecord = new MongoGroupUserSendRecord();
        mongoGroupUserSendRecord.setGroupNoteId(groupNoteId);
        mongoGroupUserSendRecord.setUserName(userName);
        mongoGroupUserSendRecord.setCount(succNum);
        mongoGroupUserSendRecord.setSendTime(TimeUtils.getCurrentTimestamp());
        try {
            mongoGroupUserSendRecordRepository.save(mongoGroupUserSendRecord);
        } catch (Exception e) {
            log.warn("insertGroupUserSendRecord mysql error groupNoteId:{} userName:{} succNum:{} e:{}", groupNoteId, userName, succNum, e);
        }
    }

    @Override
    public int countGroupUserSendRecordByUser(String userName, int startTime, int endTime) {
        int count = 0;
        List<MongoGroupUserSendRecord> mongoGroupUserSendRecordList = mongoGroupUserSendRecordRepository.findAllByUserNameAndSendTimeBetween(userName, startTime, endTime);
        for (MongoGroupUserSendRecord mongoGroupUserSendRecord : mongoGroupUserSendRecordList) {
            count += mongoGroupUserSendRecord.getCount();
        }
        return count;
    }
}
