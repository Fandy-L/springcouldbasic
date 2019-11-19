package com.to8to.tbt.msc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/19 20:04
 */
@Data
@Accessors(chain = true)
public class MessageDTO {
    private String title;
    private String content;
    private String contentType;
    private Map<String ,String> extras;
}
