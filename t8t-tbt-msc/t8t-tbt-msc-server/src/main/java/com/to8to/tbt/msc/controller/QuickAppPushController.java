package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.QuickAppDeviceInfoDTO;
import com.to8to.tbt.msc.entity.MsgCenterResponse;
import com.to8to.tbt.msc.export.QuickAppPushApi;
import com.to8to.tbt.msc.service.QuickAppPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author edmund.yu
 */
@RestController
public class QuickAppPushController implements QuickAppPushApi {

    @Autowired
    private QuickAppPushService quickAppPushService;

    @Override
    public ResResult saveDeviceInfo(@RequestBody QuickAppDeviceInfoDTO params) {
        quickAppPushService.saveDeviceInfo(params);
        return ResUtils.suc();
    }
}
