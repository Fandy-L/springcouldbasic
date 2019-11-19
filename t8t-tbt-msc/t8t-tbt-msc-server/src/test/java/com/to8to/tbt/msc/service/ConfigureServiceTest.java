package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.dto.ConfigureCreateDTO;
import com.to8to.tbt.msc.dto.ConfigureDeleteDTO;
import com.to8to.tbt.msc.dto.ConfigureSearchDTO;
import com.to8to.tbt.msc.dto.ConfigureUpdateDTO;
import com.to8to.tbt.msc.entity.OldMsgResponse;
import com.to8to.tbt.msc.entity.mysql.main.Configure;
import com.to8to.tbt.msc.enumeration.MsgConfigTypeEnum;
import com.to8to.tbt.msc.repository.mysql.main.ConfigureRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.ConfigureSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class ConfigureServiceTest extends BaseApplication {

    @Autowired
    private ConfigureService configureService;

    @Autowired
    private ConfigureRepository configureRepository;

    @Autowired
    private TemplateRepository templateRepository;

    /**
     * 根据类型和父级ID查询
     */
    @Test
    public void searchConfigurationByConfigTypeAndFatherId() {
        int configType = 3;
        int fatherId = 199;
        ConfigureSearchDTO configureSearchDTO = ConfigureSearchDTO.builder()
                .configType(configType)
                .fatherId(fatherId)
                .build();
        List<ConfigureSearchVO> configureSearchVOList = configureService.searchConfiguration(configureSearchDTO);
        log.debug("size:{} list:{}", configureSearchVOList.size(), configureSearchVOList);
        for (ConfigureSearchVO configureSearchVO : configureSearchVOList) {
            Assert.assertTrue(configureSearchVO.getConfigType() == configType && configureSearchVO.getFatherId() == fatherId);
        }
    }

    /**
     * 根据批量父级ID查询
     */
    @Test
    public void searchConfigurationByFatherIds() {
        String fatherIds = "199,477";
        ConfigureSearchDTO configureSearchDTO = ConfigureSearchDTO.builder()
                .fatherIds(fatherIds)
                .build();
        List<ConfigureSearchVO> configureSearchVOList = configureService.searchConfiguration(configureSearchDTO);
        log.debug("size:{} list:{}", configureSearchVOList.size(), configureSearchVOList);
        for (ConfigureSearchVO configureSearchVO : configureSearchVOList) {
            Assert.assertTrue(fatherIds.indexOf(String.valueOf(configureSearchVO.getFatherId())) >= 0);
        }
    }

    /**
     * 创建配置项
     */
    @Test
    public void createConfiguration() {
        int configType = 3;
        String configDescribe = "测试节点创建" + TimeUtils.getCurrentTimestamp();
        int fatherId = 198;
        int createId = 20929;
        ConfigureCreateDTO configureCreateDTO = ConfigureCreateDTO.builder()
                .configType(configType)
                .configDescribe(configDescribe)
                .fatherId(fatherId)
                .createId(createId)
                .build();
        int cid = configureService.createConfiguration(configureCreateDTO);
        log.debug("createConfiguration cid:{}", cid);
        Configure configure = configureRepository.findByConfigDescribe(configDescribe).orElse(null);
        Assert.assertTrue(configure != null);
    }

    /**
     * 更新配置项
     */
    @Test
    public void updateConfiguration() {
        int cid = findCidByConfigType(MsgConfigTypeEnum.SEND_NODE.getCode());
        String configDescribe = "测试节点更新" + TimeUtils.getCurrentTimestamp();
        int modifyId = 20929;
        ConfigureUpdateDTO configureUpdateDTO = ConfigureUpdateDTO.builder()
                .cid(cid)
                .configDescribe(configDescribe)
                .build();
        ResResult<OldMsgResponse> resResult = configureService.updateConfiguration(configureUpdateDTO);
        log.debug("updateConfiguration resResult:{}", resResult);
        Configure configure = configureRepository.findByConfigDescribe(configDescribe).orElse(null);
        Assert.assertTrue(configure != null);
    }

    /**
     * 更新配置项
     */
    @Test
    public void deleteConfigurationBySendNode() {
        int cid = findCidByConfigType(MsgConfigTypeEnum.SEND_NODE.getCode());
        ConfigureDeleteDTO configureDeleteDTO = ConfigureDeleteDTO.builder()
                .configType(MsgConfigTypeEnum.SEND_NODE.getCode())
                .cid(cid)
                .build();
        ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
        log.debug("deleteConfigurationByReceiveObject resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)) {
            Assert.assertFalse(existsConfigure(cid));
        } else {
            Assert.assertTrue(existsConfigure(cid));
        }
    }

    @Test
    public void deleteConfigurationByReceiveObject() {
        int cid = findCidByConfigType(MsgConfigTypeEnum.RECEIVE_OBJECT.getCode());
        ConfigureDeleteDTO configureDeleteDTO = ConfigureDeleteDTO.builder()
                .configType(MsgConfigTypeEnum.RECEIVE_OBJECT.getCode())
                .cid(cid)
                .build();
        ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
        log.debug("deleteConfigurationByReceiveObject resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)) {
            Assert.assertFalse(existsConfigure(cid));
        } else {
            Assert.assertTrue(existsConfigure(cid));
        }
    }

    @Test
    public void deleteConfigurationByProductModule() {
        int cid = findCidByConfigType(MsgConfigTypeEnum.PRODUCT_MODULE.getCode());
        ConfigureDeleteDTO configureDeleteDTO = ConfigureDeleteDTO.builder()
                .configType(MsgConfigTypeEnum.PRODUCT_MODULE.getCode())
                .cid(cid)
                .build();
        ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
        log.debug("deleteConfigurationByProductModule resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)) {
            Assert.assertFalse(existsConfigure(cid));
        } else {
            Assert.assertTrue(existsConfigure(cid));
        }
    }

    @Test
    public void deleteConfigurationByBusinessItem() {
        boolean hasTemplate = false;
        boolean notTemplate = false;
        ConfigureDeleteDTO configureDeleteDTO = ConfigureDeleteDTO.builder()
                .configType(MsgConfigTypeEnum.BUSINESS_ITEM.getCode())
                .build();
        List<Configure> configureList = configureRepository.findAllByConfigTypeOrderByCidDesc(MsgConfigTypeEnum.BUSINESS_ITEM.getCode());
        for (Configure configure : configureList) {
            List<Configure> childConfigureList = configureRepository.findAllByConfigTypeAndFatherId(MsgConfigTypeEnum.SEND_NODE.getCode(), configure.getCid());
            List<Integer> cidList = childConfigureList.stream().map(childConfigure -> childConfigure.getCid()).collect(Collectors.toList());
            int templateNum = templateRepository.countByNodeIdIn(cidList);
            configureDeleteDTO.setCid(configure.getCid());
            if (templateNum > 0) {
                if (!hasTemplate) {
                    hasTemplate = true;
                    ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
                    log.debug("deleteConfigurationByBusinessItem hasTemplate cid:{} resResult:{}", configure.getCid(), resResult);
                    Assert.assertTrue(existsConfigure(configure.getCid()));
                }
            } else {
                if (!notTemplate) {
                    notTemplate = true;
                    ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
                    log.debug("deleteConfigurationByBusinessItem notTemplate cid:{} resResult:{}", configure.getCid(), resResult);
                    Assert.assertFalse(existsConfigure(configure.getCid()));
                }
            }
        }
    }

    @Test
    public void deleteConfigurationByBusinessType() {
        int cid = findCidByConfigType(MsgConfigTypeEnum.BUSINESS_TYPE.getCode());
        ConfigureDeleteDTO configureDeleteDTO = ConfigureDeleteDTO.builder()
                .configType(MsgConfigTypeEnum.BUSINESS_TYPE.getCode())
                .cid(cid)
                .build();
        ResResult<OldMsgResponse> resResult = configureService.deleteConfiguration(configureDeleteDTO);
        log.debug("deleteConfigurationByBusinessType resResult:{}", resResult);
        if (ResponseUtils.isSuccess(resResult)) {
            Assert.assertFalse(existsConfigure(cid));
        } else {
            Assert.assertTrue(existsConfigure(cid));
        }
    }

    /**
     * 根据分类获取最后一条记录
     *
     * @param configType
     * @return
     */
    private int findCidByConfigType(int configType) {
        Configure configure = configureRepository.findFirstByConfigTypeOrderByCidDesc(configType).orElse(null);
        log.info("findCidByConfigType cid:{} configure:{}", configure.getCid(), configure);
        int cid = configure != null ? configure.getCid() : 0;
        Assert.assertTrue(cid > 0);
        return cid;
    }

    /**
     * 根据主键检查配置项是否存在
     *
     * @param cid
     * @return
     */
    private boolean existsConfigure(int cid) {
        Configure configure = configureRepository.findById(cid).orElse(null);
        return configure == null ? false : true;
    }
}
