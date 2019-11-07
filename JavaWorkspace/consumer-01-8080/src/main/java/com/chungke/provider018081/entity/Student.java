package com.chungke.provider018081.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Tables;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Data
@Entity
@Table(name = "student")
//@JsonIgnoreProperties({hibernateLazyInitializer})
public class Student {
//    CREATE TABLE `student` (
//            `sno` varchar(8) NOT NULL,
//  `sname` char(8) DEFAULT NULL,
//  `ssex` char(2) DEFAULT '男',
//            `sage` tinyint(4) DEFAULT NULL,
//  `sdept` char(20) DEFAULT NULL,
    @Id
    private String  sno;
    private String  sname;
    private String  ssex;
    private String  sdept;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public String getSdept() {
        return sdept;
    }

    public void setSdept(String sdept) {
        this.sdept = sdept;
    }
}
