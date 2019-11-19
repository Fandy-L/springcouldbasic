package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.SendMsgPhoneDTO;
import com.to8to.tbt.msc.dto.SendSmsBatchDTO;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.export.SmsMsgApi;
import com.to8to.tbt.msc.service.SmsMsgService;
import com.to8to.tbt.msc.vo.SendMsgPhoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class SmsMsgController implements SmsMsgApi {

    @Autowired
    private SmsMsgService smsMsgService;

    @Override
    public ResResult<ResultStatusResponse<String>> sendSmsBatch(@RequestBody List<SendSmsBatchDTO> params) {
        return smsMsgService.sendSmsBatch(params.get(0));
    }

    @Override
    public ResResult<SendMsgPhoneVO> sendMsgPhone(@RequestBody List<SendMsgPhoneDTO> params) {
        return smsMsgService.sendMsgPhone(params.get(0));
    }

    @Override
    public ResResult<SendMsgPhoneVO> sendMsgPhoneId(@RequestBody List<SendMsgPhoneDTO> params) {
        return smsMsgService.sendMsgPhoneId(params.get(0));
    }
}
