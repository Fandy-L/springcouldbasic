package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.ListTemplateDTO;
import com.to8to.tbt.msc.dto.MsgTemplateDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.ListTemplateVO;
import com.to8to.tbt.msc.vo.MsgTemplateVO;

import java.util.List;

/**
 * @author edmund.yu
 */
public interface TemplateService {

    /**
     *增加模板
     * @param msgTemplateDTO
     * @return
     */
    MsgCenterResponse addTemplate(MsgTemplateDTO msgTemplateDTO);

    /**
     * 更新模板
     * @param msgTemplateDTO
     * @return
     */
    MsgCenterResponse updateTemplate(MsgTemplateDTO msgTemplateDTO);

    /**
     * 删除模板
     * @param id
     * @return
     */
    MsgCenterResponse deleteTemplateById(String id);

    /**
     * 根据node_id获取模板
     * @param nodeId
     * @return
     */
    List<MsgTemplateVO> listTemplateByNode(Integer nodeId);

    /**
     * 获取模板
     * @param listTemplateDTO
     * @return
     */
    ListTemplateVO listTemplate(ListTemplateDTO listTemplateDTO);

    /**
     * 根据id获取模板
     * @param id
     * @return
     */
    MsgTemplateVO getTemplateById(String id);

}
