package com.to8to.tbt.msc.service.impl;

import com.to8to.sc.component.redis.operation.AbstractDefaultOperation;
import com.to8to.tbt.msc.dto.AlarmDTO;
import com.to8to.tbt.msc.dto.QywxResult;
import com.to8to.tbt.msc.dto.QywxTextMsgBodyDTO;
import com.to8to.tbt.msc.dto.QywxTextMsgDTO;
import com.to8to.tbt.msc.manager.HttpClientManager;
import com.to8to.tbt.msc.service.AlarmService;
import com.to8to.tbt.msc.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author pajero.quan
 */
@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService {
    @Value("${alarm.url}")
    private String alarmUrl;

    @Value("${alarm.template}")
    private String alarmTemplate;

    @Value("${alarm.max.msg.per.min}")
    private Integer maxMsgCountPerMin;

    @Value("${alarm.enable}")
    private Boolean alarmEnable;

    @Autowired
    private AbstractDefaultOperation<Integer> defaultOperation;

    @Autowired
    private HttpClientManager httpClientManager;

    @Override
    public void sendAlarm(AlarmDTO alarm) {
        if (alarmEnable) {
            int curMinutes = (int) (System.currentTimeMillis() / 1000 / 60);
            String cacheKey = "alarm_count:" + curMinutes;
            Integer curCount = defaultOperation.get(cacheKey);
            if (curCount != null && curCount >= maxMsgCountPerMin) {
                log.warn("超过告警最大频次限制!");
                return;
            }
            defaultOperation.increment(cacheKey, 1L);
            if (curCount == null) {
                //过期时间设置1分钟
                defaultOperation.expire(cacheKey, 1L, TimeUnit.MINUTES);
            }
            QywxTextMsgDTO req = new QywxTextMsgDTO();
            req.setMsgtype("text");
            QywxTextMsgBodyDTO text = new QywxTextMsgBodyDTO();
            text.setContent(String.format(alarmTemplate, IpUtils.getHostIP(), alarm.getModule(), alarm.getMessage()));
            text.setMentioned_list(Collections.singletonList("@all"));
            req.setText(text);

            QywxResult result = httpClientManager.execute(alarmUrl, req, QywxResult.class);
            if (result != null && result.getErrcode() != 0) {
                log.warn("发送告警失败!result=" + result);
            }
        }
    }
}
