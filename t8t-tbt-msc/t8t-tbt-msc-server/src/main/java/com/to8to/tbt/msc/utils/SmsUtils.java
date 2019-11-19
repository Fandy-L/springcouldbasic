package com.to8to.tbt.msc.utils;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.model.*;
import com.google.common.collect.Lists;
import com.to8to.common.http.DefaultWebClient;
import com.to8to.common.http.WebClient;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.dto.SendTencentSmsDTO;
import com.to8to.tbt.msc.entity.MultixMt;
import com.to8to.tbt.msc.entity.SmsWrapper;
import com.to8to.tbt.msc.entity.mongo.MongoMsgReply;
import com.to8to.tbt.msc.utils.client.Content;
import com.to8to.tbt.msc.vo.SendTencentSmsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
public class SmsUtils {
    /**
     * 请求超时
     */
    private static final int SLOW_TIME = 10000;
    /**
     * 中国的国家代码
     */
    private static final String NATION_CODE_ZH = "86";

    private static DefaultWebClient webClient;

    /**
     * 发送腾讯短信
     *
     * @param phone
     * @param content
     * @param bizType
     * @return
     */
    public static SendTencentSmsVO sendTencentSms(String phone, String content, String bizType) {
        long startTime = System.currentTimeMillis();
        String sig = DigestUtils.md5Hex(MsgCenterConfiguration.TENCENT_APPKEY + phone).toLowerCase();
        SmsWrapper.TencentSmsTel tencentSmsTel = SmsWrapper.TencentSmsTel.builder()
                .nationcode(NATION_CODE_ZH)
                .phone(phone)
                .build();
        SendTencentSmsDTO sendTencentSmsDTO = SendTencentSmsDTO.builder()
                .tel(tencentSmsTel)
                .type(bizType)
                .msg(content.trim())
                .sig(sig)
                .extend("")
                .ext("")
                .build();
        SendTencentSmsVO sendTencentSmsVO = null;
        try {
            sendTencentSmsVO = getWebClient().execute(MsgCenterConfiguration.TENCENT_URL, sendTencentSmsDTO, SendTencentSmsVO.class);
        } catch (Exception e) {
            log.warn("SmsUtils sendTencentSms exception phone:{} content:{} bizType:{} e:{}", phone, content, bizType, e);
        }
        log.info("SmsUtils sendTencentSms appKey:{} tencentSmsTel:{} params:{} response:{}", MsgCenterConfiguration.TENCENT_APPKEY.length(), tencentSmsTel, sendTencentSmsDTO, sendTencentSmsVO);
        long costTime = System.currentTimeMillis() - startTime;
        if (costTime > SLOW_TIME) {
            log.warn("SmsUtils sendTencentSms slow time：{}ms", costTime);
        }
        return sendTencentSmsVO;
    }

    /**
     * 获取网络请求对象
     *
     * @return
     */
    protected static WebClient getWebClient() {
        if (webClient == null) {
            webClient = new DefaultWebClient();
        }
        return webClient;
    }

    /**
     * 发送阿里云短信
     *
     * @param phoneList
     * @param content
     * @param signName
     * @param templateCode
     * @return
     */
    public static String sendAliyunSms(List<String> phoneList, String content, String signName, String templateCode) {
        String messageId = "";
        long startTime = System.currentTimeMillis();
        try {
            RawTopicMessage msg = new RawTopicMessage();
            msg.setMessageBody("sms-message");
            CloudTopic topic = MsgCenterConfiguration.client.getTopicRef(MsgCenterConfiguration.TOPICNAME);
            MessageAttributes messageAttributes = new MessageAttributes();

            BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
            batchSmsAttributes.setFreeSignName(signName);
            batchSmsAttributes.setTemplateCode(templateCode);

            BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
            smsReceiverParams.setParam("content", content);
            for (String phone : phoneList) {
                batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams);
            }
            messageAttributes.setBatchSmsAttributes(batchSmsAttributes);

            TopicMessage topicMessage = topic.publishMessage(msg, messageAttributes);
            log.info("SmsUtils sendAliyunSms signName:{} templateCode:{}  msg:{} messageAttributes:{} response:{}", signName, templateCode, msg, messageAttributes, topicMessage);
            messageId = topicMessage.getMessageId();
        } catch (Exception e) {
            log.warn("SmsUtils sendAliyunSms exception e:{}", e);
        }

        long costTime = System.currentTimeMillis() - startTime;
        if (costTime > SLOW_TIME) {
            log.warn("SmsUtils sendAliyunSms slow time：{}ms", costTime);
        }
        MsgCenterConfiguration.client.close();
        return messageId;
    }

    /**
     * 发送梦网短信
     *
     * @param phone
     * @param content
     * @param userId
     * @param password
     * @return
     * @throws Exception
     */
    public static String sendMongateSms(String phone, String content, String userId, String password) {
        long startTime = System.currentTimeMillis();
        MultixMt multixmt = new MultixMt();
        multixmt.setPhone(phone);
        // 短信内容需GBK转Base64编码
        try {
            multixmt.setMsgContent(EncryptUtils.encryptBase64(content.trim()));
        }catch (Exception e){
            log.warn("SmsUtils sendMongateSms encryptBase64 fail content:{} e:{}", content, e);
        }
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userId", userId));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("multixmt", multixmt.toString()));

        HttpClientRawUtils httpclient = new HttpClientRawUtils();
        String url = MsgCenterConfiguration.EMP_URL + "MongateCsSpMultixMtSend";
        Content httpResponse = null;
        try {
            httpResponse = httpclient.post(url, null, params, null, "UTF-8");
        } catch (Exception e) {
            log.warn("SmsUtils sendMongateSms fail e:{}", e);
        }
        log.info("SmsUtils sendMongateSms url:{} params:{} response:{}", url, params, httpResponse);
        String res = null;
        if (null != httpResponse) {
            try {
                String contentStr = httpResponse.getContentString();
                Document document = DocumentHelper.parseText(httpResponse.getContentString());
                Element element = document.getRootElement();
                res = element.getText();
                log.debug("SmsUtils sendMongateSms contentStr:{} document:{} element:{} res:{}", contentStr, document, element, res);
            } catch (Exception e) {
                log.warn("SmsUtils sendMongateSMS exception e", e);
            }
        }
        long costTime = System.currentTimeMillis() - startTime;
        if (costTime > SLOW_TIME) {
            log.warn("SmsUtils sendMongateSms slow time：{}", costTime);
        }
        return res;
    }

    public static List<MongoMsgReply> getMongateUplinkRecord(String userId, String password) {
        List<BasicNameValuePair> req = new ArrayList<BasicNameValuePair>();
        req.add(new BasicNameValuePair("userId", userId));
        req.add(new BasicNameValuePair("password", password));
        String httpurl = MsgCenterConfiguration.EMP_URL + "MongateCsGetSmsExEx";
        HttpClientRawUtils httpclient = new HttpClientRawUtils();
        List<MongoMsgReply> list = new ArrayList<>();
        Content httpResponse = null;
        try {
            httpResponse = httpclient.post(httpurl, null, req, null, "UTF-8");
            if (null != httpResponse) {
                Document doc = DocumentHelper.parseText(httpResponse.getContentString());
                Element el = doc.getRootElement();
                List<Element> childelements = el.elements();
                MongoMsgReply msgreply = null;
                for (Element child : childelements) {
                    msgreply = new MongoMsgReply();
                    String text = child.getText();
                    List<String> str = Arrays.asList(text.split(","));
                    String rdate = str.get(0);
                    String rtime = str.get(1);
                    String phone = str.get(2);
                    String reply = str.get(5);

                    String replytime = rdate + " " + rtime;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(replytime);

                    msgreply.setReplyTime((int) (date.getTime() / 1000));
                    msgreply.setPhone(phone);
                    msgreply.setReply(reply);
                    list.add(msgreply);
                }

            }
        } catch (Exception e) {
            if (e instanceof DocumentException) {
                log.error("短信上行：解析返回值异常！返回值{},异常信息:{}", httpResponse.getContentString(), e);
            } else {
                log.error("短信上行：调用异常！ 异常信息:{}", e);
            }
        }

        return list;
    }

    public static List<Message> getAliyunSmsReply() {
        CloudQueue queue = MsgCenterConfiguration.client.getQueueRef("sms-reply");
        List<Message> messageList = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            List<Message> tmessageList = queue.batchPopMessage(16);
            if (null != tmessageList && tmessageList.size() > 0) {
                messageList.addAll(tmessageList);
                log.debug("[getAliyunSmsReply] message:{}", tmessageList);
                List<String> receiptHandles = tmessageList.stream().map(Message::getReceiptHandle).collect(Collectors.toList());
                queue.batchDeleteMessage(receiptHandles);
            }
        }
        MsgCenterConfiguration.client.close();
        return messageList;
    }
}
