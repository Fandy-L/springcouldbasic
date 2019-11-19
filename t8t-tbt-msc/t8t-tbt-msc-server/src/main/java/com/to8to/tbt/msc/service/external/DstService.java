package com.to8to.tbt.msc.service.external;

import com.to8to.dst.dto.LocalDistrictServiceWrapper;
import com.to8to.dst.entity.vo.LocalTownVO;
import com.to8to.sc.response.ResResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@FeignClient(value = "t8t-sys-dst")
public interface DstService {
    /**
     * 根据标准ID获取城市ID
     * @param params
     * @return
     */
    @PostMapping(value = "/localId/get/byStandardId")
    ResResult<Integer> localIdGetByStandardId(@RequestBody @Valid LocalDistrictServiceWrapper.LocalIdGetByStandardIdDTO params);

    /**
     * 根据城市ID获取区域信息
     * @param params
     * @return
     */
    @PostMapping(value = "/localTown/get")
    ResResult<LocalTownVO> localTownGet(@RequestBody @Valid LocalDistrictServiceWrapper.LocalTownGetDTO params);
}
