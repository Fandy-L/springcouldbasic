package com.to8to.tbt.msc.common;

import ch.qos.logback.classic.Level;
import com.to8to.sc.compatible.Status;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 16:09
 */
public enum MyExceptionStatus implements Status {
    /**
     * 操作成功的状态码
     */
    OLD_MSG_RESPONSE_SUCCESS("操作成功", 1, Level.INFO),
    OLD_MSG_RESPONSE_FAIL("操作失败", 0, Level.WARN),
    OLD_MSG_CENTER_RESPONSE_SUCCESS("success", 200, Level.INFO),
    OLD_MSG_CENTER_RESPONSE_FAIL("fail", 405, Level.WARN),
    OLD_MSG_CENTER_RESPONSE_GROUP_NOTE_SEND_SUCCESS("短信已发送", 507, Level.WARN),
    RATE_LIMIT_REACHED("请求频率达到限流上限",10000,  Level.WARN),
    NETWORK_ERROR("网络错误请稍后重试", 10010, Level.WARN),

    TEMPLATE_NODEID_INVALID("无效 node_ids ,无法匹配！", 10001, Level.WARN),
    SEND_TYPE_INVALID("发送类型仅支持1-短信4-APP！", 10001, Level.WARN),
    KEYWORD_NOT_FOUND("关键词不存在", 10001, Level.WARN),
    KEYWORD_IS_EXISTS("关键词已经存在！", 10002, Level.WARN),
    KEYWORD_ID_INVALID("无效的关键字_id,无法匹配",10007,Level.WARN),
    TEMPLATE_CREATE_FAIL("模板创建失败！", 10011, Level.WARN),
    TEMPLATE_NOT_FOUND("查询的模板不存在！", 10012, Level.WARN),
    TEMPLATE_ID_INVALID("无效的模板_id，无法匹配",10013,Level.WARN),
    TEMPLATE_CREATE_SEND_TYPE_INVALID("发送方式为空！",10014,Level.WARN),
    TEMPLATE_CREATE_SUCCESS("模板创建成功！",10015,Level.INFO),
    MESSAGE_CANNOT_FIND("无法找到匹配该条件的消息",10016,Level.WARN),
    PARAMS_LENGTH_INVALID("参数长度不匹配",10019,Level.WARN),
    PARAMS_CONTAINS_NULL("参数为空",10020,Level.WARN),
    CONFIGURE_NOT_FOUND("配置信息不存在！", 10022, Level.WARN),
    CONFIGURE_DELETE_SUCCESS("删除业类型成功！", 0, Level.INFO),
    CONFIGURE_DELETE_SUCCESS_BY_BUSINESS_ITEM("删除业务项成功！", 0, Level.INFO),
    CONFIGURE_DELETE_FAIL_BY_BUSINESS_ITEM("删除失败：业务项的发送子节点在消息模板只中引用", 10025, Level.WARN),
    CONFIGURE_DELETE_SUCCESS_BY_PRODUCT_MODULE("删除产品模块成功！", 10026, Level.INFO),
    CONFIGURE_DELETE_FAIL_BY_PRODUCT_MODULE("删除失败：产品模块在消息模版中有引用！", 10027, Level.WARN),
    CONFIGURE_DELETE_SUCCESS_BY_RECEIVE_OBJECT("删除发送发送对象成功！", 10028, Level.INFO),
    CONFIGURE_DELETE_FAIL_BY_RECEIVE_OBJECT("删除失败：发送对象在消息模版中有引用！", 10029, Level.WARN),
    CONFIGURE_DELETE_SUCCESS_BY_SEND_NODE("发送节点删除成功！", 0, Level.INFO),
    CONFIGURE_DELETE_FAIL_BY_SEND_NODE("删除失败：发送节点在消息模板中引用", 10030, Level.WARN),
    CONFIGURE_UPDATE_SUCCESS("节点更新成功！！", 0, Level.INFO),
    GROUP_NOTE_NOT_FOUND("查询的模板不存在", 10031, Level.WARN),
    GROUP_NOTE_RECORD_IMPORT_FAIL("导入失败", 10041, Level.WARN),
    VERIFICATION_CODE_CHECK_INCORRECT("验证码不正确！", 10051, Level.WARN),
    VERIFICATION_CODE_CHECK_INVALID("验证码失效或不存在！", -1, Level.WARN),
    SEND_ALL_MESSAGE_TID_INVALID("无效的模板！", 0, Level.WARN),
    SEND_ALL_MESSAGE_SUCCESS("发送成功！", 1, Level.INFO),
    SEND_ALL_MESSAGE_FAIL("发送失败！", 0, Level.WARN),
    SEND_ALL_MESSAGE_DELAY_TIME_SUCCESS("添加延时消息成功!", 1, Level.INFO),
    SEND_ALL_MESSAGE_DELAY_TIME_FAIL("添加延时消息失败!", 0, Level.WARN),
    SEND_SMS_PHONE_INVALID("所有的号码非法!", 10071, Level.WARN),
    SEND_MSG_PHONE_INVALID("内容和电话均不可为空!", -1, Level.WARN),
    SEND_MSG_ALL_PHONE_INVALID("所有的号码非法!", -1, Level.WARN),
    SEND_MSG_ALL_PHONE_FORMAT_ERROR("电话号码格式错误!", 0, Level.WARN),
    SEND_MSG_ALL_MAX_LIMIT("已达发送上限!", 0, Level.WARN),
    SEND_WECHAT_ALARM_SUCCESS("发送告警消息成功！", 1, Level.INFO),
    SEND_WECHAT_ALARM_FAIL("发送告警消息失败！", 10100, Level.WARN),


    APP_NOT_EXIST("应用不存在", 20001),
    APP_ID_EXIST_ALREADY("应用id已经存在", 20002),
    OLD_APP_ID_USED_ALREADY("应用id已经在使用", 20003),
    CHANNEL_EXIST_ALREADY("通道已经在使用", 20004),
    CHANNEL_NOT_EXIST("通道不存在", 20005),
    CHANNEL_CONNECTION_ERROR("通道连接错误", 20006),
    CHANNEL_REQUEST_ERROR("通道请求错误", 20007),
    CHANNEL_BIZ_ERROR("通道业务错误", 20008),
    CHANNEL_UNKNOWN_ERROR("通道未知错误", 20009),
    USER_NOT_EXIST_IN_CHANNEL("用户不存在", 20010),
    UMENG_DEVICE_TOKEN_ERROR("友盟设备token更新错误", 20011),
    TOKEN_OF_THIS_VERSION_NOT_SUPPORT("当前版本的token不再支持", 20012),

    SAVE_FAIL("存储失败",20013);

    private String msg;
    private int code;
    private Level logLevel = Level.ERROR;

    MyExceptionStatus(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    MyExceptionStatus(String msg, int code, Level lv) {
        this.msg = msg;
        this.code = code;
        this.logLevel = lv;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Level getLogLevel() {
        return this.logLevel;
    }
}
