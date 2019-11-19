package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.entity.mysql.main.AppRecord;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.service.AppRecordService;
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
public class AppRecordServiceImpl implements AppRecordService {

    @Autowired
    private AppRecordRepository appRecordRepository;

    @Override
    public boolean create(int tid, int uid, String sender, String content, int sendStatus, String bizParam) {
        AppRecord appRecordEntity = new AppRecord();
        appRecordEntity.setTid(tid);
        appRecordEntity.setUid(uid);
        appRecordEntity.setSender(sender);
        appRecordEntity.setAppContent(content);
        appRecordEntity.setSendStatus(sendStatus);
        appRecordEntity.setBizParam(bizParam);
        appRecordEntity.setSendTime(TimeUtils.getCurrentTimestamp());
        appRecordEntity.setCreateTime(TimeUtils.getCurrentTimestamp());
        try {
            appRecordRepository.save(appRecordEntity);
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("appRecord"), appRecordEntity, e);
            return false;
        }
        return true;
    }
}
