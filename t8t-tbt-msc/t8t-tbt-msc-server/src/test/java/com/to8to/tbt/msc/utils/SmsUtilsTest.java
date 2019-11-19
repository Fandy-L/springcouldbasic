package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(SpringRunner.class)
public class SmsUtilsTest extends BaseApplication {

    private String phone = "15824909673";
    private String smsContent = "您的验证码是456258";
    private String tencentBizType = "1";
    private String aliyunSignName = "土巴兔";

    @Test
    public void sendTencentSms(){
        try {
            SmsUtils.sendTencentSms(phone, smsContent, tencentBizType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void sendAliyunSms(){
        List<String> phoneList = Lists.newArrayList();
        phoneList.add(phone);
        String messageId = SmsUtils.sendAliyunSms(phoneList, smsContent, aliyunSignName, MsgCenterConfiguration.TEMPLATECODE_MARKETING);
        log.debug("messageId:{}", messageId);
        Assert.assertTrue(StringUtils.isNotBlank(messageId));
    }

    @Test
    public void sendMongateSms(){
        String response = SmsUtils.sendMongateSms(phone, smsContent, MsgCenterConfiguration.EMP_TO8TO_SC_USERID, MsgCenterConfiguration.EMP_TO8TO_SC_PASSWORD);
        log.debug("response:{}", response);
        Assert.assertTrue(StringUtils.isNotBlank(response));
    }
}
