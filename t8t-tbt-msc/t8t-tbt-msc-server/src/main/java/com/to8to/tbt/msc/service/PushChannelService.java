package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.enumeration.ChannelEnum;
import com.to8to.tbt.msc.entity.dto.CreateChannelDTO;
import com.to8to.tbt.msc.entity.dto.UpdateChannelDTO;
import com.to8to.tbt.msc.entity.vo.ChannelVO;

import java.util.List;

/**
 * 推送通道
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 14:47
 */
public interface PushChannelService {
    /**
     * 创建通道
     * @param createChannelDTO
     */
    void createChannel(CreateChannelDTO createChannelDTO);

    /**
     * 编辑通道
     * @param updateChannelDTO
     */
    void editChannel(UpdateChannelDTO updateChannelDTO);

    /**
     * 删除通道
     * @param id
     */
    void removeChannel(Integer id);

    /**
     * 启用通道
     * @param id
     */
    void enableChannel(Integer id);
    /**
     * 禁用通道
     * @param id
     */
    void disableChannel(Integer id);

    /**
     * 获取通道
     * @param id
     * @return
     */
    ChannelVO getChannel(Integer id);

    /**
     * 获取通道
     * @param appId 应用id
     * @param channel 通道类型
     * @return
     */
    ChannelVO getChannel(Integer appId, ChannelEnum channel);

    /**
     * 获取应用通道列表
     * @param appId
     * @return
     */
    List<ChannelVO> getChannelList(Integer appId);
}
