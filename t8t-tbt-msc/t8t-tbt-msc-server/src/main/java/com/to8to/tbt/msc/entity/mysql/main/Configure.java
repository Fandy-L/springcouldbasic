package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;


/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_configure")
public class Configure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    /**
     * 节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块
     */
    @Column(name = "config_type")
    private int configType;

    /**
     * 节点描述
     */
    @Column(name = "config_describe")
    private String configDescribe;

    /**
     * 父节点ID
     */
    @Column(name = "father_id")
    private int fatherId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private int createTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_id")
    private int createId;

    /**
     * 修改人ID
     */
    @Column(name = "modify_id")
    private int modifyId;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private int modifyTime;
}