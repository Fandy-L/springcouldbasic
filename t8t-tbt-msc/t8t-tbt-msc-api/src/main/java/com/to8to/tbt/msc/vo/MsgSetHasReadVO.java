package com.to8to.tbt.msc.vo;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "消息批量设置已读结果")
public class MsgSetHasReadVO {
    private String msgId;

    private Integer msgTime;
}
