package com.to8to.tbt.msc.vo.wechat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yason.li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeChatResMsgVO {
    /**
     * 状态码描述
     */
    private String msg;

    /**
     * 状态码
     */
    private Boolean success;
}
