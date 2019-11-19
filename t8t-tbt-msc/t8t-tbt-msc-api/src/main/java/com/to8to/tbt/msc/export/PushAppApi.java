package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.AppServiceWrapper;
import com.to8to.tbt.msc.entity.vo.AppVO;
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
@Api(value = "应用")
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface PushAppApi {
    @ApiOperation(value = "创建应用")
    @PostMapping(value = "/app/createApp")
    ResResult createApp(@RequestBody @Valid AppServiceWrapper.CreateDTO req);

    @ApiOperation(value = "编辑应用")
    @PostMapping(value = "/app/editApp")
    ResResult editApp(@RequestBody @Valid AppServiceWrapper.EditAppDTO req);

    @ApiOperation(value = "更新应用")
    @PostMapping(value = "/app/removeApp")
    ResResult removeApp(@RequestBody @Valid AppServiceWrapper.IdDTO req);

    @ApiOperation(value = "启用应用")
    @PostMapping(value = "/app/enableApp")
    ResResult enableApp(@RequestBody @Valid AppServiceWrapper.IdDTO req);

    @ApiOperation(value = "禁用应用")
    @PostMapping(value = "/app/disableApp")
    ResResult disableApp(@RequestBody @Valid AppServiceWrapper.IdDTO req);

    @ApiOperation(value = "获取应用", response = AppVO.class)
    @PostMapping(value = "/app/getApp")
    ResResult<AppVO> getApp(@RequestBody @Valid AppServiceWrapper.IdDTO req);

    @ApiOperation(value = "获取应用列表", response = AppVO.class, responseContainer = "List")
    @PostMapping(value = "/app/getAppList")
    ResResult<List<AppVO>> getAppList();
}
