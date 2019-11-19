package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.dto.ListTemplateDTO;
import com.to8to.tbt.msc.dto.MsgTemplateDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoKeyword;
import com.to8to.tbt.msc.entity.mongo.MongoTemplateList;
import com.to8to.tbt.msc.repository.mongo.KeywordRepository;
import com.to8to.tbt.msc.repository.mongo.TemplateRepository;
import com.to8to.tbt.msc.service.TemplateService;
import com.to8to.tbt.msc.utils.PageInfoUtils;
import com.to8to.tbt.msc.vo.ListTemplateVO;
import com.to8to.tbt.msc.vo.MsgTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author edmund.yu
 */
@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private KeywordRepository keyWordRepository;

    private Map<String,String> keyWords = new HashMap<>();
    private static Pattern p1 = Pattern.compile("#([^#>;}]+)#");
    private static String ILLEGALCHARACTER = "##";
    private static String NULL = "null";

    @Override
    public MsgCenterResponse addTemplate(MsgTemplateDTO msgTemplateDTO) {

        MongoMsgTemplate mongoMsgTemplate = DozerUtils.map(msgTemplateDTO, MongoMsgTemplate.class);
        String toUserTypes = msgTemplateDTO.getToUserType();
        String sendTypes = msgTemplateDTO.getSendType();
        String[] sendType = sendTypes.split(",");
        String[] toUserType = toUserTypes.split(",");
        if (mongoMsgTemplate.getContent().contains(ILLEGALCHARACTER) || mongoMsgTemplate.getLink().contains(ILLEGALCHARACTER)
                || mongoMsgTemplate.getTitle().contains(ILLEGALCHARACTER)) {
            return Response.RESPONSE_509;
        }

        for (String s : sendType) {
            for (String t : toUserType) {
                mongoMsgTemplate.setId(null);
                mongoMsgTemplate.setSendType(s);
                mongoMsgTemplate.setToUserType(t);
                // 此方法只在从模板内容中获取关键参数对的id
                findKeyWordsId(mongoMsgTemplate);
                boolean wordFlag = mongoMsgTemplate.getWordIds() != null && mongoMsgTemplate.getWordIds().contains((NULL));
                boolean urlFlag = mongoMsgTemplate.getUrlParamIds() != null && mongoMsgTemplate.getUrlParamIds().contains(NULL);
                boolean titleFlag = mongoMsgTemplate.getTitleParamIds() != null && mongoMsgTemplate.getTitleParamIds().contains(NULL);
                if (wordFlag || urlFlag || titleFlag) {
                    return Response.RESPONSE_508;
                }
                if (mongoMsgTemplate.getNodeCategory() == null) {
                    mongoMsgTemplate.setNodeCategory(0);
                }
                if (mongoMsgTemplate.getIsGround() == null) {
                    mongoMsgTemplate.setIsGround(0);
                }
                if (mongoMsgTemplate.getSmallCategory() == null) {
                    mongoMsgTemplate.setSmallCategory(0);
                }
                if (mongoMsgTemplate.getPriority() == null) {
                    mongoMsgTemplate.setPriority(0);
                }
                mongoMsgTemplate.setStatus(1);
                mongoMsgTemplate.setCreateTime(System.currentTimeMillis() / 1000);

                try {
                    templateRepository.addTemplate(mongoMsgTemplate);
                } catch (Exception e) {
                    log.warn("TemplateServiceImpl.addTemplate throw exception msgTemplate:{} exception:", mongoMsgTemplate);
                    return Response.FAIL;
                }
            }
        }
        return Response.SUCCESS;
    }

    @Override
    public MsgCenterResponse updateTemplate(MsgTemplateDTO msgTemplateDTO) {
        MongoMsgTemplate mongoMsgTemplate = DozerUtils.map(msgTemplateDTO, MongoMsgTemplate.class);
        if (StringUtils.isNotEmpty(mongoMsgTemplate.getId())) {
            String toUserTypes = mongoMsgTemplate.getToUserType();
            String sendTypes = mongoMsgTemplate.getSendType();
            String[] sendType = sendTypes.split(",");
            String[] toUserType = toUserTypes.split(",");

            mongoMsgTemplate.setSendType(sendType[0]);
            mongoMsgTemplate.setToUserType(toUserType[0]);
            // 此方法只在从模板内容中获取关键参数对的id
            findKeyWordsId(mongoMsgTemplate);
            boolean wordFlag = mongoMsgTemplate.getWordIds() != null && mongoMsgTemplate.getWordIds().contains((NULL));
            boolean urlFlag = mongoMsgTemplate.getUrlParamIds() != null && mongoMsgTemplate.getUrlParamIds().contains(NULL);
            boolean titleFlag = mongoMsgTemplate.getTitleParamIds() != null && mongoMsgTemplate.getTitleParamIds().contains(NULL);
            if (wordFlag || urlFlag || titleFlag) {
                return Response.RESPONSE_508;
            }
            mongoMsgTemplate.setId(msgTemplateDTO.getId());
            try {
                templateRepository.updateTemplate(mongoMsgTemplate);
                return Response.SUCCESS;
            } catch (Exception e) {
                log.warn("TemplateServiceImpl.updateTemplate complexQuery exception msgTemplate:{} exception:", mongoMsgTemplate);
            }
        }
            return Response.FAIL;
    }

    @Override
    public MsgCenterResponse deleteTemplateById(String id) {
        templateRepository.deleteTemplateById(id);
        return Response.SUCCESS;
    }

    @Override
    public List<MsgTemplateVO> listTemplateByNode(Integer nodeId) {
        List<MsgTemplateVO> vos = new ArrayList<>();
        List<MongoMsgTemplate> mongoMsgTemplateList = templateRepository.listTemplateByNote(nodeId);
        for (MongoMsgTemplate mongoMsgTemplate : mongoMsgTemplateList) {
            MsgTemplateVO msgTemplateVO = DozerUtils.map(mongoMsgTemplate, MsgTemplateVO.class);
            msgTemplateVO.setNickId(msgTemplateVO.getId());
            if (msgTemplateVO.getIsGround() == null) {
                msgTemplateVO.setIsGround(0);
            }
            vos.add(msgTemplateVO);
        }
        return vos;
    }

    @Override

    public ListTemplateVO listTemplate(ListTemplateDTO listTemplateDTO) {
        List<MsgTemplateVO> vos = new ArrayList<>();
        ListTemplateVO result = new ListTemplateVO();
        MongoMsgTemplate mongoMsgTemplate = DozerUtils.map(listTemplateDTO, MongoMsgTemplate.class);
        if (listTemplateDTO.getPageInfo() == null){
            listTemplateDTO.setPageInfo(new PageInfo(1, 10));
        }
        PageInfoUtils.CreatDefaultPageInfo(listTemplateDTO.getPageInfo());
        int currentPage = listTemplateDTO.getPageInfo().getCurrPage();
        int pageSize = listTemplateDTO.getPageInfo().getPageSize();
        int offset = (currentPage -1) * pageSize;
        MongoTemplateList mongoTemplateList;
        try {
            mongoTemplateList = templateRepository.listTemplate(mongoMsgTemplate, listTemplateDTO.getSearchTime(),offset,pageSize);
        } catch (Exception e) {
            log.warn("TemplateServiceImpl.listTemplate complexQuery exception ListTemplateDTO:{} exception:", listTemplateDTO);
            return result;
        }
        long totalRecords = mongoTemplateList.getTotalRecord();
        long totalPages = (totalRecords % pageSize == 0) ? (totalRecords / pageSize) : (totalRecords / pageSize + 1);
        for (MongoMsgTemplate template: mongoTemplateList.getMongoMsgTemplates()){
            MsgTemplateVO vo = DozerUtils.map(template, MsgTemplateVO.class);
            vo.setNickId(vo.getId());
            vos.add(vo);
        }
        result.setTotalPages(totalPages);
        result.setMsgTemplates(vos);
        result.setTotalRecords(totalRecords);
        return result;
    }

    @Override
    public MsgTemplateVO getTemplateById(String id) {
        MongoMsgTemplate msgTemplate = templateRepository.getTemplateById(id);
        MsgTemplateVO msgTemplateVO = DozerUtils.map(msgTemplate, MsgTemplateVO.class);
        if (msgTemplateVO != null) {
            msgTemplateVO.setNickId(msgTemplateVO.getId());
        }
        return msgTemplateVO;
    }

    private void findKeyWordsId(MongoMsgTemplate mongoMsgTemplate) {
        List<MongoKeyword> list = keyWordRepository.findAll();
        for (MongoKeyword word : list) {
            keyWords.put(word.getKeyword(), word.getId());
        }
        StringBuffer sb = new StringBuffer();
        String content = mongoMsgTemplate.getContent();
        setParamIds(sb, content);
        if (sb.length() > 0) {
            String wordIds = sb.substring(0, sb.length() - 1);
            mongoMsgTemplate.setWordIds(wordIds);
        }
        // 链接参数
        sb = new StringBuffer();
        String link = mongoMsgTemplate.getLink();
        setParamIds(sb, link);
        if (sb.length() > 0) {
            String wordIds = sb.substring(0, sb.length() - 1);
            mongoMsgTemplate.setUrlParamIds(wordIds);
        }
        // 标题参数
        sb = new StringBuffer();
        String title = mongoMsgTemplate.getTitle();
        setParamIds(sb, title);
        if (sb.length() > 0) {
            String wordIds = sb.substring(0, sb.length() - 1);
            mongoMsgTemplate.setTitleParamIds(wordIds);
        }
    }

    private void setParamIds(StringBuffer sb, String content) {
        Matcher m1 = p1.matcher(content);
        while (m1.find()) {
            String old = m1.group();
            String old1 = old.substring(1, old.length() - 1);
            String keywordsId = keyWords.get(old1);
            sb.append(keywordsId).append(",");
        }
    }

}
