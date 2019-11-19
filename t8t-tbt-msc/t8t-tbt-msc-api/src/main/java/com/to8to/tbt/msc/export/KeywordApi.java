package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.KeywordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author edmund.yu
 */
@Api("关键字")
@Validated
public interface KeywordApi {
    /**
     * 添加关键字
     * @param params
     * @return
     */
    @ApiOperation(value = "添加关键字", response = MsgCenterResponse.class)
    @PostMapping(value = "addKeyWords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> addKeywords(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 删除关键字
     * @param params
     * @return
     */
    @ApiOperation(value = "删除关键字", response = MsgCenterResponse.class)
    @PostMapping(value = "deleteKeyWord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> deleteById(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 获取所有关键字
     * @return
     */
    @ApiOperation(value = "获取所有关键字")
    @PostMapping(value = "getKeyWordAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<List> getKeywordsAll();

    /**
     * 查询关键字
     * @param params
     * @return
     */
    @ApiOperation(value = "查询关键字")
    @PostMapping(value = "searchKeyWords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<List<KeywordVO>> searchKeyword(@RequestBody @NotEmpty @Valid List<String> params);

    /**
     * 更新关键字
     * @param params
     * @return
     */
    @ApiOperation(value = "更新关键字")
    @PostMapping(value = "updateKeyWord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> updateKeyword(@RequestBody @Size(min = 2) @Valid List<@NotBlank String> params);

    /**
     * 根据ID获取关键字参数
     * @param params
     * @return
     */
    @ApiOperation(value = "根据ID获取关键字参数", response = KeywordVO.class)
    @PostMapping(value = "getWord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<KeywordVO> getWord(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);
}

