package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.CreateTemplateDTO;
import com.to8to.tbt.msc.dto.TemplateGetDTO;
import com.to8to.tbt.msc.dto.TemplateSearchDTO;
import com.to8to.tbt.msc.dto.UpdateTemplateDTO;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.vo.MsgTemplateSearchVO;
import com.to8to.tbt.msc.vo.TemplateGetVO;

/**
 * @author juntao.guo
 */
public interface TemplateV2Service {
    /**
     * 创建模板
     *
     * @param createTemplateDTO
     */
    ResResult createTemplate(CreateTemplateDTO createTemplateDTO);

    /**
     * 更新模板
     *
     * @param updateTemplateDTO
     */
    int updateTemplateByTid(UpdateTemplateDTO updateTemplateDTO);

    /**
     * 获取单个模板
     *
     * @param templateGetDTO
     * @return
     */
    TemplateGetVO getTemplateByTid(TemplateGetDTO templateGetDTO);

    /**
     * 根据条件搜索模板
     *
     * @param templateSearchDTO
     * @return
     */
    MsgTemplateSearchVO searchMsgTemplate(TemplateSearchDTO templateSearchDTO);

    /**
     * 查询并验证模板状态
     *
     * @param tid
     * @return
     */
    Template getValidTemplate(int tid);

    /**
     * 查询并验证短信模板状态
     *
     * @param tid
     * @param keywords
     * @return
     */
    MessageTemplate getValidSmsTemplate(Integer tid, JSONObject keywords);

    /**
     * 替换模板关键字
     *
     * @param content
     * @param keywords
     * @return
     */
    String replaceKeyword(String content, JSONObject keywords);
}
