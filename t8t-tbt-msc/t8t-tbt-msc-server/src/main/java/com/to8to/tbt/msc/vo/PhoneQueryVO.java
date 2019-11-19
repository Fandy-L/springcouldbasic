package com.to8to.tbt.msc.vo;

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
public class PhoneQueryVO {

    /**
     * 手机号
     */
    private String str;

    /**
     * 手机号ID
     */
    private Integer eid;
}
