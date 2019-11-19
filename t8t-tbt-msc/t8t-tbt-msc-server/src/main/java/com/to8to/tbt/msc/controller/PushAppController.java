package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.AppServiceWrapper;
import com.to8to.tbt.msc.entity.vo.AppVO;
import com.to8to.tbt.msc.export.PushAppApi;
import com.to8to.tbt.msc.service.PushAppService;
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
public class PushAppController implements PushAppApi {
    @Autowired
    private PushAppService pushAppService;

    @Override
    public ResResult createApp(@RequestBody @Valid AppServiceWrapper.CreateDTO req) {
        pushAppService.createApp(req.getApp());
        return ResUtils.suc();
    }

    @Override
    public ResResult editApp(@RequestBody @Valid AppServiceWrapper.EditAppDTO req) {
        pushAppService.editApp(req.getApp());
        return ResUtils.suc();
    }

    @Override
    public ResResult removeApp(@RequestBody @Valid AppServiceWrapper.IdDTO req) {
        pushAppService.removeApp(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult enableApp(@RequestBody @Valid AppServiceWrapper.IdDTO req) {
        pushAppService.enableApp(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult disableApp(@RequestBody @Valid AppServiceWrapper.IdDTO req) {
        pushAppService.disableApp(req.getId());
        return ResUtils.suc();
    }

    @Override
    public ResResult<AppVO> getApp(@RequestBody @Valid AppServiceWrapper.IdDTO req) {
        return ResUtils.data(pushAppService.getApp(req.getId()));
    }

    @Override
    public ResResult<List<AppVO>> getAppList() {
        return ResUtils.data(pushAppService.getAppList());
    }
}
