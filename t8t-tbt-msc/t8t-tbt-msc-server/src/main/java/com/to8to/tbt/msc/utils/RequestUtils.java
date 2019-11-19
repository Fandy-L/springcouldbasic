package com.to8to.tbt.msc.utils;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.entity.PageInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntao.guo
 */
@Slf4j
public class RequestUtils {
    /**
     * 过滤分页参数
     *
     * @param pageInfo
     * @return
     */
    public static PageInfo filterPageInfo(PageInfo pageInfo) {
        if (pageInfo == null) {
            pageInfo = PageInfo.builder()
                    .currPage(1)
                    .pageSize(MsgConstant.PAGE_SIZE)
                    .build();
        } else {
            pageInfo.setCurrPage(IntegerUtils.intValueAsDefault(pageInfo.getCurrPage(), 1));
            pageInfo.setPageSize(IntegerUtils.intValueAsDefault(pageInfo.getPageSize(), MsgConstant.PAGE_SIZE));
        }
        return pageInfo;
    }

    /**
     * 生成Platform请求体
     *
     * @param args
     * @return
     */
    public static JSONObject buildFeignRequestBody(Object args) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("args", JSONObject.parse(JSONObject.toJSONString(args)));
            return requestBody;
        } catch (Exception e) {
            log.warn("buildFeignRequestBody exception args:{} e:{}", args, e);
            return null;
        }
    }
}
