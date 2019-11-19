//package com.to8to.tbt.msc.utils;
//
//import com.oppo.push.server.*;
//import com.to8to.tbt.msc.BaseApplication;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//public class OPPOPushTest extends BaseApplication {
//    @Test
//    public void OPPOPush() throws Exception {
//        Sender sender = new Sender("", "");
//
//        Notification notification = new Notification(); //创建通知栏消息体
//        notification.setTitle("您好");
//        notification.setContent("早上好");
//        notification.setClickActionType(2);
//        notification.setClickActionUrl("");
//        notification.setChannelId(":OPPO PUSH");
//        Target target = Target.build("CN_8fa0618f178145d8c2a44091a1326411"); //创建发送对象
//
//        Result result = sender.unicastNotification(notification, target);  //发送单推消息
//
//        log.info("状态码:{},返回码:{},消息id:{}",result.getStatusCode(),result.getReturnCode(),result.getMessageId());
//    }
//
//    @Test
//    public void OPPOPushAll() throws Exception {
//
//        Sender sender = new Sender("112c5b352ae54beaa92dbf8d34a0dd1e", "dd225473dfb74621a8424dc0fc56087e");
//
//        Notification notification = new Notification(); //创建通知栏消息体
//        notification.setTitle("卫生间舒适新装饰，快看看！");
//        notification.setContent("11套卫生间干湿分离设计方案，不看就亏了！");
//        notification.setClickActionType(2);
//        notification.setClickActionUrl("hap://app/com.tubatu.demo/index");
//        notification.setChannelId(":OPPO PUSH");
//
//        Result saveResult = sender.saveNotification(notification);
//        log.info(saveResult.toString());
//        log.info("消息id:{},返回码:{}",saveResult.getMessageId(),saveResult.getReturnCode());
//        String messageId = saveResult.getMessageId();
//        int code = saveResult.getReturnCode().getCode();
//        Target target = new Target();
//        target.setTargetType(TargetType.ALL);
//        target.setTargetValue("ALL");
//        Result result = new Result();
//        if (code == 0) {
//            result = sender.broadcastNotification(messageId,target);  //发送单推消息
//        }
//        log.info("状态码:{},返回码:{},消息id:{}",result.getStatusCode(),result.getReturnCode(),result.getMessageId());
//    }
//
//}
