package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DozerUtils;
import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoKeyword;
import com.to8to.tbt.msc.repository.mongo.KeywordRepository;
import com.to8to.tbt.msc.service.KeywordService;
import com.to8to.tbt.msc.vo.KeywordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author edmund.yu
 */
@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Override
    public MsgCenterResponse addKeywords(String keyWords) {

        Map<String,Boolean> map = new HashMap<>(10);
        boolean flag = false;
        String[] kws = keyWords.split(",");
        for (String str: kws) {
            if (str.isEmpty()) {
                continue;
            }
            MongoKeyword mongoKeyWord = keywordRepository.findFirstByKeyword(str);
            if (mongoKeyWord == null) {
                MongoKeyword mongoKeywordForSave = new MongoKeyword();
                mongoKeywordForSave.setKeyword(str);
                mongoKeywordForSave.setCreateTime(System.currentTimeMillis()/1000);
                mongoKeywordForSave.setInsertTime(System.currentTimeMillis()/1000);
                try {
                    keywordRepository.save(mongoKeywordForSave);
                }catch (Exception e){
                    log.warn("KeywordServiceImpl.addKeywords Exception keyword:{}",str);
                }
                map.put(str, true);
                flag = true;
            } else {
                map.put(str, false);
            }
        }
        log.info(map.toString());
        return flag ? Response.SUCCESS : Response.FAIL;
    }

    @Override
    public MsgCenterResponse deleteById(String id) {

        keywordRepository.deleteById(id);
        return Response.SUCCESS;
    }

    @Override
    public List<KeywordVO> getKeywordAll() {

        List<MongoKeyword> mongoKeywordList;
        List<KeywordVO> keywordVos = new ArrayList<>();
        Sort sort = Sort.by(Sort.Order.desc("create_time"));
        mongoKeywordList = keywordRepository.findAll(sort);
        for (MongoKeyword word : mongoKeywordList) {
            KeywordVO wordVO = DozerUtils.map(word, KeywordVO.class);
            wordVO.setNickId(wordVO.getId());
            keywordVos.add(wordVO);
        }
        return keywordVos;
    }

    @Override
    public List<KeywordVO> searchKeyword(String word) {
        List<MongoKeyword> mongoKeywordList;
        List<KeywordVO> keywordVos = new ArrayList<>();
        if (word == null) {
            word = "";
        }
        Pattern pattern = Pattern.compile("^.*"+word+".*$",Pattern.CASE_INSENSITIVE);
        mongoKeywordList = keywordRepository.findAllByKeywordRegexOrderByCreateTimeDesc(pattern);
        for (MongoKeyword mongoKeyWord : mongoKeywordList) {
            KeywordVO wordVO = DozerUtils.map(mongoKeyWord, KeywordVO.class);
            wordVO.setNickId(wordVO.getId());
            keywordVos.add(wordVO);
        }
        return keywordVos;
    }

    @Override
    public MsgCenterResponse updateKeyword(List<String> params) {
        String id = params.get(0);
        String word = params.get(1);
        MongoKeyword mongoKeyword;
        mongoKeyword = keywordRepository.findFirstByKeyword(word);
        if (mongoKeyword != null){
            throw new RPCException(MyExceptionStatus.KEYWORD_IS_EXISTS);
        }else {
            MongoKeyword mongoKeywordForUpdate = new MongoKeyword();
            mongoKeywordForUpdate.setId(id);
            mongoKeywordForUpdate.setKeyword(word);
            try {
                keywordRepository.save(mongoKeywordForUpdate);
            }catch (Exception e){
                log.warn("KeyWordServiceImpl.updateKeyWord throw exception:");
                return Response.FAIL;
            }
        }
        return Response.SUCCESS;
    }

    @Override
    public KeywordVO getWord(String id) {
        MongoKeyword word = keywordRepository.findFirstById(id);
        KeywordVO wordVO = DozerUtils.map(word, KeywordVO.class);
        if (wordVO != null) {
            wordVO.setNickId(wordVO.getId());
        }
        return wordVO;
    }

}
