package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.dto.SetAppMsgStatusByTidDTO;
import com.to8to.tbt.msc.entity.mongo.*;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.MsgResult;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.entity.PageResult;
import com.to8to.tbt.msc.entity.mongo.MongoAppRecord;
import com.to8to.tbt.msc.entity.mongo.MongoAppUserMsg;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.entity.mongo.MongoRecordTarget;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.repository.mongo.MongoAppRecordRepository;
import com.to8to.tbt.msc.repository.mongo.MongoAppUserMsgRepository;
import com.to8to.tbt.msc.repository.mongo.template.MsgRecordComplexRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.repository.mysql.ComplexRepository;
import com.to8to.tbt.msc.repository.mysql.main.AppTemplateRepository;
import com.to8to.tbt.msc.repository.mysql.main.CommonTplRepository;
import com.to8to.tbt.msc.utils.SmsUtils;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SmsUtils.class })
public class MessageCenterServiceImplTest extends BaseApplication {
    @Mock
    private EsMsgRecordRepository esMsgRecordRepository;
    @Mock
    private ComplexRepository complexRepository;
    @Mock
    private MsgRecordComplexRepository msgRecordComplexRepository;
    @Mock
    private MongoAppRecordRepository mongoAppRecordRepository;
    @Mock
    private MongoAppUserMsgRepository mongoAppUserMsgRepository;
    @Mock
    private AppRecordRepository appRecordRepository;
    @Mock
    private AppTemplateRepository appTemplateRepository;
    @Mock
    private CommonTplRepository commonTplRepository;

    @InjectMocks
    private MessageCenterServiceImpl messageCenterServiceMock = new MessageCenterServiceImpl();

    @Autowired
    private MessageCenterServiceImpl messageCenterService = new MessageCenterServiceImpl();

    @Before
    public void setUp()
    {
        SearchMessageRecordVO searchMessageRecordVO = new SearchMessageRecordVO();
        searchMessageRecordVO.setTotal(1L);
        JSONObject jsonObject = new JSONObject();
        JSONObject bizParam = new JSONObject();
        jsonObject.put("yid", 12);
        jsonObject.put("uid", "1");
        jsonObject.put("target_type", 1);
        jsonObject.put("id", "1551");
        jsonObject.put("tid", 10);
        jsonObject.put("title", "xi");
        jsonObject.put("send_time", 1996);
        jsonObject.put("app_content", "ha");
        jsonObject.put("is_read", 0);
        jsonObject.put("biz_param", bizParam);
        List<JSONObject> list = new ArrayList<>();
        list.add(jsonObject);
        searchMessageRecordVO.setResult(list);
        when(esMsgRecordRepository.queryAppRecord(any())).thenReturn(searchMessageRecordVO);
        when(esMsgRecordRepository.querySmsRecord(any())).thenReturn(searchMessageRecordVO);
        when(esMsgRecordRepository.queryAppRecord(argThat(SearchMessageRecordDTO -> SearchMessageRecordDTO.getIsRead() == 1))).thenThrow(new RuntimeException("Exception"));
        when(esMsgRecordRepository.querySmsRecord(argThat(SearchMessageRecordDTO -> SearchMessageRecordDTO.getIsRead() == 1))).thenThrow(new RuntimeException("Exception"));
        MongoPcRecord mongoPcRecord = new MongoPcRecord();
        MongoRecordTarget mongoRecordTarget = new MongoRecordTarget();
        mongoPcRecord.setTarget(mongoRecordTarget);
        List<MongoPcRecord> list1 = new ArrayList<>();
        list1.add(mongoPcRecord);
        when(msgRecordComplexRepository.query(any())).thenReturn(PageResult.builder().total(1L).data(list1).build());
        when(complexRepository.setAppMsgReaded(1)).thenReturn(false);
        when(complexRepository.setAppMsgReaded(2)).thenThrow(new RuntimeException("Exception"));
        when(mongoAppRecordRepository.findById(any())).thenReturn(java.util.Optional.of(new MongoAppRecord()));
        doThrow(new RuntimeException("Exception")).when(appRecordRepository).setAppMsgHasReadBatch(any());
        Set<Integer> set = new HashSet<>();
        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        when(complexRepository.queryTidListByNodeIds(any())).thenReturn(set1);
        when(complexRepository.queryTidListByNodeIds(argThat(List::isEmpty))).thenReturn(set);
        doThrow(new RuntimeException("Exception")).when(appRecordRepository).setAppMsgStatusByUidAndTid(anyInt(), anyInt(), anySet());
        MongoAppUserMsg mongoAppUserMsg = new MongoAppUserMsg();
        mongoAppUserMsg.setRead(true);
        mongoAppUserMsg.setYid(15);
        List<MongoAppUserMsg> list2 = new ArrayList<>();
        list2.add(mongoAppUserMsg);
        when(mongoAppUserMsgRepository.findAllByUserId(anyString())).thenReturn(list2);
        when(mongoAppUserMsgRepository.findFirstByUserIdAndYid(anyString(), anyInt())).thenReturn(Optional.of(mongoAppUserMsg));
        doThrow(new RuntimeException("Exception")).when(mongoAppUserMsgRepository).save(any());

        when(appTemplateRepository.findByTid(1)).thenReturn(null);
        AppTemplate appTemplate = new AppTemplate();
        appTemplate.setPushScope(1);
        appTemplate.setNeedPush(0);
        appTemplate.setPushType(1);
        Template template = new Template();
        template.setTitle("倾听");
        when(commonTplRepository.findById(2)).thenReturn(Optional.of(template));
        when(appTemplateRepository.findByTid(2)).thenReturn(Optional.of(appTemplate));

        PowerMockito.mockStatic(SmsUtils.class);
        MongoMsgReply reply = new MongoMsgReply();
        reply.setPhone("1828884841");
        List<MongoMsgReply> mongoMsgReplies= new ArrayList<>();
        mongoMsgReplies.add(reply);
        PowerMockito.when(SmsUtils.getMongateUplinkRecord(anyString(), anyString())).thenReturn(mongoMsgReplies);
    }

    @Test
    public void searchMessageRecord()
    {
        SearchMessageRecordDTO searchMessageRecordDTO = new SearchMessageRecordDTO();
        searchMessageRecordDTO.setSendType(1);
        searchMessageRecordDTO.setIsRead(0);
        SearchMessageRecordVO searchMessageRecordVO = messageCenterServiceMock.searchMessageRecord(searchMessageRecordDTO);
        Assert.assertEquals(1L, searchMessageRecordVO.getTotal().longValue());

        searchMessageRecordDTO.setIsRead(1);
        try {
            messageCenterServiceMock.searchMessageRecord(searchMessageRecordDTO);
        }catch (RPCException e){
            Assert.assertEquals(MyExceptionStatus.NETWORK_ERROR, e.getStatus());
        }

        searchMessageRecordDTO.setSendType(4);
        try {
            messageCenterServiceMock.searchMessageRecord(searchMessageRecordDTO);
        }catch (RPCException e){
            Assert.assertEquals(MyExceptionStatus.NETWORK_ERROR, e.getStatus());
        }

        searchMessageRecordDTO.setIsRead(0);
        SearchMessageRecordVO searchMessageRecordVO1 = messageCenterServiceMock.searchMessageRecord(searchMessageRecordDTO);
        Assert.assertEquals(1L, searchMessageRecordVO1.getTotal().longValue());

        searchMessageRecordDTO.setSendType(3);
        try {
            messageCenterServiceMock.searchMessageRecord(searchMessageRecordDTO);
        }catch (RPCException e) {
            Assert.assertEquals(MyExceptionStatus.SEND_TYPE_INVALID, e.getStatus());
        }
    }

    @Test
    public void listMsg() {
        ListMsgDTO listMsgDTO = new ListMsgDTO();
        listMsgDTO.setIsRead(1);
        listMsgDTO.setSendType(4);
        ListMsgVO listMsgVO = messageCenterServiceMock.listMsg(listMsgDTO);
        Assert.assertEquals(0L, listMsgVO.getTotalRecords().longValue());

        listMsgDTO.setIsRead(0);
        ListMsgVO listMsgVO1 = messageCenterServiceMock.listMsg(listMsgDTO);
        Assert.assertEquals(1L, listMsgVO1.getTotalRecords().longValue());

        listMsgDTO.setSendType(1);
        ListMsgVO listMsgVO2 = messageCenterServiceMock.listMsg(listMsgDTO);
        Assert.assertEquals(1L, listMsgVO2.getTotalRecords().longValue());
    }

    @Test
    public void setAppMsgRead() {
        try {
            messageCenterServiceMock.setAppMsgRead(1);
        }catch (RPCException e){
            Assert.assertEquals(MyExceptionStatus.NETWORK_ERROR, e.getStatus());
        }
    }

    @Test
    public void setAppMsgReadBatch(){
        messageCenterServiceMock.setAppMsgHasReadBatch("1");
        verify(appRecordRepository).setAppMsgHasReadBatch(anyList());
    }

    @Test
    public void setAppMsgStatusByUidAndNodeId() {
        List<Integer> list = new ArrayList<>();
        try {
            messageCenterServiceMock.setAppMsgStatusByUidAndNodeId(1, list, 1);
        }catch (RPCException e){
            Assert.assertEquals(MyExceptionStatus.TEMPLATE_NODEID_INVALID, e.getStatus());
        }
        list.add(1);
        messageCenterServiceMock.setAppMsgStatusByUidAndNodeId(1,list,1 );
        verify(appRecordRepository).setAppMsgStatusByUidAndTid(anyInt(), anyInt(), anySet());
    }

    @Test
    public void setAppMsgReaderByTidAndUid(){
        List<Integer> list = new ArrayList<>();
        MsgCenterResponse response = messageCenterServiceMock.setAppMsgReaderByTidAndUid(list,1);
        Assert.assertEquals(Response.FAIL, response);
    }

    @Test
    public void getUserMsgState(){
        Map<Integer,Boolean> map = messageCenterServiceMock.getUserMsgState("xi");
        Assert.assertEquals(true, map.get(15));
    }

    @Test
    public void setUserMsgReaded(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("a");
        jsonArray.add(1);
        MsgCenterResponse response = messageCenterServiceMock.setUserMsgRead(jsonArray, false);
        Assert.assertEquals(Response.SUCCESS, response);
    }

    @Test
    public void setAppMsgStatusByUidAndTid(){
        SetAppMsgStatusByTidDTO setAppMsgStatusByTidDTO = new SetAppMsgStatusByTidDTO();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        setAppMsgStatusByTidDTO.setIsRead(1);
        setAppMsgStatusByTidDTO.setTids(list);
        setAppMsgStatusByTidDTO.setUid(1);
        MsgResult msgResult = messageCenterServiceMock.setAppMsgStatusByUidAndTid(setAppMsgStatusByTidDTO);
        Assert.assertEquals(0, msgResult.getResult().longValue());
    }

    @Test
    public void insertMsgReply(){
        messageCenterServiceMock.insertMsgReply("1", "121");
    }
}