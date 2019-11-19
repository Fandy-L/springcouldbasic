package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yason.li
 */
@Data
public class SetAppMsgReadByTidUidDTO {
    @ApiModelProperty(value = "模板ID", example = "[601,602]")
    @NotEmpty
    @JsonProperty(value = "tids")
    private List<@Min(value = 0) @NotNull Integer> tids;

    @ApiModelProperty(value = "用户ID", example = "1675604")
    @NotNull(message = "请输入用户ID")
    @Min(value = 0)
    @JsonProperty(value = "uid")
    private Integer uid;

}
