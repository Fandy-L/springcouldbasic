package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yason.li
 */
@Data
public class SetUserMsgReadedDTO {

    @ApiModelProperty(value = "用户ID", example = "1599078")
    @JsonProperty(value = "user_id")
    private String userId;

    @ApiModelProperty(value = "预约项目id", example = "1359607")
    @JsonProperty(value = "yid")
    private Integer yid;
}
