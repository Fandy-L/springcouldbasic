package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.PcMsgRequestDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.export.PcMsgApi;
import com.to8to.tbt.msc.service.PcMsgService;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.PcRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author edmund.yu
 */
@RestController
@Slf4j
public class PcMsgController implements PcMsgApi {

    @Autowired
    private PcMsgService pcMsgService;

    @Override
    public ResResult<MsgCenterResponse> sendPcMsg(@RequestBody List<PcMsgRequestDTO> params){
        return ResUtils.data(pcMsgService.sendPcMsg(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> delMsg(@RequestBody JSONArray params){
        return ResUtils.data(pcMsgService.delMsg(params));
    }

    @Override
    public ResResult<Map<String,Integer>> getUnreadBig(@RequestBody List<String> params){
        return ResUtils.data(pcMsgService.getUnreadBig(params.get(0)));
    }

    @Override
    public ResResult<PcRecordVO> getMsgHuang(@RequestBody JSONArray params){
        return ResUtils.data(pcMsgService.getMsgHuang(params));
    }

    @Override
    public ResResult<MsgCenterResponse> setMsgRead(@RequestBody List<String> params){
        return ResUtils.data(pcMsgService.setMsgRead(params.get(0)));
    }

    @Override
    public ResResult<ListMsgVO> searchMsgBig(@RequestBody JSONArray params){
        return ResUtils.data(pcMsgService.searchMsgBig(params));
    }

    @Override
    public ResResult<MsgCenterResponse> setSmallNeed(@RequestBody JSONArray params) {
        return ResUtils.data(pcMsgService.setSmallNeed(params));
    }

    @Override
    public ResResult<List<Integer>> getUserSmallNeed(@RequestBody List<String> params){
        return ResUtils.data(pcMsgService.getUserSmallNeed(params.get(0)));
    }

}
