package com.to8to.tbt.msc.dto;

import lombok.Data;

import java.util.Map;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 20:01
 */
@Data
public class NotificationDTO {
    private String alert;
    private String title;
    private Integer badge;
    private String intent;
    private Map<String ,String> extras;
}
