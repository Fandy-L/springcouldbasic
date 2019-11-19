package com.to8to.tbt.msc.entity.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsMsgRecord extends EsBaseRecord {

    /**
     * 消息内容
     */
    @JSONField(name = "msg_content")
    private String msgContent;

    /**
     * 模板落地情况 1 落地 2非落地
     */
    @JSONField(name = "is_ground")
    private int isGround;

    /**
     * 通道类型
     */
    @JSONField(name = "channel_type")
    private int channelType;

    /**
     * 手机号ID
     */
    @JSONField(name = "phoneid")
    private int phoneId;

    /**
     * 错误码
     */
    @JSONField(name = "error_code")
    private int errorCode;

    /**
     * 错误码描述
     */
    @JSONField(name = "error_describle")
    private String errorDescribe;
}
