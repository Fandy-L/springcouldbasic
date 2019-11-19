package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.ListTemplateDTO;
import com.to8to.tbt.msc.dto.MsgTemplateDTO;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoKeyword;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.repository.mongo.KeywordRepository;
import com.to8to.tbt.msc.repository.mongo.TemplateRepository;
import com.to8to.tbt.msc.service.impl.TemplateServiceImpl;
import com.to8to.tbt.msc.vo.ListTemplateVO;
import com.to8to.tbt.msc.vo.MsgTemplateVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author edmund.yu
 */
public class TemplateServiceTest extends BaseApplication {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private KeywordRepository keyWordRepository;

    @Autowired
    private TemplateService templateService;

    @InjectMocks
    private TemplateService templateServiceMock = new TemplateServiceImpl();

    @Before
    public void setUp(){
        doThrow(new RuntimeException("Exception")).when(templateRepository).addTemplate(argThat(mongoMsgTemplate -> mongoMsgTemplate.getContent() == "test"));
        doThrow(new RuntimeException("Exception")).when(templateRepository).deleteTemplateById("ssss");
        doThrow(new RuntimeException("Exception")).when(templateRepository).listTemplateByNote(1551);
        doThrow(new RuntimeException("Exception")).when(templateRepository).listTemplate(any(), any(),anyInt(),anyInt());
        doThrow(new RuntimeException("Exception")).when(templateRepository).getTemplateById("aa");

        when(templateRepository.updateTemplate(argThat(mongoMsgTemplate -> mongoMsgTemplate.getContent() == "ace"))).thenThrow(new RuntimeException("Exception"));
        MongoMsgTemplate msgTemplate = new MongoMsgTemplate();
        msgTemplate.setContent("cc");
        when(templateRepository.getTemplateById("1551")).thenReturn(msgTemplate);
        List<MongoKeyword> list = new ArrayList<>();
        MongoKeyword keyWord = new MongoKeyword();
        keyWord.setKeyword("业主申请结束时间");
        keyWord.setId("1551");
        list.add(keyWord);
        when(keyWordRepository.findAll()).thenReturn(list);
    }

    @Test
    public void addTemplate() {

        MsgTemplateDTO msgTemplateDTO = new MsgTemplateDTO();
        msgTemplateDTO.setContent("test");
        msgTemplateDTO.setTitle("hello");
        msgTemplateDTO.setLink("www");
        msgTemplateDTO.setSendType("1,3");
        msgTemplateDTO.setToUserType("2,5");

        MsgTemplateDTO msgTemplateDTOForAdd = new MsgTemplateDTO();
        msgTemplateDTOForAdd.setContent("#业主申请结束时间#");
        msgTemplateDTOForAdd.setLink("业主称呼");
        msgTemplateDTOForAdd.setSendType("2");
        msgTemplateDTOForAdd.setTitle("任务监理电话");
        msgTemplateDTOForAdd.setToUserType("3");

        MsgTemplateDTO msgTemplateDTO1 = new MsgTemplateDTO();
        msgTemplateDTO1.setContent("#不存在#");
        msgTemplateDTO1.setLink("#www.baidu.com#");
        msgTemplateDTO1.setSendType("2");
        msgTemplateDTO1.setTitle("#sdfasf#");
        msgTemplateDTO1.setToUserType("3");

        MsgCenterResponse msgCenterResponse4 = templateServiceMock.addTemplate(msgTemplateDTO);
        Assert.assertEquals(Response.FAIL, msgCenterResponse4);

        MsgCenterResponse msgCenterResponse3 = templateService.addTemplate(msgTemplateDTO1);
        Assert.assertEquals(Response.RESPONSE_508,msgCenterResponse3);

        MsgCenterResponse msgCenterResponse = templateServiceMock.addTemplate(msgTemplateDTOForAdd);
        Assert.assertEquals(Response.SUCCESS, msgCenterResponse);

        msgTemplateDTOForAdd.setContent("##hello");
        MsgCenterResponse msgCenterResponse2 = templateService.addTemplate(msgTemplateDTOForAdd);
        Assert.assertEquals(Response.RESPONSE_509, msgCenterResponse2);

    }

    @Test
    public void updateTemplate() {
        MsgTemplateDTO msgTemplateDTO = new MsgTemplateDTO();
        msgTemplateDTO.setId("5d7f319f997ffe05f4bd3c3a");
        msgTemplateDTO.setContent("业主申请结束时间");
        msgTemplateDTO.setLink("#业主申请结束时间#");
        msgTemplateDTO.setSendType("1,2");
        msgTemplateDTO.setTitle("#业主申请结束时间#");
        msgTemplateDTO.setToUserType("3");

        MsgTemplateDTO msgTemplateDTO2 = new MsgTemplateDTO();
        msgTemplateDTO2.setId("5d7f319f997ffe05f4bd3c3a");
        msgTemplateDTO2.setContent("#不存在的#");
        msgTemplateDTO2.setTitle("#也不存在#");
        msgTemplateDTO2.setLink("#更不存在#");
        msgTemplateDTO2.setSendType("1,2");
        msgTemplateDTO2.setToUserType("3");

        MsgCenterResponse msgCenterResponse = templateServiceMock.updateTemplate(msgTemplateDTO);
        Assert.assertEquals(Response.SUCCESS, msgCenterResponse);

        msgTemplateDTO.setContent("ace");
        MsgCenterResponse msgCenterResponse1 = templateServiceMock.updateTemplate(msgTemplateDTO);
        Assert.assertEquals(Response.FAIL, msgCenterResponse1);

        MsgCenterResponse msgCenterResponse2 = templateServiceMock.updateTemplate(msgTemplateDTO2);
        Assert.assertEquals(Response.RESPONSE_508, msgCenterResponse2);

    }

    @Test
    public void deleteTemplateById(){

        try {
            templateServiceMock.deleteTemplateById("ssss");
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }

        MsgCenterResponse msgCenterResponse2 = templateServiceMock.deleteTemplateById("5d7f32c1997ffe15c0d0e708");
        Assert.assertEquals(Response.SUCCESS, msgCenterResponse2);
    }

    @Test
    public void listTemplateByNode(){

        try {
        templateServiceMock.listTemplateByNode(1551);
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }

        List<MsgTemplateVO> msgTemplateList2 = templateService.listTemplateByNode(102);
        Assert.assertNotNull(msgTemplateList2);
    }

    @Test
    public void listTemplate(){
        ListTemplateDTO listTemplateDTO = new ListTemplateDTO();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrPage(1);
        pageInfo.setPageSize(10);
        listTemplateDTO.setPageInfo(pageInfo);

        ListTemplateVO listTemplateVO = templateService.listTemplate(listTemplateDTO);
        Assert.assertNotEquals(0,listTemplateVO.getTotalRecords().intValue());

        ListTemplateDTO listTemplateDTO1 = new ListTemplateDTO();
        ListTemplateVO listTemplateVO1 = templateServiceMock.listTemplate(listTemplateDTO1);
        Assert.assertEquals(null, listTemplateVO1.getTotalRecords());

    }

    @Test
    public void getTemplateById(){

        MsgTemplateVO msgTemplate1 = templateServiceMock.getTemplateById("1551");
        Assert.assertEquals("cc", msgTemplate1.getContent());

        try {
            templateServiceMock.getTemplateById("aa");
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }
    }
}