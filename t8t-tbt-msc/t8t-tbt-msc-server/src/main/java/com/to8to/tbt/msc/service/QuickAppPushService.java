package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.QuickAppDeviceInfoDTO;
import com.to8to.tbt.msc.entity.MsgCenterResponse;

/**
 * @author edmund.yu
 */
public interface QuickAppPushService {

    /**
     * 存储设备信息
     * @param params
     * @return
     */
    void saveDeviceInfo(QuickAppDeviceInfoDTO params);
}
