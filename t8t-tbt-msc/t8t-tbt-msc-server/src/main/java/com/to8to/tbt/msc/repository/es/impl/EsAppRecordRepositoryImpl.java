package com.to8to.tbt.msc.repository.es.impl;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.repository.es.EsAppRecordRepository;
import com.to8to.tbt.msc.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class EsAppRecordRepositoryImpl implements EsAppRecordRepository {

    @Value(value = "${es.index}")
    public String index;

    @Value(value = "${es.app_record_type}")
    public String appRecordType;

    @Autowired
    private TransportClient transportClient;

    @Override
    public void saveRecord(JSONObject data) {
        String id = data.getString("id");
        try {
            boolean isExists = transportClient.prepareGet(index, appRecordType, id).get().isExists();
            if (isExists) {
                transportClient.prepareUpdate(index, appRecordType, id).setDoc(data.toJSONString()).get();
            } else {
                transportClient.prepareIndex(index, appRecordType, id).setSource(data).get();
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("data"), data, e);
        }
    }
}
