package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.AlarmDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class AlarmServiceTest extends BaseApplication {

    @Autowired
    private AlarmService alarmService;

    @Test
    public void sendAlarm() {
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setModule("短信发送[腾讯]");
        alarmDTO.setMessage(String.format("腾讯短信接口进行第%s次重试异常，短信未能成功发送，电话号码%s,内容%s！", 1, "15824909673", "您正在进行土巴兔商家注册，验证码为:518341，请在1分钟内输入验证码。"));
        alarmService.sendAlarm(alarmDTO);
    }
}
