package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
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
public class AppReadClearDTO extends AppSystemMsgBaseDTO{
    @ApiModelProperty(value = "消息ID", example = "10")
    private Integer msgId;

    @ApiModelProperty(value = "是否全部设置已读", example = "1")
    private Integer readAll;
}
