package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yason.li
 */
@Data
public class GetUserMsgStateDTO {
    @ApiModelProperty(value = "用户ID", example = "1532684")
    @JsonProperty(value = "user_id")
    private String userId;
}
