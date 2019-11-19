package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.GroupNoteCreateDTO;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.vo.GroupNoteGetVO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface GroupNoteApi {

    /**
     * 新增群发短信模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "新增群发短信模板", response = ResResult.class)
    @PostMapping(value = "/addGroupNote")
    ResResult addGroupNote(@RequestBody @NotEmpty @Valid List<@NotNull GroupNoteCreateDTO> params);

    /**
     * 删除群发短信模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "删除群发短信模板", response = ResResult.class)
    @PostMapping(value = "/delGroupNote")
    ResResult delGroupNote(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 更新群发短信模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "更新群发短信模板", response = ResResult.class)
    @PostMapping(value = "/updateGroupNote")
    ResResult updateGroupNote(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);

    /**
     * 搜索群发短信模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "搜索群发短信模板", response = GroupNoteSearchVO.class)
    @PostMapping(value = "/searchGroupNote")
    ResResult<GroupNoteSearchVO> searchGroupNote(@RequestBody @NotEmpty @Valid List<@NotNull GroupNoteSearchDTO> params);

    /**
     * 获取单个群发短信模板
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "获取单个群发短信模板", response = GroupNoteGetVO.class)
    @PostMapping(value = "/getGroupNote")
    ResResult<GroupNoteGetVO> getGroupNote(@RequestBody @NotEmpty @Valid List<@NotBlank String> params);
}
