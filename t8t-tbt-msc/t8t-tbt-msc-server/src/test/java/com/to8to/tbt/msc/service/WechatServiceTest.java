package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.vo.wechat.WeChatResMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class WechatServiceTest extends BaseApplication {

    @Autowired
    private AppMsgService appMsgService;

    @Test
    public void sendWeChatAlarmMsg(){
        String content = "微信告警消息";
        WeChatResMsgVO weChatResMsgVO = appMsgService.sendWeChatAlarmMsg(content);
        log.debug("weChatResMsgVO:{}", weChatResMsgVO);
        Assert.assertTrue(weChatResMsgVO.getSuccess());
    }
}
