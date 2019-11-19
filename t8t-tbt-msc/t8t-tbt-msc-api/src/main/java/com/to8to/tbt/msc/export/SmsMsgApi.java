package com.to8to.tbt.msc.export;


import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.SendMsgPhoneDTO;
import com.to8to.tbt.msc.dto.SendSmsBatchDTO;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.vo.SendMsgPhoneVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface SmsMsgApi {

    /**
     * 群发短信
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "群发短信", response = ResResult.class)
    @PostMapping(value = "/sendSMSBatch")
    ResResult<ResultStatusResponse<String>> sendSmsBatch(@RequestBody @Valid List<@NotNull SendSmsBatchDTO> params);

    /**
     * 根据批量手机号发送短信
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "根据批量手机号短信", response = SendMsgPhoneVO.class)
    @PostMapping(value = "/sendMsgPhone")
    ResResult<SendMsgPhoneVO> sendMsgPhone(@RequestBody @NotEmpty @Valid List<@NotNull SendMsgPhoneDTO> params);

    /**
     * 根据批量手机号ID发送短信
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "根据批量手机号短信", response = SendMsgPhoneVO.class)
    @PostMapping(value = "/sendMsgPhoneId")
    ResResult<SendMsgPhoneVO> sendMsgPhoneId(@RequestBody @NotEmpty @Valid List<@NotNull SendMsgPhoneDTO> params);
}
