package com.to8to.tbt.msc.service;

import com.to8to.common.search.PageResult;
import com.to8to.tbt.msc.dto.CreateMessageDTO;
import com.to8to.tbt.msc.entity.dto.GetMessageListDTO;
import com.to8to.tbt.msc.entity.vo.MessageVO;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 21:17
 */
public interface PushMessageService {
    /**
     * 创建消息记录
     *
     * @param createMessageDTO
     */
    void createMessage(CreateMessageDTO createMessageDTO);

    /**
     * 获取消息记录列表
     *
     * @param query
     * @return
     */
    PageResult<MessageVO> getMessageList(GetMessageListDTO query);

    /**
     * 清理历史消息
     * @param duration 时间区间
     */
    void cleanMessages(int duration);
}
