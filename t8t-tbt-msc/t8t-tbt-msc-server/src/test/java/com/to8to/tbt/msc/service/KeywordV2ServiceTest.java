package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.KeywordCreateDTO;
import com.to8to.tbt.msc.dto.KeywordSearchDTO;
import com.to8to.tbt.msc.entity.mysql.main.Keyword;
import com.to8to.tbt.msc.repository.mysql.main.KeywordV2Repository;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.KeywordSearchVO;
import lombok.extern.slf4j.Slf4j;
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
public class KeywordV2ServiceTest extends BaseApplication {

    @Autowired
    private KeywordV2Service keywordV2Service;

    @Autowired
    private KeywordV2Repository keywordV2Repository;

    @Test
    public void listKeyword(){
        String keyword = "监理";
        KeywordSearchDTO keywordSearchDTO =  KeywordSearchDTO.builder()
                .keyword("%" + keyword + "%")
                .build();
        List<KeywordSearchVO> keywordSearchVOList = keywordV2Service.listKeyword(keywordSearchDTO);
        log.debug("size:{} list:{}", keywordSearchVOList.size(), keywordSearchVOList);
        for (KeywordSearchVO keywordSearchVO : keywordSearchVOList){
            Assert.assertTrue(keywordSearchVO.getKeyword().indexOf(keyword) >= 0);
        }
    }

    @Test
    public void createKeyword(){
        String keyword = "关键词测试创建" + TimeUtils.getCurrentTimestamp();
        int createId = 1554;
        KeywordCreateDTO keywordCreateDTO = KeywordCreateDTO.builder()
                .keyword(keyword)
                .createId(createId)
                .build();
        int id = keywordV2Service.createKeyword(keywordCreateDTO);
        log.debug("createKeyword id:{}", id);
        Keyword MsgcKeywrd = keywordV2Repository.findByKeyword(keyword).orElse(null);
        Assert.assertTrue(keyword != null);
    }
}
