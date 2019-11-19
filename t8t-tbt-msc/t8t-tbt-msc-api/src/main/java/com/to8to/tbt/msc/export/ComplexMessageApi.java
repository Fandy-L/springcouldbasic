package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SendAllMessageDTO;
import com.to8to.tbt.msc.dto.SendMsgDTO;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.MsgRecordVO;
import com.to8to.tbt.msc.vo.SendMsgVO;
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
public interface ComplexMessageApi {
    /**
     * 根据手机号码批量查询消息
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "根据手机号码批量查询消息", response = MsgRecordVO.class)
    @PostMapping(value = "/selectMsgByPhone")
    ResResult<ListMsgVO<MsgRecordVO>> selectMsgByPhone(@RequestBody @NotEmpty @Valid List<@NotNull ListMsgDTO> params);

    /**
     * 发送短信及APP消息
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "发送短信及APP消息", response = ResultStatusResponse.class)
    @PostMapping(value = "/sendAllMessage")
    ResResult<ResultStatusResponse> sendAllMessage(@RequestBody @NotEmpty @Valid List<@NotNull SendAllMessageDTO> params);

    /**
     * 旧版综合消息发送
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "旧版综合消息发送", response = SendMsgVO.class)
    @PostMapping(value = "/sendMsg")
    ResResult<SendMsgVO> sendMsg(@RequestBody @NotEmpty @Valid List<@NotNull SendMsgDTO> params);
}
