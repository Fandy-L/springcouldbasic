package com.to8to.tbt.msc.controller;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.PushMessageServiceWrapper;
import com.to8to.tbt.msc.entity.vo.MessageVO;
import com.to8to.tbt.msc.export.PushMessageApi;
import com.to8to.tbt.msc.service.PushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/23 11:08
 */
@RestController
public class PushMessageController implements PushMessageApi {

    @Autowired
    private PushMessageService pushMessageService;

    @Override
    public ResResult<PageResult<MessageVO>> getMessageList(@RequestBody @Valid PushMessageServiceWrapper.GetListDTO req) {
        return ResUtils.data(pushMessageService.getMessageList(req.getQuery()));
    }
}
