package com.to8to.tbt.msc.entity.es;

import com.alibaba.fastjson.annotation.JSONField;
import com.to8to.tbt.msc.entity.AppMsgWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsAppRecord extends EsBaseRecord {

    /**
     * 消息标题
     */
    private String title;

    /**
     * 发送人
     */
    private String sender;

    @JSONField(name = "app_content")
    private String appContent;

    /**
     * 应用ID
     */
    @JSONField(name = "app_id")
    private Integer appId;

    /**
     * 业主ID
     */
    private Integer uid;

    /**
     * 项目ID
     */
    private int yid;

    /**
     * 读取状态：0-缺省值，未读；1-已读
     */
    @JSONField(name = "is_read")
    private int isRead;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private int createTime;

    /**
     * 业务数据
     */
    @JSONField(name = "biz_param")
    private String bizParam;

    private AppMsgWrapper.BizParams bizParams;
}
