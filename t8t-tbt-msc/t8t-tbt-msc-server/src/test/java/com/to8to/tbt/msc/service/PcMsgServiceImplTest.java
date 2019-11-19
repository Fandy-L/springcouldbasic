package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONArray;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.PcMsgRequestDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.mongo.MongoMsgTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordBizData;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordList;
import com.to8to.tbt.msc.repository.mongo.PcRecordComplexRepository;
import com.to8to.tbt.msc.repository.mongo.TemplateRepository;
import com.to8to.tbt.msc.service.impl.PcMsgServiceImpl;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.PcRecordVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class PcMsgServiceImplTest extends BaseApplication {
    @Autowired
    private PcMsgService pcMsgService;

    @Mock
    private PcRecordComplexRepository pcRecordComplexRepository;

    @Mock
    private TemplateRepository  templateRepository;

    @Autowired
    private AsyncTaskService asyncTaskService1;

    @Mock
    private AsyncTaskService asyncTaskService = asyncTaskService1;

    @Mock
    private ValueOperations<String, Map<String, Integer>> valueOperations;

    @InjectMocks
    private PcMsgService pcMsgServiceMock = new PcMsgServiceImpl();

    @Before
    public void setUp(){
        List<MongoMsgTemplate> templates = new ArrayList<>();
        MongoMsgTemplate template = new MongoMsgTemplate();
        template.setContent("a#1#b#2#c#3#");
        template.setWordIds("547bd2e9dc59e06ae4d04b36,54af75e60521a340afc1303b,54af75e60521a340afc1303a");
        template.setSendType("1");
        template.setLink("1#a#2");
        template.setUrlParamIds("547bd2e9dc59e06ae4d04b36");
        template.setTitle("title#a#");
        template.setTitleParamIds("547bd2e9dc59e06ae4d04b36");
        templates.add(template);
        template.setNodeCategory(1551);
        template.setSmallCategory(1);
        template.setToUserType("1");
        template.setPriority(1);
        when(templateRepository.getTemplateByNodeAndToUserType(1, 1)).thenReturn(templates);
        when(templateRepository.getTemplateByNodeAndToUserType(2, 1)).thenReturn(null);
        when(templateRepository.getTemplateByNodeAndToUserType(3, 1)).thenThrow(new RuntimeException("Exception"));
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("1551", 1551);
        when(valueOperations.get("1226686")).thenReturn(map1);
        MongoPcRecord pcRecord = new MongoPcRecord();
        pcRecord.setContent("倾听");
        pcRecord.setBizData(new MongoPcRecordBizData());
        when(pcRecordComplexRepository.getMsgHuang("1",2)).thenReturn(pcRecord);
        Map<String,Integer> map = new HashMap<>();
        map.put("9", 1);
        map.put("10", -5);
        when(pcRecordComplexRepository.getUnreadBig("1")).thenReturn(map);
        List<Integer> list = new ArrayList<>();
        list.add(1551);
        when(pcRecordComplexRepository.getUserSmallNeed("2")).thenReturn(list);
        MongoPcRecordList mongoPcRecordList = new MongoPcRecordList();
        mongoPcRecordList.setTotalRecords(10);
        List<MongoPcRecord> mongoPcRecords = new ArrayList<>();
        mongoPcRecords.add(pcRecord);
        mongoPcRecordList.setMongoPcRecords(mongoPcRecords);
        when(pcRecordComplexRepository.searchMsg(argThat(ListMsgDTO -> ListMsgDTO.getContent() == "A"), anyInt(), anyInt(), anyInt())).thenReturn(mongoPcRecordList);
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).getMsgHuang("2", 2);
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).delMsg("1");
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).getUnreadBig("2");
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).setSmallNeed(argThat(String -> String.contains("1")), anyList());
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).getUserSmallNeed("1");
        doThrow(new RuntimeException("Exception")).when(pcRecordComplexRepository).searchMsg(argThat(ListMsgDTO -> ListMsgDTO.getContent() == "B"), anyInt(),anyInt(),anyInt());
        when(pcRecordComplexRepository.getPcMsgById("1")).thenThrow(new RuntimeException("Exception"));
    }

    @Test
    public void sendPCMsg() throws InterruptedException {
        PcMsgRequestDTO pcMsgRequestDTO = new PcMsgRequestDTO();
        pcMsgRequestDTO.setNodeId("1");
        pcMsgRequestDTO.setToUserType(1);
        Map<String,String> map = new HashMap<>();
        map.put("phone", "18280288034");
        map.put("email", "邮件");
        map.put("app", "APP");
        pcMsgRequestDTO.setContactParams(map);
        pcMsgRequestDTO.setToUserId("1226686");

        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("547bd2e9dc59e06ae4d04b36", "倾听");
        paramMap.put("54af75e60521a340afc1303b", "感受");
        paramMap.put("54af75e60521a340afc1303a", "思考");
        pcMsgRequestDTO.setParams(paramMap);
        pcMsgRequestDTO.setTitleParams(paramMap);
        pcMsgRequestDTO.setUrlParams(paramMap);
        pcMsgRequestDTO.setLevel(1);

        MsgCenterResponse response0 = pcMsgServiceMock.sendPcMsg(pcMsgRequestDTO);
        Assert.assertEquals(Response.SUCCESS, response0);

        pcMsgRequestDTO.setNodeId("2");
        MsgCenterResponse response1 = pcMsgServiceMock.sendPcMsg(pcMsgRequestDTO);
        Assert.assertEquals(Response.FAIL, response1);

        pcMsgRequestDTO.setNodeId("3");
        MsgCenterResponse response2 = pcMsgServiceMock.sendPcMsg(pcMsgRequestDTO);
        Assert.assertEquals(Response.FAIL, response2);
        Thread.sleep(1000);

    }

    @Test
    public void getMsgHuang() {
        JSONArray params = new JSONArray();
        params.add("1");
        params.add(2);
        PcRecordVO pcRecordVO = pcMsgServiceMock.getMsgHuang(params);
        Assert.assertEquals("倾听",pcRecordVO.getContent());

        params.set(0, "2");
        PcRecordVO pcRecordVO1 = pcMsgServiceMock.getMsgHuang(params);
        Assert.assertEquals(null, pcRecordVO1);
    }

    @Test
    public void delMsg(){
        JSONArray params = new JSONArray();
        params.add("1");
        params.add("1");
        MsgCenterResponse response = pcMsgServiceMock.delMsg(params);
        Assert.assertEquals(Response.FAIL, response);

        params.set(1, "2");
        MsgCenterResponse response1 = pcMsgServiceMock.delMsg(params);
        Assert.assertEquals(Response.SUCCESS, response1);
    }

    @Test
    public void getUnreadBig(){

        Map map =pcMsgServiceMock.getUnreadBig("1");
        Assert.assertEquals(1, map.get("9"));

        Map map1 =pcMsgServiceMock.getUnreadBig("1");
        Assert.assertEquals(1, map1.get("9"));

        Map map2 = pcMsgServiceMock.getUnreadBig("2");
        Assert.assertEquals(0, map2.size());

    }

    @Test
    public void setSmallNeed(){
        List<Integer> smalls = new ArrayList<>();
        smalls.add(917);
        smalls.add(919);
        smalls.add(921);
        smalls.add(1551);

        JSONArray params = new JSONArray();
        params.add("2");
        params.add(smalls);
        MsgCenterResponse response = pcMsgServiceMock.setSmallNeed(params);
        Assert.assertEquals(Response.SUCCESS, response);

        JSONArray params3 = new JSONArray();
        params3.add("1");
        params3.add(new ArrayList<>());
        MsgCenterResponse response3 = pcMsgServiceMock.setSmallNeed(params3);
        Assert.assertEquals(Response.FAIL, response3);
    }

    @Test
    public void getUserSmallNeed(){
        List<Integer> smalls = pcMsgServiceMock.getUserSmallNeed("2");
        Assert.assertEquals(1, smalls.size());

        List<Integer> smalls4 = pcMsgServiceMock.getUserSmallNeed("1");
        Assert.assertEquals(0, smalls4.size());
    }

    @Test
    public void searchMsgBig(){
        JSONArray params = new JSONArray();
        ListMsgDTO listMsgDTO = new ListMsgDTO();
        listMsgDTO.setContent("A");
        params.add(listMsgDTO);
        params.add(null);
        ListMsgVO listMsgVO = pcMsgServiceMock.searchMsgBig(params);
        Assert.assertEquals(new Long(10), listMsgVO.getTotalRecords());
        ListMsgDTO listMsgDTO1 = new ListMsgDTO();
        listMsgDTO1.setContent("B");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrPage(5);
        pageInfo.setPageSize(10);
        listMsgDTO1.setPageInfo(pageInfo);
        params.set(0, listMsgDTO1);
        ListMsgVO listMsgVO2 = pcMsgServiceMock.searchMsgBig(params);
        Assert.assertEquals(0, listMsgVO2.getTotalRecords().longValue());

    }

    @Test
    public void setMsgReaded() throws InterruptedException {
        MsgCenterResponse response = pcMsgServiceMock.setMsgRead("1");
        Assert.assertEquals(Response.SUCCESS, response);
        Thread.sleep(1000);
    }
}