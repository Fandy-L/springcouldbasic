package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONArray;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.response.MsgResult;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;

import java.util.List;
import java.util.Map;


/**
 * @author juntao.guo
 */
public interface MessageCenterService {

    /**
     * 查询短信及APP消息记录
     *
     * @param params
     * @return
     */
    SearchMessageRecordVO searchMessageRecord(SearchMessageRecordDTO params);

    /**
     * 查询消息记录
     *
     * @param params
     * @return
     */
    ListMsgVO listMsg(ListMsgDTO params);

    /**
     * 设置消息已读
     *
     * @param id
     * @return
     */
    MsgCenterResponse setAppMsgRead(int id);

    /**
     * 批量设置消息已读-根据消息ID
     *
     * @param msgIds
     */
    void setAppMsgHasReadBatch(List<Integer> msgIds);

    /**
     * 批量设置消息已读-根据消息ID
     * @param msgIdStr
     * @return
     */
    MsgCenterResponse setAppMsgHasReadBatch(String msgIdStr);

    /**
     * 批量设置消息已读-根据用户及节点ID
     * @param setAppMsgStatusNodeDTO
     * @return
     */
    MsgResult setAppMsgStatusByUidAndNodeId(SetAppMsgStatusNodeDTO setAppMsgStatusNodeDTO);

    /**
     * 批量设置消息已读-根据用户及节点ID
     * @param uid
     * @param nodeIds
     * @param isRead
     * @return
     */
    MsgResult setAppMsgStatusByUidAndNodeId(Integer uid, List<Integer> nodeIds, Integer isRead);

    /**
     * 根据模板id和uid批量设置已读
     *
     * @param tids
     * @param uid
     * @return
     */
    MsgCenterResponse setAppMsgReaderByTidAndUid(List<Integer> tids, Integer uid);

    /**
     * 查询用户消息状态
     *
     * @param userId
     * @return
     */
    Map<Integer, Boolean> getUserMsgState(String userId);

    /**
     * 设置用户消息已读状态
     *
     * @param params
     * @param readFlg
     * @return
     */
    MsgCenterResponse setUserMsgRead(JSONArray params, Boolean readFlg);

    /**
     * 根据用户及模板设置消息状态
     * @param setAppMsgStatusByTidDTO
     * @return
     */
    MsgResult setAppMsgStatusByUidAndTid(SetAppMsgStatusByTidDTO setAppMsgStatusByTidDTO);

    /**
     * 根据 uid 和 批量模板id 查询APP消息数量
     *
     * @param uidList
     * @param tidList
     * @param isRead
     * @return
     */
    MsgResult getAppMsgRecordCountByUidAndTid(List<Integer> uidList, List<Integer> tidList, Integer isRead);

    /**
     * 根据 uid 和 批量节点id 查询APP消息数量
     *
     * @param uidList
     * @param nodeList
     * @param isRead
     * @return
     */
    MsgResult getAppMsgRecordCountByUidAndNodeId(List<Integer> uidList, List<Integer> nodeList, Integer isRead);

}
