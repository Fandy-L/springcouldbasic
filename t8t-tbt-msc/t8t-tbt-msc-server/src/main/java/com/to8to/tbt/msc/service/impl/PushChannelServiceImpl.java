package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DateUtils;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.cache.ChannelCache;
import com.to8to.tbt.msc.enumeration.BooleanEnum;
import com.to8to.tbt.msc.enumeration.ChannelEnum;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.entity.dto.CreateChannelDTO;
import com.to8to.tbt.msc.entity.dto.UpdateChannelDTO;
import com.to8to.tbt.msc.entity.mysql.push.Channel;
import com.to8to.tbt.msc.entity.vo.ChannelVO;
import com.to8to.tbt.msc.repository.mysql.push.ChannelRepository;
import com.to8to.tbt.msc.service.PushChannelService;
import com.to8to.tbt.msc.utils.BoolUtils;
import com.to8to.tbt.msc.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 18:41
 */
@Slf4j
@Service
public class PushChannelServiceImpl implements PushChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelCache channelCache;

    @Override
    public void createChannel(CreateChannelDTO createChannelDTO) {
        boolean exist = channelRepository.existsByPushAppIdAndChannelAndDeleted(createChannelDTO.getPushAppId(),
                createChannelDTO.getChannel(), BooleanEnum.FALSE.getValue());
        Preconditions.checkArgument(!exist, MyExceptionStatus.CHANNEL_EXIST_ALREADY);
        Channel channel = DozerUtils.map(createChannelDTO, Channel.class);
        channel.setEnable(BoolUtils.toByte(createChannelDTO.getEnable()));
        channel.setDeleted(BooleanEnum.FALSE.getValue());
        channel.setCreateTime(DateUtils.currentSeconds());
        channel.setUpdateTime(DateUtils.currentSeconds());

        channelRepository.save(channel);
        channelCache.put(channel.getPushAppId() + ":" + channel.getChannel(), channel);
    }

    @Override
    public void editChannel(UpdateChannelDTO updateChannelDTO) {
        Channel channel = channelRepository.findById(updateChannelDTO.getId()).orElse(null);
        Preconditions.checkNotNull(channel, MyExceptionStatus.CHANNEL_NOT_EXIST);

        channel.setId(updateChannelDTO.getId());
        channel.setPushAppId(updateChannelDTO.getPushAppId());
        channel.setChannel(updateChannelDTO.getChannel().byteValue());
        channel.setAppId(updateChannelDTO.getAppId());
        channel.setAppKey(updateChannelDTO.getAppKey());
        channel.setAppSecret(updateChannelDTO.getAppSecret());
        channel.setMasterSecret(updateChannelDTO.getMasterSecret());
        channel.setEnable(BoolUtils.toByte(updateChannelDTO.getEnable()));
        channel.setDeleted(BooleanEnum.FALSE.getValue());
        channel.setUpdateTime(DateUtils.currentSeconds());

        channel = channelRepository.save(channel);

        channelCache.put(channel.getPushAppId() + ":" + channel.getChannel(), channel);
    }


    @Override
    public void removeChannel(Integer id) {
        Channel channel = channelRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(channel, MyExceptionStatus.CHANNEL_NOT_EXIST);
        channel.setUpdateTime(DateUtils.currentSeconds());
        channel.setDeleted(BooleanEnum.TRUE.getValue());
        channel = channelRepository.save(channel);
        channelCache.evict(channel.getPushAppId() + ":" + channel.getChannel());
    }

    @Override
    public void enableChannel(Integer id) {
        Channel channel = channelRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(channel, MyExceptionStatus.CHANNEL_NOT_EXIST);
        channel.setUpdateTime(DateUtils.currentSeconds());
        channel.setEnable(BooleanEnum.TRUE.getValue());
        channelRepository.save(channel);
        channelCache.evict(channel.getPushAppId() + ":" + channel.getChannel());
    }

    @Override
    public void disableChannel(Integer id) {
        Channel channel = channelRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(channel, MyExceptionStatus.CHANNEL_NOT_EXIST);
        channel.setUpdateTime(DateUtils.currentSeconds());
        channel.setEnable(BooleanEnum.FALSE.getValue());
        channelRepository.save(channel);
        channelCache.evict(channel.getPushAppId() + ":" + channel.getChannel());
    }

    @Override
    public ChannelVO getChannel(Integer id) {
        Channel channel = channelRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(channel, MyExceptionStatus.CHANNEL_NOT_EXIST);
        return this.entity2VO(channel);
    }

    @Override
    public ChannelVO getChannel(Integer appId, ChannelEnum channelEnum) {
        Channel channel = channelCache.get(appId + ":" + channelEnum.getValue());
        if (channel == null) {
            channel = channelRepository.findTopByPushAppIdAndChannelAndDeleted(appId, channelEnum.getValue(),BooleanEnum.FALSE.getValue()).orElse(null);
            channelCache.put(channel.getPushAppId() + ":" + channel.getChannel(),channel);
        }

        return this.entity2VO(channel);
    }

    @Override
    public List<ChannelVO> getChannelList(Integer appId) {
        List<Channel> channelList = channelRepository.findByPushAppIdAndDeleted(appId,BooleanEnum.FALSE.getValue());
        return channelList.stream().map(this::entity2VO).collect(Collectors.toList());
    }

    private ChannelVO entity2VO(Channel channel) {
        ChannelVO channelVO = DozerUtils.map(channel, ChannelVO.class);
        channelVO.setEnable(BoolUtils.toBoolean(channel.getEnable()));
        return channelVO;
    }
}
