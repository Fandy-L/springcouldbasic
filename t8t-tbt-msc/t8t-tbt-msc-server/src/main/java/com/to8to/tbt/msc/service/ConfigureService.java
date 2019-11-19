package com.to8to.tbt.msc.service;


import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.ConfigureCreateDTO;
import com.to8to.tbt.msc.dto.ConfigureDeleteDTO;
import com.to8to.tbt.msc.dto.ConfigureSearchDTO;
import com.to8to.tbt.msc.dto.ConfigureUpdateDTO;
import com.to8to.tbt.msc.entity.OldMsgResponse;
import com.to8to.tbt.msc.vo.ConfigureSearchVO;
import com.to8to.tbt.msc.vo.MsgcConfigureVO;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface ConfigureService {
    /**
     * 查询配置信息
     *
     * @param configType
     * @param fathrerId
     * @param fatherIdStr
     * @return
     */
    List<MsgcConfigureVO> searchConfiguration(Integer configType, Integer fathrerId, String fatherIdStr);

    /**
     * 创建配置项
     *
     * @param configureCreateDTO
     * @return
     */
    int createConfiguration(ConfigureCreateDTO configureCreateDTO);

    /**
     * 修改配置项
     *
     * @param configureUpdateDTO
     * @return
     */
    ResResult updateConfiguration(ConfigureUpdateDTO configureUpdateDTO);

    /**
     * 删除配置项
     *
     * @param configureDeleteDTO
     * @return
     */
    ResResult deleteConfiguration(ConfigureDeleteDTO configureDeleteDTO);

    /**
     * 搜索配置项
     *
     * @param configureSearchDTO
     * @return
     */
    List<ConfigureSearchVO> searchConfiguration(ConfigureSearchDTO configureSearchDTO);

    /**
     * 根据配置项类型和ID查找所有符合条件的节点ID
     *
     * @param configType
     * @param cid
     * @return
     */
    List<Integer> queryNodeIds(Integer configType, Integer cid);
}
