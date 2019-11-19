package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.DeviceTokenServiceWrapper;
import com.to8to.tbt.msc.entity.vo.DeviceTokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author pajero.quan
 */
@Api("设备token")
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface DeviceTokenApi {
//    @ApiOperation(value = "更新token")
//    @PostMapping(value = "/token/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    ResResult updateToken(@RequestBody JSONObject req);

    @ApiOperation(value = "查询token", response = String.class)
    @PostMapping(value = "/token/getToken", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<DeviceTokenVO> getToken(@RequestBody @Valid DeviceTokenServiceWrapper.GetDTO req);

//    @ApiOperation(value = "查询token关联的tags", response = String.class)
//    @PostMapping(value = "/token/getTags", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    ResResult<List<String>> getTags(@RequestBody @Valid DeviceTokenServiceWrapper.GetTagsDTO req);
}
