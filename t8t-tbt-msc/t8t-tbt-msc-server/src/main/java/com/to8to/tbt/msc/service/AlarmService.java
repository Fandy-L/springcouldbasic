package com.to8to.tbt.msc.service;


import com.to8to.tbt.msc.dto.AlarmDTO;

/**
 * @author pajero.quan
 */
public interface AlarmService {
    /**
     * 发送告警
     * @param alarm
     */
    void sendAlarm(AlarmDTO alarm);
}
