package com.to8to.tbt.msc.constant;

import com.alibaba.fastjson.JSONObject;

/**
 * @author juntao.guo
 */
public class MongateResponseConstant {
    public static JSONObject MONGATERESPONSE = new JSONObject();

    static {
        MONGATERESPONSE.put("-1", "参数为空。信息、电话号码等有空指针，登陆失败");
        MONGATERESPONSE.put("-2", "电话号码个数超过1000");
        MONGATERESPONSE.put("-3", "短时间内短信重复发送!");
        MONGATERESPONSE.put("-4", "短信字数超过65个字符!");
        MONGATERESPONSE.put("-5", "短信内容转码失败!");
        MONGATERESPONSE.put("-6", "短信发送异常!");
        MONGATERESPONSE.put("-8", "解析梦网返回值失败!");
        MONGATERESPONSE.put("-9", "梦网返回值为空");
        MONGATERESPONSE.put("-10", "申请缓存空间失败");
        MONGATERESPONSE.put("-11", "电话号码中有非数字字符");
        MONGATERESPONSE.put("-12", "有异常电话号码");
        MONGATERESPONSE.put("-13", "电话号码个数与实际个数不相等");
        MONGATERESPONSE.put("-14", "实际号码个数超过1000");
        MONGATERESPONSE.put("-101", "发送消息等待超时");
        MONGATERESPONSE.put("-102", "发送或接收消息失败");
        MONGATERESPONSE.put("-103", "接收消息超时");
        MONGATERESPONSE.put("-200", "其他错误");
        MONGATERESPONSE.put("-999", "服务器内部错误");
        MONGATERESPONSE.put("-10001", "用户登陆不成功(帐号密码错误)");
        MONGATERESPONSE.put("-10002", "提交格式不正确");
        MONGATERESPONSE.put("-10003", "用户余额不足");
        MONGATERESPONSE.put("-10004", "手机号码不正确");
        MONGATERESPONSE.put("-10005", "计费用户帐号错误");
        MONGATERESPONSE.put("-10006", "计费用户密码错");
        MONGATERESPONSE.put("-10007", "账号已经被停用");
        MONGATERESPONSE.put("-10008", "账号类型不支持该功能");
        MONGATERESPONSE.put("-10009", "其它错误");
        MONGATERESPONSE.put("-10010", "企业代码不正确");
        MONGATERESPONSE.put("-10011", "信息内容超长");
        MONGATERESPONSE.put("-10012", "不能发送联通号码");
        MONGATERESPONSE.put("-10013", "操作员权限不够");
        MONGATERESPONSE.put("-10014", "费率代码不正确");
        MONGATERESPONSE.put("-10015", "服务器繁忙");
        MONGATERESPONSE.put("-10016", "企业权限不够");
        MONGATERESPONSE.put("-10017", "此时间段不允许发送");
        MONGATERESPONSE.put("-10018", "经销商用户名或密码错");
        MONGATERESPONSE.put("-10019", "手机列表或规则错误");
        MONGATERESPONSE.put("-10021", "没有开停户权限");
        MONGATERESPONSE.put("-10022", "没有转换用户类型的权限");
        MONGATERESPONSE.put("-10023", "没有修改用户所属经销商的权限");
        MONGATERESPONSE.put("-10024", "经销商用户名或密码错");
        MONGATERESPONSE.put("-10025", "操作员登陆名或密码错误");
        MONGATERESPONSE.put("-10026", "操作员所充值的用户不存在");
        MONGATERESPONSE.put("-10027", "操作员没有充值商务版的权限");
        MONGATERESPONSE.put("-10028", "该用户没有转正不能充值");
        MONGATERESPONSE.put("-10029", "此用户没有权限从此通道发送信息(用户没有绑定该性质的通道，比如：用户发了小灵通的号码)");
        MONGATERESPONSE.put("-10030", "不能发送移动号码");
        MONGATERESPONSE.put("-10031", "手机号码(段)非法");
        MONGATERESPONSE.put("-10032", "用户使用的费率代码错误");
        MONGATERESPONSE.put("-10033", "非法关键词");
        MONGATERESPONSE.put("-10057", "非法IP地址");
        MONGATERESPONSE.put("-10123", "定时发送时间被禁止（时间格式错误、定时时间小于当前时间、定时时间大于等于存活时间）");
        MONGATERESPONSE.put("-10124", "无效定时发送时间（时间格式错误、定时时间小于当前时间、定时时间大于等于存活时间）");
        MONGATERESPONSE.put("-10125", "短信有效存活时间被禁止（时间格式错误、存活时间小于当前时间、定时时间大于等于存活时间）");
        MONGATERESPONSE.put("-10126", "短信有效存活时间无效（时间格式错误、存活时间小于当前时间、定时时间大于等于存活时间）");
        MONGATERESPONSE.put("-10252", "业务类型错（长度超长或不为字母、数字、字母数字混合）");
        MONGATERESPONSE.put("-10253", "自定义参数错（长度超长）");
        MONGATERESPONSE.put("-10254", "EMP软件认证失败，暂停发送。（可能因为产品序列号过期、长时间无法连接认证服务器等造成）");
        MONGATERESPONSE.put("-20001", "文件MD5校验失败");
        MONGATERESPONSE.put("-20002", "文件块缺失");
        MONGATERESPONSE.put("-20003", "该文件流水号不存在");
        //腾讯通道错误码
        MONGATERESPONSE.put("1000", "腾讯接口返回值为空");
        MONGATERESPONSE.put("1001", "AppKey");
        MONGATERESPONSE.put("1002", "短信/语音内容中含有脏字");
        MONGATERESPONSE.put("1003", "未填AppKey");
        MONGATERESPONSE.put("1004", "REST API请求有参数错误");
        MONGATERESPONSE.put("1006", "没有权限");
        MONGATERESPONSE.put("1007", "其它错误");
        MONGATERESPONSE.put("1008", "下发短信超时");
        MONGATERESPONSE.put("1009", "用户IP不在白名单中");
        MONGATERESPONSE.put("1011", "REST API命令字错误");
        MONGATERESPONSE.put("1012", "短信内容错误");
        MONGATERESPONSE.put("1013", "下发短信频率限制");
        MONGATERESPONSE.put("1014", "模板未经审批");
        MONGATERESPONSE.put("1015", "黑名单手机");
        MONGATERESPONSE.put("1016", "错误的手机号格式");
        MONGATERESPONSE.put("1017", "短信内容过长");
        MONGATERESPONSE.put("1018", "调用异常");
    }

    public MongateResponseConstant() {

    }
}
