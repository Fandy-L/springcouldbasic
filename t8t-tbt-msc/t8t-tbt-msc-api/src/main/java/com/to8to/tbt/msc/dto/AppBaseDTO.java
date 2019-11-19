package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppBaseDTO {
    @ApiModelProperty(value = "APP公参")
    private String pubArgs;
}
