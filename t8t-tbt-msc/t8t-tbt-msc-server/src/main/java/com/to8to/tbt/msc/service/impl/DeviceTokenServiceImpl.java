package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.to8to.common.util.BSIDUtils;
import com.to8to.common.util.DateUtils;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.*;
import com.to8to.tbt.msc.dto.UserBindingDTO;
import com.to8to.tbt.msc.entity.dto.GetDeviceTokenDTO;
import com.to8to.tbt.msc.entity.dto.UpdateDeviceTokenDTO;
import com.to8to.tbt.msc.entity.mysql.push.DeviceToken;
import com.to8to.tbt.msc.entity.vo.AppVO;
import com.to8to.tbt.msc.entity.vo.DeviceTokenVO;
import com.to8to.tbt.msc.enumeration.BooleanEnum;
import com.to8to.tbt.msc.enumeration.ChannelEnum;
import com.to8to.tbt.msc.enumeration.PlatformEnum;
import com.to8to.tbt.msc.manager.HttpClientManager;
import com.to8to.tbt.msc.repository.mysql.push.DeviceTokenRepository;
import com.to8to.tbt.msc.service.PushAppService;
import com.to8to.tbt.msc.service.AsyncTaskService;
import com.to8to.tbt.msc.service.ChannelDeviceTokenService;
import com.to8to.tbt.msc.service.DeviceTokenService;
import com.to8to.tbt.msc.utils.Preconditions;
import com.to8to.tbt.msc.utils.VersionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.to8to.tbt.msc.common.Constants.APP_ID_T8T;

/**
 * @author pajero.quan
 */
@Slf4j
@Service
public class DeviceTokenServiceImpl implements DeviceTokenService {
    @Value("${push.old.url.adduser}")
    private String oldAddUserUrl;

    @Value("${push.old.url.getuser}")
    private String oldGetUserUrl;

    @Autowired
    @Qualifier("jiguangDeviceTokenService")
    private ChannelDeviceTokenService channelDeviceTokenService;

    @Autowired
    private PushAppService pushAppService;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private HttpClientManager httpClientManager;

    @Override
    public void updateToken(UserBindingDTO userBindingDTO) {
        AppVO appVO = pushAppService.getAppByPushAppId(userBindingDTO.getAppId());

        //应用在新的服务中不存在
        if (appVO == null) {
            this.updateOldToken(userBindingDTO, null);
            log.info("app not config, update old token!req={}", userBindingDTO);
            return;
        }
        //应用在新的服务中没有启用
        if (!appVO.getEnable()) {
            this.updateOldToken(userBindingDTO, appVO);
            log.info("app disabled, update old token!req={}", userBindingDTO);
            return;
        }
        boolean isNewToken = isNewToken(userBindingDTO.getAppVersion(), userBindingDTO.getType(), appVO);
        if (isNewToken) {
            this.updateNewToken(userBindingDTO);
        } else {
            if (userBindingDTO.getAppId().equals(APP_ID_T8T)) {
                log.info("当前版本的token不再支持!req={}", userBindingDTO);
                throw new RPCException(MyExceptionStatus.TOKEN_OF_THIS_VERSION_NOT_SUPPORT);
            }
            updateOldToken(userBindingDTO, appVO);
            log.info("version is not support, update old token!req={}", userBindingDTO);
        }
    }

    @Override
    public DeviceTokenVO queryToken(GetDeviceTokenDTO query) {
        DeviceToken deviceToken = null;
        if (!StringUtils.isEmpty(query.getUid())) {
            deviceToken = this.queryDeviceTokenByUid(query.getAppId(), query.getChannel().byteValue(), query.getPlatform().byteValue(), query.getUid());
        } else if (!StringUtils.isEmpty(query.getFirstId())) {
            deviceToken = this.queryDeviceTokenByFirstId(query.getAppId(), query.getChannel().byteValue(), query.getPlatform().byteValue(), query.getFirstId());
        }
        if (deviceToken != null) {
            return DozerUtils.map(deviceToken, DeviceTokenVO.class);
        }
        return null;
    }

    @Override
    public DeviceToken queryDeviceTokenByUid(Integer pushAppId, Byte channel, Byte platform, Integer uid) {
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByPushAppIdAndUid(pushAppId, uid);
        return deviceTokenList
                .stream()
                .filter(deviceToken1 -> isValidToken(deviceToken1, pushAppId, platform, null))
                .findFirst()
                .orElse(null);
    }

    @Override
    public DeviceToken queryDeviceTokenByToken(Integer pushAppId, Byte channel, Byte platform, String tokenStr) {
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByDeviceToken(tokenStr);
        return deviceTokenList
                .stream()
                .filter(deviceToken -> isValidToken(deviceToken, pushAppId, platform, channel)).max(Comparator.comparing(DeviceToken::getUpdateTime))
                .orElse(null);
    }

    @Override
    public DeviceToken queryDeviceTokenByFirstId(final Integer pushAppId, Byte channel, Byte platform, String firstId) {
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByFirstId(firstId);
        return deviceTokenList
                .stream()
                .filter(deviceToken -> isValidToken(deviceToken, pushAppId, platform, channel)).max(Comparator.comparing(DeviceToken::getUpdateTime))
                .orElse(null);
    }

    @Override
    public List<DeviceToken> queryDeviceTokenByUidList(Integer pushAppId, Byte channel, Byte platform, List<Integer> uidList) {
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByPushAppIdAndUidIn(pushAppId, uidList);
        return deviceTokenList
                .stream()
                .filter(deviceToken -> isValidToken(deviceToken, pushAppId, platform, channel))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceToken> queryDeviceTokenByFirstIdList(Integer pushAppId, Byte channel, Byte platform, List<String> firstIdList) {
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAllByFirstIdIn(firstIdList);
        return deviceTokenList
                .stream()
                .filter(deviceToken -> isValidToken(deviceToken, pushAppId, platform, null))
                .collect(Collectors.toList());
    }

    @Override
    public DeviceToken insertDeviceToken(UpdateDeviceTokenDTO updateDeviceTokenDTO) {
        DeviceToken newToken = DozerUtils.map(updateDeviceTokenDTO, DeviceToken.class);
        newToken.setCreateTime(DateUtils.currentSeconds());
        newToken.setUpdateTime(DateUtils.currentSeconds());
        newToken.setDeleted(BooleanEnum.FALSE.getValue());
        deviceTokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public void updateDeviceToken(DeviceToken deviceToken, Integer uid, String version, String firstId) {
        deviceToken.setUid(uid);
        deviceToken.setUpdateTime(DateUtils.currentSeconds());
        if (!StringUtils.isEmpty(version)) {
            deviceToken.setVersion(version);
        }
        if (!StringUtils.isEmpty(firstId)) {
            deviceToken.setFirstId(firstId);
        }
        deviceTokenRepository.save(deviceToken);
    }

    @Override
    public void deleteToken(int appId, Byte channel, Integer uid) {
        DeviceToken deviceToken = this.queryDeviceTokenByUid(appId, channel, null, uid);
        if (deviceToken != null) {
            deviceTokenRepository.deleteById(deviceToken.getId());
        }
    }

    private void removeDeviceFromUmeng(String bsid, Integer appId, Integer uid) {
        BSIDUtils.putRelationBsid(bsid);
        Map<String, String> queryUserArgs = new HashMap<>();
        queryUserArgs.put("appid", String.valueOf(appId));
        queryUserArgs.put("uid", String.valueOf(uid));

        //解绑设备和UID
        queryUserArgs.put("del", "1");
        String response = httpClientManager.execute(oldGetUserUrl, queryUserArgs, String.class);
        log.info("del umeng token:" + response);
        BSIDUtils.removeRelationBsid();
    }

    /**
     * 验证token有效性
     *
     * @param deviceToken
     * @param pushAppId
     * @param platform
     * @param channel
     * @return
     */
    private boolean isValidToken(DeviceToken deviceToken, Integer pushAppId, Byte platform, Byte channel) {
        // AppId不相等
        if (!deviceToken.getPushAppId().equals(pushAppId)) {
            return false;
        }

        // 通道不相同
        if (channel != null && !deviceToken.getChannel().equals(channel)) {
            return false;
        }

        // 平台不相等
        if (platform != null && !deviceToken.getPlatform().equals(platform)) {
            return false;
        }
        // 已经删除
        return !deviceToken.getDeleted().equals(BooleanEnum.TRUE.getValue());
    }

    private void updateNewToken(UserBindingDTO userBindingDTO) {
        UpdateDeviceTokenDTO deviceToken = new UpdateDeviceTokenDTO();
        deviceToken.setUid(userBindingDTO.getUid());
        deviceToken.setPushAppId(userBindingDTO.getAppId());
            /*
              后续需要支持多平台,当前默认是极光
             */
        Integer pushPlatform = userBindingDTO.getPlatform();
        if (pushPlatform == null) {
            pushPlatform = 0;
        }
        byte channel = ChannelEnum.valueOf(pushPlatform.byteValue()).getValue();
        byte platform = PlatformEnum.valueOf(userBindingDTO.getType().byteValue()).getValue();
        deviceToken.setChannel(channel);
        deviceToken.setPlatform(platform);
        deviceToken.setDeviceToken(userBindingDTO.getDeviceId());
        deviceToken.setFirstId(userBindingDTO.getFirstId());
        deviceToken.setVersion(userBindingDTO.getAppVersion());
        final String bsid = BSIDUtils.getCurBsid();
        // 异步更新新token
        asyncTaskService.submit(() -> {
            BSIDUtils.putRelationBsid(bsid);
            channelDeviceTokenService.updateToken(deviceToken);
            BSIDUtils.removeRelationBsid();
        });

        // 异步线程删除PHP的用户token,PHP后台没有匿名用户
        if (userBindingDTO.getUid() > 0) {
            if (!userBindingDTO.getAppId().equals(APP_ID_T8T)) {
                asyncTaskService.submit(() -> removeDeviceFromUmeng(bsid, userBindingDTO.getAppId(), userBindingDTO.getUid()));
            }
        }
    }

    private void updateOldToken(UserBindingDTO userBindingDTO, final AppVO appVO) {
        Map<String, String> addUserArgs = new HashMap<>();
        addUserArgs.put("appid", userBindingDTO.getAppId().toString());
        addUserArgs.put("deviceid", userBindingDTO.getDeviceId());
        addUserArgs.put("platform", userBindingDTO.getPlatform().toString());
        addUserArgs.put("uid", userBindingDTO.getUid().toString());
        if (!StringUtils.isEmpty(userBindingDTO.getUidSub())) {
            addUserArgs.put("uid_sub", userBindingDTO.getUidSub().toString());
        }
        addUserArgs.put("type", userBindingDTO.getType().toString());
        final String bsid = BSIDUtils.getCurBsid();
        //降级处理
        asyncTaskService.submit(() -> {
            BSIDUtils.putRelationBsid(bsid);
            Integer uid = userBindingDTO.getUid();
            if (appVO != null && appVO.getEnable()) {
                if (uid != null && uid > 0) {
                    this.deleteToken(appVO.getPushAppId(), ChannelEnum.JIGUANG.getValue(), uid);
                }
            }
            BSIDUtils.removeRelationBsid();
        });
        String response;
        try {
            response = httpClientManager.execute(oldAddUserUrl, addUserArgs, String.class);
            log.info("更新友盟token:" + response);
        } catch (Exception e) {
            log.warn("友盟token更新异常!", e);
            throw new RPCException(MyExceptionStatus.UMENG_DEVICE_TOKEN_ERROR);
        }

        Preconditions.checkStringNotEmpty(response, MyExceptionStatus.UMENG_DEVICE_TOKEN_ERROR);
        JSONObject respObj = JSON.parseObject(response);
        Preconditions.checkArgument(respObj.containsKey("status"), MyExceptionStatus.UMENG_DEVICE_TOKEN_ERROR);
        String status = respObj.getString("status");
        Preconditions.checkArgument(Constants.HTTP_RESP_OK_STR.equalsIgnoreCase(respObj.getString("status")),
                MyExceptionStatus.UMENG_DEVICE_TOKEN_ERROR, respObj.getString("result"));
    }

    private boolean isNewToken(String version, Integer appType, AppVO appVO) {
        // 如果版本号为空，认为是旧版本
        if (version == null) {
            return false;
        }
        if (appType == 0) {
            return VersionUtils.compareVersion(version, appVO.getAndroidMinVersion()) >= 0;
        } else {
            return VersionUtils.compareVersion(version, appVO.getIosMinVersion()) >= 0;
        }
    }
}
