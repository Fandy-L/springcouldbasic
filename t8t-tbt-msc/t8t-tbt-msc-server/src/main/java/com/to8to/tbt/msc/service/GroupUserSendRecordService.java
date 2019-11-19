package com.to8to.tbt.msc.service;

/**
 * @author juntao.guo
 */
public interface GroupUserSendRecordService {

    /**
     * 保存运营用户发送群发短信的记录
     *
     * @param groupNoteId
     * @param userName
     * @param succNum
     */
    void insertGroupUserSendRecord(String groupNoteId, String userName, int succNum);

    /**
     * 根据用户和时间区间统计发送总量
     *
     * @param userName
     * @param startTime
     * @param endTime
     * @return
     */
    int countGroupUserSendRecordByUser(String userName, int startTime, int endTime);
}
