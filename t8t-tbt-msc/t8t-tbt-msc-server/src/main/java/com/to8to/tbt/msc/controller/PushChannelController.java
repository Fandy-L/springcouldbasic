package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.ChannelServiceWrapper;
import com.to8to.tbt.msc.entity.vo.ChannelVO;
import com.to8to.tbt.msc.export.PushChannelApi;
import com.to8to.tbt.msc.service.PushChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/23 11:08
 */
@RestController
public class PushChannelController implements PushChannelApi {
    @Autowired
    private PushChannelService pushChannelService;

    @Override
    public ResResult createChannel(@RequestBody @Valid ChannelServiceWrapper.CreateDTO req) {
        pushChannelService.createChannel(req.getChannel());
        return ResUtils.suc();
    }

    @Override
    public ResResult editChannel(@RequestBody @Valid ChannelServiceWrapper.EditDTO req) {
        pushChannelService.editChannel(req.getChannel());
        return ResUtils.suc();
    }

    @Override
    public ResResult removeChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req) {
        pushChannelService.removeChannel(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult enableChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req) {
        pushChannelService.enableChannel(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult disableChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req) {
        pushChannelService.disableChannel(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult<ChannelVO> getChannel(@RequestBody @Valid ChannelServiceWrapper.IdDTO req) {
        return ResUtils.data(pushChannelService.getChannel(req.getId()));
    }

    @Override
    public ResResult<List<ChannelVO>> getChannelList(@RequestBody @Valid ChannelServiceWrapper.AppIdDTO req) {
        return ResUtils.data(pushChannelService.getChannelList(req.getAppId()));
    }
}
