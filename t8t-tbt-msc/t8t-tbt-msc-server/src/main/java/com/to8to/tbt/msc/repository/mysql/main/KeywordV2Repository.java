package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.Keyword;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author juntao.guo
 */
public interface KeywordV2Repository extends CrudRepository<Keyword, Integer> {
    /**
     * 根据关键词查询
     *
     * @param keyword
     * @return
     */
    Optional<Keyword> findByKeyword(String keyword);

    /**
     * 根据关键词模糊查询
     *
     * @param keyword
     * @return
     */
    List<Keyword> findByKeywordLike(String keyword);
}
