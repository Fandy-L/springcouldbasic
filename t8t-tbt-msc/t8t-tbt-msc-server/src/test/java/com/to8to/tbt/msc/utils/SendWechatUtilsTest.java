package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.BaseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class SendWechatUtilsTest extends BaseApplication {

    private String openId = "111111111111111111111111";
    private String content = "微信消息发送测试";

    @Test
    public void sendWeixin() {
        SendWechatUtils.sendWeixin(openId, content);
    }

    @Test
    public void sendWeixinEp() {
        SendWechatUtils.sendWeixinEp(openId, content);
    }
}
