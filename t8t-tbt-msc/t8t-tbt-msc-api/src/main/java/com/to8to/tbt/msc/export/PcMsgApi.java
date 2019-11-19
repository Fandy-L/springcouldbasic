package com.to8to.tbt.msc.export;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.PcMsgRequestDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.PcRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @author edmund.yu
 */
@Api(value = "PC消息")
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface PcMsgApi {

    /**
     * 发送PC消息
     * @param params
     * @return
     */
    @ApiOperation(value = "发送PC消息", response = MsgCenterResponse.class)
    @PostMapping(value = "sendPCMsg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> sendPcMsg(@RequestBody @NotEmpty @Valid List<@NotNull PcMsgRequestDTO> params);

    /**
     * 删除消息
     * @param params
     * @return
     */
    @ApiOperation(value = "删除消息", response = MsgCenterResponse.class)
    @PostMapping(value = "delMsg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> delMsg(@RequestBody @Size(min = 2,message = "参数长度不匹配") JSONArray params);

    /**
     * 获取用户未读数
     * @param params
     * @return
     */
    @ApiOperation(value = "获取用户未读数量", response = Map.class)
    @PostMapping(value = "getUnreadBig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<Map<String, Integer>> getUnreadBig(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 获取小黄条信息
     * @param params
     * @return
     */
    @ApiOperation(value = "获取小黄条消息", response = PcRecordVO.class)
    @PostMapping(value = "getMsgHuang", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<PcRecordVO> getMsgHuang(@RequestBody @Size(min = 2,message = "参数长度不匹配") JSONArray params);

    /**
     * 设置消息已读
     * @param params
     * @return
     */
    @ApiOperation(value = "设置消息已读", response = MsgCenterResponse.class)
    @PostMapping(value = "setMsgReaded", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setMsgRead(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 搜索PC消息中的莫个大类消息
     * @param params
     * @return
     */
    @ApiOperation(value = "搜索PC消息中某个大类的消息", response = ListMsgVO.class)
    @PostMapping(value = "searchMsgBig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<ListMsgVO> searchMsgBig(@RequestBody @Size(min = 2,message = "参数长度不匹配") JSONArray params);

    /**
     * 设置用户不需要接收的消息类别
     * @param params
     * @return
     */
    @ApiOperation(value = "设置用户不需要接受的消息类别", response = MsgCenterResponse.class)
    @PostMapping(value = "setSmallNeed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setSmallNeed(@RequestBody @Size(min = 2,message = "参数长度不匹配") JSONArray params);

    /**
     *获取用户设置不需要读取的小类消息
     * @param params
     * @return
     */
    @ApiOperation(value = "获取用户设置不需要读取的小类信息", response = List.class)
    @PostMapping(value = "getUserSmallNeed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<List<Integer>> getUserSmallNeed(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);
}
