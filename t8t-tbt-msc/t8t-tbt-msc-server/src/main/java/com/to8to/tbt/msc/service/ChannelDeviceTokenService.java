package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.entity.dto.UpdateDeviceTokenDTO;

import java.util.List;

/**
 * 设备token服务接口类
 *
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 19:46
 */
public interface ChannelDeviceTokenService {
//    /**
//     * 删除token
//     *
//     * @param appId
//     * @param channel
//     * @param uid
//     */
//    void deleteToken(int appId, Byte channel, Integer uid);

    /**
     * 更新token
     * @param updateDeviceTokenDTO
     */
    void updateToken(UpdateDeviceTokenDTO updateDeviceTokenDTO);

    /**
     * 根据uid查询token
     *
     * @param appId
     * @param channel
     * @param uid
     * @return
     */
    String getTokenByUid(int appId, Byte channel, Integer uid);

    /**
     * 根据firstId查询token
     *
     * @param appId
     * @param channel
     * @param firstId
     * @return
     */
    String getTokenByFirstId(int appId, Byte channel, String firstId);

    /**
     * 根据uid查询token列表
     *
     * @param uidList
     * @return
     */
    List<String> getTokenListByUidList(int appId, Byte channel, List<Integer> uidList);

    /**
     * 根据firstId查询token列表
     *
     * @param firstIdList
     * @return
     */
    List<String> getTokenListByFirstIdList(int appId, Byte channel, List<String> firstIdList);

    /**
     * 根据deviceToken获取tags
     *
     * @param pushAppId
     * @param deviceToken
     * @return
     */
    List<String> getTags(Integer pushAppId, String deviceToken);

//    /**
//     * 从Redis批量添加标签
//     */
//    void batchAddTagFromRedis();
//
//    /**
//     * 从MySQL批量添加标签
//     */
//    void batchAddTagFromMysql();
}
