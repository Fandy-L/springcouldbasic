package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DateUtils;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.cache.AppCache;
import com.to8to.tbt.msc.enumeration.BooleanEnum;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.entity.dto.CreateAppDTO;
import com.to8to.tbt.msc.entity.dto.UpdateAppDTO;
import com.to8to.tbt.msc.entity.mysql.push.App;
import com.to8to.tbt.msc.entity.vo.AppVO;
import com.to8to.tbt.msc.repository.mysql.push.AppRepository;
import com.to8to.tbt.msc.service.PushAppService;
import com.to8to.tbt.msc.service.PushChannelService;
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
 * @date: 2019/4/22 18:45
 */
@Slf4j
@Service
public class PushAppServiceImpl implements PushAppService {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private PushChannelService pushChannelService;

//    @Autowired
//    private AbstractPrefixOperation<App> appPrefixOperation;
    @Autowired
    private AppCache appCache;

    @Override
    public void createApp(CreateAppDTO createAppDTO) {
        //pushAppId检查重复
        this.checkPushAppIdExistAlready(createAppDTO.getPushAppId());

        App app = DozerUtils.map(createAppDTO, App.class);
        app.setEnable(createAppDTO.getEnable() ? BooleanEnum.TRUE.getValue() : BooleanEnum.FALSE.getValue());
        app.setDeleted(BooleanEnum.FALSE.getValue());
        app.setCreateTime(DateUtils.currentSeconds());
        app.setUpdateTime(DateUtils.currentSeconds());

        appRepository.save(app);
//        appMapper.insert(app);

        appCache.put(app.getPushAppId(), app);

        //this.cacheApp(app);
    }

    @Override
    public void editApp(UpdateAppDTO updateAppDTO) {
        // 检查要编辑的app是否存在
        App app = appRepository.findById(updateAppDTO.getId()).orElse(null);
        Preconditions.checkNotNull(app, MyExceptionStatus.APP_NOT_EXIST);

        // 检查新的pushAppId是否已经存在
        if (!app.getPushAppId().equals(updateAppDTO.getPushAppId())) {
            // 检查pushAppId是否被使用
            Preconditions.checkArgument(pushChannelService.getChannelList(app.getPushAppId()).isEmpty(), MyExceptionStatus.OLD_APP_ID_USED_ALREADY);
            this.checkPushAppIdExistAlready(updateAppDTO.getPushAppId());
        }
        app.setId(updateAppDTO.getId());
        app.setPushAppId(updateAppDTO.getPushAppId());
        app.setAndroidMinVersion(updateAppDTO.getAndroidMinVersion());
        app.setIosMinVersion(updateAppDTO.getIosMinVersion());
        app.setEnable(updateAppDTO.getEnable() ? BooleanEnum.TRUE.getValue() : BooleanEnum.FALSE.getValue());
        app.setPackageName(updateAppDTO.getPackageName());
        app.setName(updateAppDTO.getName());
        app.setUriActivity(updateAppDTO.getUriActivity());
        app.setUpdateTime(DateUtils.currentSeconds());
        appRepository.save(app);

        //this.cacheApp(app);
        appCache.put(app.getPushAppId(), app);
    }

    @Override
    public void removeApp(Integer id) {
        App app = appRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(app, MyExceptionStatus.APP_NOT_EXIST);
        app.setUpdateTime(DateUtils.currentSeconds());
        app.setDeleted(BooleanEnum.TRUE.getValue());
        appRepository.save(app);

        //this.removeAppCache(app.getPushAppId());
        appCache.evict(app.getPushAppId());
    }

    @Override
    public void enableApp(Integer id) {
        App app = appRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(app, MyExceptionStatus.APP_NOT_EXIST);
        app.setUpdateTime(DateUtils.currentSeconds());
        app.setEnable(BooleanEnum.TRUE.getValue());
        app = appRepository.save(app);
        //this.removeAppCache(app.getPushAppId());
        appCache.put(app.getPushAppId(), app);
    }

    @Override
    public void disableApp(Integer id) {
        App app = appRepository.findById(id).orElse(null);
        Preconditions.checkNotNull(app, MyExceptionStatus.APP_NOT_EXIST);
        app.setUpdateTime(DateUtils.currentSeconds());
        app.setEnable(BooleanEnum.FALSE.getValue());
        appRepository.save(app);
        //this.removeAppCache(app.getPushAppId());
        appCache.put(app.getPushAppId(), app);
    }

    @Override
    public AppVO getApp(Integer id) {
        App app = appRepository.findById(id).orElse(null);
        if (app == null) {
            return null;
        }
        appCache.put(app.getPushAppId(), app);
        return entityToVO(app);
    }

//    @Override
//    public AppVO getApp(Integer pushAppId) {
//        Search search = new Search();
//        search.put("pushAppId", pushAppId);
//        search.put("deleted", BooleanEnum.FALSE.getValue());
//        Example example = new ExampleBuilder(App.class).search(search).build();
//        App app = appMapper.selectOneByExample(example);
//        return entityToVO(app);
//    }

    @Override
    public AppVO getAppByPushAppId(Integer pushAppId) {
        App app = appCache.get(pushAppId);
        if (app == null) {
            app = appRepository.findTopByPushAppIdAndDeleted(pushAppId, BooleanEnum.FALSE.getValue()).orElse(null);
            appCache.put(app.getPushAppId(), app);
        }
        return entityToVO(app);
    }

    @Override
    public List<AppVO> getAppList() {
        List<App> appList = appRepository.findByDeleted(BooleanEnum.FALSE.getValue());
        return appList.stream().map(this::entityToVO).collect(Collectors.toList());
    }

    /**
     * 检查PushAPPId是否已经存在
     *
     * @param pushAppId
     */
    private void checkPushAppIdExistAlready(Integer pushAppId) {
        boolean exist = appRepository.existsByPushAppIdAndDeleted(pushAppId, BooleanEnum.FALSE.getValue());
        Preconditions.checkArgument(!exist, MyExceptionStatus.APP_ID_EXIST_ALREADY);
    }

    private AppVO entityToVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = DozerUtils.map(app, AppVO.class);
        appVO.setEnable(app.getEnable().equals(BooleanEnum.TRUE.getValue()));
        return appVO;
    }

//    private App getAppFromCache(Integer appId) {
//        return appPrefixOperation.get("app:" + appId);
//    }
//
//    private void cacheApp(App app) {
//        if (app == null) {
//            return;
//        }
//        appPrefixOperation.set("app:" + app.getPushAppId(), app, 30, TimeUnit.MINUTES);
//    }
//
//    private void removeAppCache(Integer appId) {
//        appPrefixOperation.delete("app:" + appId);
//    }
}
