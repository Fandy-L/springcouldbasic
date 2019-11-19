package com.to8to.tbt.msc.export;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Api(value = "APP消息中心")
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface AppMsgApi {
    /**
     * 关注消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "关注消息列表", response = AppInteractionMsgVO.class)
    @PostMapping(value = "/app/follow/list")
    ResResult<PageResult<AppInteractionMsgVO>> followList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params);

    /**
     * 互动消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "互动消息列表", response = AppInteractionMsgVO.class)
    @PostMapping(value = "/app/interactive/list")
    ResResult<PageResult<AppInteractionMsgVO>> interactionList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params);

    /**
     * 系统消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "系统消息列表", response = AppSystemMsgVO.class)
    @PostMapping(value = "/app/system/list")
    ResResult<PageResult<AppSystemMsgVO>> systemList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppSystemListDTO params);

    /**
     * 消息聚合页
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "消息聚合页", response = AppAggregationMsgVO.class)
    @PostMapping(value = "/app/msg/main")
    ResResult<AppAggregationMsgVO> aggregationMsg(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgMainDTO params);

    /**
     * 装修进度消息列表页
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "装修进度消息列表页", response = AppProcessMsgVO.class)
    @PostMapping(value = "/app/process/list")
    ResResult<ListMsgVO> processMsgList(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppMsgListDTO params);

    /**
     * 消息设置已读
     *
     * @param uid
     * @param params
     * @return
     */
    @ApiOperation(value = "消息设置已读", response = MsgSetHasReadVO.class)
    @PostMapping(value = "/app/read/clear")
    ResResult<List<MsgSetHasReadVO>> msgSetHasRead(@RequestHeader(value = "rpc-uid", required = false) Integer uid, @RequestBody @Valid AppReadClearDTO params);

    /**
     * 添加用户设备绑定
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "添加用户设备绑定", response = ResResult.class)
    @PostMapping(value = "/addAppUser")
    ResResult addAppUser(@RequestBody @NotEmpty @Valid List<@NotNull UserBindingDTO> params);

    /**
     * 添加用户设备绑定
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "重定向app消息", response = ResResult.class)
    @PostMapping(value = "/redirectAppMsg")
    ResResult redirectAppMsg(@RequestBody @NotEmpty @Valid List<@NotNull RedirectAppMsgDTO> params);

    /**
     * 微信消息发送
     * @param params
     * @return
     */
    @ApiOperation(value = "微信消息发送", response = ResResult.class)
    @PostMapping(value = "/sendWeChatAlarmMsg")
    ResResult<ResultStatusResponse<String>> sendWeChatAlarmMsg(@RequestBody @NotEmpty @Valid List<@NotNull SendWeChatAlarmMsgDTO> params);

}
