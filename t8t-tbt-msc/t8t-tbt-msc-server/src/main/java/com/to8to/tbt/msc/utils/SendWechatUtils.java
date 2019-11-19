package com.to8to.tbt.msc.utils;

import com.google.common.collect.ImmutableMap;
import com.to8to.common.http.*;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.entity.WechatMessageWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Slf4j
public class SendWechatUtils {

    private static WebClient webClient;

    /**
     * 发送微信消息
     *
     * @param openid
     * @param content
     * @return
     */
    public static boolean sendWeixin(String openid, String content) {
        WechatMessageWrapper.SendDTO sendDTO = WechatMessageWrapper.SendDTO.builder()
                .openid(openid)
                .content(content)
                .build();
        try {
            WechatMessageWrapper.SendVO sendVO = getWebClient().execute(MsgCenterConfiguration.WEIXIN_SEND_MSG_URL, sendDTO, WechatMessageWrapper.SendVO.class);
            if (sendVO != null && sendVO.getCode() == MsgConstant.WECHAT_RESPONSE_SUCCESS_CODE) {
                log.info("sendWeixin success openid:{} content:{} sendVO:{}", openid, content, sendVO);
                return true;
            } else {
                log.warn("sendWeixin error openid:{} content:{} sendVO:{}", openid, content, sendVO);
                return false;
            }
        } catch (Exception e) {
            log.warn("sendWeixin fail openid:{} content:{} e:{}", openid, content, e);
            return false;
        }
    }

    /**
     * 发送企业微信
     *
     * @param username
     * @param content
     * @return
     */
    public static boolean sendWeixinEp(String username, String content) {
        try {
            Map<String, String> header = new HashMap<>(1);
            header.put("Content-Type", "application/x-www-form-urlencoded");
            Map<String, String> map = ImmutableMap.of("username", username, content, content);
            RequestBuilder builder = RequestBuilder.builder()
                    .url(MsgCenterConfiguration.WEIXINEP_SEND_MSG_URL)
                    .body(map)
                    .method(HttpMethod.POST)
                    .typeReference(new TypeReference<WechatMessageWrapper.SendEpVO>() {
                    })
                    .header(header)
                    .build();
            WechatMessageWrapper.SendEpVO sendEpVO = getWebClient().execute(builder);
            if (sendEpVO != null && sendEpVO.getCode() == MsgConstant.WECHAT_RESPONSE_SUCCESS_CODE) {
                log.info(LogUtils.buildTemplate("openid content sendVO"), username, content, sendEpVO);
                return true;
            } else {
                log.warn(LogUtils.buildTemplate("openid content sendVO"), username, content, sendEpVO);
                return false;
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildExceptionTemplate("openid content"), username, content, e);
            return false;
        }
    }

    /**
     * 获取http请求实例
     *
     * @return
     */
    private static WebClient getWebClient() {
        if (webClient == null) {
            webClient = new DefaultWebClient();
        }
        return webClient;
    }
}
