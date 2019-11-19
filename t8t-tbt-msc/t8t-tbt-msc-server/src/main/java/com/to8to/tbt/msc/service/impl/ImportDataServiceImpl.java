package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.entity.RabbitMqWrapper;
import com.to8to.tbt.msc.entity.mysql.main.AppRecord;
import com.to8to.tbt.msc.entity.mysql.main.MessageRecord;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.repository.mysql.main.MessageRecordRepository;
import com.to8to.tbt.msc.service.ImportDataService;
import com.to8to.tbt.msc.service.NoteSyncService;
import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.entity.mysql.main.AppRecord;
import com.to8to.tbt.msc.entity.mysql.main.MessageRecord;
import com.to8to.tbt.msc.repository.es.EsAppRecordRepository;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.repository.mysql.main.MessageRecordRepository;
import com.to8to.tbt.msc.service.ImportDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author yason.li
 */
@Slf4j
@Service
public class ImportDataServiceImpl implements ImportDataService {

    @Value(value = "${app.msg.import.data.size}")
    private Integer importDataSize;

    @Autowired
    private MessageRecordRepository messageRecordRepository;

    @Autowired
    private NoteSyncService noteSyncService;

    @Autowired
    private AppRecordRepository appRecordRepository;

    @Override
    public void syncMsgRecord() {
        Long total = messageRecordRepository.count();
        Long success = 0L;
        Long pageNum = total % importDataSize == 0 ? total / importDataSize : total /importDataSize + 1;
        log.info("ImportData.syncMsgRecord开始，总条数：{}", total);
        // 开始同步数据
        for (int i = 0; i < pageNum; i++) {
            Pageable pageable2 = PageRequest.of(i, importDataSize);
            Page<MessageRecord> data2 = messageRecordRepository.findAllBySendStatusIsNotNullOrderById(pageable2);
            for (MessageRecord dataobj : data2){
                RabbitMqWrapper.MessageRecord record = DozerUtils.map(dataobj, RabbitMqWrapper.MessageRecord.class);
                boolean flag = noteSyncService.syncMessageRecord(record);
                if (flag){
                    success ++;
                }
            }
        }
        log.info("成功导入了{}条短信数据，失败了了{}条短信数据",success,total-success);
    }

    @Override
    public void syncAppRecord() {
        Long total = appRecordRepository.count();
        Long success = 0L;
        Long pageNum = total % importDataSize == 0 ? total / importDataSize : total /importDataSize + 1;
        log.info("ImportData.syncAppRecord开始，总条数：{}", total);
        // 开始同步数据
        for (int i = 0; i < pageNum; i++) {
            Pageable pageable2 = PageRequest.of(i, importDataSize);
            Page<AppRecord> data2 = appRecordRepository.findAllBySendStatusIsNotNullOrderById(pageable2);
            for (AppRecord dataobj : data2){
                RabbitMqWrapper.AppRecord record = DozerUtils.map(dataobj, RabbitMqWrapper.AppRecord.class);
                boolean flag = noteSyncService.syncAppRecord(record);
                if (flag){
                    success ++;
                }
            }
        }
        log.info("成功导入了{}条APP消息数据，失败了了{}条APP消息数据",success,total-success);
    }
}
