package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.entity.mysql.main.AppTemplate;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class AppTemplateRepositoryTest extends BaseApplication {

    private int appId = 1;
    private int page = 1;
    private int size = 10;

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

    @Test
    public void searchByAppId(){
        List<Integer> tidList = Lists.newArrayList();
        tidList.add(1179);
        tidList.add(1180);
        Page<AppTemplate> appTemplatePage = appTemplateRepository.search(appId, null, tidList, 1, pageable);
        log.debug("appTemplatePage:{}", appTemplatePage);
        Assert.assertTrue(appTemplatePage.getTotalElements() == 2L);
        for (AppTemplate appTemplate : appTemplatePage.getContent()){
            Assert.assertTrue(appTemplate.getAppId() == appId);
        }
    }
}
