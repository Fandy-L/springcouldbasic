package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.ChannelServiceWrapper;
import com.to8to.tbt.msc.entity.vo.ChannelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/23 13:54
 */
@Api(value = "通道")
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface PushChannelApi {
    @ApiOperation(value = "创建通道")
    @PostMapping(value = "/channel/createChannel")
    ResResult createChannel(@RequestBody @Valid ChannelServiceWrapper.CreateDTO req);

    @ApiOperation(value = "编辑通道")
    @PostMapping(value = "/channel/editChannel")
    ResResult editChannel(@RequestBody @Valid ChannelServiceWrapper.EditDTO req);

    @ApiOperation(value = "删除通道")
    @PostMapping(value = "/channel/removeChannel")
    ResResult removeChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req);

    @ApiOperation(value = "启用通道")
    @PostMapping(value = "/channel/enableChannel")
    ResResult enableChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req);

    @ApiOperation(value = "禁用通道")
    @PostMapping(value = "/channel/disableChannel")
    ResResult disableChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req);

    @ApiOperation(value = "获取通道", response = ChannelVO.class)
    @PostMapping(value = "/channel/getChannel")
    ResResult<ChannelVO> getChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req);

    @ApiOperation(value = "获取通道列表", response = ChannelVO.class, responseContainer = "List")
    @PostMapping(value = "/channel/getChannelList")
    ResResult<List<ChannelVO>> getChannelList(@RequestBody @Valid ChannelServiceWrapper.AppIdDTO req);
}
