package com.to8to.tbt.msc.repository.mysql.push;

import com.to8to.tbt.msc.entity.mysql.push.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author pajero.quan
 */
public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    /**
     * 检查通道是否存在
     * @param pushAppId
     * @param channel
     * @param deleted
     * @return
     */
    boolean existsByPushAppIdAndChannelAndDeleted(Integer pushAppId, Byte channel, Byte deleted);

    /**
     * @param pushAppId
     * @param channel
     * @return
     */
    Optional<Channel> findTopByPushAppIdAndChannelAndDeleted(Integer pushAppId, Byte channel,Byte deleted);

    /**
     *
     * @param pushAppId
     * @return
     */
    List<Channel> findByPushAppIdAndDeleted(Integer pushAppId,Byte deleted);
}
