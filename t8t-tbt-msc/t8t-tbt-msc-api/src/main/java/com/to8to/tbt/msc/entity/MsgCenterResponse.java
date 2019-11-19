package com.to8to.tbt.msc.entity;

import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
public class MsgCenterResponse {
    private int code;
    private String content;
    public MsgCenterResponse(int code,String content)
    {
        this.code = code;
        this.content = content;
    }
}
