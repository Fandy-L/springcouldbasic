package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.constant.MongateResponseConstant;
import com.to8to.tbt.msc.entity.RabbitMqWrapper;
import com.to8to.tbt.msc.entity.es.EsAppRecord;
import com.to8to.tbt.msc.entity.es.EsBaseRecord;
import com.to8to.tbt.msc.entity.es.EsMsgRecord;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Configure;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.repository.es.EsAppRecordRepository;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.ConfigureRepository;
import com.to8to.tbt.msc.repository.mysql.main.MessageTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.service.NoteSyncService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class NoteSyncServiceImpl implements NoteSyncService {

    /**
     * 数据库插入动作的标志
     */
    private final String ACTION_INSERT = "INSERT";
    private final String ACTION_UPDATE = "UPDATE";
    /**
     * 短信及APP消息数据表名
     */
    private final String TABLE_MESSAGE_RECORD = "msgc_message_record";
    private final String TABLE_APP_RECORD = "msgc_app_record";
    /**
     * 消息索引解析
     */
    private final int INDEX_DATABASE = 0;
    private final int INDEX_TABLE = 1;
    private final int INDEX_ACTION = 2;
    private final int INDEX_LIST = 4;

    @Autowired
    private ConfigureRepository configureRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private EsAppRecordRepository esAppRecordRepository;

    @Autowired
    private EsMsgRecordRepository esMsgRecordRepository;

    @Override
    public void execute(JSONArray data) {
        try {
            RabbitMqWrapper.NoteSyncData<? super Object> noteSyncData = new RabbitMqWrapper.NoteSyncData<>();
            noteSyncData.setDatabase(data.getString(INDEX_DATABASE));
            noteSyncData.setTable(data.getString(INDEX_TABLE));
            noteSyncData.setAction(data.getString(INDEX_ACTION));
            noteSyncData.setList(Lists.newArrayList());
            if (!noteSyncData.getTable().equals(TABLE_MESSAGE_RECORD) && !noteSyncData.getTable().equals(TABLE_APP_RECORD)) {
                log.info(LogUtils.buildTemplate("invalidMessage"), data);
                return;
            }
            JSONArray recordList = data.getJSONArray(INDEX_LIST);
            for (int i = 0; i < recordList.size(); i++) {
                JSONObject item = recordList.getJSONObject(i);
                JSONObject targetItem = new JSONObject();
                for (String key : item.keySet()) {
                    targetItem.put(key, item.getJSONArray(key).getString(0));
                }
                if (noteSyncData.getTable().equals(TABLE_APP_RECORD)) {
                    RabbitMqWrapper.AppRecord appRecord = JSONObject.toJavaObject(targetItem, RabbitMqWrapper.AppRecord.class);
                    syncAppRecord(appRecord);
                } else {
                    RabbitMqWrapper.MessageRecord messageRecord = JSONObject.toJavaObject(targetItem, RabbitMqWrapper.MessageRecord.class);
                    syncMessageRecord(messageRecord);
                }
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("data"), data, e);
        }
    }

    /**
     * 同步短信记录
     *
     * @param messageRecord
     */
    @Override
    public boolean syncMessageRecord(RabbitMqWrapper.MessageRecord messageRecord) {
        try {
            EsMsgRecord msgRecordEntity = DozerUtils.map(messageRecord, EsMsgRecord.class);
            if (IntegerUtils.isEqLimitValue(messageRecord.getTid())) {
                log.warn("syncMessageRecord tid invalid messageRecord:{}", messageRecord);
                return false;
            }
            int tid = messageRecord.getTid();
            MessageTemplate mt = messageTemplateRepository.findByTid(tid).orElse(null);
            if (mt != null){
                msgRecordEntity.setIsGround(mt.getIsGround());
                msgRecordEntity.setChannelType(mt.getChannelType());
            }
            try {
                supplyBaseRecord(msgRecordEntity, tid);
            }catch (Exception e){
                log.info("消息记录的基础信息缺失");
                return false;
            }
            int errorCode = messageRecord.getErrorCode();
            if (errorCode != 0) {
                msgRecordEntity.setErrorDescribe(MongateResponseConstant.MONGATERESPONSE.getString(String.valueOf(errorCode)));
            } else {
                msgRecordEntity.setErrorDescribe("");
            }
            try {
                esMsgRecordRepository.saveRecord((JSONObject) JSONObject.toJSON(msgRecordEntity));
                return true;
            } catch (Exception e) {
                log.warn("syncMessageRecord saveRecord exception msgRecordEntity:{} e:{}", msgRecordEntity, e);
            }

        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("messageRecord"), messageRecord, e);
        }
        return false;
    }

    /**
     * 同步APP消息记录
     *
     * @param appRecord
     */
    @Override
    public boolean syncAppRecord(RabbitMqWrapper.AppRecord appRecord) {
        try {
            EsAppRecord esAppRecordEntity = DozerUtils.map(appRecord, EsAppRecord.class);
            int tid = IntegerUtils.intValueAsDefault(appRecord.getTid());
            if (tid > 0) {
                AppTemplate appTemplate = appTemplateRepository.findByTid(tid).orElse(null);
                if (appTemplate != null){
                    esAppRecordEntity.setAppId(appTemplate.getAppId());
                }else {
                    return false;
                }
            }
            try {
                supplyBaseRecord(esAppRecordEntity, tid);
            }catch (Exception e){
                log.info("消息记录的基础信息缺失");
                return false;
            }
            JSONObject item = (JSONObject) JSONObject.toJSON(esAppRecordEntity);
            String bizParam = appRecord.getBizParam();
            if (StringUtils.isNotBlank(bizParam)) {
                try {
                    JSONObject bizParams = JSONObject.parseObject(bizParam);
                    for (String key : bizParams.keySet()) {
                        item.put(key, bizParams.get(key));
                    }
                } catch (Exception e) {
                    log.warn(LogUtils.buildExceptionTemplate("bizParam"), bizParam, e);
                }
            }
            try {
                esAppRecordRepository.saveRecord(item);
                return true;
            } catch (Exception e) {
                log.warn("syncAppRecord saveRecord exception item:{} e:{}", item, e);
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildTemplate("appRecord"), appRecord, e);
        }
        return false;
    }

    /**
     * 补充消息记录的基础信息
     *
     * @param esBaseRecord
     * @param tid
     * @return
     */
    private void supplyBaseRecord(EsBaseRecord esBaseRecord, int tid) {
        Template template = templateRepository.findById(tid).orElse(null);
        int nodeId = template.getNodeId();
        Configure node = configureRepository.findById(nodeId).orElse(null);
        Configure small = configureRepository.findById(node.getFatherId()).orElse(null);
        Configure big = configureRepository.findById(small.getFatherId()).orElse(null);
        esBaseRecord.setNodeType(nodeId);
        esBaseRecord.setNodeTypeDes(node.getConfigDescribe());
        esBaseRecord.setSmallType(small.getCid());
        esBaseRecord.setSmallTypeDes(small.getConfigDescribe());
        esBaseRecord.setLargeType(big.getCid());
        esBaseRecord.setLargeTypeDes(big.getConfigDescribe());
        esBaseRecord.setTitle(template.getTitle());
        esBaseRecord.setIsActive(template.getIsActive());
        esBaseRecord.setIsAuto(template.getIsAuto());
        esBaseRecord.setTargetType(template.getTargetType());
        esBaseRecord.setSendType(template.getSendType());
        esBaseRecord.setPmModule(template.getPmModule());
    }
}
