package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.MsgTemplateDTO;
import com.to8to.tbt.msc.dto.ListTemplateDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.ListTemplateVO;
import com.to8to.tbt.msc.vo.MsgTemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author edmund.yu
 */
@Api(value = "模板")
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface TemplateApi {

    /**
     * 新建模板
     * @param params
     * @return
     */
    @ApiOperation(value = "新建模板", response = MsgCenterResponse.class)
    @PostMapping(value = "addTemplate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> addTemplate(@RequestBody @NotEmpty @Valid List<@NotNull MsgTemplateDTO> params);

    /**
     * 更新模板1
     * @param params
     * @return
     */
    @ApiOperation(value = "更新模板", response = MsgCenterResponse.class)
    @PostMapping(value = "updateTemplate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> updateTemplate(@RequestBody @NotEmpty @Valid List<@NotNull MsgTemplateDTO> params);

    /**
     * 根据ID删除模板
     * @param params
     * @return
     */
    @ApiOperation(value = "依据ID删除模板", response = MsgCenterResponse.class)
    @PostMapping(value = "deleteTemplateById", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgCenterResponse> deleteTemplateById(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 依据节点列出模板
     * @param params
     * @return
     */
    @ApiOperation(value = "依据节点列出模板")
    @PostMapping(value = "listTemplByNote", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<List<MsgTemplateVO>> listTemplateByNote(@RequestBody @NotEmpty @Valid List<@NotNull Integer> params);

    /**
     * 列出模板
     * @param params
     * @return
     */
    @ApiOperation(value = "列出模板")
    @PostMapping(value = "listTemplate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<ListTemplateVO> listTemplate(@RequestBody @NotEmpty @Valid List<@NotNull ListTemplateDTO> params);

    /**
     * 得到一个消息模板
     * @param params
     * @return
     */
    @ApiOperation(value = "得到一个消息模板", response = MsgTemplateVO.class)
    @PostMapping(value = "getTemplate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResResult<MsgTemplateVO> getTemplate(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);
}
