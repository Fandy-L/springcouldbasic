package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.export.KeywordApi;
import com.to8to.tbt.msc.service.KeywordService;
import com.to8to.tbt.msc.vo.KeywordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author edmund.yu
 */
@RestController
public class KeywordController implements KeywordApi {

    @Autowired
    private KeywordService keywordService;

   @Override
    public ResResult<MsgCenterResponse> addKeywords(@RequestBody List<String> params){
        return ResUtils.data(keywordService.addKeywords(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> deleteById(@RequestBody List<String> params){
        return ResUtils.data(keywordService.deleteById(params.get(0)));
    }

    @Override
    public ResResult<List> getKeywordsAll(){
        return ResUtils.data(keywordService.getKeywordAll());
    }

    @Override
    public ResResult<List<KeywordVO>> searchKeyword(@RequestBody List<String> params){
        return ResUtils.data(keywordService.searchKeyword(params.get(0)));
    }

    @Override
    public ResResult<MsgCenterResponse> updateKeyword(@RequestBody List<String> params){
        return ResUtils.data(keywordService.updateKeyword(params));
    }

    @Override
    public ResResult<KeywordVO> getWord(@RequestBody List<String> params){
        return ResUtils.data(keywordService.getWord(params.get(0)));
    }
}

