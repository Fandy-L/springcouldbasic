package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONArray;
import com.to8to.tbt.msc.dto.PcMsgRequestDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.PcRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @author edmund.yu
 */
public interface PcMsgService {

    /**
     * 发送PC消息
     * @param pcMsgRequestDTO
     * @return
     */
    MsgCenterResponse sendPcMsg(PcMsgRequestDTO pcMsgRequestDTO);

    /**
     * 获取小黄条信息
     * @param params
     * @return
     */
    PcRecordVO getMsgHuang(JSONArray params);

    /**
     * 删除消息
     * @param jsonArray
     * @return
     */
    MsgCenterResponse delMsg(JSONArray jsonArray);


    /**
     *获取该用户的大分类未读消息的数量
     * @param uid
     * @return
     */
    Map<String, Integer> getUnreadBig(String uid);

    /**
     * 设置用户不需要接受消息类别
     * @param params
     * @return
     */
    MsgCenterResponse setSmallNeed(JSONArray params);

    /**
     *获取用户设置不需要读取的小类信息
     * @param uid
     * @return
     */
    List<Integer> getUserSmallNeed(String uid);

    /**
     * 搜索PC消息中某个大类的消息
     * @param params
     * @return
     */
    ListMsgVO searchMsgBig(JSONArray params);

    /**
     * 设置消息已读
     * @param id
     * @return
     */
    MsgCenterResponse setMsgRead(String id);
}
