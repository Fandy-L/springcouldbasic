package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.UserBindingDTO;
import com.to8to.tbt.msc.entity.dto.GetDeviceTokenDTO;
import com.to8to.tbt.msc.entity.dto.UpdateDeviceTokenDTO;
import com.to8to.tbt.msc.entity.mysql.push.DeviceToken;
import com.to8to.tbt.msc.entity.vo.DeviceTokenVO;

import java.util.List;

/**
 * 设备token服务接口定义
 *
 * @author pajero.quan
 */
public interface DeviceTokenService {
    /**
     * 更新token
     *
     * @param userBindingDTO
     * @return
     */
    void updateToken(UserBindingDTO userBindingDTO);

    /**
     * 查询token
     *
     * @param query
     * @return
     */
    DeviceTokenVO queryToken(GetDeviceTokenDTO query);

    /**
     * 根据uid查询DeviceToken
     *
     * @param pushAppId
     * @param uid
     * @return
     */
    DeviceToken queryDeviceTokenByUid(Integer pushAppId, Byte channel, Byte platform, Integer uid);

    /**
     * 根据token查询DeviceToken
     *
     * @param pushAppId
     * @param tokenStr
     * @param platform
     * @return
     */
    DeviceToken queryDeviceTokenByToken(Integer pushAppId, Byte channel, Byte platform, String tokenStr);


    /**
     * 根据firstId查询DeviceToken
     *
     * @param pushAppId
     * @param firstId
     * @return
     */
    DeviceToken queryDeviceTokenByFirstId(final Integer pushAppId, Byte channel, final Byte platform, final String firstId);

    /**
     * 根据uid列表查询DeviceToken
     *
     * @param pushAppId
     * @param uidList
     * @return
     */
    List<DeviceToken> queryDeviceTokenByUidList(Integer pushAppId, Byte channel, Byte platform, List<Integer> uidList);

    /**
     * 根据first_id列表查询DeviceToken
     *
     * @param pushAppId
     * @param firstIdList
     * @return
     */
    List<DeviceToken> queryDeviceTokenByFirstIdList(Integer pushAppId, Byte channel, Byte platform, List<String> firstIdList);

    /**
     * 插入token到数据库
     *
     * @param updateDeviceTokenDTO
     * @return
     */
    DeviceToken insertDeviceToken(UpdateDeviceTokenDTO updateDeviceTokenDTO);

    /**
     * 更新设备token到数据库
     *
     * @param deviceToken
     * @param uid
     * @param firstId
     */
    void updateDeviceToken(DeviceToken deviceToken, Integer uid, String version, String firstId);

    /**
     * 删除token
     *
     * @param appId
     * @param channel
     * @param uid
     */
    void deleteToken(int appId, Byte channel, Integer uid);
}
