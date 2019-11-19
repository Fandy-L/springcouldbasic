package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.entity.mysql.main.AppTiming;
import com.to8to.tbt.msc.repository.mysql.main.AppTimingRepository;
import com.to8to.tbt.msc.service.TimingAppService;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class TimingAppServiceImpl implements TimingAppService {

    @Autowired
    private AppTimingRepository appTimingRepository;

    @Override
    public boolean create(int tid, String uids, String sender, int appId, int version, String content, JSONObject bizData, int delayTime, int sendMode) {
        AppTiming appTiming = AppTiming.builder()
                .tid(tid)
                .uids(uids)
                .sender(sender)
                .appId(appId)
                .version(version)
                .msgContent(content)
                .bizData(bizData.toJSONString())
                .delayTime(delayTime)
                .sendMode(sendMode)
                .putTime(TimeUtils.getCurrentTimestamp())
                .build();
        try {
            appTimingRepository.save(appTiming);
        }catch (Exception e){
            log.warn("TimingAppService create mysql error appTiming e:{}", appTiming, e);
        }
        return appTiming.getId() > 0;
    }
}
