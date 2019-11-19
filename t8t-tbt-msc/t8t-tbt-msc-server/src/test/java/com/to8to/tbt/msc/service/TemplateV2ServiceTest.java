package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.CreateTemplateDTO;
import com.to8to.tbt.msc.dto.TemplateGetDTO;
import com.to8to.tbt.msc.dto.TemplateSearchDTO;
import com.to8to.tbt.msc.dto.UpdateTemplateDTO;
import com.to8to.tbt.msc.entity.OldMsgResponse;
import com.to8to.tbt.msc.entity.TemplateSearchItem;
import com.to8to.tbt.msc.entity.TemplateWrapper;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.template.TemplateInfo;
import com.to8to.tbt.msc.enumeration.MsgSendTypeEnum;
import com.to8to.tbt.msc.repository.mysql.main.AppTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.MessageTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.MsgTemplateSearchVO;
import com.to8to.tbt.msc.vo.TemplateGetVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class TemplateV2ServiceTest extends BaseApplication {

    @Autowired
    private TemplateV2Service templateV2Service;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    /**
     * 短信模板查询-根据城市
     */
    @Test
    public void searchMsgTemplateBySmsCityId(){
        int isLikeGround = 1;
        Long cityId = 110100000000L;
        TemplateSearchDTO templateSearchDTO = new TemplateSearchDTO();
        templateSearchDTO.setSendType(MsgSendTypeEnum.SMS.getCode());
        templateSearchDTO.setCityId(cityId);
        MsgTemplateSearchVO msgTemplateSearchVO = templateV2Service.searchMsgTemplate(templateSearchDTO);
        log.debug("size:{} list:{}", msgTemplateSearchVO.getTotal(), msgTemplateSearchVO.getResult());
        Assert.assertTrue(msgTemplateSearchVO.getTotal() > 0L);
        for (TemplateSearchItem templateSearchItem : msgTemplateSearchVO.getResult()){
            Assert.assertTrue(templateSearchItem.getIsGround() == isLikeGround || templateSearchItem.getIsGround() == 3 || templateSearchItem.getCityIds().indexOf(String.valueOf(cityId)) >= 0);
        }
    }

    /**
     * 短信模板查询-综合查询
     */
    @Test
    public void searchMsgTemplateBySmsComplex(){
        int configType = 2;
        int cid = 199;
        int isGround = 1;
        int channelType = 4;
        int pmModule = 174;
        int nodeId = 206;
        TemplateSearchDTO templateSearchDTO = new TemplateSearchDTO();
        templateSearchDTO.setSendType(MsgSendTypeEnum.SMS.getCode());
        templateSearchDTO.setConfigType(configType);
        templateSearchDTO.setCid(cid);
        templateSearchDTO.setIsGround(isGround);
        templateSearchDTO.setChannelType(channelType);
        templateSearchDTO.setPmModule(pmModule);
        templateSearchDTO.setNodeId(nodeId);
        MsgTemplateSearchVO msgTemplateSearchVO = templateV2Service.searchMsgTemplate(templateSearchDTO);
        log.debug("size:{} list:{}", msgTemplateSearchVO.getTotal(), msgTemplateSearchVO.getResult());
        Assert.assertTrue(msgTemplateSearchVO.getTotal() > 0L);
        for (TemplateSearchItem templateSearchItem : msgTemplateSearchVO.getResult()){
            Assert.assertTrue(templateSearchItem.getIsGround() == isGround || templateSearchItem.getPmModule() == pmModule || templateSearchItem.getNodeId() == nodeId);
            Assert.assertTrue(templateSearchItem.getChannelType() == channelType);
        }
    }

    /**
     * APP模板查询
     */
    @Test
    public void searchMsgTemplateByApp(){
        int configType = 3;
        int cid = 410;
        int appId = 2;
        int pmModule = 413;
        int nodeId = 410;
        TemplateSearchDTO templateSearchDTO = TemplateSearchDTO.builder()
                .sendType(MsgSendTypeEnum.APP.getCode())
                .configType(configType)
                .cid(cid)
                .appId(appId)
                .pmModule(pmModule)
                .nodeId(nodeId)
                .build();
        MsgTemplateSearchVO msgTemplateSearchVO = templateV2Service.searchMsgTemplate(templateSearchDTO);
        log.debug("size:{} list:{}", msgTemplateSearchVO.getTotal(), msgTemplateSearchVO.getResult());
        Assert.assertTrue(msgTemplateSearchVO.getTotal() > 0L);
        for (TemplateSearchItem templateSearchItem : msgTemplateSearchVO.getResult()){
            Assert.assertTrue(templateSearchItem.getAppId() == appId || templateSearchItem.getPmModule() == pmModule || templateSearchItem.getNodeId() == nodeId);
        }
    }

    @Test
    public void getTemplateByTidBySms(){
        int tid = 1174;
        TemplateGetDTO templateGetDTO = TemplateGetDTO.builder()
                .tid(tid)
                .build();
        TemplateGetVO<MessageTemplate> templateGetVO = templateV2Service.getTemplateByTid(templateGetDTO);
        log.debug("templateGetVO:{}", templateGetVO);
        TemplateInfo<MessageTemplate> templateInfo = templateGetVO.getResult();
        Assert.assertTrue(templateInfo.getSendType() == MsgSendTypeEnum.SMS.getCode());
    }

    @Test
    public void getTemplateByTidByApp(){
        int tid = 1180;
        TemplateGetDTO templateGetDTO = TemplateGetDTO.builder()
                .tid(tid)
                .build();
        TemplateGetVO<MessageTemplate> templateGetVO = templateV2Service.getTemplateByTid(templateGetDTO);
        TemplateInfo<MessageTemplate> templateInfo = templateGetVO.getResult();
        log.debug("templateGetVO:{}", templateGetVO);
        Assert.assertTrue(templateInfo.getSendType() == MsgSendTypeEnum.APP.getCode());
        Assert.assertTrue(templateInfo.getTid() > 0);
    }

    @Test
    public void createTemplateBySms(){
        int sendType = 1;
        String msgContent = "亲，你有新的量房项目，{time}，请及时登入土巴兔后台查询！";
        int needIpLimit = 1;
        int ipLimitNum = 10;
        int nodeId = 51;
        String title = "轮单短信息" + TimeUtils.getCurrentTimestamp();
        int isActive = 1;
        int isAuto = 2;
        int targetType = 5;
        int pmModule = 6;
        String msgRemark = "备注信息";
        int createId = 20930;
        int isGround = 3;
        int channelType = 4;
        String cityIds = "11,110100000000,";
        CreateTemplateDTO createTemplateDTO = CreateTemplateDTO.builder()
                .sendType(sendType)
                .nodeId(nodeId)
                .title(title)
                .isActive(isActive)
                .isAuto(isAuto)
                .targetType(targetType)
                .pmModule(String.valueOf(pmModule))
                .msgRemark(msgRemark)
                .createId(createId)
                .msgContent(msgContent)
                .isGround(isGround)
                .channelType(channelType)
                .needIpLimit(needIpLimit)
                .ipLimitNum(ipLimitNum)
                .cityIds(cityIds)
                .build();
        ResResult<OldMsgResponse> resResult = templateV2Service.createTemplate(createTemplateDTO);
        log.debug("resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)){
            Template template = templateRepository.findFirstByNodeIdOrderByCreateTimeDesc(nodeId).orElse(null);
            log.debug("template:{}", template);
            Assert.assertTrue(template != null);
            Assert.assertTrue(template.getSendType() == sendType
                    && template.getNodeId() == nodeId
                    && template.getTitle().equals(title)
                    && template.getIsActive() == isActive
                    && template.getIsAuto() == isAuto
                    && template.getTargetType() == targetType
                    && template.getPmModule().equals(String.valueOf(pmModule))
                    && template.getMsgRemark().equals(msgRemark)
                    && template.getCreateId() == createId);
            MessageTemplate messageTemplate = messageTemplateRepository.findByTid(template.getTid()).orElse(null);
            Assert.assertTrue(messageTemplate != null);
            Assert.assertTrue(messageTemplate.getMsgContent().equals(msgContent)
                    && messageTemplate.getIsGround() == isGround
                    && messageTemplate.getChannelType() == channelType
                    && messageTemplate.getNeedIpLimit() == needIpLimit
                    && messageTemplate.getIpLimitNum() == ipLimitNum
                    && messageTemplate.getCityIds().equals(cityIds));
        }
    }

    @Test
    public void createTemplateByApp(){
        int sendType = 4;
        int nodeId = 3;
        String title = "订金退款" + TimeUtils.getCurrentTimestamp();
        int isActive = 1;
        int isAuto = 1;
        int targetType = 4;
        int pmModule = 5;
        String msgRemark = "备注信息";
        int createId = 20923;
        String appContent = "您申请的订金退款没有通过审核，请到订金管理查看原因";
        int appId = 15;
        int needPush = 1;
        int pushType = 1;
        int pushScope = 2;
        CreateTemplateDTO createTemplateDTO = CreateTemplateDTO.builder()
                .sendType(sendType)
                .nodeId(nodeId)
                .title(title)
                .isActive(isActive)
                .isAuto(isAuto)
                .targetType(targetType)
                .pmModule(String.valueOf(pmModule))
                .msgRemark(msgRemark)
                .createId(createId)
                .msgContent(appContent)
                .appId(appId)
                .needPush(needPush)
                .pushType(pushType)
                .pushScope(pushScope)
                .build();
        ResResult<OldMsgResponse> resResult = templateV2Service.createTemplate(createTemplateDTO);
        log.debug("resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)){
            Template template = templateRepository.findFirstByNodeIdOrderByCreateTimeDesc(nodeId).orElse(null);
            log.debug("template:{}", template);
            Assert.assertTrue(template != null);
            Assert.assertTrue(template.getSendType() == sendType
                    && template.getNodeId() == nodeId
                    && template.getTitle().equals(title)
                    && template.getIsActive() == isActive
                    && template.getIsAuto() == isAuto
                    && template.getTargetType() == targetType
                    && template.getPmModule().equals(String.valueOf(pmModule))
                    && template.getMsgRemark().equals(msgRemark)
                    && template.getCreateId() == createId);
            AppTemplate appTemplate = appTemplateRepository.findByTid(template.getTid()).orElse(null);
            Assert.assertTrue(appTemplate != null);
            Assert.assertTrue(appTemplate.getAppContent().equals(appContent)
                    && appTemplate.getAppId() == appId
                    && appTemplate.getNeedPush() == needPush
                    && appTemplate.getPushType() == pushType
                    && appTemplate.getPushScope() == pushScope);
        }
    }

    @Test
    public void updateTemplateBySms(){
        int tid = 20006;
        int sendType = 1;
        int isActive = 2;
        int isAuto = 1;
        int modifyId = 20930;
        int needIpLimit = 0;
        TemplateWrapper.Template templateVO = TemplateWrapper.Template.builder()
                .isActive(isActive)
                .isAuto(isAuto)
                .modifyId(modifyId)
                .build();
        TemplateWrapper.MsgTemplate msgTemplate = TemplateWrapper.MsgTemplate.builder()
                .needIpLimit(needIpLimit)
                .build();
        UpdateTemplateDTO updateTemplateDTO = UpdateTemplateDTO.builder()
                .tid(tid)
                .type(sendType)
                .common(templateVO)
                .msg(msgTemplate)
                .build();
        int id = templateV2Service.updateTemplateByTid(updateTemplateDTO);
        log.debug("id:{}", id);
        if (id > 0){
            Template template = templateRepository.findById(tid).orElse(null);
            Assert.assertTrue(template.getIsActive() == isActive && template.getIsAuto() == isAuto && template.getModifyId() == modifyId);
        }
    }

    @Test
    public void updateTemplateApp(){
        int tid = 20001;
        int sendType = 4;
        int needPush = 0;
        TemplateWrapper.MsgTemplate msgTemplate = TemplateWrapper.MsgTemplate.builder()
                .needPush(needPush)
                .build();
        UpdateTemplateDTO updateTemplateDTO = UpdateTemplateDTO.builder()
                .tid(tid)
                .type(sendType)
                .msg(msgTemplate)
                .build();
        int id = templateV2Service.updateTemplateByTid(updateTemplateDTO);
        log.debug("id:{}", id);
        if (id > 0){
            AppTemplate appTemplate = appTemplateRepository.findByTid(tid).orElse(null);
            Assert.assertTrue(appTemplate.getNeedPush() == needPush);
        }
    }
}
