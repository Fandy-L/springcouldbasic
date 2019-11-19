package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.ListTemplateDTO;
import com.to8to.tbt.msc.dto.MsgTemplateDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.export.TemplateApi;
import com.to8to.tbt.msc.service.TemplateService;
import com.to8to.tbt.msc.vo.ListTemplateVO;
import com.to8to.tbt.msc.vo.MsgTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author edmund.yu
 */
@RestController
public class TemplateController implements TemplateApi {

    @Autowired
    private TemplateService templateService;

    @Override
    public ResResult<MsgCenterResponse> addTemplate(@RequestBody List<MsgTemplateDTO> params){
        return ResUtils.data(templateService.addTemplate(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> updateTemplate(@RequestBody List<MsgTemplateDTO> params){
        return ResUtils.data(templateService.updateTemplate(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> deleteTemplateById(@RequestBody List<String> params){
        return ResUtils.data(templateService.deleteTemplateById(params.get(0)));
    }

    @Override
    public ResResult<List<MsgTemplateVO>> listTemplateByNote(@RequestBody List<Integer> params){
        return ResUtils.data(templateService.listTemplateByNode(params.get(0)));
    }

    @Override
    public ResResult<ListTemplateVO> listTemplate(@RequestBody List<ListTemplateDTO> params){
        return ResUtils.data(templateService.listTemplate(params.get(0)));
    }

    @Override
    public ResResult<MsgTemplateVO> getTemplate(@RequestBody List<String> params){
        return ResUtils.data(templateService.getTemplateById(params.get(0)));
    }
}
