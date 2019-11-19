package com.to8to.tbt.msc.configuration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.service.ExternalService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.vo.OperateConfigGetVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author juntao.guo
 */
@Slf4j
@Configuration
public class OperateConfigConfiguration {
    /**
     * 配置参数名
     */
    public static final List<String> CONFIG_PARAMS = new ArrayList<>(Arrays.asList(
            "msgc_repeat_limit"
            , "groupSendNote_count_limit"
            , "groupSendNote_nolimit_user"
            , "sms_batch_size"
            , "sms_route"
            , "msgc_repeat_tid_limit"));

    /**
     * phone 重复限制
     */
    public static int ALL_REPEAT_LIMIT = 10;

    /**
     * 内容+phone 重复限制
     */
    public static int CON_REPEAT_LIMIT = 2;

    /**
     * 每日群发数量限制
     */
    public static int DAY_COUNT_MAX = 0;

    /**
     * 每周群发数量限制
     */
    public static int WEEK_COUNT_MAX = 0;

    public static int SMS_BATCH_SIZE = 0;

    /**
     * 模板ID总量 重复限制  默认值
     */
    public static int SMS_WHOLE_TID_REPEAT_LIMIT = 10000;
    /**
     * 模板ID 重复限制开关
     */
    public static boolean SMS_WHOLE_TID_REPEAT_SWITCH = false;
    /**
     * 模板ID + phone 重复限制  默认值
     */
    public static int SMS_PHONE_TID_REPEAT_LIMIT = 20;
    /**
     * 模板ID + phone 重复限制开关
     */
    public static boolean SMS_PHONE_TID_REPEAT_SWITCH = false;
    /**
     * “模板总量” 上限设置策略。
     *
     * @0 所有模板“模板总量” 都没有上限值（即没有限制），如有需要指定上限控制，需在 "smsWholeTidCustoms" 指定。
     * @1 所有模板“模板总量”上限默认值是 "SMS_whole_TID_REPEAT_LIMIT" ，如有需要自定义模板总量上限值需在 "smsWholeTidCustoms" 指定。 （如没有指定默认为 0）
     */
    public static int SMS_WHOLE_TID_STRATEGY;
    /**
     * “模板id + 手机号” 上限设置策略。
     *
     * @0 所有模板“模板id + 手机号” 都没有上限值（即没有限制），如有需要指定上限控制，需在 "tid_phone_custom" 指定。
     * @1 所有模板“模板id + 手机号”上限默认值是 "SMS_PHONE_TID_REPEAT_LIMIT" ，如有需要自定义上限值需在 "smsPhoneTidCustoms" 指定。  （如没有指定默认为 0）
     */
    public static int SMS_PHONE_TID_STRATEGY;
    /**
     * 模板ID总量 重复限制 自定义配置
     */
    public static Map<Integer, Integer> smsWholeTidCustoms = new HashMap<>();
    /**
     * 模板ID + phone 重复限制 自定义配置
     */
    public static Map<Integer, Integer> smsPhoneTidCustoms = new HashMap<>();

    public static List<String> GROUP_SEND_NOTE_NO_LIMIT_USER = new ArrayList<>();

    public static JSONObject SMS_ROUTE = new JSONObject();

    @Autowired
    public ExternalService externalService;

    @PostConstruct
    public void initialize() {
        try {
            OperateConfigGetVO operateConfigGetVO;
            try {
                operateConfigGetVO = externalService.getOpeConfigs(CONFIG_PARAMS);
                if (operateConfigGetVO == null) {
                    return;
                }
            } catch (Exception e) {
                log.error("OperateConfigConfiguration initialize getOpeConfigs exception e:{}", e);
                return;
            }
            log.info("OperateConfigConfiguration initialize operateConfigGetVO:{}", operateConfigGetVO);

            initRepeatLimit(operateConfigGetVO.getMsgRepeatLimit());

            initCountLimit(operateConfigGetVO.getGroupSendNoteCountLimit());

            initLimitUserList(operateConfigGetVO.getGroupSendNoteNoLimitUser());

            initSmsBatchSize(operateConfigGetVO.getSmsBatchSize());

            initSmsRoute(operateConfigGetVO.getSmsRoute());

            initSmsTidLimit(operateConfigGetVO.getMsgRepeatTidLimit());

        } catch (Exception e) {
            log.error("OperateConfigConfiguration initialize exception e:{}", e);
        }
    }

    /**
     * 短信上限配置
     *
     * @param str
     */
    public static void initRepeatLimit(String str) {
        try {
            String[] arr = str.split(",");
            ALL_REPEAT_LIMIT = Integer.parseInt(arr[0].trim());
            CON_REPEAT_LIMIT = Integer.parseInt(arr[1].trim());
        } catch (Exception e) {
            log.info("msgc_repeat_limit配置异常! error:{}", log);
            ALL_REPEAT_LIMIT = 0;
            CON_REPEAT_LIMIT = 0;
        }
        ALL_REPEAT_LIMIT = ALL_REPEAT_LIMIT > 0 ? ALL_REPEAT_LIMIT : 10;
        CON_REPEAT_LIMIT = CON_REPEAT_LIMIT > 0 ? CON_REPEAT_LIMIT : 2;
        log.info("短信上限配置:ALL_REPEAT_LIMIT:{},CON_REPEAT_LIMIT:{}", ALL_REPEAT_LIMIT, CON_REPEAT_LIMIT);
    }

    /**
     * 群发短信上限配置
     *
     * @param str
     */
    public static void initCountLimit(String str) {
        try {
            String[] arr = str.split(",");
            DAY_COUNT_MAX = Integer.parseInt(arr[0].trim());
            WEEK_COUNT_MAX = Integer.parseInt(arr[1].trim());
        } catch (Exception e) {
            log.info("groupSendNote_count_limit配置异常!");
            DAY_COUNT_MAX = 0;
            WEEK_COUNT_MAX = 0;
        }
        log.info("群发短信上限配置:DAY_COUNT_MAX:{},WEEK_COUNT_MAX:{}", DAY_COUNT_MAX, WEEK_COUNT_MAX);
    }

    /**
     * 群发短信不受限制的name
     *
     * @param str
     */
    public static void initLimitUserList(String str) {
        GROUP_SEND_NOTE_NO_LIMIT_USER.clear();
        try {
            String[] arr = str.split(",");
            for (String name : arr) {
                GROUP_SEND_NOTE_NO_LIMIT_USER.add(name.trim());
            }
        } catch (Exception e) {
            GROUP_SEND_NOTE_NO_LIMIT_USER.add("god");
            log.info("groupSendNote_nolimit_user未配置异常!默认初始化为GOD");
        }
        log.info("群发短信不受限制的name:GROUP_SEND_NOTE_NO_LIMIT_USER:{}", GROUP_SEND_NOTE_NO_LIMIT_USER);
    }

    /**
     * 群发短信数量配置
     *
     * @param smsBatchSize
     */
    public static void initSmsBatchSize(Integer smsBatchSize) {
        try {
            SMS_BATCH_SIZE = IntegerUtils.intValueAsDefault(smsBatchSize);
        } catch (Exception e) {
            SMS_BATCH_SIZE = 200;
            log.info("sms_batch_size配置异常!将自动配置为默认值");
        }
        log.info("群发短信数量配置:SMS_BATCH_SIZE:{}", SMS_BATCH_SIZE);
    }

    /**
     * 短信服务商路由列表
     *
     * @param confStr
     * @throws Exception
     */
    public static void initSmsRoute(String confStr) throws Exception {
        try {
            JSONObject routeConf = JSONObject.parseObject(confStr);
            SMS_ROUTE.clear();
            SMS_ROUTE.putAll(routeConf);
        } catch (Exception e) {
            throw new Exception("sms_route 配置异常！");
        }
        if (SMS_ROUTE.size() == 0) {
            throw new Exception("sms_route 未配置！");
        }
        log.info("短信服务商路由列表：{}", SMS_ROUTE.toJSONString());
    }

    /**
     * 短信模板限制配置
     *
     * @param configStr
     */
    private static void initSmsTidLimit(String configStr) {
        try {
            JSONObject configForTid = JSONObject.parseObject(configStr);
            fillSmsTidLimitCustomsConfig(configForTid.getJSONArray("tid_whole_custom"), smsWholeTidCustoms);
            fillSmsTidLimitCustomsConfig(configForTid.getJSONArray("tid_phone_custom"), smsPhoneTidCustoms);
            fillSmsTidLimitBaseConfig(configForTid);
        } catch (Exception e) {
            smsTidLimitDefaultConfigSet();
            log.error("短信模板限制配置异常！msgc_repeat_tid_limit :{},{}", configStr, log);
        }
        log.info("短信模板限制配置信息！{},SMS_WHOLE_TID_REPEAT_LIMIT:{},SMS_WHOLE_TID_REPEAT_SWITCH:{},SMS_PHONE_TID_REPEAT_LIMIT:{}," +
                        "SMS_PHONE_TID_REPEAT_SWITCH:{},SMS_WHOLE_TID_STRATEGY:{},SMS_PHONE_TID_STRATEGY:{},smsWholeTidCustoms:{},smsPhoneTidCustoms:{}"
                , configStr, SMS_WHOLE_TID_REPEAT_LIMIT, SMS_WHOLE_TID_REPEAT_SWITCH, SMS_PHONE_TID_REPEAT_LIMIT, SMS_PHONE_TID_REPEAT_SWITCH, SMS_WHOLE_TID_STRATEGY
                , SMS_PHONE_TID_STRATEGY, smsWholeTidCustoms.toString(), smsPhoneTidCustoms.toString());
    }

    /**
     * 补充短信模板限制基础信息
     *
     * @param customsConfigArray
     * @param configMap
     */
    private static void fillSmsTidLimitCustomsConfig(JSONArray customsConfigArray, Map<Integer, Integer> configMap) {
        configMap.clear();
        if (customsConfigArray != null && customsConfigArray.size() > 0) {
            customsConfigArray.forEach(customConfigObject -> {
                if (customConfigObject instanceof JSONObject) {
                    JSONObject customConfigJson = (JSONObject) customConfigObject;
                    int tid = customConfigJson.getIntValue("tid");
                    int value = customConfigJson.getIntValue("value");
                    if (tid > 0 && value > 0) {
                        configMap.put(tid, value);
                    }
                }
            });
        }
    }

    /**
     * 补充短信模板限制基础信息
     *
     * @param baseConfig
     */
    private static void fillSmsTidLimitBaseConfig(JSONObject baseConfig) {
        SMS_WHOLE_TID_REPEAT_SWITCH = baseConfig.getBooleanValue("tid_whole_switch_open");
        SMS_PHONE_TID_REPEAT_SWITCH = baseConfig.getBooleanValue("tid_phone_switch_open");
        SMS_WHOLE_TID_REPEAT_LIMIT = baseConfig.getIntValue("tid_whole_default_value");
        SMS_PHONE_TID_REPEAT_LIMIT = baseConfig.getIntValue("tid_phone_default_value");
        SMS_WHOLE_TID_STRATEGY = baseConfig.getIntValue("tid_whole_strategy");
        SMS_PHONE_TID_STRATEGY = baseConfig.getIntValue("tid_phone_strategy");

        /* 如果 tid 总量上限没有指定 默认 10000 */
        if (SMS_WHOLE_TID_REPEAT_LIMIT <= 0) {
            SMS_WHOLE_TID_REPEAT_LIMIT = 10000;
        }
        /* 如果 tid + phone 上限没有指定 默认 10 */
        if (SMS_PHONE_TID_STRATEGY <= 0) {
            SMS_WHOLE_TID_REPEAT_LIMIT = 10;
        }
        /* 如果 tid 总量上限策略 只有 0 和 1 （其他值都转换为 1）*/
        if (SMS_WHOLE_TID_STRATEGY != 0) {
            SMS_WHOLE_TID_STRATEGY = 1;
        }
        /* 如果 tid + phone 上限策略 只有 0 和 1 （其他值都转换为 1）*/
        if (SMS_PHONE_TID_STRATEGY != 0) {
            SMS_PHONE_TID_STRATEGY = 1;
        }
    }

    /**
     * 默认 tid 总量上限 和 tid + phone 总量上限控制，默认关闭。
     */
    private static void smsTidLimitDefaultConfigSet() {
        SMS_WHOLE_TID_REPEAT_SWITCH = false;
        SMS_PHONE_TID_REPEAT_SWITCH = false;
        smsWholeTidCustoms.clear();
        smsPhoneTidCustoms.clear();
    }
}
