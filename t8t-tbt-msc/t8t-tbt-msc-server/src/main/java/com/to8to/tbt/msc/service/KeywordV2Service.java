package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.KeywordCreateDTO;
import com.to8to.tbt.msc.dto.KeywordSearchDTO;
import com.to8to.tbt.msc.vo.KeywordSearchVO;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface KeywordV2Service {

    /**
     * 获取关键词ID
     *
     * @param keyword
     * @return
     */
    int getKeywordId(String keyword);

    /**
     * 扫描关键词
     *
     * @param content
     * @return
     */
    Boolean scanKeyword(String content);

    /**
     * 新增关键词
     *
     * @param keywordCreateDTO
     * @return
     */
    int createKeyword(KeywordCreateDTO keywordCreateDTO);

    /**
     * 搜索关键词
     *
     * @param keywordSearchDTO
     * @return
     */
    List<KeywordSearchVO> listKeyword(KeywordSearchDTO keywordSearchDTO);
}
