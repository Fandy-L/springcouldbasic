package com.to8to.tbt.msc.service.external;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.vo.OperateConfigGetVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@FeignClient(value = "t8t-ope-config")
public interface OperateConfigService {
    /**
     * 查询用户信息
     *
     * @param opeConfigGetDTO
     * @return
     */
    @PostMapping(value = "/getConfigs")
    ResResult<OperateConfigGetVO> getConfigs(@RequestBody @Valid JSONObject opeConfigGetDTO);
}
