package com.to8to.tbt.msc.service.impl;

import com.google.common.collect.Lists;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.dto.KeywordCreateDTO;
import com.to8to.tbt.msc.dto.KeywordSearchDTO;
import com.to8to.tbt.msc.entity.mysql.main.Keyword;
import com.to8to.tbt.msc.repository.mysql.main.KeywordV2Repository;
import com.to8to.tbt.msc.service.KeywordV2Service;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.KeywordSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class KeywordV2ServiceImpl implements KeywordV2Service {
    @Autowired
    private KeywordV2Repository keywordV2Repository;

    Pattern keywordPattern = Pattern.compile("\\{([^#>;}]+)\\}");


    @Override
    public int getKeywordId(String keyword) {
        int kid = 0;
        try {
            Keyword entity = keywordV2Repository.findByKeyword(keyword).orElse(new Keyword());
            log.debug("getKeywordId keyword:{} entity:{}", keyword, entity);
            kid = entity.getKid();
        } catch (Exception e) {
            log.warn("getKeywordId error keyword:{} e:{}", keyword, e);
        }
        return kid;
    }

    @Override
    public Boolean scanKeyword(String content) {
        List<String> keywordList = new LinkedList<>();
        Matcher matcher = keywordPattern.matcher(content);
        while (matcher.find()) {
            String hit = matcher.group();
            keywordList.add(hit);
        }
        for (String keyword : keywordList) {
            keyword = StringUtils.substringBetween(keyword, "{", "}");
            int kid = getKeywordId(keyword);
            if (kid == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int createKeyword(KeywordCreateDTO keywordCreateDTO) {
        if (isKeywordExist(keywordCreateDTO.getKeyword())) {
            return 0;
        }
        Keyword keyword = DozerUtils.map(keywordCreateDTO, Keyword.class);
        keyword.setCreateTime(TimeUtils.getCurrentTimestamp());
        try {
            keywordV2Repository.save(keyword);
            return keyword.getKid();
        } catch (Exception e) {
            log.warn("createKeyWord mysql error keywordCreateDTO:{} e:{}", keywordCreateDTO, e);
            return -1;
        }
    }

    @Override
    public List<KeywordSearchVO> listKeyword(KeywordSearchDTO keywordSearchDTO) {
        List<KeywordSearchVO> keywordSearchVOList = new ArrayList<>();
        try {
            List<Keyword> keywordList;
            if (StringUtils.isNotBlank(keywordSearchDTO.getKeyword())) {
                keywordList = keywordV2Repository.findByKeywordLike("%"+keywordSearchDTO.getKeyword()+"%");
            } else {
                keywordList = Lists.newArrayList(keywordV2Repository.findAll());
            }
            for (Keyword keyword : keywordList) {
                keywordSearchVOList.add(DozerUtils.map(keyword, KeywordSearchVO.class));
            }
        } catch (Exception e) {
            log.warn("listKeyWord exception keywordSearchDTO:{} e:{}", keywordSearchDTO, e);
        }
        return keywordSearchVOList;
    }

    /**
     * 检查关键词是否存在
     *
     * @param word
     * @return
     */
    protected boolean isKeywordExist(String word) {
        Keyword keyword = keywordV2Repository.findByKeyword(word).orElse(null);
        return keyword != null;
    }
}
