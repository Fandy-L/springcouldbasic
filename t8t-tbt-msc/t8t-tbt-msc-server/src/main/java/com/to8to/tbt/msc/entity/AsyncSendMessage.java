package com.to8to.tbt.msc.entity;

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
public class AsyncSendMessage<T> {
    /**
     * 消息类型1-短信4-APP消息
     */
    private Integer sendType;

    /**
     * 消息体
     */
    private T message;
}
