package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.entity.mongo.MongoKeyword;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author edmund.yu
 */

public interface KeywordRepository extends MongoRepository<MongoKeyword,String> {

    /**
     * 根据关键字查找
     * @param keyword
     * @return
     */
    MongoKeyword findFirstByKeyword(String keyword);

    /**
     * 根据关键字正则匹配
     * @param pattern
     * @return
     */
    List<MongoKeyword> findAllByKeywordRegexOrderByCreateTimeDesc(Pattern pattern);

    /**
     * 根据Id查找
     * @param id
     * @return
     */
    MongoKeyword findFirstById(String id);
}
