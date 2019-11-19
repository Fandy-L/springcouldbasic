package com.to8to.tbt.msc.service.external;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.FeignRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@FeignClient(value = "t8t-crm-background")
public interface CrmService {
    /**
     * 根据手机号获取关联ID
     * @param params
     * @return
     */
    @PostMapping(value = "/getIdByPhone")
    ResResult getIdByPhone(@RequestBody @Valid FeignRequestDTO params);
}
