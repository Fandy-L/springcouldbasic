package com.to8to.tbt.msc.constant;

import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
public class MsgConstant {
    /**
     * 短信
     */
    public static final int SENDTYPE_SMS = 1;
    /**
     * app消息
     */
    public static final int SENDTYPE_APP = 4;
    /**
     * pc消息
     */
    public static final int SENDTYPE_PC = 5;

    public static final int PC_RECORD_SEND_TYPE = 5;

    public static final int APP_RECORD_SEND_TYPE = 4;

    /**
     * 验证码前缀
     */
    public static final String PREFIX_VERIFY_CODE = "msgc_verify_code_";
    /**
     * 验证码验证通过凭证前缀
     */
    public static final String PREFIX_CODE_CERTIFICATE = "msgc_code_certificate_";
    /**
     * 验证码默认失效时间
     */
    public static final int CODE_DEFAULT_EXPIRES_TIME = 5 * 60;
    /**
     * 凭证默认失效时间
     */
    public static final int CODE_CERTIFICATE_DEFAULT_EXPIRES_TIME = 5 * 60;
    /**
     * 默认平台
     */
    public static final String DEFAULT_PLATFORM = "msgc";
    /**
     * 手机号匹配规则
     */
    public static Pattern PHONE_VALIDATION_REGEX = Pattern.compile("[\\d]{11}");
    /**
     * 配置类型-业务类型
     */
    public static final Integer CONFIG_TYPE_BUSINESS_TYPE = 1;
    /**
     * 配置类型-业务项
     */
    public static final Integer CONFIG_TYPE_BUSINESS_ITEM = 2;
    /**
     * 配置类型-发送节点
     */
    public static final Integer CONFIG_TYPE_SEND_NODE = 3;


    /**
     * 每页加载条数
     */
    public static final int PAGE_SIZE = 30;


    /**
     * APP消息-已读状态
     */
    public static final int APP_RECORD_HAS_READ_STATUS = 1;

    /**
     * 消息记录表名-MONGO
     */
    public static final String COLLECITON_MSG_RECORD_PC = "pc_record";

    /**
     * 短信记录表名-MONGO
     */
    public static final String COLLECTION_NOTE_RECORD = "note_record";

    /**
     * 邮件记录表名-MONGO
     */
    public static final String COLLECTION_MAIL_RECORD = "mail_record";

    /**
     * 微信记录表名-MONGO
     */
    public static final String COLLECTION_WEIXIN_RECORD = "weixin_record";

    /**
     * APP记录表名-MONGO
     */
    public static final String COLLECTION_APP_RECORD = "app_record";

    /**
     * 群发短信模板表名-MONGO
     */
    public static final String COLLECTION_GROUP_NOTE = "group_note";

    /**
     * 微信消息发送接口请求成功的状态码
     */
    public static final int WECHAT_RESPONSE_SUCCESS_CODE = 200;
}
