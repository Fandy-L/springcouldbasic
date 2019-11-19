package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.KeywordCreateDTO;
import com.to8to.tbt.msc.dto.KeywordSearchDTO;
import com.to8to.tbt.msc.vo.KeywordSearchVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface KeywordV2Api {

    /**
     * 创建关键词
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "创建关键词", response = ResResult.class)
    @PostMapping(value = "/createKeyWord")
    ResResult createKeyword(@RequestBody @NotEmpty @Valid List<@NotNull KeywordCreateDTO> params);

    /**
     * 搜索关键词
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "搜索关键词", response = KeywordSearchVO.class)
    @PostMapping(value = "/listKeyWord")
    ResResult<List<KeywordSearchVO>> listKeyword(@RequestBody @NotEmpty @Valid List<@NotNull KeywordSearchDTO> params);
}
