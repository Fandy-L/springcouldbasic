package com.to8to.tbt.msc.service;



import com.to8to.tbt.msc.entity.dto.CreateAppDTO;
import com.to8to.tbt.msc.entity.dto.UpdateAppDTO;
import com.to8to.tbt.msc.entity.vo.AppVO;

import java.util.List;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/22 14:43
 */
public interface PushAppService {
    /**
     * 创建应用
     * @param createAppDTO
     */
    void createApp(CreateAppDTO createAppDTO);

    /**
     * 编辑应用
     * @param updateAppDTO
     */
    void editApp(UpdateAppDTO updateAppDTO);

    /**
     * 删除应用
     * @param id 主键id
     */
    void removeApp(Integer id);

    /**
     * 启用应用
     * @param id 主键id
     */
    void enableApp(Integer id);

    /**
     * 禁用应用
     * @param id 主键id
     */
    void disableApp(Integer id);
    /**
     * 获取应用
     * @param id 主键id
     * @return
     */
    AppVO getApp(Integer id);
    /**
     * 获取应用
     * @param pushAppId 推送应用id
     * @return
     */
    AppVO getAppByPushAppId(Integer pushAppId);

    /**
     * 获取应用列表
     * @return
     */
    List<AppVO> getAppList();
}
