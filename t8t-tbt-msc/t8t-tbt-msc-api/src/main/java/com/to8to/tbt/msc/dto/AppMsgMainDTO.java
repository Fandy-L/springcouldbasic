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
public class AppMsgMainDTO extends AppSystemMsgBaseDTO{
    @ApiModelProperty(value = "系统消息未读数", example = "0")
    private Integer sysMsgReadCount;
}
