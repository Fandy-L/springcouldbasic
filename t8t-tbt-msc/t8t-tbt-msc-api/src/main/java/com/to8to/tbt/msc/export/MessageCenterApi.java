package com.to8to.tbt.msc.export;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.MsgResult;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Api(value = "消息中心")
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface MessageCenterApi {

    /**
     * 查询短信及APP消息记录
     * @param params
     * @return
     */
    @ApiOperation(value = "查询短信及APP消息记录", response = SearchMessageRecordVO.class)
    @PostMapping(value = "/searchMessageRecord", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<SearchMessageRecordVO> searchMessageRecord(@RequestBody @NotEmpty @Valid List<@NotNull SearchMessageRecordDTO> params);

    /**
     * 查询消息记录
     * @param params
     * @return
     */
    @ApiOperation(value = "查询消息记录", response = ListMsgVO.class)
    @PostMapping(value = "/listMsg", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<ListMsgVO> listMsg(@RequestBody @NotEmpty @Valid List<@NotNull ListMsgDTO> params);

    /**
     * 设置消息已读
     * @param params
     * @return
     */
    @ApiOperation(value = "设置消息已读", response = ResResult.class)
    @PostMapping(value = "/setAppMsgReaded", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setAppMsgRead(@RequestBody @NotEmpty @Valid List<@Min(value = 0) @NotNull Integer> params);

    /**
     * 批量设置消息已读-根据消息ID
     * @param params
     * @return
     */
    @ApiOperation(value = "批量设置消息已读-根据消息ID", response = ResResult.class)
    @PostMapping(value = "/setAppMsgReadedBatch", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setAppMsgReadBatch(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);
    /**
     * 批量设置消息已读-根据用户ID及节点ID
     * @param params
     * @return
     */
    @ApiOperation(value = "批量设置消息已读-根据用户ID及节点ID", response = ResResult.class)
    @PostMapping(value = "/setAppMsgStatusByUidAndNodeId", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgResult> setAppMsgStatusByUidAndNodeId(@RequestBody @NotEmpty List<SetAppMsgStatusNodeDTO> params);

    /**
     * 根据模板及用户设置已读
     * @param params
     * @return
     */
    @ApiOperation(value = "根据模板及用户设置已读", response = ResResult.class)
    @PostMapping(value = "/setAppMsgReadedByTidAndUid", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setAppMsgReadByTidAndUid(@RequestBody @NotEmpty @Valid List<@NotNull SetAppMsgReadByTidUidDTO> params);

    /**
     * 查询该用户消息状态
     * @param params
     * @return
     */
    @ApiOperation(value = "查询该用户消息状态", response = ResResult.class)
    @PostMapping(value = "/getUserMsgState", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<Map<Integer, Boolean>> getUserMsgState(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 设置用户消息已读
     * @param params
     * @return
     */
    @ApiOperation(value = "设置用户消息已读", response = ResResult.class)
    @PostMapping(value = "/setUserMsgReaded", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> setUserMsgRead(@RequestBody @Size(min = 2) JSONArray params);

    /**
     * 根据用户及模板设置消息状态
     * @param params
     * @return
     */
    @ApiOperation(value = "根据用户及模板设置消息状态", response = ResResult.class)
    @PostMapping(value = "/setAppMsgStatusByUidAndTid", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgResult> setAppMsgStatusByUidAndTid(@RequestBody @NotEmpty List<SetAppMsgStatusByTidDTO> params);

    /**
     * 根据批量uid和批量模板id查询APP消息数量
     * @param params
     * @return
     */
    @ApiOperation(value = "根据批量uid和批量模板id查询APP消息数量", response = ResResult.class)
    @PostMapping(value = "/getAppMsgRecordCountByUidAndTid", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgResult> getAppMsgRecordCountByUidAndTid(@RequestBody @NotEmpty @Valid List<@NotNull GetAppMsgRecordCountByTid> params);

    /**
     * 根据批量uid和批量节点id查询APP消息数量
     * @param params
     * @return
     */
    @ApiOperation(value = "根据批量uid和批量节点id查询APP消息数量", response = ResResult.class)
    @PostMapping(value = "/getAppMsgRecordCountByUidAndNodeId", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult getAppMsgRecordCountByUidAndNodeId(@RequestBody @NotEmpty @Valid List<@NotNull GetAppMsgRecordCountByNodeIdDTO> params);

}
