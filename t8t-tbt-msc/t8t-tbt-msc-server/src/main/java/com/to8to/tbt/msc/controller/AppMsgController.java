package com.to8to.tbt.msc.controller;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.export.AppMsgApi;
import com.to8to.tbt.msc.service.AppMsgSendService;
import com.to8to.tbt.msc.service.AppMsgService;
import com.to8to.tbt.msc.service.DeviceTokenService;
import com.to8to.tbt.msc.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RestController
public class AppMsgController implements AppMsgApi {

    @Autowired
    private AppMsgService appMsgService;

    @Autowired
    private AppMsgSendService appMsgSendService;

    @Autowired
    private DeviceTokenService deviceTokenService;

    @Override
    public ResResult<PageResult<AppInteractionMsgVO>> followList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params) {
        return ResUtils.data(appMsgService.queryFollowList(uid, params));
    }

    @Override
    public ResResult<PageResult<AppInteractionMsgVO>> interactionList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params) {
        return ResUtils.data(appMsgService.queryInteractionList(uid, params));
    }

    @Override
    public ResResult<PageResult<AppSystemMsgVO>> systemList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppSystemListDTO params) {
        return ResUtils.data(appMsgService.aggregationSystemList(uid, params));
    }

    @Override
    public ResResult<AppAggregationMsgVO> aggregationMsg(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgMainDTO params) {
        return ResUtils.data(appMsgService.aggregationMsg(uid, params));
    }

    @Override
    public ResResult<ListMsgVO> processMsgList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params) {
        return ResUtils.data(appMsgService.queryProcessList(uid, params));
    }

    @Override
    public ResResult<List<MsgSetHasReadVO>> msgSetHasRead(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppReadClearDTO params) {
        return ResUtils.data(appMsgService.msgSetHasRead(uid, params));
    }

    @Override
    public ResResult addAppUser(@RequestBody List<UserBindingDTO> params) {
        deviceTokenService.updateToken(params.get(0));
        return ResUtils.data(1);
    }

    @Override
    public ResResult redirectAppMsg(@RequestBody List<RedirectAppMsgDTO> params) {
        return ResUtils.data(appMsgSendService.sendAppMsgV2(params.get(0)));
    }

    @Override
    public ResResult<ResultStatusResponse<String>> sendWeChatAlarmMsg(@RequestBody List<SendWeChatAlarmMsgDTO> params) {
        return appMsgService.sendWeChatAlarmMsg(params.get(0));
    }
}
