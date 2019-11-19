package com.to8to.tbt.msc.service.external;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.vo.CompanyResultWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author juntao.guo
 */
@FeignClient(value = "t8t-dcp-dcm")
public interface CompanyService {
    /**
     * 查询装企信息
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/views/decInfo/queryList")
    ResResult<List<CompanyResultWrapper.Business>> decInfoQueryList(@RequestBody @Valid JSONObject params);

    /**
     * 查询装企子账号信息
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/views/decMember/queryList")
    ResResult<List<CompanyResultWrapper.DecMember>> decMemberQueryList(@RequestBody @Valid JSONObject params);
}
