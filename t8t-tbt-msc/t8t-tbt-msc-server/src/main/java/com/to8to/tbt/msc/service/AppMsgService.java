package com.to8to.tbt.msc.service;

import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.ResultStatusResponse;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.vo.wechat.WeChatResMsgVO;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface AppMsgService {

    /**
     * 查询关注消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    PageResult<AppInteractionMsgVO> queryFollowList(Integer uid, AppMsgListDTO params);

    /**
     * 查询互动消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    PageResult<AppInteractionMsgVO> queryInteractionList(Integer uid, AppMsgListDTO params);

    /**
     * 查询系统消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    PageResult<AppSystemMsgVO> aggregationSystemList(Integer uid, AppSystemListDTO params);

    /**
     * 聚合消息
     *
     * @param uid
     * @param params
     * @return
     */
    AppAggregationMsgVO aggregationMsg(Integer uid, AppMsgMainDTO params);

    /**
     * 装修进度消息列表
     *
     * @param uid
     * @param params
     * @return
     */
    ListMsgVO queryProcessList(Integer uid, AppMsgListDTO params);

    /**
     * 消息设置已读
     *
     * @param uid
     * @param params
     * @return
     */
    List<MsgSetHasReadVO> msgSetHasRead(Integer uid, AppReadClearDTO params);

    /**
     * 发送微信告警消息－API
     *
     * @param sendWeChatAlarmMsgDTO
     * @return
     */
    ResResult<ResultStatusResponse<String>> sendWeChatAlarmMsg(SendWeChatAlarmMsgDTO sendWeChatAlarmMsgDTO);

    /**
     * 发送微信消息
     *
     * @param content
     * @return
     */
    WeChatResMsgVO sendWeChatAlarmMsg(String content);
}
