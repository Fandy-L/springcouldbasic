package com.to8to.tbt.msc.entity.mysql.push;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author edmund.yu
 */
@Data
@Entity
@Table(name = "quick_app_push_record")
public class QuickAppPushRecord {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "msg_id")
    private String msgId;

    private Integer provider;

    @Column(name = "push_num")
    private Integer pushNum;

    @Column(name = "provider_push_num")
    private Integer providerPushNum;

    @Column(name = "arrived_num")
    private Integer arrivedNum;

    @Column(name = "open_num")
    private Integer openNum;
}
