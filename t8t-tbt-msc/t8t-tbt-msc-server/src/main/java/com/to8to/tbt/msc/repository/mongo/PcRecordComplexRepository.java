package com.to8to.tbt.msc.repository.mongo;

import com.mongodb.client.result.UpdateResult;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecord;
import com.to8to.tbt.msc.entity.mongo.MongoPcRecordList;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
public interface PcRecordComplexRepository {
    /**
     * 获取小黄条消息
     * @param uid
     * @param num
     * @return
     */
    MongoPcRecord getMsgHuang(String uid, int num);

    /**
     * 删除
     * @param id
     */
    void delMsg(String id);

    /**
     * 获取大类未读
     * @param uid
     * @return
     */
    Map<String, Integer> getUnreadBig(String uid);

    /**
     * 设置用户不需要消息类型
     * @param uid
     * @param smalls
     */
    void setSmallNeed(String uid, List<Integer> smalls);

    /**
     * 获取用户不需要消息
     * @param uid
     * @return
     */
    List<Integer> getUserSmallNeed(String uid);

    /**
     * 获取消息
     * @param req
     * @param bigType
     * @param skip
     * @param limit
     * @return
     */
    MongoPcRecordList searchMsg(ListMsgDTO req, int bigType, int skip, int limit);

    /**
     * 获取PC未读消息
     * @param id
     * @return
     */
    UpdateResult setPcMsgRead(String id);

    /**
     * 根据_id获取消息
     * @param id
     * @return
     */
    MongoPcRecord getPcMsgById(String id);
}
