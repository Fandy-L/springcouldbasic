package com.to8to.tbt.msc.entity.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Builder
@Data
public class RedirectAppMsgResult {

    private String result;
    private Integer code;
    private String msg;
}
