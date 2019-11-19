package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.entity.mysql.main.MessageTemplate;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class MessageTemplateRepositoryTest extends BaseApplication {
    /**
     * 分页信息
     */
    private int page = 1;
    private int size = 10;

    /**
     * 查询参数
     */
    private int channelType = 2;
    private int isGround = 1;
    private int isLikeGround = 1;
    private String cityIds = "110100000000";
    private int id = 855;
    private int tid = 1158;

    /**
     * 查询结果记录数
     */
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Test
    public void searchByCityIds(){
        Page<MessageTemplate> messageTemplatePage = messageTemplateRepository.search(null, null, null, isLikeGround, cityIds, null, null, null, pageable);
        log.debug("messageTemplatePage:{}", messageTemplatePage);
        Assert.assertTrue(messageTemplatePage.getTotalElements() > 0L);
        for (MessageTemplate messageTemplate : messageTemplatePage.getContent()){
            Assert.assertTrue(messageTemplate.getIsGround() == isLikeGround || messageTemplate.getIsGround() == 3 || messageTemplate.getCityIds().indexOf(cityIds) >= 0);
        }
    }

    @Test
    public void searchByChannelAndIsGround(){
        Page<MessageTemplate> messageTemplatePage = messageTemplateRepository.search(null, channelType, isGround, null, null, null, null, null, pageable);
        log.debug("messageTemplatePage:{}", messageTemplatePage);
        Assert.assertTrue(messageTemplatePage.getTotalElements() > 0L);
        for (MessageTemplate messageTemplate : messageTemplatePage.getContent()){
            Assert.assertTrue(messageTemplate.getIsGround() == isGround || messageTemplate.getChannelType() == channelType);
        }
    }

    @Test
    public void searchByIdAndTid(){
        Page<MessageTemplate> messageTemplatePage = messageTemplateRepository.search(id, null, null, null, null, tid, null, null, pageable);
        log.debug("messageTemplatePage:{}", messageTemplatePage);
        Assert.assertTrue(messageTemplatePage.getTotalElements() == 1L);
        for (MessageTemplate messageTemplate : messageTemplatePage.getContent()){
            Assert.assertTrue(messageTemplate.getId() == id || messageTemplate.getTid() == tid);
        }
    }

    @Test
    public void searchByTidList(){
        List<Integer> tidList = Lists.newArrayList();
        tidList.add(1158);
        tidList.add(1125);
        Page<MessageTemplate> messageTemplatePage = messageTemplateRepository.search(null, null, null, null, null, null, tidList, 1, pageable);
        log.debug("messageTemplatePage:{}", messageTemplatePage);
        Assert.assertTrue(messageTemplatePage.getTotalElements() == 2L);
        for (MessageTemplate messageTemplate : messageTemplatePage.getContent()){
            Assert.assertTrue(tidList.contains(messageTemplate.getTid()));
        }
    }
}
