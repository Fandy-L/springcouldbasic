package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.export.TemplateV2Api;
import com.to8to.tbt.msc.service.TemplateV2Service;
import com.to8to.tbt.msc.vo.MsgTemplateSearchVO;
import com.to8to.tbt.msc.vo.TemplateGetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class TemplateV2Controller implements TemplateV2Api {

    @Autowired
    private TemplateV2Service templateV2Service;

    @Override
    public ResResult createTemplate(@RequestBody List<CreateTemplateDTO> params) {
        return templateV2Service.createTemplate(params.get(0));
    }

    @Override
    public ResResult updateTemplateByTid(@RequestBody List<UpdateTemplateDTO> params) {
        int id = templateV2Service.updateTemplateByTid(params.get(0));
        return ResUtils.suc(String.valueOf(id));
    }

    @Override
    public ResResult deleteTemplateByTid(@RequestBody List<DeleteTemplateDTO> params) {
        return ResUtils.fail("拒绝删除");
    }

    @Override
    public ResResult<TemplateGetVO> getTemplateByTid(@RequestBody List<TemplateGetDTO> params) {
        return ResUtils.data(templateV2Service.getTemplateByTid(params.get(0)));
    }

    @Override
    public ResResult<MsgTemplateSearchVO> searchMsgTemplate(@RequestBody List<TemplateSearchDTO> params) {
        return ResUtils.data(templateV2Service.searchMsgTemplate(params.get(0)));
    }
}
