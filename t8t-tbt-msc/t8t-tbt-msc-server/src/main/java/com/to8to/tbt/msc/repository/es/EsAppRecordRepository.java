package com.to8to.tbt.msc.repository.es;

import com.alibaba.fastjson.JSONObject;


/**
 * @author juntao.guo
 */
public interface EsAppRecordRepository {

    /**
     * 保存消息
     *
     * @param data
     * @return
     */
    void saveRecord(JSONObject data);
}
