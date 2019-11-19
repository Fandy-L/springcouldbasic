package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.KeywordVO;

import java.util.List;

/**
 * @author edmund.yu
 */
public interface KeywordService {

    /**
     * 增加关键字
     * @param keyWords
     * @return
     */
    MsgCenterResponse addKeywords(String keyWords);

    /**
     * 根据ID删除关键字
     * @param id
     * @return
     */
    MsgCenterResponse deleteById(String id);

    /**
     * 获取所有关键字
     * @return
     */
    List<KeywordVO> getKeywordAll();

    /**
     * 根据字符寻找匹配关键字
     * @param word
     * @return
     */
    List<KeywordVO> searchKeyword(String word);

    /**
     * 根据ID和字符更新关键字
     * @param params
     * @return
     */
    MsgCenterResponse updateKeyword(List<String> params);

    /**
     * 根据ID获取关键字
     * @param id
     * @return
     */
    KeywordVO getWord(String id);
}
