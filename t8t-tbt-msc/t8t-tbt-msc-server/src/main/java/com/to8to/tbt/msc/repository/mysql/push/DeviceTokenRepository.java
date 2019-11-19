package com.to8to.tbt.msc.repository.mysql.push;

import com.to8to.tbt.msc.entity.mysql.push.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author pajero.quan
 */
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Integer> {
    /**
     * @param pushAppId
     * @param uid
     * @return
     */
    List<DeviceToken> findAllByPushAppIdAndUid(Integer pushAppId, Integer uid);

    /**
     * @param token
     * @return
     */
    List<DeviceToken> findAllByDeviceToken(String token);

    /**
     * @param token
     * @return
     */
    List<DeviceToken> findAllByFirstId(String token);

    /**
     * @param pushAppId
     * @param uidList
     * @return
     */
    List<DeviceToken> findAllByPushAppIdAndUidIn(Integer pushAppId, List<Integer> uidList);

    List<DeviceToken> findAllByFirstIdIn(List<String> firstIdList);
}
