package com.to8to.tbt.msc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pajero.quan
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlarmDTO {
    /**
     * 模块名
     */
    private String module;
    /**
     * 告警消息内容
     */
    private String message;
}
