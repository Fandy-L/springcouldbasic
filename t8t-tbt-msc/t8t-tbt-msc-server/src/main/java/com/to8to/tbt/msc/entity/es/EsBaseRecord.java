package com.to8to.tbt.msc.entity.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsBaseRecord {
    private int id;

    /**
     * 项级分类
     */
    @JSONField(name = "large_type")
    private int largeType;

    /**
     * 顶级分类描述
     */
    @JSONField(name = "large_type_des")
    private String largeTypeDes;

    /**
     * 父级分类
     */
    @JSONField(name = "small_type")
    private int smallType;

    /**
     * 父级分类描述
     */
    @JSONField(name = "small_type_des")
    private String smallTypeDes;

    /**
     * 节点类型
     */
    @JSONField(name = "node_type")
    private int nodeType;

    /**
     * 节点类型描述
     */
    @JSONField(name = "node_type_des")
    private String nodeTypeDes;

    /**
     * 目标对象类型
     */
    @JSONField(name = "target_type")
    private int targetType;

    /**
     * 产品模块
     */
    @JSONField(name = "pm_module")
    private String pmModule;

    /**
     * 模板ID
     */
    private int tid;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 模板启用状态 1.启用 2.停用
     */
    @JSONField(name = "is_active")
    private int isActive;

    /**
     * 模板触发方式 1.手动 2.自动
     */
    @JSONField(name = "is_auto")
    private int isAuto;

    /**
     * 模板发送方式 1.短信，2.pc消息 3.邮件 4.微信
     */
    @JSONField(name = "send_type")
    private int sendType;

    /**
     * 短信发送状态 0 发送失败 1发送成功
     */
    @JSONField(name = "send_status")
    private int sendStatus;

    /**
     * 发送时间
     */
    @JSONField(name = "send_time")
    private Long sendTime;
}
