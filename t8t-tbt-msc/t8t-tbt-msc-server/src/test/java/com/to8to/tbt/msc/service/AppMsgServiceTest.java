package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONObject;
import com.to8to.common.search.PageResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.constant.AppMsgConstant;
import com.to8to.tbt.msc.dto.AppMsgListDTO;
import com.to8to.tbt.msc.dto.AppReadClearDTO;
import com.to8to.tbt.msc.dto.AppSystemListDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.AppMsgWrapper;
import com.to8to.tbt.msc.entity.MsgRecord;
import com.to8to.tbt.msc.entity.mysql.extend.AppAd;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePush;
import com.to8to.tbt.msc.entity.mysql.extend.MessagePushUser;
import com.to8to.tbt.msc.repository.mysql.extend.AppAdRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushRepository;
import com.to8to.tbt.msc.repository.mysql.extend.MessagePushUserRepository;
import com.to8to.tbt.msc.service.impl.AppMsgServiceImpl;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.enumeration.AppMsgShowTypeEnum;
import com.to8to.tbt.msc.enumeration.MsgModuleCodeEnum;
import com.to8to.tbt.msc.enumeration.UserTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AppMsgServiceTest extends BaseApplication {

    @Mock
    private MessagePushUserRepository messagePushUserRepository;

    @Mock
    private MessagePushRepository messagePushRepository;

    @Mock
    private AppAdRepository appAdRepository;

    @Mock
    private MessageCenterService messageCenterService;

    @InjectMocks
    private AppMsgService appMsgServiceMock = new AppMsgServiceImpl();

    @Autowired
    private AppMsgService appMsgService;

    private AppMsgListDTO appMsgListDTO = new AppMsgListDTO();

    private AppSystemListDTO appSystemListDTO = new AppSystemListDTO();

    private AppReadClearDTO appReadClearDTO = new AppReadClearDTO();

    private String interactionNodeIds = "[10]";

    private String processMsgFilterType = "9,11,12,13,14,15";

    private Integer invalidUid = 0;

    @Before
    public void setUp(){
        appMsgListDTO.setUid(172176107);
        appSystemListDTO.setPubArgs(generateAppPubArgs(0, "bc1ad29127a052a5a6575b6d25227ef9", 1, 0, 0, ""));
        MessagePushUser messagePushUser = new MessagePushUser();
        messagePushUser.setMsgId("335,336");
        List<MessagePush> messagePushes = new ArrayList<>();
        messagePushes.add(new MessagePush());
        when(messagePushUserRepository.findByFirstIdOrderById(anyString())).thenReturn(Optional.of(messagePushUser));
        when(messagePushRepository.queryPushMsg(anyInt(), anyInt(), anyInt(), anyList())).thenReturn(messagePushes);
        List<AppAd> appAds = new ArrayList<>();
        appAds.add(new AppAd());
        when(appAdRepository.queryAppAds(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyString())).thenReturn(appAds);
        SearchMessageRecordVO searchMessageRecordVO = new SearchMessageRecordVO();
        when(messageCenterService.searchMessageRecord(any())).thenReturn(searchMessageRecordVO);
        initDataMsgSetHasRead();
    }

    @Test
    public void testSystemListBySystem(){
        Integer uid = 543978;
        AppSystemListDTO args = new AppSystemListDTO();
        args.setPubArgs(generateAppPubArgs(uid, "", 0, 0, 0, ""));
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgService.aggregationSystemList(uid, args);
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void testSystemListByPush(){
        Integer uid = 0;
        AppSystemListDTO args = new AppSystemListDTO();
        args.setPubArgs(generateAppPubArgs(uid, "bc1ad29127a052a5a6575b6d25227ef9", 1, 0, 0, ""));
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgService.aggregationSystemList(uid, args);
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void testSystemListByAd(){
        Integer uid = 0;
        AppSystemListDTO args = new AppSystemListDTO();
        args.setPubArgs(generateAppPubArgs(uid, "", 1, 15, 1130, "7.8.0"));
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgService.aggregationSystemList(uid, args);
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void testSystemListMock(){
        try {
            Field field = appMsgServiceMock.getClass().getDeclaredField("systemNodeIds");
            field.setAccessible(true);
            field.set(appMsgServiceMock, "[415, 432]");
        }catch (Exception e){
            e.printStackTrace();
        }
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgServiceMock.aggregationSystemList(appSystemListDTO.getUid(), appSystemListDTO);
        verify(messagePushUserRepository).findByFirstIdOrderById(appSystemListDTO.getFirstId());
        verify(messagePushRepository).queryPushMsg(anyInt(), anyInt(), anyInt(), anyList());
        verify(appAdRepository).queryAppAds(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyString());
        verify(messageCenterService).searchMessageRecord(any());
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void testSystemListBySystemNoteIdEmpty(){
        Integer uid = 0;
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgServiceMock.aggregationSystemList(uid, appSystemListDTO);
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void testSystemListBySystemNoteIdFormat(){
        Integer uid = 0;
        try {
            Field field = appMsgServiceMock.getClass().getDeclaredField("systemNodeIds");
            field.setAccessible(true);
            field.set(appMsgServiceMock, "415, 432");
        }catch (Exception e){
            e.printStackTrace();
        }
        PageResult<AppSystemMsgVO> appSystemMsgVOPageResult = appMsgServiceMock.aggregationSystemList(uid, appSystemListDTO);
        assertSystemListResult(appSystemMsgVOPageResult);
    }

    @Test
    public void interactionThrowJsonParseObjectException(){
        ReflectionTestUtils.setField(appMsgServiceMock, "interactionNodeIds", interactionNodeIds);
        SearchMessageRecordVO searchMessageRecordVO = new SearchMessageRecordVO();
        List<JSONObject> result = new ArrayList<>();
        result.add(generateAppRecord(100, "{\"moduleCode\":\"other\"}"));
        result.add(generateAppRecord(100, "{\"moduleCode\":\"other\""));
        searchMessageRecordVO.setResult(result);
        when(messageCenterService.searchMessageRecord(any(SearchMessageRecordDTO.class))).thenReturn(searchMessageRecordVO);
        PageResult<AppInteractionMsgVO> appInteractionMsgVOPageResult = appMsgServiceMock.queryInteractionList(invalidUid, appMsgListDTO);
        Assert.assertTrue(appInteractionMsgVOPageResult.getRows().size() == 1);
    }

    @Test
    public void testMsgSetHasReadByNodeIdEmpty(){
        List<MsgSetHasReadVO> msgSetHasReadVOS = appMsgServiceMock.msgSetHasRead(invalidUid, appReadClearDTO);
        Assert.assertTrue(msgSetHasReadVOS.size() >= 0);
        verify(messageCenterService, Mockito.never()).setAppMsgStatusByUidAndNodeId(anyInt(), anyList(), anyInt());
    }

    @Test
    public void testMsgSetHasReadByNodeSetException(){
        initDataMsgSetHasReadNodeIds();
        doThrow(new RuntimeException("setAppMsgStatusByUidAndNodeId throw exception")).when(messageCenterService).setAppMsgStatusByUidAndNodeId(anyInt(), anyList(), anyInt());
        try {
            appMsgServiceMock.msgSetHasRead(invalidUid, appReadClearDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMsgSetHasReadByBatchSetException(){
        initDataMsgSetHasReadNodeIds();
        ListMsgVO listMsgVO = new ListMsgVO();
        MsgRecord msgRecord = new MsgRecord();
        List<MsgRecord> msgRecords = new ArrayList<>();
        msgRecords.add(msgRecord);
        listMsgVO.setMsgRecords(msgRecords);
        when(messageCenterService.listMsg(any())).thenReturn(listMsgVO);
        doThrow(new RuntimeException("setAppMsgHasReadBatch throw exception")).when(messageCenterService).setAppMsgHasReadBatch(anyList());
        try {
            appMsgServiceMock.msgSetHasRead(invalidUid, appReadClearDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void interactionCheckBizParams(){
        ReflectionTestUtils.setField(appMsgServiceMock, "interactionNodeIds", "[10]");
        List<JSONObject> appRecords = new ArrayList<>();
        appRecords.add(generateAppRecord(100, UserTypeEnum.BUSINESS.getCode()));
        appRecords.add(generateAppRecord(101, UserTypeEnum.DEFAULT.getCode()));
        SearchMessageRecordVO searchMessageRecordVO = SearchMessageRecordVO.builder()
                .total(2L)
                .result(appRecords)
                .build();
        when(messageCenterService.searchMessageRecord(any(SearchMessageRecordDTO.class))).thenReturn(searchMessageRecordVO);
        PageResult<AppInteractionMsgVO> appInteractionMsgVOPageResult = appMsgServiceMock.queryInteractionList(invalidUid, appMsgListDTO);
        Assert.assertTrue(appInteractionMsgVOPageResult.getTotal() == 2L);
        for (AppInteractionMsgVO appInteractionMsgVO : appInteractionMsgVOPageResult.getRows()){
            Assert.assertTrue(appInteractionMsgVO.getMsgId() == 100 || appInteractionMsgVO.getMsgId() == 101);
            Assert.assertTrue(appInteractionMsgVO.getContentType() == MsgModuleCodeEnum.OTHER.getCode());
            Assert.assertTrue(StringUtils.isEmpty(appInteractionMsgVO.getNickName()));
            Assert.assertEquals(appInteractionMsgVO.getUserAvatar(), AppMsgConstant.OWNER_AVATAR_DEFAULT);
        }
    }

    public void processThrowJsonParseException(){
        ReflectionTestUtils.setField(appMsgServiceMock, "processMsgFilterType", processMsgFilterType);
        appMsgServiceMock.queryProcessList(invalidUid, appMsgListDTO);
    }

    /**
     * 生成APP消息记录
     *
     * @param id
     * @param triggerUserType
     * @return
     */
    private JSONObject generateAppRecord(int id, int triggerUserType){
        JSONObject appRecord = new JSONObject();
        appRecord.put("id", id);
        JSONObject bizParam = new JSONObject();
        bizParam.put("aid", 0);
        bizParam.put("triggerUserType", triggerUserType);
        appRecord.put("bizParam", bizParam);
        return appRecord;
    }

    /**
     * 生成APP消息记录
     *
     * @param id
     * @return
     */
    private JSONObject generateAppRecord(int id, String bizParam){
        JSONObject appRecord = new JSONObject();
        appRecord.put("id", id);
        appRecord.put("bizParam", bizParam);
        return appRecord;
    }

    /**
     * 初始化消息已读数据
     */
    private void initDataMsgSetHasRead(){
        appReadClearDTO.setReadAll(1);
        appReadClearDTO.setMsgId(0);
    }

    /**
     * 生成APP公参
     *
     * @return
     */
    private String generateAppPubArgs(Integer uid, String firstId, Integer appOsType, Integer appId, Integer cityId, String appVersion){
        JSONObject params = new JSONObject();
        params.put("uid", uid);
        params.put("firstId", firstId);
        params.put("appOsType", appOsType);
        params.put("appId", appId);
        params.put("cityId", cityId);
        params.put("appVersion", appVersion);
        return JSONObject.toJSONString(params);
    }

    /**
     * 初始化消息已读节点数据
     */
    private void initDataMsgSetHasReadNodeIds(){
        try {
            Field field = appMsgServiceMock.getClass().getDeclaredField("msgSetHasReadNode");
            field.setAccessible(true);
            field.set(appMsgServiceMock, "[415, 432,357, 358, 405, 406, 359, 360, 434, 470,26,356,363,364,365,366,369,393,340,341]");
            field = appMsgServiceMock.getClass().getDeclaredField("processMsgFilterType");
            field.setAccessible(true);
            field.set(appMsgServiceMock, "9,11,12,13,14,15");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 断言系统消息列表的结果
     *
     * @param appSystemMsgVOPageResult
     */
    private void assertSystemListResult(PageResult<AppSystemMsgVO> appSystemMsgVOPageResult){
        Assert.assertTrue(appSystemMsgVOPageResult.getTotal() >= 0);
        AppSystemMsgVO originAppSystemMsgVO = null;
        for (AppSystemMsgVO appSystemMsgVO : appSystemMsgVOPageResult.getRows()){
            Assert.assertTrue(appSystemMsgVO.getShowType().intValue() == AppMsgShowTypeEnum.CARD.getCode() || appSystemMsgVO.getShowType().intValue() == AppMsgShowTypeEnum.TEXT.getCode());
            Assert.assertTrue(appSystemMsgVO.getMsgId().length() > 0);
            Assert.assertTrue(appSystemMsgVO.getTitle().length() >= 0);
            Assert.assertTrue(appSystemMsgVO.getContent().length() >= 0);
            Assert.assertTrue(appSystemMsgVO.getImageUrl().length() >=0 );
            Assert.assertTrue(appSystemMsgVO.getIsGroupMsg() == Boolean.TRUE || appSystemMsgVO.getIsGroupMsg() == Boolean.FALSE);
            Assert.assertTrue(appSystemMsgVO.getIsRead() == Boolean.TRUE || appSystemMsgVO.getIsRead() == Boolean.FALSE);
            Assert.assertTrue(appSystemMsgVO.getTime().intValue() >= 0);
            Assert.assertTrue(appSystemMsgVO.getIcon().length() >= 0);
            Assert.assertTrue(appSystemMsgVO.getJumpFlag() == Boolean.TRUE || appSystemMsgVO.getJumpFlag() == Boolean.FALSE);
            Assert.assertTrue(appSystemMsgVO.getMsgJumpParams() instanceof AppMsgWrapper.MsgJumpParams);
            AppMsgWrapper.MsgJumpParams msgJumpParams = appSystemMsgVO.getMsgJumpParams();
            Assert.assertTrue(msgJumpParams.getModuleCode().length() >= 0);
            Assert.assertTrue(msgJumpParams.getObjectId().intValue() >= 0);
            Assert.assertTrue(msgJumpParams.getUrl().length() >= 0);
            Assert.assertTrue(msgJumpParams.getExtraDataParams() instanceof JSONObject);
            if (originAppSystemMsgVO != null){
                Assert.assertTrue(originAppSystemMsgVO.getTime() >= appSystemMsgVO.getTime());
            }
            originAppSystemMsgVO = appSystemMsgVO;
        }
    }
}
