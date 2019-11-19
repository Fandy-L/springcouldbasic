package com.to8to.tbt.msc.configuration;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.to8to.tbt.msc.utils.IntegerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
@Configuration
public class MsgCenterConfiguration {

    public static String MAIL_SERVER = null;

    public static String MAIL_PORT = null;

    public static String MAIL_USERNAME = null;

    public static String MAIL_PASSWORD = null;

    public static String WEIXIN_SEND_MSG_URL = null;

    public static String WEIXINEP_SEND_MSG_URL = null;

    public static Map<String, String> keyWords = null;

    public static String queue;

    public static boolean MAIL_OPEN = false;

    public static String sendAppUrl = null;

    public static String sendAppUrlV2 = null;

    public static String addAppUserUrl = null;

    public static String appModel = null;

    public static String appAction = null;

    public static String appVersion = null;

    public static String appId = "1";

    public static String appToken = null;

    public static boolean SMS_PREFIX = false;

    public static long expireTime = 1000 * 60 * 60;

    public static String TENCENT_URL = null;

    public static String TENCENT_BATCH_URL = null;

    /**
     * 随机数
     */
    public static String RANDOM = null;

    public static String TENCENT_SDKAPPID = null;

    public static String TENCENT_APPKEY = null;

    /**
     * 梦网短信通道
     */
    public static String EMP_TO8TO_SC_USERID = null;

    public static String EMP_TO8TO_SC_PASSWORD = null;

    public static String EMP_TMY_SC_USERID = null;

    public static String EMP_TMY_SC_PASSWORD = null;

    public static String EMP_DESIGNBOOK_SC_USERID = null;

    public static String EMP_DESIGNBOOK_SC_PASSWORD = null;

    public static String EMP_TO8TO_YX_USERID = null;

    public static String EMP_TO8TO_YX_PASSWORD = null;

    public static String EMP_TMY_YX_USERID = null;

    public static String EMP_TMY_YX_PASSWORD = null;

    public static String EMP_DESIGNBOOK_YX_USERID = null;

    public static String EMP_DESIGNBOOK_YX_PASSWORD = null;

    public static String EMP_URL = null;

    public static String TEST_PHONE = null;

    public static final int MWYX_TMY = 1;

    public static final int MWYX_TO8TO = 2;

    public static final int MWSC_TMY = 3;

    public static final int MWSC_TO8TO = 4;

    public static final int MWYX_DESIGNBOOK = 5;

    public static final int MWSC_DESIGNBOOK = 6;

    public static int OPEN_TEST = 0;

    public static String YXTAG = "回TD退订";

    public static int SEND_PERMIT = 0;

    public static String ENV = System.getenv("ENV");

    public static String WECHAT_ALARM_URL = "";

    public static String ACCESSID = null;

    public static String ACCESSKEY = null;

    public static String ACCOUNTENDPOINT = null;

    public static String TOPICNAME = null;

    public static String TEMPLATECODE_INFORM = null;

    public static String TEMPLATECODE_MARKETING = null;

    public static CloudAccount aliyunAccount;

    public static MNSClient client;

    @PostConstruct
    public void initialize() {
        Random random = new Random();
        random.nextInt(1000000);
        RANDOM = String.valueOf(random.nextInt(1000000));
        try {
            TENCENT_URL = String.format(TENCENT_URL, TENCENT_SDKAPPID, RANDOM);
            TENCENT_BATCH_URL = String.format(TENCENT_BATCH_URL, TENCENT_SDKAPPID, RANDOM);
        }catch (Exception e){
            log.error("MsgCenterConfiguration initialize exception e:{}", e);
        }
        try {
            aliyunAccount = new CloudAccount(ACCESSID, ACCESSKEY, ACCOUNTENDPOINT);
            client = aliyunAccount.getMNSClient();
        }catch (Exception e){
            log.error("MsgCenterConfiguration initialize aliyunClient e:{} accessId:{} accessKey:{} accountEndpoint:{}", e, ACCESSID, ACCESSKEY, ACCOUNTENDPOINT);
        }
        log.info("MsgCenterConfiguration initialize tencent url:{} batchUrl:{} appId:{} appKey:{} random:{}", TENCENT_URL, TENCENT_BATCH_URL, TENCENT_SDKAPPID, TENCENT_APPKEY, RANDOM);
        log.info("MsgCenterConfiguration initialize aliyun accessId:{} accessKey:{} accountEndpoint:{} topicName:{} templateCodeInform:{} templateCodeMarketing:{}",
                ACCESSID,
                ACCESSKEY,
                ACCOUNTENDPOINT,
                TOPICNAME,
                TEMPLATECODE_INFORM,
                TEMPLATECODE_MARKETING);
        log.info("MsgCenterConfiguration initialize emp sc empUrl:{} empTo8toScUserid:{} empTo8toScPassword:{} empTmyScUserid:{} empTmyScPassword:{} empDesignbookScUserid:{} empDesignbookScPassword:{}",
                EMP_URL,
                EMP_TO8TO_SC_USERID,
                EMP_TO8TO_SC_PASSWORD,
                EMP_TMY_SC_USERID,
                EMP_TMY_SC_PASSWORD,
                EMP_DESIGNBOOK_SC_USERID,
                EMP_DESIGNBOOK_SC_PASSWORD);
        log.info("MsgCenterConfiguration initialize emp sc empTmyYxUserid:{} empTmyYxPassword:{} empTo8toYxUserid:{} empTo8toYxPassword:{} empDesignbookYxUserid:{} empDesignbookYxPassword:{}",
                EMP_TMY_YX_USERID,
                EMP_TMY_YX_PASSWORD,
                EMP_TO8TO_YX_USERID,
                EMP_TO8TO_YX_PASSWORD,
                EMP_DESIGNBOOK_YX_USERID,
                EMP_DESIGNBOOK_YX_PASSWORD);
        log.info("MsgCenterConfiguration initialize operate testPhones:{} openTest:{} yxTag:{} sendPermit:{} smsPrefix:{}",
                TEST_PHONE,
                OPEN_TEST,
                YXTAG,
                SEND_PERMIT,
                SMS_PREFIX);
        log.info("MsgCenterConfiguration initialize email open:{} server:{} port:{} username:{} password:{}",
                MAIL_OPEN,
                MAIL_SERVER,
                MAIL_PORT,
                MAIL_USERNAME,
                MAIL_PASSWORD);
        log.info("MsgCenterConfiguration initialize weixin weixinSendMsgUrl:{} weixinEPSendMsgUrl:{}",
                WEIXIN_SEND_MSG_URL,
                WEIXINEP_SEND_MSG_URL);
    }

    /**
     * 腾讯通发送地址
     */
    @Value(value = "${sms.tencent.url}")
    public void setTencentUrl(String tencentUrl) {
        MsgCenterConfiguration.TENCENT_URL = tencentUrl;
    }

    /**
     * 腾讯通道批量发送地址
     */
    @Value(value = "${sms.tencent.batch.url}")
    public void setTencentBatchUrl(String tencentBatchUrl) {
        MsgCenterConfiguration.TENCENT_BATCH_URL = tencentBatchUrl;
    }

    /**
     * 腾讯通道应用ID
     */
    @Value(value = "${sms.tencent.appId}")
    public void setTencentSdkappid(String tencentSdkappid) {
        MsgCenterConfiguration.TENCENT_SDKAPPID = tencentSdkappid;
    }

    /**
     * 腾讯通道应用密钥
     */
    @Value(value = "${sms.tencent.appKey}")
    public void setTencentAppkey(String tencentAppkey) {
        MsgCenterConfiguration.TENCENT_APPKEY = tencentAppkey;
    }

    /**
     * EMP配置
     *
     * @param empTo8toScUserid
     */
    @Value(value = "${emp.to8to.sc.userId}")
    public void setEmpTo8toScUserid(String empTo8toScUserid) {
        MsgCenterConfiguration.EMP_TO8TO_SC_USERID = empTo8toScUserid;
    }

    @Value(value = "${emp.to8to.sc.password}")
    public void setEmpTo8toScPassword(String empTo8toScPassword) {
        MsgCenterConfiguration.EMP_TO8TO_SC_PASSWORD = empTo8toScPassword;
    }

    @Value(value = "${emp.tmy.sc.userid}")
    public void setEmpTmyScUserid(String empTmyScUserid) {
        MsgCenterConfiguration.EMP_TMY_SC_USERID = empTmyScUserid;
    }

    @Value(value = "${emp.tmy.sc.password}")
    public void setEmpTmyScPassword(String empTmyScPassword) {
        MsgCenterConfiguration.EMP_TMY_SC_PASSWORD = empTmyScPassword;
    }

    @Value(value = "${emp.designBook.sc.userId}")
    public void setEmpDesignbookScUserid(String empDesignbookScUserid) {
        MsgCenterConfiguration.EMP_DESIGNBOOK_SC_USERID = empDesignbookScUserid;
    }

    @Value(value = "${emp.designBook.sc.password}")
    public void setEmpDesignbookScPassword(String empDesignbookScPassword) {
        MsgCenterConfiguration.EMP_DESIGNBOOK_SC_PASSWORD = empDesignbookScPassword;
    }

    @Value(value = "${emp.tmy.yx.userid}")
    public void setEmpTmyYxUserid(String empTmyYxUserid) {
        MsgCenterConfiguration.EMP_TMY_YX_USERID = empTmyYxUserid;
    }

    @Value(value = "${emp.tmy.yx.password}")
    public void setEmpTmyYxPassword(String empTmyYxPassword) {
        MsgCenterConfiguration.EMP_TMY_YX_PASSWORD = empTmyYxPassword;
    }

    @Value(value = "${emp.to8to.yx.userId}")
    public void setEmpTo8toYxUserid(String empTo8toYxUserid) {
        MsgCenterConfiguration.EMP_TO8TO_YX_USERID = empTo8toYxUserid;
    }

    @Value(value = "${emp.to8to.yx.password}")
    public void setEmpTo8toYxPassword(String empTo8toYxPassword) {
        MsgCenterConfiguration.EMP_TO8TO_YX_PASSWORD = empTo8toYxPassword;
    }

    @Value(value = "${emp.designBook.yx.userId}")
    public void setEmpDesignbookYxUserid(String empDesignbookYxUserid) {
        MsgCenterConfiguration.EMP_DESIGNBOOK_YX_USERID = empDesignbookYxUserid;
    }

    @Value(value = "${emp.designBook.yx.password}")
    public void setEmpDesignbookYxPassword(String empDesignbookYxPassword) {
        MsgCenterConfiguration.EMP_DESIGNBOOK_YX_PASSWORD = empDesignbookYxPassword;
    }

    @Value(value = "${emp.url}")
    public void setEmpUrl(String empUrl) {
        MsgCenterConfiguration.EMP_URL = empUrl;
    }

    /**
     * 阿里云短信配置
     *
     * @param accessId
     */
    @Value(value = "${sms.aliyun.accessId}")
    public void setAccessId(String accessId) {
        MsgCenterConfiguration.ACCESSID = accessId;
    }

    @Value(value = "${sms.aliyun.accessKey}")
    public void setAccessKey(String accessKey) {
        MsgCenterConfiguration.ACCESSKEY = accessKey;
    }

    @Value(value = "${sms.aliyun.accountEndpoint}")
    public void setAccountEndpoint(String accountEndpoint) {
        MsgCenterConfiguration.ACCOUNTENDPOINT = accountEndpoint;
    }

    @Value(value = "${sms.aliyun.topicName}")
    public void setTopicName(String topicName) {
        MsgCenterConfiguration.TOPICNAME = topicName;
    }

    @Value(value = "${sms.aliyun.templateCode.inform}")
    public void setTemplateCodeInform(String templateCodeInform) {
        MsgCenterConfiguration.TEMPLATECODE_INFORM = templateCodeInform;
    }

    @Value(value = "${sms.aliyun.templateCode.marketing}")
    public void setTemplateCodeMarketing(String templateCodeMarketing) {
        MsgCenterConfiguration.TEMPLATECODE_MARKETING = templateCodeMarketing;
    }

    @Value(value = "${sms.test.phones}")
    public void setTestPhone(String testPhone) {
        MsgCenterConfiguration.TEST_PHONE = testPhone;
    }

    @Value(value = "${sms.open.test}")
    public void setOpenTest(int openTest) {
        MsgCenterConfiguration.OPEN_TEST = openTest;
    }

    @Value(value = "${sms.yx.tag}")
    public void setYxTag(String yxTag) {
        MsgCenterConfiguration.YXTAG = yxTag;
    }

    @Value(value = "${sms.send.permit}")
    public void setSendPermit(int sendPermit) {
        MsgCenterConfiguration.SEND_PERMIT = sendPermit;
    }

    @Value(value = "${sms.prefix}")
    public void setUsePrefix(Integer usePrefix) {
        MsgCenterConfiguration.SMS_PREFIX = IntegerUtils.isEqLimitValue(usePrefix);
    }

    @Value(value = "${mail.open}")
    public void setMailOpen(Integer mailOpen) {
        MsgCenterConfiguration.MAIL_OPEN = IntegerUtils.isGtLimitValue(mailOpen);
    }

    @Value(value = "${mail.server}")
    public void setMailServer(String mailServer) {
        MsgCenterConfiguration.MAIL_SERVER = mailServer;
    }

    @Value(value = "${mail.port}")
    public void stMailPort(String mailPort) {
        MsgCenterConfiguration.MAIL_PORT = mailPort;
    }

    @Value(value = "${mail.username}")
    public void setMailFromUsername(String mailFromUsername) {
        MsgCenterConfiguration.MAIL_USERNAME = mailFromUsername;
    }

    @Value(value = "${mail.password}")
    public void setMailPassword(String mailPassword) {
        MsgCenterConfiguration.MAIL_PASSWORD = mailPassword;
    }

    @Value(value = "${weixin.sendMsgUrl}")
    public void setWeixinSendMsgUrl(String weixinSendMsgUrl) {
        MsgCenterConfiguration.WEIXIN_SEND_MSG_URL = weixinSendMsgUrl;
    }

    @Value(value = "${weixinep.sendMsgUrl}")
    public void setWeixinEpSendMsgUrl(String weixinEpSendMsgUrl) {
        MsgCenterConfiguration.WEIXINEP_SEND_MSG_URL = weixinEpSendMsgUrl;
    }
}
