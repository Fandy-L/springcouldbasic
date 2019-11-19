package com.to8to.tbt.msc.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import com.to8to.tbt.msc.enumeration.ChannelEnum;
import com.to8to.tbt.msc.entity.dto.UpdateDeviceTokenDTO;
import com.to8to.tbt.msc.entity.mysql.push.DeviceToken;
import com.to8to.tbt.msc.entity.vo.ChannelVO;
import com.to8to.tbt.msc.service.ChannelDeviceTokenService;
import com.to8to.tbt.msc.service.PushChannelService;
import com.to8to.tbt.msc.service.DeviceTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 14:53
 */
@Slf4j
@Service("jiguangDeviceTokenService")
public class JiguangDeviceTokenServiceImpl implements ChannelDeviceTokenService {

    @Autowired
    private PushChannelService pushChannelService;

    @Autowired
    private DeviceTokenService deviceTokenService;

    @Override
    public void updateToken(UpdateDeviceTokenDTO updateDeviceTokenDTO) {
        try {
            Integer pushAppId = updateDeviceTokenDTO.getPushAppId();
            byte platform = updateDeviceTokenDTO.getPlatform().byteValue();
            //Byte channel = updateDeviceTokenDTO.getChannel();
            Integer uid = updateDeviceTokenDTO.getUid();
            String firstId = updateDeviceTokenDTO.getFirstId();
            String token = updateDeviceTokenDTO.getDeviceToken();
            String version = updateDeviceTokenDTO.getVersion();

            if (uid == 0) {
                // 未登录用户，检查token是否存在
                DeviceToken deviceToken = deviceTokenService.queryDeviceTokenByToken(pushAppId, ChannelEnum.JIGUANG.getValue(), platform, token);
                if (deviceToken == null) {
                    // 库里不存在的设备，插入
                    this.insertNewDevice(updateDeviceTokenDTO);
                } else {
                    // 库里存在该设备，用户解除绑定
                    if (deviceToken.getUid() != 0) {
                        this.unbindDevice(deviceToken, version, firstId);
                    } else {
                        log.info("token相同不要处理");
                        // do nothing
                    }
                }
            } else {
                // 登录用户
                // 1. 检查用户是否存在
                DeviceToken deviceToken = deviceTokenService.queryDeviceTokenByUid(pushAppId, ChannelEnum.JIGUANG.getValue(), null, uid);
                if (deviceToken != null) {
                    //用户存在，而且token相等，说明终端重复上报
                    if (deviceToken.getDeviceToken().equals(token)) {
                        // token相等的情况下不做任何处理。
                        log.info("token相同!更新版本号和firstId");
                        deviceTokenService.updateDeviceToken(deviceToken, uid, version, firstId);
                        return;
                    } else {
                        //用户存在，但token不相同，说明用户更换了设备, 解除绑定
                        log.info("用户存在，但token不相同， 解除绑定!");
                        this.unbindDevice(deviceToken, null, null);
                    }
                }  // 用户不存在


                // 2. 查询设备是否存在
                DeviceToken originDeviceToken = deviceTokenService.queryDeviceTokenByToken(pushAppId, ChannelEnum.JIGUANG.getValue(), platform, token);
                if (originDeviceToken != null) {
                    // token存在，说明设备换了用户, 更新绑定关系
                    this.bindDevice(originDeviceToken, uid, version, firstId);
                } else {
                    // 插入新的设备id
                    this.insertNewDevice(updateDeviceTokenDTO);
                }
            }
        } catch (Exception e) {
            log.warn("更新token出现未知异常", e);
        }
    }

    @Override
    public String getTokenByUid(int appId, Byte channel, Integer uid) {
        DeviceToken deviceToken = deviceTokenService.queryDeviceTokenByUid(appId, ChannelEnum.JIGUANG.getValue(), null, uid);
        if (deviceToken != null) {
            return deviceToken.getDeviceToken();
        }
        return null;
    }

    @Override
    public String getTokenByFirstId(int appId, Byte channel, String firstId) {
        DeviceToken deviceToken = deviceTokenService.queryDeviceTokenByFirstId(appId, ChannelEnum.JIGUANG.getValue(), null, firstId);
        if (deviceToken != null) {
            return deviceToken.getDeviceToken();
        }
        return null;
    }

    @Override
    public List<String> getTokenListByUidList(int appId, Byte channel, List<Integer> uidList) {
        List<DeviceToken> deviceTokenList = deviceTokenService.queryDeviceTokenByUidList(appId, ChannelEnum.JIGUANG.getValue(), null, uidList);
        return deviceTokenList.stream().map(DeviceToken::getDeviceToken).collect(Collectors.toList());
    }

    @Override
    public List<String> getTokenListByFirstIdList(int appId, Byte channel, List<String> firstIdList) {
        List<DeviceToken> deviceTokenList = deviceTokenService.queryDeviceTokenByFirstIdList(appId, ChannelEnum.JIGUANG.getValue(), null, firstIdList);
        return deviceTokenList.stream().map(DeviceToken::getDeviceToken).collect(Collectors.toList());
    }

    @Override
    public List<String> getTags(Integer pushAppId, String deviceToken) {
        ChannelVO channelVO = pushChannelService.getChannel(pushAppId, ChannelEnum.JIGUANG);
        JPushClient jpushClient = new JPushClient(channelVO.getMasterSecret(),
                channelVO.getAppKey(),
                null,
                ClientConfig.getInstance());
        try {
            TagAliasResult result = jpushClient.getDeviceTagAlias(deviceToken);
            return result.tags;
        } catch (APIConnectionException | APIRequestException e) {
            log.warn("getTags", e);
        }
        return new ArrayList<>();
    }

    /**
     * 插入新的设备
     *
     * @param updateDeviceTokenDTO
     */
    private void insertNewDevice(UpdateDeviceTokenDTO updateDeviceTokenDTO) {
        log.info("token更新-插入新的设备:token=" + updateDeviceTokenDTO.getDeviceToken() + ",uid=" + updateDeviceTokenDTO.getUid());
        deviceTokenService.insertDeviceToken(updateDeviceTokenDTO);
    }

    /**
     * 绑定用户到设备
     *
     * @param deviceToken
     * @param uid
     */
    private void bindDevice(DeviceToken deviceToken, Integer uid, String version, String firstId) {
        log.info("token更新-绑定设备:token=" + deviceToken.getDeviceToken() + ",uid=" + uid);
        deviceTokenService.updateDeviceToken(deviceToken, uid, version, firstId);
    }

    /**
     * 解绑设备
     *
     * @param deviceToken
     */
    private void unbindDevice(DeviceToken deviceToken, String version, String firstId) {
        log.info("token更新-解绑设备:token=" + deviceToken.getDeviceToken());
        deviceTokenService.updateDeviceToken(deviceToken, 0, version, firstId);
    }
}
