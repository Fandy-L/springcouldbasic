package com.to8to.tbt.msc.entity.mysql.main;

import lombok.Data;

import javax.persistence.*;

/**
 * @author juntao.guo
 */
@Data
@Entity
@Table(name = "msgc_keyword")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kid;

    private String keyword;

    @Column(name = "create_time")
    private int createTime;

    @Column(name = "create_id")
    private int createId;
}
