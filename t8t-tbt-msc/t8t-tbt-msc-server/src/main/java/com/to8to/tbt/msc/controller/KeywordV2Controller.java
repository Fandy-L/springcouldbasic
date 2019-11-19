package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.KeywordCreateDTO;
import com.to8to.tbt.msc.dto.KeywordSearchDTO;
import com.to8to.tbt.msc.export.KeywordV2Api;
import com.to8to.tbt.msc.service.KeywordV2Service;
import com.to8to.tbt.msc.vo.KeywordSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class KeywordV2Controller implements KeywordV2Api {
    @Autowired
    private KeywordV2Service keywordV2Service;

    @Override
    public ResResult createKeyword(@RequestBody List<KeywordCreateDTO> params) {
        int id = keywordV2Service.createKeyword(params.get(0));
        return ResUtils.suc(String.valueOf(id));
    }

    @Override
    public ResResult<List<KeywordSearchVO>> listKeyword(@RequestBody List<KeywordSearchDTO> params) {
        return ResUtils.data(keywordV2Service.listKeyword(params.get(0)));
    }
}
