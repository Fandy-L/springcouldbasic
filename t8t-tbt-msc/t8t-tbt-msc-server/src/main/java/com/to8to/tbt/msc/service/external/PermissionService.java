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
@FeignClient(value = "t8t-plt-permission")
public interface PermissionService {

    /**
     * 查询用户信息
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/user/getlist")
    ResResult userGetList(@RequestBody @Valid FeignRequestDTO params);
}
