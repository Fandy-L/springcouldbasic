package com.to8to.tbt.msc.export;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.QuickAppDeviceInfoDTO;
import com.to8to.tbt.msc.entity.MsgCenterResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author edmund.yu
 */

@Api(value = "快应用push")
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
@Validated
public interface QuickAppPushApi {

    /**
     * 存储设备信息
     * @param params
     * @return
     */
    @ApiOperation(value = "存储设备信息", response = PageResult.class)
    @PostMapping(value = "/saveDeviceInfo")
    ResResult saveDeviceInfo(@RequestBody @Valid QuickAppDeviceInfoDTO params);

}
