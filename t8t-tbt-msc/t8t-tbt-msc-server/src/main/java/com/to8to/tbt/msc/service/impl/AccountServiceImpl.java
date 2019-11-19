package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.to8to.tbt.msc.constant.ApiConstant;
import com.to8to.tbt.msc.entity.UserWrapper;
import com.to8to.tbt.msc.service.AccountService;
import com.to8to.tbt.msc.utils.HttpUtils;
import com.to8to.tbt.msc.vo.T8tTbtApiResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Value(value = "${t8t.tbt.api.http.sign}")
    private String sign;

    @Value(value = "${t8t.tbt.api.http.timeout}")
    private int timeout;

    @Autowired
    private HttpUtils httpUtils;

    @Override
    public HashMap<Integer, UserWrapper.Owner> batchQueryOwner(List<Integer> uidList) {
        HashMap<Integer, UserWrapper.Owner> ownerHashMap = new HashMap<>(20);
        if (uidList.size() > 0) {
            String uids = StringUtils.join(uidList, ",");
            String params = "?userIds=" + uids + "&sign=" + generateApiSign(uids);
            Object object = executeGet(ApiConstant.API_GET_USER_INFO + params);
            if (object != null) {
                JSONObject result = (JSONObject) JSONObject.toJSON(object);
                for (String uid : result.keySet()) {
                    ownerHashMap.put(Integer.valueOf(uid), result.getJSONObject(uid).toJavaObject(UserWrapper.Owner.class));
                }
                log.debug("AccountServiceImpl.batchQueryOwner uidList:{} ownerHashMap:{} size:{} object:{} result:{}", uidList, ownerHashMap, ownerHashMap.size(), object, result);
            }
        }
        return ownerHashMap;
    }

    /**
     * 生成签名
     *
     * @param value
     * @return
     */
    private String generateApiSign(String value) {
        value += sign;
        return DigestUtils.md5DigestAsHex(value.getBytes());
    }

    /**
     * 执行GET请求
     *
     * @param api
     * @return
     */
    private Object executeGet(String api) {
        try {
            T8tTbtApiResultVO resResult = sendGetRequest(buildApiUrl(api), new TypeReference<T8tTbtApiResultVO>() {});
            if (resResult instanceof T8tTbtApiResultVO) {
                if (resResult.getErrorCode().intValue() != ApiConstant.ERROR_CODE_SUCCESS) {
                    log.warn("AccountServiceImpl.execute error url:{} resResult:{}", api, resResult);
                } else {
                    log.debug("AccountServiceImpl.execute url:{} resResult:{}", api, resResult);
                    return resResult.getData();
                }
            } else {
                log.warn("AccountServiceImpl.execute fail url:{}", api);
            }
        } catch (Exception e) {
            log.warn("AccountServiceImpl.execute exception url:{} timeout:{} e:{}", api, timeout, e);
        }
        return null;
    }

    /**
     * 生成请求地址
     *
     * @param api
     * @return
     */
    private String buildApiUrl(String api) {
        return ApiConstant.T8T_TBT_API_HOST + api;
    }

    /**
     * 调用php接口发送get请求
     * @param url
     * @param typeReference
     * @param <T>
     * @return
     */
    private <T> T sendGetRequest(String url, TypeReference<T> typeReference) {
        try {
            log.debug(">>>>>>>>>>> start send get request... url : {}", url);
            long start = System.currentTimeMillis();
            String result = httpUtils.doGet(url);
            T t = JSON.parseObject(result, typeReference);
            long end = System.currentTimeMillis();
            log.debug(">>>>>>>>>>> send get request success. url : {}, costTime : {}, res : {}", url, (end - start), JSON.toJSONString(t));
            return t;
        } catch (Exception e) {
            log.error(">>>>>>>>>>> send get request exception : {}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }
}
