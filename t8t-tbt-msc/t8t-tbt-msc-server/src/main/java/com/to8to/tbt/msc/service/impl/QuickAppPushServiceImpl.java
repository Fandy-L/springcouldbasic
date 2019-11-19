package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DozerUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.QuickAppDeviceInfoDTO;
import com.to8to.tbt.msc.entity.MsgCenterResponse;
import com.to8to.tbt.msc.entity.Response;
import com.to8to.tbt.msc.entity.mysql.push.QuickAppDeviceInfo;
import com.to8to.tbt.msc.repository.mysql.push.DeviceInfoRepository;
import com.to8to.tbt.msc.service.QuickAppPushService;
import com.to8to.tbt.msc.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edmund.yu
 */
@Slf4j
@Service
public class QuickAppPushServiceImpl implements QuickAppPushService {

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Override
    public void saveDeviceInfo(QuickAppDeviceInfoDTO params) {

        QuickAppDeviceInfo deviceInfoForSave = DozerUtils.map(params, QuickAppDeviceInfo.class);
        QuickAppDeviceInfo deviceInfoForId = deviceInfoRepository.findByDeviceId(params.getDeviceId()).orElse(null);

        deviceInfoForSave.setId(deviceInfoForId == null ? null : deviceInfoForId.getId());
        deviceInfoForSave.setUpdateTime(TimeUtils.getCurrentTimestampLong());
        //新增生成创建时间
        if (deviceInfoForId == null){
            deviceInfoForSave.setCreatTime(TimeUtils.getCurrentTimestampLong());
        }else{
        //更新对原有创建时间做赋值
            deviceInfoForSave.setCreatTime(deviceInfoForId.getCreatTime());
        }
        deviceInfoRepository.save(deviceInfoForSave);
    }

}
