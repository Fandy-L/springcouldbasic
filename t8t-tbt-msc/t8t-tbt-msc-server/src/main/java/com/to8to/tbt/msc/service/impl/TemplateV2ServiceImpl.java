package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.to8to.common.util.DozerUtils;
import com.to8to.dst.dto.LocalDistrictServiceWrapper;
import com.to8to.dst.entity.vo.LocalTownVO;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.CreateTemplateDTO;
import com.to8to.tbt.msc.dto.TemplateGetDTO;
import com.to8to.tbt.msc.dto.TemplateSearchDTO;
import com.to8to.tbt.msc.dto.UpdateTemplateDTO;
import com.to8to.tbt.msc.entity.ConfigureWrapper;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.TemplateSearchItem;
import com.to8to.tbt.msc.entity.TemplateWrapper;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Configure;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.entity.template.TemplateInfo;
import com.to8to.tbt.msc.repository.mysql.main.AppTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.ConfigureRepository;
import com.to8to.tbt.msc.repository.mysql.main.MessageTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.service.ConfigureService;
import com.to8to.tbt.msc.service.ExternalService;
import com.to8to.tbt.msc.service.KeywordV2Service;
import com.to8to.tbt.msc.service.TemplateV2Service;
import com.to8to.tbt.msc.service.external.DstService;
import com.to8to.tbt.msc.utils.*;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.enumeration.MsgSendTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class TemplateV2ServiceImpl implements TemplateV2Service {

    /**
     * 模板关键词的超始字符
     */
    private static final String KEYWORD_START_CHARACTER = "{";
    private static final String KEYWORD_END_CHARACTER = "}";
    /**
     * 关键词替换的正则表达式
     */
    private static final Pattern KEYWORD_REPLACE_PATTERN = Pattern.compile("\\{([^#>;}]+)\\}");

    @Autowired
    private KeywordV2Service keywordV2Service;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Autowired
    private ConfigureRepository configureRepository;

    @Autowired
    private DstService dstService;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private ConfigureService configureService;

    @Override
    public ResResult createTemplate(CreateTemplateDTO createTemplateDTO) {
        if (createTemplateDTO.getSendType() != MsgSendTypeEnum.APP.getCode() && createTemplateDTO.getSendType() != MsgSendTypeEnum.SMS.getCode()) {
            return ResponseUtils.fail(MyExceptionStatus.TEMPLATE_CREATE_SEND_TYPE_INVALID.getMessage());
        }
        // 对模板内容进行检测是否存在关键字
        if (!keywordV2Service.scanKeyword(createTemplateDTO.getMsgContent())) {
            return ResponseUtils.fail(MyExceptionStatus.KEYWORD_NOT_FOUND.getMessage());
        }
        // 创建公共模板
        Template template;
        try {
            template = DozerUtils.map(createTemplateDTO, Template.class);
            template.setMsgRemark(StringUtils.defaultString(template.getMsgRemark()));
            template.setCreateTime(TimeUtils.getCurrentTimestamp());
            template = templateRepository.save(template);
        } catch (Exception e) {
            log.warn(LogUtils.buildTemplate("createTemplateDTO"), createTemplateDTO, e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
        if (template.getTid() > 0) {
            if (IntegerUtils.isEqLimitValue(createTemplateDTO.getNeedIpLimit())) {
                createTemplateDTO.setIpLimitNum(0);
            } else {
                createTemplateDTO.setIpLimitNum(IntegerUtils.intValueAsDefault(createTemplateDTO.getIpLimitNum()));
            }
            if (createTemplateDTO.getSendType().equals(MsgSendTypeEnum.SMS.getCode())) {
                // 创建短信模板
                MessageTemplate messageTemplate = null;
                try {
                    messageTemplate = DozerUtils.map(createTemplateDTO, MessageTemplate.class);
                    messageTemplate.setTid(template.getTid());
                    messageTemplate = messageTemplateRepository.save(messageTemplate);
                } catch (Exception e) {
                    log.warn("[createTemplate] messageTemplateSave exception messageTemplate:{} error：{}", messageTemplate, e);
                }
                if (IntegerUtils.isEqLimitValue(messageTemplate.getId())) {
                    log.warn("createTemplate messageTemplateSave fail messageTemplate:{}", messageTemplate);
                } else {
                    return ResUtils.data(Response.TEMPLATE_CREATE_SUCCESS);
                }
            } else if (createTemplateDTO.getSendType().equals(MsgSendTypeEnum.APP.getCode())) {
                AppTemplate appTemplate = null;
                try {
                    appTemplate = DozerUtils.map(createTemplateDTO, AppTemplate.class);
                    appTemplate.setAppContent(createTemplateDTO.getMsgContent());
                    appTemplate.setTid(template.getTid());
                    appTemplate.setCreateTime(TimeUtils.getCurrentTimestamp());
                    appTemplate = appTemplateRepository.save(appTemplate);
                } catch (Exception e) {
                    log.warn("createTemplate appTemplateSave exception appTemplate:{} e:{}", appTemplate, e);
                }
                if (IntegerUtils.isEqLimitValue(appTemplate.getId())) {
                    log.warn("createTemplate appTemplateSave fail appTemplate:{}", appTemplate);
                } else {
                    return ResUtils.data(Response.TEMPLATE_CREATE_SUCCESS);
                }
            }
        }
        ResultStatusResponse<String> resultStatusResponse = Response.TEMPLATE_CREATE_FAIL;
        ResResult<ResultStatusResponse<String>> resResult = ResUtils.fail(resultStatusResponse.getStatus());
        resResult.setData(resultStatusResponse);
        return resResult;
    }

    @Override
    public int updateTemplateByTid(UpdateTemplateDTO updateTemplateDTO) {
        try {
            if (updateTemplateDTO.getCommon() != null) {
                Template template = templateRepository.findById(updateTemplateDTO.getTid()).orElse(null);
                if (template == null) {
                    return 0;
                }
                DozerUtils.map(updateTemplateDTO.getCommon(), template);
                template.setModifyTime(TimeUtils.getCurrentTimestamp());
                templateRepository.save(template);
            }
            if (updateTemplateDTO.getMsg() != null) {
                TemplateWrapper.MsgTemplate msg = updateTemplateDTO.getMsg();
                if (StringUtils.isNotBlank(msg.getMsgContent())) {
                    boolean checkState = keywordV2Service.scanKeyword(msg.getMsgContent());
                    if (!checkState) {
                        return 0;
                    }
                }
                if (IntegerUtils.isEqLimitValue(updateTemplateDTO.getType(), MsgSendTypeEnum.SMS.getCode())) {
                    MessageTemplate messageTemplate = messageTemplateRepository.findByTid(updateTemplateDTO.getTid()).orElse(null);
                    if (messageTemplate != null) {
                        DozerUtils.map(msg, messageTemplate);
                        messageTemplateRepository.save(messageTemplate);
                        return messageTemplate.getId();
                    }
                } else if (IntegerUtils.isEqLimitValue(updateTemplateDTO.getType(), MsgSendTypeEnum.APP.getCode())) {
                    AppTemplate appTemplate = appTemplateRepository.findByTid(updateTemplateDTO.getTid()).orElse(null);
                    if (appTemplate != null) {
                        DozerUtils.map(msg, appTemplate);
                        if (StringUtils.isNotBlank(msg.getMsgContent())){
                            appTemplate.setAppContent(msg.getMsgContent());
                        }
                        appTemplateRepository.save(appTemplate);
                        return appTemplate.getId();
                    }
                }
            }
        } catch (Exception e) {
            log.error("updateTemplateByTid exception updateTemplateDTO:{} e:{}", updateTemplateDTO, e);
        }
        return 0;
    }

    @Override
    public TemplateGetVO getTemplateByTid(TemplateGetDTO templateGetDTO) {
        Template template = getCommonTemplateByTid(templateGetDTO.getTid());
        if (null == template) {
            log.warn("getTemplateByTid params invalid tid:{}", templateGetDTO.getTid());
            return null;
        }
        //短信模板
        if (IntegerUtils.isEqLimitValue(template.getSendType(), MsgSendTypeEnum.SMS.getCode())) {
            TemplateInfo<MessageTemplateVO> templateInfo = DozerUtils.map(template, TemplateInfo.class);
            MessageTemplate messageTemplate = getMessageTemplateByTid(templateGetDTO.getTid());
            templateInfo.setType(MsgSendTypeEnum.SMS.getCode());
            MessageTemplateVO messageTemplateVO = DozerUtils.map(messageTemplate, MessageTemplateVO.class);
            messageTemplateVO.setMsgContent(StringUtils.defaultString(messageTemplate.getMsgContent()));
            messageTemplateVO.setCityIds(StringUtils.defaultString(messageTemplate.getCityIds()));
            templateInfo.setMsgTemplate(messageTemplateVO);
            return TemplateGetVO.<MessageTemplateVO>builder()
                    .result(templateInfo)
                    .build();
        } else if (IntegerUtils.isEqLimitValue(template.getSendType(), MsgSendTypeEnum.APP.getCode())) {
            TemplateInfo<AppTemplateVO> templateInfo = DozerUtils.map(template, TemplateInfo.class);
            AppTemplate appTemplate = getAppTemplateByTid(templateGetDTO.getTid());
            templateInfo.setType(MsgSendTypeEnum.APP.getCode());
            templateInfo.setMsgTemplate(DozerUtils.map(appTemplate, AppTemplateVO.class));
            return TemplateGetVO.<AppTemplateVO>builder()
                    .result(templateInfo)
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public MsgTemplateSearchVO searchMsgTemplate(TemplateSearchDTO templateSearchDTO) {
        MsgTemplateSearchVO msgTemplateSearchVO = null;
        templateSearchDTO.setPageInfo(RequestUtils.filterPageInfo(templateSearchDTO.getPageInfo()));
        templateSearchDTO.setSendType(IntegerUtils.intValueAsDefault(templateSearchDTO.getSendType(), MsgSendTypeEnum.SMS.getCode()));
        if (IntegerUtils.isEqLimitValue(templateSearchDTO.getSendType(), MsgSendTypeEnum.SMS.getCode())) {
            msgTemplateSearchVO = queryMessageTemplate(templateSearchDTO);
        } else if (IntegerUtils.isEqLimitValue(templateSearchDTO.getSendType(), MsgSendTypeEnum.APP.getCode())) {
            msgTemplateSearchVO = queryAppTemplate(templateSearchDTO);
        }
        return msgTemplateSearchVO;
    }

    @Override
    public Template getValidTemplate(int tid) {
        if (tid <= 0) {
            return null;
        }
        Template template = null;
        try {
            template = templateRepository.findById(tid).orElse(null);
        } catch (Exception e) {
            log.warn("getValidTemplate findById exception e:{}", e);
        }
        if (null == template) {
            return null;
        }
        int nodeId = template.getNodeId();
        int isActive = template.getIsActive();
        int sendType = template.getSendType();
        int targetType = template.getTargetType();
        // 发送节点id不合法,停用了模板,没有发送方式的模板,接受对象不存在的模板 都不允许发送
        int disableStatus = 2;
        if (nodeId <= 0 || isActive == disableStatus || sendType <= 0 || targetType <= 0) {
            return null;
        }
        return template;
    }

    @Override
    public MessageTemplate getValidSmsTemplate(Integer tid, JSONObject keywords) {
        MessageTemplate mt = messageTemplateRepository.findByTid(tid).orElse(null);
        if (null == mt || mt.getChannelType() <= 0 || StringUtils.isEmpty(mt.getMsgContent())) {
            return null;
        }
        if (mt.getMsgContent().contains(KEYWORD_START_CHARACTER) || mt.getMsgContent().contains(KEYWORD_END_CHARACTER)) {
            try {
                String content = replaceKeyword(mt.getMsgContent(), keywords);
                mt.setMsgContent(content);
            } catch (NullPointerException exception) {
                log.warn("替换短信内容关键字失败(null)! 内容:{},关键字参数:{},req:{},{}", mt.getMsgContent(), keywords.toJSONString(), tid, exception);
                return null;
            } catch (Exception e) {
                log.warn("替换短信内容关键字失败! 内容:{},关键字参数:{},req:{},错误:{}", mt.getMsgContent(), keywords.toJSONString(), tid, e);
                return null;
            }
        }
        return mt;
    }

    @Override
    public String replaceKeyword(String content, JSONObject keywords) {
        // 解析短信内容中的关键字
        List<String> keywordList = new ArrayList<>();
        Matcher m = KEYWORD_REPLACE_PATTERN.matcher(content);
        while (m.find()) {
            String hit = m.group();
            keywordList.add(hit);
        }
        for (String keywrod : keywordList) {
            String realKey = keywrod.replace("{", "").replace("}", "").trim();
            content = content.replace(keywrod, keywords.getString(realKey));
        }
        return content;
    }

    /**
     * 查找所有符合条件的模板ID
     *
     * @param templateSearchDTO
     * @return
     */
    private List<Integer> queryTidList(TemplateSearchDTO templateSearchDTO) {
        List<Integer> tidList = null;
        List<Integer> nodeIds = configureService.queryNodeIds(templateSearchDTO.getConfigType(), templateSearchDTO.getCid());
        Integer coverNodeIds = nodeIds == null || nodeIds.isEmpty() ? null : 1;
        try {
            List<Template> templateList = templateRepository.search(String.valueOf(templateSearchDTO.getPmModule()), templateSearchDTO.getSendType(), templateSearchDTO.getTargetType(), templateSearchDTO.getIsAuto(), templateSearchDTO.getIsActive(), templateSearchDTO.getNodeId(), nodeIds, coverNodeIds, templateSearchDTO.getStartTime(), templateSearchDTO.getEndTime());
            if (!templateList.isEmpty()) {
                tidList = templateList.stream().map(Template::getTid).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("queryTidList mysql error templateSearchDTO:{} e:{}", templateSearchDTO, e);
        }
        log.debug("queryTidList tidList:{}", tidList);
        return tidList;
    }

    /**
     * 根据批量模板ID查询基础模板及配置项信息
     *
     * @param tidList
     * @return
     */
    private List<TemplateSearchItem> queryTemplateSearchItemList(List<Integer> tidList) {
        List<TemplateSearchItem> templateSearchItemList = Lists.newArrayList();
        List<Template> templateList = Lists.newArrayList();
        try {
            templateList = templateRepository.findAllByTidInOrderByTidDesc(tidList);
        } catch (Exception e) {
            log.warn("queryTemplateSearchItemList templateRepository.findAllByTidIn mysql error e:{}", e);
        }
        if (templateList.isEmpty()) {
            return templateSearchItemList;
        }
        //用户信息
        List<Integer> uidList = templateList.stream().map(Template::getCreateId).collect(Collectors.toList());
        Map<Integer, PermissionWrapper.CrmUserVO> userHashMap = externalService.crmUserQueryMap(uidList);
        //配置项信息
        HashMap<Integer, Configure> configureHashMap = Maps.newHashMap();
        Set<Integer> nodeIds = templateList.stream().map(Template::getNodeId).collect(Collectors.toSet());
        List<Configure> configureList = Lists.newArrayList();
        try {
            configureList = configureRepository.findAllByCidIn(nodeIds);
        } catch (Exception e) {
            log.warn("queryTemplateSearchItemList configureRepository.findAllByCidIn mysql error e:{}", e);
        }
        for (Configure configure : configureList) {
            configureHashMap.put(configure.getCid(), configure);
        }
        //父级配置项信息
        Map<Integer, ConfigureWrapper.Configure> fatherConfigureMap = queryConfigureMap(configureList, Maps.newHashMap(), 1, 2);
        for (Template template : templateList) {
            Configure configure = configureHashMap.getOrDefault(template.getNodeId(), new Configure());
            PermissionWrapper.CrmUserVO crmUserVO = userHashMap.getOrDefault(template.getCreateId(), new PermissionWrapper.CrmUserVO());
            TemplateSearchItem templateSearchItem = TemplateSearchItem.builder()
                    .tid(template.getTid())
                    .nodeId(template.getNodeId())
                    .title(template.getTitle())
                    .isActive(template.getIsActive())
                    .isAuto(template.getIsAuto())
                    .targetType(template.getTargetType())
                    .sendType(template.getSendType())
                    .msgRemark(template.getMsgRemark())
                    .pmModule(StringUtils.isBlank(template.getPmModule()) ? 0 : Integer.valueOf(template.getPmModule()))
                    .createTime(template.getCreateTime())
                    .createId(template.getCreateId())
                    .fatherId(IntegerUtils.intValueAsDefault(configure.getFatherId()))
                    .configDescribe(StringUtils.defaultString(configure.getConfigDescribe()))
                    .nick(StringUtils.defaultString(crmUserVO.getNick()))
                    .build();
            //父配置项信息
            if (IntegerUtils.isGtLimitValue(configure.getFatherId())) {
                ConfigureWrapper.Configure bigType = fatherConfigureMap.getOrDefault(configure.getFatherId(), null);
                templateSearchItem.setBigType(bigType);
                templateSearchItem.setSmallType(bigType != null ? fatherConfigureMap.getOrDefault(bigType.getFatherId(), null) : null);
            } else {
                templateSearchItem.setSmallType(null);
                templateSearchItem.setBigType(null);
            }
            templateSearchItemList.add(templateSearchItem);
        }
        return templateSearchItemList;
    }

    /**
     * 查询短信模板
     *
     * @param templateSearchDTO
     * @return
     */
    private MsgTemplateSearchVO queryMessageTemplate(TemplateSearchDTO templateSearchDTO) {
        List<Integer> tidList = queryTidList(templateSearchDTO);
        Integer coverTidList = tidList == null || tidList.isEmpty() ? null : 1;
        PageInfo pageInfo = templateSearchDTO.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getCurrPage() - 1, pageInfo.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        Integer isLikeGround = getIsLikeGround(templateSearchDTO.getCityId());
        //查询短信模板并且补充基础与配置项信息
        Page<MessageTemplate> messageTemplatePage;
        try {
            messageTemplatePage = messageTemplateRepository.search(null, templateSearchDTO.getChannelType(), templateSearchDTO.getIsGround(), isLikeGround, String.valueOf(templateSearchDTO.getCityId()), templateSearchDTO.getTid(), tidList, coverTidList, pageable);
        } catch (Exception e) {
            log.warn("queryMessageTemplate mysql error e:{}", e);
            return MsgTemplateSearchVO.builder()
                    .total(0L)
                    .result(Lists.newArrayList())
                    .build();
        }
        List<TemplateSearchItem> templateSearchItemList = Lists.newArrayList();
        if (!messageTemplatePage.getContent().isEmpty()) {
            HashMap<Integer, MessageTemplate> messageTemplateHashMap = Maps.newHashMap();
            for (MessageTemplate messageTemplate : messageTemplatePage.getContent()) {
                messageTemplateHashMap.put(messageTemplate.getTid(), messageTemplate);
            }
            List<Integer> msgTidList = messageTemplatePage.getContent().stream().map(MessageTemplate::getTid).collect(Collectors.toList());
            templateSearchItemList = queryTemplateSearchItemList(msgTidList);
            for (TemplateSearchItem templateSearchItem : templateSearchItemList) {
                MessageTemplate messageTemplate = messageTemplateHashMap.getOrDefault(templateSearchItem.getTid(), new MessageTemplate());
                templateSearchItem.setMsgContent(messageTemplate.getMsgContent());
                templateSearchItem.setChannelType(messageTemplate.getChannelType());
                templateSearchItem.setIsGround(messageTemplate.getIsGround());
                templateSearchItem.setCityIds(messageTemplate.getCityIds());
            }
        }
        MsgTemplateSearchVO msgTemplateSearchVO = new MsgTemplateSearchVO();
        msgTemplateSearchVO.setTotal(messageTemplatePage.getTotalElements());
        msgTemplateSearchVO.setResult(templateSearchItemList);
        return msgTemplateSearchVO;
    }

    /**
     * 根据国标城市ID获取是否非落地城市
     *
     * @param cityId
     * @return
     */
    private Integer getIsLikeGround(Long cityId) {
        Integer isLikeGround = null;
        if (cityId != null) {
            try {
                LocalDistrictServiceWrapper.LocalIdGetByStandardIdDTO localIdGetByStandardIdDTO = LocalDistrictServiceWrapper.LocalIdGetByStandardIdDTO.builder()
                        .standardId(cityId)
                        .build();
                ResResult<Integer> resResult = dstService.localIdGetByStandardId(localIdGetByStandardIdDTO);
                if (resResult != null && resResult.getStatus() == 0) {
                    LocalDistrictServiceWrapper.LocalTownGetDTO localTownGetDTO = LocalDistrictServiceWrapper.LocalTownGetDTO.builder()
                            .townId(resResult.getData())
                            .build();
                    ResResult<LocalTownVO> localTownVOResResult = dstService.localTownGet(localTownGetDTO);
                    if (localTownVOResResult != null && localTownVOResResult.getStatus() == 0) {
                        LocalTownVO localTownVO = localTownVOResResult.getData();
                        isLikeGround = localTownVO.getIsGrounded() == 1 ? 1 : 2;
                    }
                }
                log.debug("queryMessageTemplate localIdGetByStandardId resResult:{} localTownGet resResult:{}", resResult, localIdGetByStandardIdDTO);
            } catch (Exception e) {
                log.warn("queryMessageTemplate getIsGrounded exception e:{}", e);
            }
        }
        return isLikeGround;
    }

    /**
     * 查询APP模板
     *
     * @param templateSearchDTO
     * @return
     */
    private MsgTemplateSearchVO queryAppTemplate(TemplateSearchDTO templateSearchDTO) {
        PageInfo pageInfo = templateSearchDTO.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getCurrPage() - 1, pageInfo.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        List<Integer> tidList = queryTidList(templateSearchDTO);
        Integer coverTidList = tidList == null || tidList.isEmpty() ? null : 1;
        Page<AppTemplate> appTemplatePage;
        try {
            appTemplatePage = appTemplateRepository.search(templateSearchDTO.getAppId(), templateSearchDTO.getTid(), tidList, coverTidList, pageable);
        } catch (Exception e) {
            log.warn("queryAppTemplate appTemplateRepository.search mysql error e:{}", e);
            return MsgTemplateSearchVO.builder()
                    .total(0L)
                    .result(Lists.newArrayList())
                    .build();
        }
        List<TemplateSearchItem> templateSearchItemList = Lists.newArrayList();
        if (!appTemplatePage.getContent().isEmpty()) {
            HashMap<Integer, AppTemplate> appTemplateHashMap = Maps.newHashMap();
            for (AppTemplate appTemplate : appTemplatePage.getContent()) {
                appTemplateHashMap.put(appTemplate.getTid(), appTemplate);
            }
            List<Integer> msgTidList = appTemplatePage.getContent().stream().map(AppTemplate::getTid).collect(Collectors.toList());
            templateSearchItemList = queryTemplateSearchItemList(msgTidList);
            for (TemplateSearchItem templateSearchItem : templateSearchItemList) {
                AppTemplate appTemplate = appTemplateHashMap.getOrDefault(templateSearchItem.getTid(), new AppTemplate());
                templateSearchItem.setAppId(IntegerUtils.intValueAsDefault(appTemplate.getAppId()));
                templateSearchItem.setAppContent(StringUtils.defaultString(appTemplate.getAppContent()));
            }
        }
        MsgTemplateSearchVO msgTemplateSearchVO = new MsgTemplateSearchVO();
        msgTemplateSearchVO.setTotal(appTemplatePage.getTotalElements());
        msgTemplateSearchVO.setResult(templateSearchItemList);
        return msgTemplateSearchVO;
    }

    /**
     * 读取模板公用信息
     *
     * @param tid
     * @return
     */
    protected Template getCommonTemplateByTid(int tid) {
        Template template = null;
        try {
            template = templateRepository.findById(tid).orElse(null);
        } catch (Exception e) {
            log.warn("getCommonTemplateByTid mysql error tid:{} e:{}", tid, e);
        }
        return template;
    }

    /**
     * 递归查询父节点信息
     *
     * @param configureList
     * @param configureMap
     * @param level
     * @param maxLevel
     * @return
     */
    protected Map<Integer, ConfigureWrapper.Configure> queryConfigureMap(List<Configure> configureList, Map<Integer, ConfigureWrapper.Configure> configureMap, int level, int maxLevel) {
        if (configureList.isEmpty() || level > maxLevel) {
            return configureMap;
        }
        Set<Integer> fartherIdList = configureList.stream().map(Configure::getFatherId).collect(Collectors.toSet());
        level++;
        List<Configure> fartherConfigureList = configureRepository.findAllByCidIn(fartherIdList);
        for (Configure configure : fartherConfigureList) {
            ConfigureWrapper.Configure configureVO = DozerUtils.map(configure, ConfigureWrapper.Configure.class);
            configureMap.put(configure.getCid(), configureVO);
        }
        queryConfigureMap(fartherConfigureList, configureMap, level, maxLevel);
        return configureMap;
    }

    /**
     * 读取短信模板信息
     *
     * @param tid
     * @return
     */
    protected MessageTemplate getMessageTemplateByTid(int tid) {
        MessageTemplate messageTemplate = null;
        try {
            messageTemplate = messageTemplateRepository.findByTid(tid).orElse(new MessageTemplate());
        } catch (Exception e) {
            log.warn("getMessageTemplateByTid exception tid:{} e:{}", tid, e);
        }
        return messageTemplate;
    }

    /**
     * 读取APP模板信息
     *
     * @param tid
     * @return
     */
    protected AppTemplate getAppTemplateByTid(int tid) {
        AppTemplate appTemplate = null;
        try {
            appTemplate = appTemplateRepository.findByTid(tid).orElse(new AppTemplate());
        } catch (Exception e) {
            log.warn("getAppTemplateByTid exception tid:{} e:{}", tid, e);
        }
        return appTemplate;
    }
}
