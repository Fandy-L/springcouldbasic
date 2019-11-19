package com.to8to.tbt.msc.entity;

import com.to8to.tbt.msc.entity.MsgCenterResponse;

/**
 * @author edmund.yu
 */
public class Response {
    public static final MsgCenterResponse SUCCESS = new MsgCenterResponse(200,"success");
    public static final MsgCenterResponse FAIL = new MsgCenterResponse(405, "fail");
    public static final MsgCenterResponse RESPONSE_503 = new MsgCenterResponse(503, "该用户已屏蔽此类消息");
    public static final MsgCenterResponse RESPONSE_507 = new MsgCenterResponse(507, "短信已发送");
    public static final MsgCenterResponse RESPONSE_508 = new MsgCenterResponse(508, "发现无效关键词，请确认“##”中间的关键词在关键词列表中存在");
    public static final MsgCenterResponse RESPONSE_509 = new MsgCenterResponse(509, "请不要在 内容 或 标题 或 链接 中填入“##”");
}
