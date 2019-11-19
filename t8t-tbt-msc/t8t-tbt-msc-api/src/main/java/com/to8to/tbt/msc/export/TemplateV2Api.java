package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.TemplateSearchItem;
import com.to8to.tbt.msc.vo.MsgTemplateSearchVO;
import com.to8to.tbt.msc.vo.TemplateGetVO;
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
public interface TemplateV2Api {
    /**
     * 创建模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "创建模板", response = ResResult.class)
    @PostMapping(value = "/createTemplate")
    ResResult createTemplate(@RequestBody @NotEmpty @Valid List<@NotNull CreateTemplateDTO> params);

    /**
     * 更新模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "更新模板", response = ResResult.class)
    @PostMapping(value = "/updateTemplateByTid")
    ResResult updateTemplateByTid(@RequestBody @NotEmpty @Valid List<@NotNull UpdateTemplateDTO> params);

    /**
     * 删除模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "删除模板", response = ResResult.class)
    @PostMapping(value = "/deleteTemplateByTid")
    ResResult deleteTemplateByTid(@RequestBody @NotEmpty @Valid List<@NotNull DeleteTemplateDTO> params);

    /**
     * 获取单个模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "获取单个模板", response = TemplateGetVO.class)
    @PostMapping(value = "/getTemplateByTid")
    ResResult<TemplateGetVO> getTemplateByTid(@RequestBody @NotEmpty @Valid List<@NotNull TemplateGetDTO> params);

    /**
     * 模板搜索
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "模板搜索", response = TemplateSearchItem.class)
    @PostMapping(value = "/searchMsgTemplate")
    ResResult<MsgTemplateSearchVO> searchMsgTemplate(@RequestBody @NotEmpty @Valid List<@NotNull TemplateSearchDTO> params);
}
