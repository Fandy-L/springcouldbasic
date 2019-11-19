package com.to8to.tbt.msc.export;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.PushMessageServiceWrapper;
import com.to8to.tbt.msc.entity.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/23 13:55
 */
@Api(value = "Push消息")
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface PushMessageApi {
    @ApiOperation(value = "查询消息列表", response = PageResult.class)
    @PostMapping(value = "/message/getMessageList")
    ResResult<PageResult<MessageVO>> getMessageList(@RequestBody @Valid PushMessageServiceWrapper.GetListDTO req);
}
