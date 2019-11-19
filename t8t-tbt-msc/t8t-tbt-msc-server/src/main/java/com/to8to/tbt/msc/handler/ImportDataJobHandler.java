package com.to8to.tbt.msc.handler;

import com.to8to.tbt.msc.service.ImportDataService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yason.li
 */
@Component
@JobHandler(value = "importDataJobHandler")
@Slf4j
public class ImportDataJobHandler extends IJobHandler {

    @Autowired
    private ImportDataService importDataService;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            long startTime = System.currentTimeMillis();
            importDataService.syncMsgRecord();
            importDataService.syncAppRecord();
            log.info("导入耗时:{}",System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
