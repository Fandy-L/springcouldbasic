package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.MsgResult;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;
import com.to8to.tbt.msc.export.MessageCenterApi;
import com.to8to.tbt.msc.service.MessageCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@RestController
public class MessageCenterController implements MessageCenterApi {

    @Autowired
    private MessageCenterService messageCenterService;

    @Override
    public ResResult<SearchMessageRecordVO> searchMessageRecord(@RequestBody List<SearchMessageRecordDTO> params) {
        return ResUtils.data(messageCenterService.searchMessageRecord(params.get(0)));
    }

    @Override
    public ResResult<ListMsgVO> listMsg(@RequestBody List<ListMsgDTO> params) {
        return ResUtils.data(messageCenterService.listMsg(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> setAppMsgRead(@RequestBody List<Integer> params) {
        return ResUtils.data(messageCenterService.setAppMsgRead(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> setAppMsgReadBatch(@RequestBody List<String> params) {
        return ResUtils.data(messageCenterService.setAppMsgHasReadBatch(params.get(0)));
    }

    @Override
    public ResResult<MsgResult> setAppMsgStatusByUidAndNodeId(@RequestBody List<SetAppMsgStatusNodeDTO> params) {
        return ResUtils.data(messageCenterService.setAppMsgStatusByUidAndNodeId(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> setAppMsgReadByTidAndUid(@RequestBody List<SetAppMsgReadByTidUidDTO> params) {
        return ResUtils.data(messageCenterService.setAppMsgReaderByTidAndUid(params.get(0).getTids(), params.get(0).getUid()));
    }

    @Override
    public ResResult<Map<Integer, Boolean>> getUserMsgState(@RequestBody List<String> params) {
        return ResUtils.data(messageCenterService.getUserMsgState(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> setUserMsgRead(@RequestBody JSONArray params) {
        return ResUtils.data(messageCenterService.setUserMsgRead(params, false));
    }

    @Override
    public ResResult<MsgResult> setAppMsgStatusByUidAndTid(@RequestBody List<SetAppMsgStatusByTidDTO> params) {
        return ResUtils.data(messageCenterService.setAppMsgStatusByUidAndTid(params.get(0)));
    }

    @Override
    public ResResult<MsgResult> getAppMsgRecordCountByUidAndTid(@RequestBody List<GetAppMsgRecordCountByTid> params) {
        return ResUtils.data(messageCenterService.getAppMsgRecordCountByUidAndTid(params.get(0).getUids(), params.get(0).getTids(), params.get(0).getIsRead()));
    }

    @Override
    public ResResult getAppMsgRecordCountByUidAndNodeId(@RequestBody List<GetAppMsgRecordCountByNodeIdDTO> params) {
        return ResUtils.data(messageCenterService.getAppMsgRecordCountByUidAndNodeId(params.get(0).getUids(), params.get(0).getNodeIds(), params.get(0).getIsRead()));
    }
}
