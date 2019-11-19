package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.to8to.common.search.PageResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.AppMsgListDTO;
import com.to8to.tbt.msc.dto.AppReadClearDTO;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.AppMsgWrapper;
import com.to8to.tbt.msc.entity.MsgRecord;
import com.to8to.tbt.msc.entity.UserWrapper;
import com.to8to.tbt.msc.entity.es.EsAppRecord;
import com.to8to.tbt.msc.entity.mysql.extend.AppAd;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePush;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePushUser;
import com.to8to.tbt.msc.repository.mysql.extend.AppAdRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushUserRepository;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.enumeration.MsgModuleCodeEnum;
import com.to8to.tbt.msc.enumeration.UserTypeEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class AppMsgServiceImplTest extends BaseApplication {

    @Mock
    private MessageCenterService messageCenterService;

    @Mock
    private ExternalService externalService;

    @Mock
    private MessagePushUserRepository messagePushUserRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MessagePushRepository messagePushRepository;

    @Mock
    private AppAdRepository appAdRepository;

    @Mock
    private PushService pushService;

    @InjectMocks
    private AppMsgService appMsgService = new AppMsgServiceImpl();

    @Before
    public void setUp() throws Exception {

        HashMap<Integer, String> diaryPushMsg = new HashMap<>();
        diaryPushMsg.put(301, "评论了您的日记");
        diaryPushMsg.put(302, "回复了您的评论");
        diaryPushMsg.put(303, "赞了您的日记");
        diaryPushMsg.put(304, "收藏了您的日记");

        ReflectionTestUtils.setField(appMsgService,"msgSetHasReadNode", "[9,15]");
        ReflectionTestUtils.setField(appMsgService,"diaryPushMsg", diaryPushMsg);

        CompanyResultWrapper.Business business1 = new CompanyResultWrapper.Business();
        business1.setName("hauwei");
        business1.setShortName("hw");
        CompanyResultWrapper.Business business2 = new CompanyResultWrapper.Business();
        business2.setShortName("hw");

        Map<Integer, CompanyResultWrapper.Business> businessMap = new HashMap<>();
        businessMap.put(1551,business1);
        businessMap.put(1553, business2);

        when(externalService.decInfoQueryList(anyList())).thenReturn(businessMap);

        UserWrapper.Owner owner1 = new UserWrapper.Owner();
        owner1.setAuthorName("edward");
        owner1.setUid(1515);
        owner1.setAuthorAvatar("xixi");
        owner1.setIdentificationDesc("yi");
        owner1.setIdentificationPic("er");
        owner1.setIdentificationStatus(1);
        owner1.setIdentificationTime(2);
        owner1.setIdentificationType(3);
        UserWrapper.Owner owner2 = new UserWrapper.Owner();
        owner2.setAuthorName("edmund");

        HashMap<Integer, UserWrapper.Owner> ownerMap = new HashMap<>();
        ownerMap.put(251, owner1);
        ownerMap.put(252, owner2);

        when(accountService.batchQueryOwner(anyList())).thenReturn(ownerMap);

        when(pushService.send(any())).thenReturn("success");
    }

    @Test
    /**
     * 测试followMsgId为0以及uid=0
     */
    public void queryFollowList1()
    {
        int uid = 1551;
        int page = 10;
        int pagesize = 5;
        int uid1 =0;
        ReflectionTestUtils.setField(appMsgService,"followMsgTid", 0);
        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid1);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);
        PageResult<AppInteractionMsgVO> pageResult = appMsgService.queryFollowList(uid1, appMsgListDTO);
        appMsgListDTO.setUid(uid);
        PageResult<AppInteractionMsgVO> pageResult1 = appMsgService.queryFollowList(uid, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult.getRows());
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult1.getRows());
    }

    @Test
    /**
     * 测试searchRecord返回结果为null
     */
    public void queryFollowList2()
    {
        int uid = 1551;
        int page = 10;
        int pagesize = 5;
        SearchMessageRecordVO searchMessageRecordVO = null;
        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);
        when(messageCenterService.searchMessageRecord(any(SearchMessageRecordDTO.class))).thenReturn(searchMessageRecordVO);
        ReflectionTestUtils.setField(appMsgService,"followMsgTid", 250);
        PageResult<AppInteractionMsgVO> pageResult1 = appMsgService.queryFollowList(uid, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult1.getRows());
    }

    @Test
    /**
     * 测试searchRecord返回结果的三条分支
     */
    public void queryFollowList() {
        int uid = 1551;
        int page = 10;
        int pagesize = 5;
        ReflectionTestUtils.setField(appMsgService,"followMsgTid", 250);

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        AppMsgWrapper.BizParams bizParams1 = new AppMsgWrapper.BizParams();
        bizParams1.setModuleCode(MsgModuleCodeEnum.DIARY.getCode());
        bizParams1.setObjectId(1098);
        bizParams1.setTitle("消息标题");
        bizParams1.setContent("消息内容");
        bizParams1.setOriginContentDesc("一朵fa");
        bizParams1.setOriginContentPic("红色!750.webp的");
        bizParams1.setUrl("fa");
        bizParams1.setExtraDataParams(new JSONObject());
        bizParams1.setTriggerUserType(UserTypeEnum.BUSINESS.getCode());
        bizParams1.setTriggerAccountId(1551);
        bizParams1.setTriggerUid(250);
        bizParams1.setRemark("备注");
        bizParams1.setNoteId(301);
        bizParams1.setTriggerId(2333);
        bizParams1.setType(13);

        AppMsgWrapper.BizParams bizParams2 = new AppMsgWrapper.BizParams();
        bizParams2.setModuleCode(MsgModuleCodeEnum.RELATIONSHIP.getCode());
        bizParams2.setTriggerUid(251);
        bizParams2.setTriggerAccountId(1552);
        bizParams2.setTriggerUserType(UserTypeEnum.DEFAULT.getCode());
        bizParams2.setTitle("标题");

        AppMsgWrapper.BizParams bizParams3 = new AppMsgWrapper.BizParams();
        bizParams3.setModuleCode(MsgModuleCodeEnum.RELATIONSHIP.getCode());
        bizParams3.setTriggerUid(251);
        bizParams3.setTriggerAccountId(1552);
        bizParams3.setTriggerUserType(UserTypeEnum.BUSINESS.getCode());

        String str1 = JSON.toJSONString(bizParams1);
        String str2 = JSON.toJSONString(bizParams2);
        String str3 = JSON.toJSONString(bizParams3);

        EsAppRecord esAppRecord1 = new EsAppRecord();
        esAppRecord1.setBizParam(str1);
        esAppRecord1.setId(222);
        esAppRecord1.setIsRead(1);
        esAppRecord1.setSendTime(1995L);
        esAppRecord1.setAppContent("hello");

        EsAppRecord esAppRecord2 = new EsAppRecord();
        esAppRecord2.setBizParam(str2);
        esAppRecord2.setId(111);
        esAppRecord2.setIsRead(0);
        esAppRecord2.setSendTime(1996L);
        esAppRecord2.setAppContent("world");

        EsAppRecord esAppRecord3 = new EsAppRecord();
        esAppRecord3.setBizParam(str3);
        esAppRecord3.setId(150);
        esAppRecord3.setIsRead(0);
        esAppRecord3.setSendTime(1997L);
        esAppRecord3.setAppContent("world");

        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(esAppRecord1);
        JSONObject jsonObject2 = (JSONObject) JSONObject.toJSON(esAppRecord2);
        JSONObject jsonObject3 = (JSONObject) JSONObject.toJSON(esAppRecord3);

        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(jsonObject1);
        jsonObjectList.add(jsonObject2);
        jsonObjectList.add(jsonObject3);

        SearchMessageRecordVO searchMessageRecordVO = new SearchMessageRecordVO();
        searchMessageRecordVO.setResult(jsonObjectList);
        searchMessageRecordVO.setTotal((long) 3);

        when(messageCenterService.searchMessageRecord(any(SearchMessageRecordDTO.class))).thenReturn(searchMessageRecordVO);

        int uid1 = 0;
        appMsgListDTO.setUid(uid1);
        PageResult<AppInteractionMsgVO> pageResult1 = appMsgService.queryFollowList(uid1, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult1.getRows());

        AppMsgWrapper.MsgJumpParams msgJumpParams1 = new AppMsgWrapper.MsgJumpParams();
        msgJumpParams1.setModuleCode(MsgModuleCodeEnum.DIARY.getCode());
        msgJumpParams1.setObjectId(1098);
        msgJumpParams1.setUrl("fa");
        msgJumpParams1.setExtraDataParams(new JSONObject());

        AppMsgWrapper.MsgJumpParams msgJumpParams2 = new AppMsgWrapper.MsgJumpParams();
        msgJumpParams2.setModuleCode(MsgModuleCodeEnum.RELATIONSHIP.getCode());
        msgJumpParams2.setObjectId(251);
        msgJumpParams2.setUrl("");
        msgJumpParams2.setExtraDataParams(new JSONObject());

        AppMsgWrapper.MsgJumpParams msgJumpParams3 = new AppMsgWrapper.MsgJumpParams();
        msgJumpParams3.setModuleCode(MsgModuleCodeEnum.RELATIONSHIP.getCode());
        msgJumpParams3.setObjectId(1552);
        msgJumpParams3.setUrl("");
        msgJumpParams3.setExtraDataParams(new JSONObject());

        AppMsgWrapper.Comment comment = new AppMsgWrapper.Comment();
        comment.setOrigin("我的评论：备注");
        AppMsgWrapper.Comment comment1 = new AppMsgWrapper.Comment();
        comment1.setOrigin("");

        appMsgListDTO.setUid(uid);
        PageResult<AppInteractionMsgVO> pageResult = appMsgService.queryFollowList(uid, appMsgListDTO);
        Assert.assertEquals(3,pageResult.getTotal());
        List<AppInteractionMsgVO> appInteractionMsgVOList = pageResult.getRows();

        Assert.assertEquals("评论了您的日记", appInteractionMsgVOList.get(0).getMsgAction());
        Assert.assertEquals("标题", appInteractionMsgVOList.get(1).getMsgAction());
        Assert.assertEquals("world", appInteractionMsgVOList.get(2).getMsgAction());
        Assert.assertEquals("消息内容", appInteractionMsgVOList.get(0).getMsgDesc());
        Assert.assertEquals("", appInteractionMsgVOList.get(1).getMsgDesc());
        Assert.assertEquals("", appInteractionMsgVOList.get(2).getMsgDesc());
        Assert.assertEquals(1995,(long) appInteractionMsgVOList.get(0).getMsgTime());
        Assert.assertEquals(1996, (long)appInteractionMsgVOList.get(1).getMsgTime());
        Assert.assertEquals(1997, (long)appInteractionMsgVOList.get(2).getMsgTime());
        Assert.assertEquals(true, appInteractionMsgVOList.get(0).getIsRead());
        Assert.assertEquals(false, appInteractionMsgVOList.get(1).getIsRead());
        Assert.assertEquals(false, appInteractionMsgVOList.get(2).getIsRead());
        Assert.assertEquals(MsgModuleCodeEnum.DIARY.getCode(), appInteractionMsgVOList.get(0).getContentType());
        Assert.assertEquals(MsgModuleCodeEnum.RELATIONSHIP.getCode(), appInteractionMsgVOList.get(1).getContentType());
        Assert.assertEquals(MsgModuleCodeEnum.RELATIONSHIP.getCode(), appInteractionMsgVOList.get(2).getContentType());
        Assert.assertEquals("红色的", appInteractionMsgVOList.get(0).getOriginContentPic());
        Assert.assertEquals("", appInteractionMsgVOList.get(1).getOriginContentPic());
        Assert.assertEquals("", appInteractionMsgVOList.get(2).getOriginContentPic());
        Assert.assertEquals(msgJumpParams1,appInteractionMsgVOList.get(0).getMsgJumpParams());
        Assert.assertEquals(msgJumpParams2,appInteractionMsgVOList.get(1).getMsgJumpParams());
        Assert.assertEquals(msgJumpParams3,appInteractionMsgVOList.get(2).getMsgJumpParams());
        Assert.assertEquals(comment, appInteractionMsgVOList.get(0).getMsgContent().getComment());
        Assert.assertEquals(comment1, appInteractionMsgVOList.get(1).getMsgContent().getComment());
        Assert.assertEquals(comment1, appInteractionMsgVOList.get(2).getMsgContent().getComment());
        Assert.assertEquals("hw", appInteractionMsgVOList.get(0).getNickName());
        Assert.assertEquals("edward", appInteractionMsgVOList.get(1).getNickName());
        Assert.assertEquals(null, appInteractionMsgVOList.get(2).getNickName());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(0).getIdentificationStatus());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(0).getIdentificationType());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(0).getIdentificationTime());
        Assert.assertEquals("", appInteractionMsgVOList.get(0).getIdentificationDesc());
        Assert.assertEquals("", appInteractionMsgVOList.get(0).getIdentificationPic());
        Assert.assertEquals(1, (long)appInteractionMsgVOList.get(1).getIdentificationStatus());
        Assert.assertEquals(3, (long)appInteractionMsgVOList.get(1).getIdentificationType());
        Assert.assertEquals(2, (long)appInteractionMsgVOList.get(1).getIdentificationTime());
        Assert.assertEquals("yi", appInteractionMsgVOList.get(1).getIdentificationDesc());
        Assert.assertEquals("er", appInteractionMsgVOList.get(1).getIdentificationPic());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(2).getIdentificationStatus());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(2).getIdentificationType());
        Assert.assertEquals(0, (long)appInteractionMsgVOList.get(2).getIdentificationTime());
        Assert.assertEquals("", appInteractionMsgVOList.get(2).getIdentificationDesc());
        Assert.assertEquals("", appInteractionMsgVOList.get(2).getIdentificationPic());
    }

    @Test
    /**
     * 测试uid=0和interactionNodeIds = ""
     */
    public void queryInteractionList() {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        int uid1 = 0;
        ReflectionTestUtils.setField(appMsgService,"interactionNodeIds", "");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid1);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        PageResult<AppInteractionMsgVO> pageResult = appMsgService.queryInteractionList(uid1, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult.getRows());

        appMsgListDTO.setUid(uid);
        PageResult<AppInteractionMsgVO> pageResult1 = appMsgService.queryInteractionList(uid, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult1.getRows());
    }

    @Test
    /**
     * 测试interactionNodeIds输入不合法
     */
    public void queryInteractionList1()
    {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        ReflectionTestUtils.setField(appMsgService,"interactionNodeIds", "152648");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        PageResult<AppInteractionMsgVO> pageResult1 = appMsgService.queryInteractionList(uid, appMsgListDTO);
        Assert.assertEquals(ResponseUtils.buildPageResult().getRows(),pageResult1.getRows());
    }

    @Test
    /**
     * 测试UID=0以及proceMsgFilterType为""
     */
    public void queryProcessList()
    {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        int uid1 =0;
        ReflectionTestUtils.setField(appMsgService,"processMsgFilterType", "");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid1);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        ListMsgVO<AppProcessMsgVO> pageResult1 = appMsgService.queryProcessList(uid1, appMsgListDTO);
        Assert.assertTrue(pageResult1.getMsgRecords().isEmpty());

        appMsgListDTO.setUid(uid);
        ListMsgVO<AppProcessMsgVO> pageResult = appMsgService.queryProcessList(uid, appMsgListDTO);
        Assert.assertTrue(pageResult.getMsgRecords().isEmpty());
    }

    @Test
    /**
     * 测试processMsgFilterType格式不合法
     */
    public void queryProcessList1()
    {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        ReflectionTestUtils.setField(appMsgService,"processMsgFilterType", "afafafa");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        ListMsgVO<AppProcessMsgVO> pageResult = appMsgService.queryProcessList(uid, appMsgListDTO);
        Assert.assertTrue(pageResult.getMsgRecords().isEmpty());
    }

    @Test
    /**
     * 测试listMsg()返回的listMsgVO各值为0
     */
    public void queryProcessList2()
    {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        ReflectionTestUtils.setField(appMsgService,"processMsgFilterType", "9,13,15");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        ListMsgVO listMsgVO;
        listMsgVO = ListMsgVO.builder()
                .msgRecords(new ArrayList<>())
                .totalPages(0L)
                .totalRecords(0L)
                .build();
        when(messageCenterService.listMsg(any(ListMsgDTO.class))).thenReturn(listMsgVO);

        ListMsgVO<AppProcessMsgVO> pageResult = appMsgService.queryProcessList(uid, appMsgListDTO);
        Assert.assertTrue(pageResult.getMsgRecords().isEmpty());
    }

    @Test
    /**
     * 测试三个ListMsg()返回结果后的三条分支
     */
    public void queryProcessList3()
    {
        int uid = 1551;
        int page = 5;
        int pagesize = 10;
        ReflectionTestUtils.setField(appMsgService,"processMsgFilterType", "9,13,15");

        AppMsgListDTO appMsgListDTO = new AppMsgListDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setPage(page);
        appMsgListDTO.setPageSize(pagesize);

        AppMsgWrapper.BizData bizData1 = new AppMsgWrapper.BizData();
        bizData1.setYid(1551);
        bizData1.setType(9);

        AppMsgWrapper.BizData bizData2 = new AppMsgWrapper.BizData();
        bizData2.setYid(1551);
        bizData2.setType(10);

        MsgRecord msgRecord1 = new MsgRecord();
        msgRecord1.setId("11");
        msgRecord1.setTitle("xixi");
        msgRecord1.setContent("haha");
        msgRecord1.setIsRead(0);
        msgRecord1.setSendTime(1996);
        msgRecord1.setBizdata(JSONObject.toJSONString(bizData1));


        MsgRecord msgRecord2 = new MsgRecord();
        msgRecord2.setId("12");
        msgRecord2.setTitle("erer");
        msgRecord2.setContent("sasa");
        msgRecord2.setIsRead(1);
        msgRecord2.setSendTime(1997);
        //msgRecord2.setBizdata("xixusanf");

        MsgRecord msgRecord3 = new MsgRecord();
        msgRecord3.setId("13");
        msgRecord3.setTitle("sansan");
        msgRecord3.setContent("shanshan");
        msgRecord3.setIsRead(1);
        msgRecord3.setSendTime(1997);
        msgRecord3.setBizdata(JSONObject.toJSONString(bizData2));

        List<MsgRecord> msgRecordList = new ArrayList<>();
        msgRecordList.add(msgRecord1);
        msgRecordList.add(msgRecord2);
        msgRecordList.add(msgRecord3);

        ListMsgVO listMsgVO = new ListMsgVO();
        listMsgVO.setMsgRecords(msgRecordList);

        when(messageCenterService.listMsg(any(ListMsgDTO.class))).thenReturn(listMsgVO);

        ListMsgVO<AppProcessMsgVO> pageResult = appMsgService.queryProcessList(uid, appMsgListDTO);

        Assert.assertEquals(12, (long)pageResult.getMsgRecords().get(0).getId());
        Assert.assertEquals(13, (long)pageResult.getMsgRecords().get(1).getId());
        Assert.assertEquals("sasa", pageResult.getMsgRecords().get(0).getContent());
        Assert.assertEquals("shanshan", pageResult.getMsgRecords().get(1).getContent());
        Assert.assertEquals("erer", pageResult.getMsgRecords().get(0).getTitle());
        Assert.assertEquals("sansan", pageResult.getMsgRecords().get(1).getTitle());
        Assert.assertEquals(1997, (long)pageResult.getMsgRecords().get(0).getSendTime());
        Assert.assertEquals(1997, (long)pageResult.getMsgRecords().get(1).getSendTime());
        Assert.assertEquals(1, (long)pageResult.getMsgRecords().get(0).getIsRead());
        Assert.assertEquals(1, (long)pageResult.getMsgRecords().get(1).getIsRead());
    }

    @Test
    public void msgSetHasRead()
    {
        /**
         * 输入参数
         */
        int uid = 1551;
        int uid1 = 0;
        int msgId = 0;
        int readAll =4;
        int readAll1 = 0;
        String firstId = "";
        String firstId1 = "haha";
        String firstId2 = "xixi";
        String firstId3 = "sasa";
        int appOsType = 0;
        String appVersion="";
        int cityId=0;
        int appId=0;

        /**
         * 模拟的返回结果
         */
        MessagePushUser messagePushUser = new MessagePushUser();
        messagePushUser.setMsgId("12,13,14,15,16");

        MessagePushUser messagePushUser1 = new MessagePushUser();
        messagePushUser1.setMsgId("afasfAf");

        MessagePush messagePush = new MessagePush();
        messagePush.setId(1551);
        messagePush.setPushImg("ab");
        messagePush.setTitle("cd");
        messagePush.setContent("ef");
        messagePush.setPushTime(1995);
        messagePush.setUrl("gh");

        MessagePush messagePush1 = new MessagePush();

        AppAd appAd = new AppAd();
        appAd.setId(2015);
        appAd.setAdName("yi");
        appAd.setNormalImg("er");
        appAd.setBeginTime(1991);
        appAd.setLinkUrl("san");
        AppAd appAd1 = new AppAd();

        List<MessagePush> messagePushList = new ArrayList<>();
        messagePushList.add(messagePush);
        messagePushList.add(messagePush1);

        List<AppAd> appAdList = new ArrayList<>();
        appAdList.add(appAd);
        appAdList.add(appAd1);

        when(messagePushRepository.queryPushMsg(anyInt(),anyInt(),anyInt(),anyList())).thenReturn(messagePushList);
        when(appAdRepository.queryAppAds(anyInt(),anyInt(),anyInt(),anyInt(),anyInt(),anyString())).thenReturn(appAdList);
        when(messagePushUserRepository.findByFirstIdOrderById(firstId1)).thenReturn(null);
        when(messagePushUserRepository.findByFirstIdOrderById(firstId2)).thenReturn(java.util.Optional.of(messagePushUser));
        when(messagePushUserRepository.findByFirstIdOrderById(firstId3)).thenReturn(java.util.Optional.of(messagePushUser1));

        /**
         * uid = 0,readAll=0
         */
        AppReadClearDTO appMsgListDTO = new AppReadClearDTO();
        appMsgListDTO.setUid(uid1);
        appMsgListDTO.setMsgId(msgId);
        appMsgListDTO.setReadAll(readAll1);
        appMsgListDTO.setFirstId(firstId);
        appMsgListDTO.setAppOsType(appOsType);
        appMsgListDTO.setAppVersion(appVersion);
        appMsgListDTO.setCityId(cityId);
        appMsgListDTO.setAppId(appId);
        List<MsgSetHasReadVO> listResult = appMsgService.msgSetHasRead(uid1, appMsgListDTO);
        Assert.assertEquals(0, listResult.size());

        /**
         * uid=0,readAll>0,firstId=""
         */
        AppReadClearDTO appMsgListDTO1 = new AppReadClearDTO();
        appMsgListDTO.setUid(uid1);
        appMsgListDTO.setMsgId(msgId);
        appMsgListDTO.setReadAll(readAll);
        appMsgListDTO.setFirstId(firstId);
        appMsgListDTO.setAppOsType(appOsType);
        appMsgListDTO.setAppVersion(appVersion);
        appMsgListDTO.setCityId(cityId);
        appMsgListDTO.setAppId(appId);
        List<MsgSetHasReadVO> listResult1 = appMsgService.msgSetHasRead(uid1, appMsgListDTO1);
        Assert.assertEquals(1991, (long)listResult1.get(1).getMsgTime());
        Assert.assertEquals(0, (long)listResult1.get(2).getMsgTime());
        Assert.assertEquals(0, (long)listResult1.get(3).getMsgTime());
        Assert.assertEquals("msg1551", listResult1.get(0).getMsgId());
        Assert.assertEquals("ad2015", listResult1.get(1).getMsgId());
        Assert.assertEquals("msg0", listResult1.get(2).getMsgId());
        Assert.assertEquals("ad0", listResult1.get(3).getMsgId());

        /**
         * messagePushUserRepository.findByFirstIdOrderById()返回null
         */
        AppReadClearDTO appMsgListDTO2 = new AppReadClearDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setMsgId(msgId);
        appMsgListDTO.setReadAll(readAll);
        appMsgListDTO.setFirstId(firstId1);
        appMsgListDTO.setAppOsType(appOsType);
        appMsgListDTO.setAppVersion(appVersion);
        appMsgListDTO.setCityId(cityId);
        appMsgListDTO.setAppId(appId);
        List<MsgSetHasReadVO> listResult2 = appMsgService.msgSetHasRead(uid, appMsgListDTO2);
        Assert.assertEquals(4, listResult2.size());
        Assert.assertEquals(1995, (long)listResult2.get(0).getMsgTime());
        Assert.assertEquals(1991, (long)listResult2.get(1).getMsgTime());
        Assert.assertEquals(0, (long)listResult2.get(2).getMsgTime());
        Assert.assertEquals(0, (long)listResult2.get(3).getMsgTime());
        Assert.assertEquals("msg1551", listResult2.get(0).getMsgId());
        Assert.assertEquals("ad2015", listResult2.get(1).getMsgId());
        Assert.assertEquals("msg0", listResult2.get(2).getMsgId());
        Assert.assertEquals("ad0", listResult2.get(3).getMsgId());

        /**
         * messagePushUserRepository.findByFirstIdOrderById()返回可用结果
         */
        AppReadClearDTO appMsgListDTO3 = new AppReadClearDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setMsgId(msgId);
        appMsgListDTO.setReadAll(readAll);
        appMsgListDTO.setFirstId(firstId2);
        appMsgListDTO.setAppOsType(appOsType);
        appMsgListDTO.setAppVersion(appVersion);
        appMsgListDTO.setCityId(cityId);
        appMsgListDTO.setAppId(appId);
        List<MsgSetHasReadVO> listResult3 = appMsgService.msgSetHasRead(uid, appMsgListDTO3);
        Assert.assertEquals(4, listResult3.size());
        Assert.assertEquals(1995, (long)listResult3.get(0).getMsgTime());
        Assert.assertEquals(1991, (long)listResult3.get(1).getMsgTime());
        Assert.assertEquals(0, (long)listResult3.get(2).getMsgTime());
        Assert.assertEquals(0, (long)listResult3.get(3).getMsgTime());
        Assert.assertEquals("msg1551", listResult3.get(0).getMsgId());
        Assert.assertEquals("ad2015", listResult3.get(1).getMsgId());
        Assert.assertEquals("msg0", listResult3.get(2).getMsgId());
        Assert.assertEquals("ad0", listResult3.get(3).getMsgId());

        /**
         * messagePushUserRepository.findByFirstIdOrderById()返回结果的属性后续操作会抛出异常
         */
        AppReadClearDTO appMsgListDTO4 = new AppReadClearDTO();
        appMsgListDTO.setUid(uid);
        appMsgListDTO.setMsgId(msgId);
        appMsgListDTO.setReadAll(readAll);
        appMsgListDTO.setFirstId(firstId3);
        appMsgListDTO.setAppOsType(appOsType);
        appMsgListDTO.setAppVersion(appVersion);
        appMsgListDTO.setCityId(cityId);
        appMsgListDTO.setAppId(appId);
        List<MsgSetHasReadVO> listResult4 = appMsgService.msgSetHasRead(uid, appMsgListDTO4);
        Assert.assertEquals(4, listResult4.size());
        Assert.assertEquals(1995, (long)listResult4.get(0).getMsgTime());
        Assert.assertEquals(1991, (long)listResult4.get(1).getMsgTime());
        Assert.assertEquals(0, (long)listResult4.get(2).getMsgTime());
        Assert.assertEquals(0, (long)listResult4.get(3).getMsgTime());
        Assert.assertEquals("msg1551", listResult4.get(0).getMsgId());
        Assert.assertEquals("ad2015", listResult4.get(1).getMsgId());
        Assert.assertEquals("msg0", listResult4.get(2).getMsgId());
        Assert.assertEquals("ad0", listResult4.get(3).getMsgId());

    }

}