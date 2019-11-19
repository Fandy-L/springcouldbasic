package com.to8to.tbt.msc.service.impl;

import com.google.common.collect.Lists;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.ConfigureCreateDTO;
import com.to8to.tbt.msc.dto.ConfigureDeleteDTO;
import com.to8to.tbt.msc.dto.ConfigureSearchDTO;
import com.to8to.tbt.msc.dto.ConfigureUpdateDTO;
import com.to8to.tbt.msc.entity.mysql.main.Configure;
import com.to8to.tbt.msc.enumeration.MsgConfigTypeEnum;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.ConfigureSearchVO;
import com.to8to.tbt.msc.vo.MsgcConfigureVO;
import com.to8to.tbt.msc.vo.PermissionWrapper;
import com.to8to.tbt.msc.repository.mysql.main.ConfigureRepository;
import com.to8to.tbt.msc.service.ConfigureService;
import com.to8to.tbt.msc.service.ExternalService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class ConfigureServiceImpl implements ConfigureService {

    @Autowired
    private ConfigureRepository configureRepository;

    @Autowired
    private ExternalService externalService;

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public List<MsgcConfigureVO> searchConfiguration(Integer configType, Integer fathrerId, String fatherIdStr) {
        List<Configure> configList = null;
        List<MsgcConfigureVO> msgcConfigureVOList = new ArrayList<>();
        if (IntegerUtils.isMinValue(configType) && IntegerUtils.isMinValue(fathrerId)) {
            configList = configureRepository.findAllByConfigTypeAndFatherId(configType, fathrerId);
        } else if (StringUtils.isNotBlank(fatherIdStr)) {
            List<Integer> fatherIds = Arrays.stream(StringUtils.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            configList = configureRepository.findAllByFatherIdIn(fatherIds);
        }
        log.debug("ConfigureServiceImpl.searchConfiguration findAllBy configType:{} fatherId:{} fatherIdStr:{} configList:{}", configType, fathrerId, fatherIdStr, configList);
        if (configList != null && configList.size() > 0) {
            //补全创建人昵称
            List<Integer> uids = new ArrayList<>();
            for (Configure configure : configList) {
                uids.add(configure.getCreateId());
            }
            List<PermissionWrapper.CrmUserVO> userList = externalService.userGetList(uids);
            Map<Integer, String> nickMap = new HashMap<>(userList.size());
            for (PermissionWrapper.CrmUserVO userGetListItemVO : userList) {
                nickMap.put(userGetListItemVO.getUid(), userGetListItemVO.getNick());
            }
            for (Configure configure : configList) {
                MsgcConfigureVO msgcConfigureVO = MsgcConfigureVO.builder()
                        .cid(configure.getCid())
                        .configType(configure.getConfigType())
                        .configDescribe(configure.getConfigDescribe())
                        .createId(configure.getCreateId())
                        .createTime(configure.getCreateTime())
                        .modifyId(configure.getModifyId())
                        .modifyTime(configure.getModifyTime())
                        .nick(nickMap.getOrDefault(configure.getCreateId(), ""))
                        .build();
                msgcConfigureVOList.add(msgcConfigureVO);
            }
            log.debug("ConfigureServiceImpl.searchConfiguration result uids:{} userList:{} msgcConfigureVOList:{}", uids, userList, msgcConfigureVOList);
        }
        return msgcConfigureVOList;
    }

    @Override
    public int createConfiguration(ConfigureCreateDTO configureCreateDTO) {
        if (existsConfigDescribe(configureCreateDTO.getConfigDescribe())) {
            return -1;
        }
        Configure configure = DozerUtils.map(configureCreateDTO, Configure.class);
        configure.setCreateTime(TimeUtils.getCurrentTimestamp());
        configure.setModifyId(0);
        configure.setModifyTime(0);
        try {
            log.debug("createConfiguration configure:{}", configure);
            configureRepository.save(configure);
            return configure.getCid();
        } catch (Exception e) {
            log.warn("createConfiguration save fail configureCreateDTO:{} e:{}", configureCreateDTO, e);
            return -1;
        }
    }

    @Override
    public ResResult updateConfiguration(ConfigureUpdateDTO configureUpdateDTO) {
        try {
            Configure configure = configureRepository.findById(configureUpdateDTO.getCid()).orElse(null);
            if (configure == null) {
                return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_NOT_FOUND.getMessage());
            }
            configure.setConfigDescribe(configureUpdateDTO.getConfigDescribe());
            configure.setModifyTime(TimeUtils.getCurrentTimestamp());
            if (IntegerUtils.isGtLimitValue(configureUpdateDTO.getFatherId())) {
                configure.setFatherId(configureUpdateDTO.getFatherId());
            }
            configureRepository.save(configure);
            return ResponseUtils.success(MyExceptionStatus.CONFIGURE_UPDATE_SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("updateConfiguration mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    @Override
    public ResResult deleteConfiguration(ConfigureDeleteDTO configureDeleteDTO) {
        try {
            if (IntegerUtils.isEqLimitValue(configureDeleteDTO.getConfigType(), MsgConfigTypeEnum.SEND_NODE.getCode())) {
                return deleteSendNode(configureDeleteDTO.getCid());
            } else if (IntegerUtils.isEqLimitValue(configureDeleteDTO.getConfigType(), MsgConfigTypeEnum.RECEIVE_OBJECT.getCode())) {
                return deleteReceiveObject(configureDeleteDTO.getCid());
            } else if (IntegerUtils.isEqLimitValue(configureDeleteDTO.getConfigType(), MsgConfigTypeEnum.PRODUCT_MODULE.getCode())) {
                return deleteProductModel(configureDeleteDTO.getCid());
            } else if (IntegerUtils.isEqLimitValue(configureDeleteDTO.getConfigType(), MsgConfigTypeEnum.BUSINESS_ITEM.getCode())) {
                return deleteBusinessItem(configureDeleteDTO.getCid());
            } else if (IntegerUtils.isEqLimitValue(configureDeleteDTO.getConfigType(), MsgConfigTypeEnum.BUSINESS_TYPE.getCode())) {
                return deleteBusinessType(configureDeleteDTO.getCid());
            }
        } catch (Exception e) {
            log.warn("deleteConfiguration fail configureDeleteDTO:{} e:{}", configureDeleteDTO, e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
        return null;
    }

    @Override
    public List<ConfigureSearchVO> searchConfiguration(ConfigureSearchDTO configureSearchDTO) {
        List<ConfigureSearchVO> configureSearchVOS = new ArrayList<>();
        try {
            List<Configure> configureList = new ArrayList<>();
            configureSearchDTO.setFatherId(IntegerUtils.intValueAsDefault(configureSearchDTO.getFatherId()));
            if (IntegerUtils.isGtLimitValue(configureSearchDTO.getConfigType()) && IntegerUtils.isMinValue(configureSearchDTO.getFatherId())) {
                configureList = configureRepository.findAllByConfigTypeAndFatherId(configureSearchDTO.getConfigType(), configureSearchDTO.getFatherId());
            } else if (StringUtils.isNotBlank(configureSearchDTO.getFatherIds())) {
                List<Integer> fatherIds = Arrays.stream(configureSearchDTO.getFatherIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
                configureList = configureRepository.findAllByFatherIdIn(fatherIds);
            }
            List<Integer> createIdList = configureList.stream().map(configure -> configure.getCreateId()).collect(Collectors.toList());
            Map<Integer, PermissionWrapper.CrmUserVO> crmUserVOMap = externalService.crmUserQueryMap(createIdList);
            for (Configure configure : configureList) {
                ConfigureSearchVO configureSearchVO = DozerUtils.map(configure, ConfigureSearchVO.class);
                PermissionWrapper.CrmUserVO crmUserVO = crmUserVOMap.getOrDefault(configure.getCreateId(), new PermissionWrapper.CrmUserVO());
                configureSearchVO.setNick(StringUtils.defaultString(crmUserVO.getNick()));
                configureSearchVOS.add(configureSearchVO);
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildTemplate(), e);
        }
        return configureSearchVOS;
    }

    @Override
    public List<Integer> queryNodeIds(Integer configType, Integer cid) {
        List<Integer> nodeIds = null;
        List<Configure> configureList = null;
        if (IntegerUtils.isGtLimitValue(configType) && IntegerUtils.isGtLimitValue(cid)) {
            try {
                if (IntegerUtils.isEqLimitValue(configType, MsgConfigTypeEnum.BUSINESS_TYPE.getCode())) {
                    List<Configure> itemConfigurateList = configureRepository.findAllByConfigTypeAndFatherId(MsgConfigTypeEnum.BUSINESS_ITEM.getCode(), cid);
                    List<Integer> cidList = itemConfigurateList.stream().map(Configure::getCid).collect(Collectors.toList());
                    if (!cidList.isEmpty()) {
                        configureList = configureRepository.findAllByConfigTypeAndFatherIdIn(MsgConfigTypeEnum.SEND_NODE.getCode(), cidList);
                    }
                } else if (IntegerUtils.isEqLimitValue(configType, MsgConfigTypeEnum.BUSINESS_ITEM.getCode())) {
                    configureList = configureRepository.findAllByConfigTypeAndFatherId(MsgConfigTypeEnum.SEND_NODE.getCode(), cid);
                } else if (IntegerUtils.isEqLimitValue(configType, MsgConfigTypeEnum.SEND_NODE.getCode())) {
                    Configure configure = configureRepository.findByConfigTypeAndCid(MsgConfigTypeEnum.SEND_NODE.getCode(), cid).orElse(null);
                    if (configure != null) {
                        nodeIds = Lists.newArrayList();
                        nodeIds.add(configure.getCid());
                    }
                }
                if (configureList != null && !configureList.isEmpty()) {
                    nodeIds = configureList.stream().map(Configure::getCid).collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.warn("queryNodeIds mysql error configType:{} cid:{} e:{}", configType, cid, e);
            }
        }
        log.debug("queryNodeIds nodeIds:{}", nodeIds);
        return nodeIds;
    }

    /**
     * 删除发送节点类型的配置项
     *
     * @param cid
     */
    protected ResResult deleteSendNode(Integer cid) {
        try {
            int templateNum = templateRepository.countByNodeId(cid);
            log.debug("deleteSendNode cid:{} templateNum:{}", cid, templateNum);
            if (templateNum > 0) {
                return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_DELETE_FAIL_BY_SEND_NODE.getMessage());
            } else {
                configureRepository.deleteById(cid);
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS_BY_SEND_NODE.getMessage());
            }
        } catch (Exception e) {
            log.warn("deleteSendNode mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 删除发送对象类型的配置项
     *
     * @param cid
     */
    protected ResResult deleteReceiveObject(Integer cid) {
        try {
            int templateNum = templateRepository.countByTargetType(cid);
            log.debug("deleteReceiveObject cid:{} templateNum:{}", cid, templateNum);
            if (templateNum > 0) {
                return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_DELETE_FAIL_BY_RECEIVE_OBJECT.getMessage());
            } else {
                configureRepository.deleteById(cid);
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS_BY_RECEIVE_OBJECT.getMessage());
            }
        } catch (Exception e) {
            log.warn("deleteReceiveObject mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 删除产品模块类型的配置项
     *
     * @param cid
     * @return
     */
    protected ResResult deleteProductModel(Integer cid) {
        try {
            int templateNum = templateRepository.countByPmModule(String.valueOf(cid));
            log.debug("deleteProductModel cid:{} templateNum:{}", cid, templateNum);
            if (templateNum > 0) {
                return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_DELETE_FAIL_BY_PRODUCT_MODULE.getMessage());
            } else {
                configureRepository.deleteById(cid);
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS_BY_PRODUCT_MODULE.getMessage());
            }
        } catch (Exception e) {
            log.warn("deleteProductModel mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 删除业务项类型的配置项
     *
     * @param cid
     */
    protected ResResult deleteBusinessItem(Integer cid) {
        try {
            List<Configure> configureList = configureRepository.findAllByConfigTypeAndFatherId(MsgConfigTypeEnum.SEND_NODE.getCode(), cid);
            log.info("deleteBusinessItem cid:{} childConfigureSize:{}", cid, configureList.size());
            if (configureList.isEmpty()) {
                configureRepository.deleteById(cid);
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS_BY_BUSINESS_ITEM.getMessage());
            } else {
                List<Integer> cidList = configureList.stream().map(configure -> configure.getCid()).collect(Collectors.toList());
                log.debug("deleteBusinessItem findAllByConfigTypeAndFatherId cidList:{}", cidList);
                int templateNum = templateRepository.countByNodeIdIn(cidList);
                if (templateNum > 0) {
                    log.info("deleteBusinessItem delete fail cid:{} templateNum:{}", cid, templateNum);
                    return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_DELETE_FAIL_BY_BUSINESS_ITEM.getMessage());
                } else {
                    configureRepository.deleteById(cid);
                    for (Integer kid : cidList) {
                        configureRepository.deleteById(kid);
                    }
                    return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS_BY_BUSINESS_ITEM.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("deleteBusinessItem mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 删除业务类型的配置项
     *
     * @param cid
     * @return
     */
    protected ResResult deleteBusinessType(Integer cid) {
        try {
            List<Configure> configureList = configureRepository.findAllByConfigTypeAndFatherId(MsgConfigTypeEnum.BUSINESS_ITEM.getCode(), cid);
            if (configureList.isEmpty()) {
                configureRepository.deleteById(cid);
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS.getMessage());
            } else {
                for (Configure configure : configureList) {
                    ResResult resResult = deleteBusinessItem(configure.getCid());
                    if (!ResponseUtils.isSuccess(resResult)){
                        return ResponseUtils.fail(MyExceptionStatus.CONFIGURE_DELETE_FAIL_BY_BUSINESS_ITEM.getMessage());
                    }
                }
                return ResponseUtils.success(MyExceptionStatus.CONFIGURE_DELETE_SUCCESS.getMessage());
            }
        } catch (Exception e) {
            log.warn("deleteBusinessType mysql error e:{}", e);
            return ResponseUtils.fail(MyExceptionStatus.NETWORK_ERROR.getMessage());
        }
    }

    /**
     * 检查节点描述是否存在
     *
     * @param configDescribe
     * @return
     */
    protected boolean existsConfigDescribe(String configDescribe) {
        Configure configure = configureRepository.findByConfigDescribe(configDescribe).orElse(null);
        return configure != null;
    }
}
