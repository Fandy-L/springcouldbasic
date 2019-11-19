package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.ConfigureCreateDTO;
import com.to8to.tbt.msc.dto.ConfigureDeleteDTO;
import com.to8to.tbt.msc.dto.ConfigureSearchDTO;
import com.to8to.tbt.msc.dto.ConfigureUpdateDTO;
import com.to8to.tbt.msc.entity.OldMsgResponse;
import com.to8to.tbt.msc.vo.ConfigureSearchVO;
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
public interface ConfigureApi {

    /**
     * 创建配置项
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "创建配置项", response = ResResult.class)
    @PostMapping(value = "/createConfiguration")
    ResResult createConfiguration(@RequestBody @NotEmpty @Valid List<@NotNull ConfigureCreateDTO> params);

    /**
     * 修改配置项
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改配置项", response = ResResult.class)
    @PostMapping(value = "/updateConfiguration")
    ResResult updateConfiguration(@RequestBody @NotEmpty @Valid List<@NotNull ConfigureUpdateDTO> params);

    /**
     * 删除配置项
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "删除配置项", response = ResResult.class)
    @PostMapping(value = "/deleteConfiguration")
    ResResult deleteConfiguration(@RequestBody @NotEmpty @Valid List<@NotNull ConfigureDeleteDTO> params);

    /**
     * 搜索配置项
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "搜索配置项", response = ConfigureSearchVO.class)
    @PostMapping(value = "/searchConfiguration")
    ResResult<List<ConfigureSearchVO>> searchConfiguration(@RequestBody @NotEmpty @Valid List<@NotNull ConfigureSearchDTO> params);
}
