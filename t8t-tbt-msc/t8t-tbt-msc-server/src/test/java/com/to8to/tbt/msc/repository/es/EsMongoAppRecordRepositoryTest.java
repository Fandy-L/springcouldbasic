package com.to8to.tbt.msc.repository.es;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(SpringRunner.class)
public class EsMongoAppRecordRepositoryTest extends BaseApplication {

    @Autowired
    private EsAppRecordRepository esAppRecordRepository;

    @Test
    public void saveRecord() {
        JSONObject map = new JSONObject();
        map.put("id", 999998);
        map.put("send_time", TimeUtils.getCurrentTimestamp());
        execSaveRecord(map);
        map.put("id", 113869);
        execSaveRecord(map);

    }

    /**
     * 执行消息保存
     *
     * @param map
     */
    private void execSaveRecord(JSONObject map) {
        try {
            esAppRecordRepository.saveRecord(map);
        } catch (Exception e) {
            log.warn("saveRecord exception e:{}", e);
        }
    }
}
