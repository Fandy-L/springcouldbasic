package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.DeviceTokenServiceWrapper;
import com.to8to.tbt.msc.entity.vo.DeviceTokenVO;
import com.to8to.tbt.msc.export.DeviceTokenApi;
import com.to8to.tbt.msc.service.ChannelDeviceTokenService;
import com.to8to.tbt.msc.service.DeviceTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.to8to.tbt.msc.common.Constants.HTTP_RESP_OK;

@RestController
public class DeviceTokenController implements DeviceTokenApi {
    @Autowired
    private DeviceTokenService deviceTokenService;

    @Autowired
    @Qualifier("jiguangDeviceTokenService")
    private ChannelDeviceTokenService channelDeviceTokenService;

//    @Override
//    public ResResult updateToken(@RequestBody JSONObject req) {
//        String response = deviceTokenService.updateToken(req);
//        JSONObject jsonObject = JSON.parseObject(response);
//        int status = Integer.parseInt(jsonObject.getString("status"));
//        if (status == HTTP_RESP_OK) {
//            return ResUtils.suc();
//        } else {
//            return ResUtils.fail(jsonObject.getString("msg"));
//        }
//    }

    @Override
    public ResResult<DeviceTokenVO> getToken(@RequestBody DeviceTokenServiceWrapper.@Valid GetDTO req) {
        return ResUtils.data(deviceTokenService.queryToken(req.getQuery()));
    }

//    @Override
//    public ResResult<List<String>> getTags(@RequestBody DeviceTokenServiceWrapper.@Valid GetTagsDTO req) {
//        List<String> tags = channelDeviceTokenService.getTags(req.getPushAppId(), req.getDeviceToken());
//        return ResUtils.data(tags);
//    }
}
