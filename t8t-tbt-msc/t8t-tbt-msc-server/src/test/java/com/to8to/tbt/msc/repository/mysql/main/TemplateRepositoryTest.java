package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class TemplateRepositoryTest extends BaseApplication {

    @Autowired
    private TemplateRepository templateRepository;

    private int nodeId = 227;
    private int targetType = 4;
    private int sendType = 1;
    private int isActive = 1;
    private int isAuto = 1;
    private String pmModule = "174";
    private int startTime = 1530522841;
    private int endTime = 1568203159;

    @Test
    public void searchByNodeIds(){
        List<Integer> nodeIds = Lists.newArrayList();
        nodeIds.add(227);
        nodeIds.add(482);
        List<Template> templateList = templateRepository.search(null, null, null, null, null, null, nodeIds, 1, null, null);
        log.debug("size:{} templateList:{}", templateList.size(), templateList);
        Assert.assertFalse(templateList.isEmpty());
        for (Template template : templateList){
            Assert.assertTrue(nodeIds.contains(template.getNodeId()));
        }
    }

    @Test
    public void search(){
        List<Template> templateList = templateRepository.search(pmModule, sendType, targetType, isAuto, isActive, nodeId, null, null, startTime, endTime);
        log.debug("size:{} templateList:{}", templateList.size(), templateList);
        Assert.assertFalse(templateList.isEmpty());
        for (Template template : templateList){
            Assert.assertTrue(template.getPmModule().equals(pmModule) && template.getTargetType() == targetType && template.getSendType() == sendType && template.getIsActive() == isActive && template.getIsAuto() == isAuto);
            Assert.assertTrue(template.getNodeId() == nodeId && template.getCreateTime() >= startTime && template.getCreateTime() <= endTime);
        }
    }
}
