package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SendAllMessageDTO;
import com.to8to.tbt.msc.dto.SendMsgDTO;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.export.ComplexMessageApi;
import com.to8to.tbt.msc.service.ComplexMessageService;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.MsgRecordVO;
import com.to8to.tbt.msc.vo.SendMsgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class ComplexMessageController implements ComplexMessageApi {

    @Autowired
    private ComplexMessageService complexMessageService;

    @Override
    public ResResult<ListMsgVO<MsgRecordVO>> selectMsgByPhone(@RequestBody List<ListMsgDTO> params) {
        return ResUtils.data(complexMessageService.selectMsgByPhone(params.get(0)));
    }

    @Override
    public ResResult<ResultStatusResponse> sendAllMessage(@RequestBody List<SendAllMessageDTO> params) {
        return complexMessageService.sendAllMessage(params.get(0));
    }

    @Override
    public ResResult<SendMsgVO> sendMsg(@RequestBody List<SendMsgDTO> params) {
        return ResUtils.data(complexMessageService.sendMsg(params.get(0)));
    }
}
