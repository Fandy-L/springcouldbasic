package com.to8to.tbt.msc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
public class PushMessageCountVO {

    @ApiModelProperty(value = "推送数")
    private Integer pushNum;

    @ApiModelProperty(value = "厂商实际推送数")
    private Integer providerPushNum;

    @ApiModelProperty(value = "送达数")
    private Integer arrivedNum;

    @ApiModelProperty(value = "打开数")
    private Integer openNum;
}
