//package com.to8to.tbt.msc.utils;
//
//import com.to8to.tbt.msc.BaseApplication;
//import com.to8to.tbt.msc.common.Constants;
//import com.xiaomi.xmpush.server.Constants;
//import com.xiaomi.xmpush.server.Message;
//import com.xiaomi.xmpush.server.Result;
//import com.xiaomi.xmpush.server.Sender;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.json.simple.parser.ParseException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//public class MiPushTest extends BaseApplication {
//
//    @Test
//    public void MiPush() throws Exception {
//        Constants.useSandbox();
//        Sender sender = new Sender("a");
//        Message message =new  Message.Builder()
//                .title("标题")
//                .description("描述")
//                .passThrough(0)
//                .extra(Constants.EXTRA_PARAM_WEB_URI, "http://www.xiaomi.com")
//                .build();
//
//        List<String> audiences = new ArrayList<>();
//        audiences.add("regId");
//        Result result = sender.sendHybridMessageByRegId(message, audiences,3);
//        log.info("错误码:{},错误原因:{},消息id:{}", result.getErrorCode(),result.getReason(),result.getMessageId());
//    }
//
//    @Test
//    public void MiPushAll() throws IOException, ParseException {
//        Constants.useOfficial();
//        Sender sender = new Sender("y645CY6FgQXr3CuUs4ei/w==");
//        Message message = new Message.Builder()
//                .title("卫生间舒适新装饰，快看看！")
//                .description("11套卫生间干湿分离设计方案，不看就亏了！")
//                .passThrough(0)
//                .notifyType(1)
//                .restrictedPackageName("com.tubatu.demo")
//                .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_WEB)
//                .extra(Constants.EXTRA_PARAM_WEB_URI, "hap://app/com.tubatu.demo/index")
//                .build();
//        Result result = sender.broadcastHybridAll(message, 3);
//        log.info("错误码:{},错误原因:{},消息id:{}", result.getErrorCode().getDescription(),result.getReason(),result.getMessageId());
//    }
//
//}
