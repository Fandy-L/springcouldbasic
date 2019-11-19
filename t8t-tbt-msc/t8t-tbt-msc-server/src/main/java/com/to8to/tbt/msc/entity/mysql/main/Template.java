package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_template")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    /**
     * 模板配置表中的发送节点id
     */
    @Column(name = "nodeid")
    private int nodeId;

    /**
     * 模版名称
     */
    private String title;

    /**
     * 启用状态 1.启用 2.停用
     */
    @Column(name = "is_active")
    private int isActive;

    /**
     * 触发方式 1.手动 2.自动
     */
    @Column(name = "is_auto")
    private int isAuto;

    /**
     * 接收角色（现在为发送对象)
     */
    @Column(name = "target_type")
    private int targetType;

    /**
     * 发送方式 1.短信，2.pc消息 3.邮件 4.微信
     */
    @Column(name = "send_type")
    private int sendType;

    /**
     * 产品模块
     */
    @Column(name = "pm_module")
    private String pmModule;

    /**
     * 备注
     */
    @Column(name = "msg_remark")
    private String msgRemark;

    /**
     *创建时间
     */
    @Column(name = "create_time")
    private int createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_id")
    private int createId;

    /**
     * 修改人id
     */
    @Column(name = "modify_id")
    private int modifyId;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private int modifyTime;
}
