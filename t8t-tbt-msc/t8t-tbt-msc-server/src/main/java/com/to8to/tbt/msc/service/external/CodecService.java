package com.to8to.tbt.msc.service.external;

import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.vo.PhoneQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author juntao.guo
 */
@FeignClient(value = "t8t-plt-codec")
public interface CodecService {
    /**
     * 根据ID批量获取手机号
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/getPhonesByIds")
    ResResult<List<PhoneQueryVO>> getPhonesByIds(@RequestBody JSONObject params);
}
