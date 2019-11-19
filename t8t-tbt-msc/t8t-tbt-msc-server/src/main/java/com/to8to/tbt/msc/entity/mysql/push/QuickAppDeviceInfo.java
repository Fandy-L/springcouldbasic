package com.to8to.tbt.msc.entity.mysql.push;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author edmund.yu
 */
@Data
@Entity
@Table(name = "push_device_info")
public class QuickAppDeviceInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "reg_id")
    private String regId;

    private Integer provider;

    private String version;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "creat_time")
    private Long creatTime;

}
